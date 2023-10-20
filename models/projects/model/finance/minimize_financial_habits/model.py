# -*- coding: utf-8 -*-
"""

@author: bevan
@project: Financial Habits with Churn Rate
    
"""
# --------------------------------------------------------------- #
#### Importing Libraries ####
# --------------------------------------------------------------- #

from sklearn.feature_selection import RFE
from sklearn.model_selection import cross_val_score
from sklearn.metrics import confusion_matrix, accuracy_score, f1_score, precision_score, recall_score
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
import pandas as pd
import numpy as np
import random
import seaborn as sn
import matplotlib.pyplot as plt

dataset = pd.read_csv('../data/new_churn_data.csv')  # get the dataset

# --------------------------------------------------------------- #
#### Cleaning Data ####
# --------------------------------------------------------------- #

# Data Preperation
user_identifier = dataset['user']

dataset = dataset.drop(columns=['user'])

# One-Hot Encoding
dataset.housing.value_counts()
dataset = pd.get_dummies(dataset)
dataset.columns
dataset = dataset.drop(
    columns=['housing_na', 'zodiac_sign_na', 'payment_type_na'])

# Splitting the dataset into the Training set and Test set

X_train, X_test, y_train, y_test = train_test_split(dataset.drop(columns='churn'),
                                                    dataset['churn'],
                                                    test_size=0.2,
                                                    random_state=0)


# --------------------------------------------------------------- #
#### Feature Scaling ####
# --------------------------------------------------------------- #

# Balancing the Training Set
y_train.value_counts()

pos_index = y_train[y_train.values == 1].index
neg_index = y_train[y_train.values == 0].index

if len(pos_index) > len(neg_index):
    higher = pos_index
    lower = neg_index
else:
    lower = pos_index
    higher = neg_index

random.seed(0)
higher = np.random.choice(higher, size=len(lower))
lower = np.asarray(lower)

new_indexes = np.concatenate((lower, higher))

X_train = X_train.loc[new_indexes, ]
y_train = y_train[new_indexes]


# Feature Scaling

sc_X = StandardScaler()

X_train2 = pd.DataFrame(sc_X.fit_transform(X_train))
X_test2 = pd.DataFrame(sc_X.transform(X_test))

X_train2.columns = X_train.columns.values
X_test.columns = X_test.columns.values

X_train2.index = X_train.index.values
X_test.index = X_test.index.values

X_train = X_train2
X_test = X_test2


# --------------------------------------------------------------- #
#### Model Building ####
# --------------------------------------------------------------- #

# Fitting Model to the Training Set

classifier = LogisticRegression(random_state=0)
classifier.fit(X_train, y_train)

# Prediction Test Set
y_pred = classifier.predict(X_test)

# Evaluating Results

cm = confusion_matrix(y_test, y_pred)
accuracy_score(y_test, y_pred)
precision_score(y_test, y_pred)
recall_score(y_test, y_pred)
f1_score(y_test, y_pred)

# Confusion Matrix
df_cm = pd.DataFrame(cm, index=(0, 1), columns=(0, 1))
plt.figure(figsize=(10, 7))
sn.set(font_scale=1.4)
sn.heatmap(df_cm, annot=True, fmt='g')
print("Test Data Accuracy: %0.4f" % accuracy_score(y_test, y_pred))


# --------------------------------------------------------------- #
#### Applying k-fold Cross Validation ####
# --------------------------------------------------------------- #


accuracies = cross_val_score(estimator=classifier,
                             X=X_train,
                             y=y_train,
                             cv=10)

# Analyzing Coefficients
pd.concat([pd.DataFrame(X_train.columns, columns=['features']),
           pd.DataFrame(np.transpose(classifier.coef_), columns=["coef"])],
          axis=1)


# --------------------------------------------------------------- #
#### Feature Selection ####
# --------------------------------------------------------------- #


# Model to Test
classifier = LogisticRegression()
rfe = RFE(classifier, 20)
rfe = rfe.fit(X_train, y_train)

# Selection of the attributes
print(rfe.support_)
X_train.columns[rfe.support_]
rfe.ranking_

# Fitting Model to the Training Set

classifier = LogisticRegression(random_state=0)
classifier.fit(X_train[X_train.columns[rfe.support_]], y_train)

# Prediction Test Set
y_pred = classifier.predict(X_test[X_test.columns[rfe.support_]])

# Evaluating Results

cm = confusion_matrix(y_test, y_pred)
accuracy_score(y_test, y_pred)
precision_score(y_test, y_pred)
recall_score(y_test, y_pred)
f1_score(y_test, y_pred)

# Confusion Matrix
df_cm = pd.DataFrame(cm, index=(0, 1), columns=(0, 1))
plt.figure(figsize=(10, 7))
sn.set(font_scale=1.4)
sn.heatmap(df_cm, annot=True, fmt='g')
print("Test Data Accuracy: %0.4f" % accuracy_score(y_test, y_pred))

# Analyzing Coefficients
pd.concat([pd.DataFrame(X_train.columns[rfe.support_], columns=['features']),
           pd.DataFrame(np.transpose(classifier.coef_), columns=["coef"])],
          axis=1)


# --------------------------------------------------------------- #
#### Final Results ####
# --------------------------------------------------------------- #
final_results = pd.concat([y_test, user_identifier], axis=1).dropna()
final_results['predicted_churn'] = y_pred
final_results = final_results[['user', 'churn',
                               'predicted_churn']].reset_index(drop=True)
