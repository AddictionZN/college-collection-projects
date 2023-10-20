# -*- coding: utf-8 -*-
"""

@author: bevan
@project: Credit Card Fraud Detection
    
"""

# --------------------------------------------------------------- #
#### Importing Libraries ####
# --------------------------------------------------------------- #

from sklearn.metrics import confusion_matrix
from sklearn import svm, datasets
import cmModel
from keras.layers import Dropout
from keras.layers import Dense
from keras.models import Sequential
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sn

import keras as keras

np.random.seed(2)

dataset = pd.read_csv('../data/creditcard.csv')

# --------------------------------------------------------------- #
#### EDA ####
# --------------------------------------------------------------- #

dataset.head()
dataset.columns
dataset.describe()

# --------------------------------------------------------------- #
#### Data Pre-processing ####
# --------------------------------------------------------------- #


dataset['normalizedAmount'] = StandardScaler().fit_transform(
    dataset['Amount'].values.reshape(-1, 1))
dataset = dataset.drop(['Amount'], axis=1)

dataset.head()

dataset = dataset.drop(['Time'], axis=1)

dataset.head()

X = dataset.iloc[:, dataset.columns != 'Class']
y = dataset.iloc[:, dataset.columns == 'Class']

X.head()
y.head()

# --------------------------------------------------------------- #
#### Splitting the Data ####
# --------------------------------------------------------------- #


X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.3, random_state=0)

X_train.shape

X_test.shape

# Modeling the Data
X_train = np.array(X_train)
X_test = np.array(X_test)
y_train = np.array(y_train)
y_test = np.array(y_test)

# --------------------------------------------------------------- #
#### Deep Neural Network ####
# --------------------------------------------------------------- #


model = Sequential([
    Dense(units=16, input_dim=29, activation='relu'),
    Dense(units=24, activation='relu'),
    Dropout(0.5),
    Dense(20, activation='relu'),
    Dense(24, activation='relu'),
    Dense(1, activation='sigmoid'),
])

model.summary()

# --------------------------------------------------------------- #
#### Training The Model ####
# --------------------------------------------------------------- #

model.compile(optimizer='adam', loss='binary_crossentropy',
              metrics=['accuracy'])
model.fit(X_train, y_train, batch_size=15, epochs=5)

score = model.evaluate(X_test, y_test)

print(score)

# --------------------------------------------------------------- #
#### Predict Model ####
# --------------------------------------------------------------- #


# Round 1
y_pred = model.predict(X_test)
y_test = pd.DataFrame(y_test)

cnf_matrix = confusion_matrix(y_test, y_pred.round())

print(cnf_matrix)

cmModel.plot_confusion_matrix(cnf_matrix, classes=[0, 1])

plt.show()

# Round 2
y_pred = model.predict(X)
y_expected = pd.DataFrame(y)

cnf_matrix = confusion_matrix(y_expected, y_pred.round())

cmModel.plot_confusion_matrix(cnf_matrix, classes=[0, 1])

plt.show()
