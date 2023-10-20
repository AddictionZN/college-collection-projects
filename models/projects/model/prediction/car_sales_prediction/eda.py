# -*- coding: utf-8 -*-
"""

@author: bevan
@project: Car Purchase Amount Predictions
    
"""
# --------------------------------------------------------------- #
#### Importing Libraries ####
# --------------------------------------------------------------- #

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import tensorflow.keras

from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense

dataset = pd.read_csv('../data/Car_Purchasing_Data.csv', encoding=('ISO-8859-1'))  # get the dataset

# --------------------------------------------------------------- #
#### EDA ####
# --------------------------------------------------------------- #

dataset.head()
dataset.columns
dataset.describe()

sns.pairplot(dataset)

# --------------------------------------------------------------- #
#### Create Testing and Training Dataset/Data Cleaning ####
# --------------------------------------------------------------- #

X = dataset.drop(['Customer Name', 'Customer e-mail', 'Country', 'Car Purchase Amount'], axis = 1)
y = dataset['Car Purchase Amount']

y = y.values.reshape(-1,1)

scaler = MinMaxScaler()

X_scaled = scaler.fit_transform(X)
y_scaled = scaler.fit_transform(y)

# --------------------------------------------------------------- #
#### Training the Model ####
# --------------------------------------------------------------- #

X_train, X_test, y_train, y_test = train_test_split(X_scaled, y_scaled)

model = Sequential()
model.add(Dense(40, input_dim = 5 , activation = 'relu'))
model.add(Dense(40, activation = 'relu'))
model.add(Dense(1, activation = 'linear'))

model.summary()

model.compile(optimizer = 'adam', loss='mean_squared_error')

epochs_hist = model.fit(X_train, y_train, 
                        epochs = 100, 
                        batch_size = 50, 
                        verbose = 1,
                        validation_split = 0.2)

# --------------------------------------------------------------- #
#### Model Validation ####
# --------------------------------------------------------------- #

epochs_hist.history.keys()

plt.plot(epochs_hist.history['loss'])
plt.plot(epochs_hist.history['val_loss'])
plt.title('Model Loss Progress During Training')
plt.ylabel('Training and Validation Loss')
plt.xlabel('Epoch Number')
plt.legend(['Training Loss', 'Validation Loss'])
