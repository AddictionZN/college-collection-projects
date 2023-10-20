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
from sklearn.tree import DecisionTreeClassifier
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
#### Decision Trees ####
# --------------------------------------------------------------- #


decision_tree = DecisionTreeClassifier()

decision_tree.fit(X_train, y_train.values.ravel())

y_pred = decision_tree.predict(X_test)

decision_tree.score(X_test, y_test)

# --------------------------------------------------------------- #
#### Predict Model ####
# --------------------------------------------------------------- #


# Round 1
y_pred = decision_tree.predict(X)

y_expected = pd.DataFrame(y)

cnf_matrix = confusion_matrix(y_expected, y_pred.round())

cmModel.plot_confusion_matrix(cnf_matrix, classes=[0, 1])
plt.show()
