# -*- coding: utf-8 -*-
"""

@author: bevan
@project: Predict Stock of Future Prices
    
"""

# --------------------------------------------------------------- #
#### Importing Libraries ####
# --------------------------------------------------------------- #

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sn
import plotly.express as px
import plotly.figure_factory as ff
import funcModels

from copy import copy
from scipy import stats
from sklearn.linear_model import LinearRegression
from sklearn.svm import SVR
from sklearn.model_selection import train_test_split
from sklearn.metrics import r2_score
from sklearn.preprocessing import MinMaxScaler
from sklearn.linear_model import Ridge
from tensorflow import keras

# Import datasets
# Stock Prices
stock_price_dataset = pd.read_csv('../data/stock.csv')

# Stock Volume
stock_vol_dataset = pd.read_csv('../data/stock_volume.csv')


# --------------------------------------------------------------- #
#### EDA ####
# --------------------------------------------------------------- #

stock_price_dataset = stock_price_dataset.sort_values(by=['Date'])
stock_vol_dataset = stock_vol_dataset.sort_values(by=['Date'])


# Check for Null Values
stock_price_dataset.isnull().sum()
stock_vol_dataset.isnull().sum()

# Check the average price of the stocks
stock_price_dataset.describe()
stock_vol_dataset.describe()


# --------------------------------------------------------------- #
#### Plot the Data ####
# --------------------------------------------------------------- #

funcModels.interactive_plot(stock_price_dataset, 'Stock Prices')

funcModels.interactive_plot(stock_vol_dataset, 'Stock Volume')

# Normalize the Plot
funcModels.interactive_plot(funcModels.normalize(
    stock_price_dataset), 'Normalize Stock Prices')

funcModels.interactive_plot(funcModels.normalize(
    stock_vol_dataset), 'Normalize Stock Volume')


# --------------------------------------------------------------- #
#### Prepare the Data Before Training ####
# --------------------------------------------------------------- #

price_volume_dataset = funcModels.individual_stock(
    stock_price_dataset, stock_vol_dataset, 'AAPL')

price_volume_target = funcModels.trading_window(price_volume_dataset)

# Remove Any Null Values
price_volume_target = price_volume_target[:-1]

# Scale the Data
sc = MinMaxScaler(feature_range=(0, 1))
price_volume_target_scaled = sc.fit_transform(
    price_volume_target.drop(columns=['Date']))

# Create Feature and Target
X = price_volume_target_scaled[:, :2]
y = price_volume_target_scaled[:, 2:]

# Check the data
X.shape
y.shape

split = int(0.65 * len(X))

X_train = X[:split]
y_train = y[:split]

X_test = X[split:]
y_test = y[split:]

# Plot the graph for training

funcModels.show_plot(X_train, 'Training Data')
funcModels.show_plot(X_test, 'Testing Data')

# --------------------------------------------------------------- #
#### Build and Train (Ridge Linear Regression Model) ####
# --------------------------------------------------------------- #

# Create and train (Ridge Linear Regression Model)
regression_model = Ridge()
regression_model.fit(X_train, y_train)

# Test the model and calculate its accuracy
lr_accuracy = regression_model.score(X_test, y_test)
print('Ridge Regression Score: ', lr_accuracy)

# Make Prediction
predicted_prices = regression_model.predict(X)

# Append predicted values into a list
predicted = []

for i in predicted_prices:
    predicted.append(i[0])

# Appednd the close values to the list
close = []

for i in price_volume_target_scaled:
    close.append(i[0])

# Create a DataFrame based on the dates in the individual stock data
df_predicted = price_volume_target[['Date']]

df_predicted['Close'] = close
df_predicted['Prediction'] = predicted

# Plot the Results
funcModels.interactive_plot(df_predicted, 'Original vs. Predictions')


# --------------------------------------------------------------- #
#### Train an LSTM Time Series Model ####
# --------------------------------------------------------------- #

price_volume_dataset = funcModels.individual_stock(
    stock_price_dataset, stock_vol_dataset, 'sp500')

training_data = price_volume_dataset.iloc[:, 1:3].values

sc = MinMaxScaler(feature_range=(0, 1))
training_set_scaled = sc.fit_transform(training_data)

# Create the training and testing data
X = []
y = []

for i in range(1, len(price_volume_dataset)):
    X.append(training_set_scaled[i-1: i, 0])
    y.append(training_set_scaled[i, 0])

# Convert the data into array format
X = np.asarray(X)
y = np.asarray(y)

# Split the Data
split = int(0.7 * len(X))

X_train = X[:split]
y_train = y[:split]

X_test = X[split:]
y_test = y[split:]

# Reshape the 1D arrays to 3D arrays to feed in the model
X_train = np.reshape(X_train, (X_train.shape[0], X_train.shape[1], 1))
X_test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1], 1))

# Create the Model
inputs = keras.layers.Input(shape=(X_train.shape[1], X_train.shape[2]))

x = keras.layers.LSTM(150, return_sequences=True)(inputs)
x = keras.layers.LSTM(150, return_sequences=True)(x)
x = keras.layers.LSTM(150, return_sequences=True)(x)

outputs = keras.layers.Dense(1, activation='linear')(x)

model = keras.Model(inputs=inputs, outputs=outputs)
model.compile(optimizer='adam', loss='mse')
model.summary()

# Train the model
history = model.fit(X_train, y_train, epochs=2,
                    batch_size=32, validation_split=0.2)

# Make Prediction
predicted = model.predict(X)

# Append the predictions to a list
test_predicted = []

for i in predicted:
    test_predicted.append(i[0][0])


df_predicted = price_volume_dataset[1:][['Date', 'Close']]
df_predicted['predictions'] = test_predicted

close = []

for i in training_set_scaled:
    close.append(i[0])


df_predicted['Close'] = close[1:]

funcModels.interactive_plot(df_predicted, 'Original vs. LSTM Predictions')
