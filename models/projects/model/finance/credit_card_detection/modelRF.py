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
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

np.random.seed(2)

dataset = pd.read_csv('../data/creditcard.csv')

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

# --------------------------------------------------------------- #
#### Random Forest ####
# --------------------------------------------------------------- #


random_forest = RandomForestClassifier(n_estimators=100)
random_forest.fit(X_train, y_train.values.ravel())

y_pred = random_forest.predict(X_test)

random_forest.score(X_test, y_test)

# --------------------------------------------------------------- #
#### Predict Model ####
# --------------------------------------------------------------- #


# Round 1
cnf_matrix = confusion_matrix(y_test, y_pred)

cmModel.plot_confusion_matrix(cnf_matrix, classes=[0, 1])

plt.show()

# Round 2
y_pred = random_forest.predict(X)
cnf_matrix = confusion_matrix(y, y_pred.round())

cmModel.plot_confusion_matrix(cnf_matrix, classes=[0, 1])

plt.show()
