# -*- coding: utf-8 -*-
"""

@author: bevan
@project: E-Signing a Loan Based on Financial History
    
"""
# --------------------------------------------------------------- #
#### Importing Libraries ####
# --------------------------------------------------------------- #

from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import cross_val_score
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.metrics import confusion_matrix, accuracy_score, f1_score, precision_score, recall_score
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
import pandas as pd
import numpy as np
import time
import random
import seaborn as sn
import matplotlib.pyplot as plt

random.seed()

dataset = pd.read_csv('../data/Financial-Data.csv')  # get the dataset

# --------------------------------------------------------------- #
#### Feature Engineering ####
# --------------------------------------------------------------- #

dataset = dataset.drop(columns=['months_employed'])
dataset['personal_account_months'] = (
    dataset.personal_account_m + (dataset.personal_account_y * 12))
dataset[['personal_account_m', 'personal_account_y',
         'personal_account_months']].head()
dataset = dataset.drop(columns=['personal_account_m', 'personal_account_y'])

# --------------------------------------------------------------- #
#### Data Preproccessing ####
# --------------------------------------------------------------- #

# One Hot Encoding
dataset = pd.get_dummies(dataset)
dataset.columns
dataset = dataset.drop(columns=['pay_schedule_semi-monthly'])

# Removing extra columns
response = dataset['e_signed']
users = dataset['entry_id']
dataset = dataset.drop(columns=['e_signed', 'entry_id'])

# Splitting into Train and Test set

X_train, X_test, y_train, y_test = train_test_split(dataset,
                                                    response,
                                                    test_size=0.2,
                                                    random_state=0)

# --------------------------------------------------------------- #
#### Feature Scaling ####
# --------------------------------------------------------------- #

sc_X = StandardScaler()

X_train2 = pd.DataFrame(sc_X.fit_transform(X_train))
X_test2 = pd.DataFrame(sc_X.fit_transform(X_test))

X_train2.columns = X_train.columns.values
X_test2.columns = X_test.columns.values

X_train2.index = X_train.index.values
X_test2.index = X_test.index.values

X_train = X_train2
X_test = X_test2


# --------------------------------------------------------------- #
#### Linear Regression (lasso) ####
# --------------------------------------------------------------- #

# Comparing models

classifier = LogisticRegression(random_state=0, penalty='l2')
classifier.fit(X_train, y_train)

# Predicting Test Sets
y_pred = classifier.predict(X_test)


acc = accuracy_score(y_test, y_pred)
prec = precision_score(y_test, y_pred)
rec = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)

results = pd.DataFrame([['Linear Regression (lasso)', acc, prec, rec, f1]],
                       columns=['Model', 'Accuracy', 'Precision', 'Recall', 'F1 Score'])


# --------------------------------------------------------------- #
#### SVM (Linear) ####
# --------------------------------------------------------------- #

classifier = SVC(random_state=0, kernel='linear')
classifier.fit(X_train, y_train)

# Predicting Test Sets
y_pred = classifier.predict(X_test)


acc = accuracy_score(y_test, y_pred)
prec = precision_score(y_test, y_pred)
rec = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)

model_results = pd.DataFrame([['SVM (Linear)', acc, prec, rec, f1]],
                             columns=['Model', 'Accuracy', 'Precision', 'Recall', 'F1 Score'])

results = results.append(model_results, ignore_index=True)

# --------------------------------------------------------------- #
#### SVM (RBF) ####
# --------------------------------------------------------------- #

classifier = SVC(random_state=0, kernel='rbf')
classifier.fit(X_train, y_train)

# Predicting Test Sets
y_pred = classifier.predict(X_test)


acc = accuracy_score(y_test, y_pred)
prec = precision_score(y_test, y_pred)
rec = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)

model_results = pd.DataFrame([['SVM (RBF)', acc, prec, rec, f1]],
                             columns=['Model', 'Accuracy', 'Precision', 'Recall', 'F1 Score'])

results = results.append(model_results, ignore_index=True)

# --------------------------------------------------------------- #
#### Random Forest Classifier ####
# --------------------------------------------------------------- #

classifier = RandomForestClassifier(random_state=0, n_estimators=100,
                                    criterion='entropy')
classifier.fit(X_train, y_train)

# Predicting Test Sets
y_pred = classifier.predict(X_test)


acc = accuracy_score(y_test, y_pred)
prec = precision_score(y_test, y_pred)
rec = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)

model_results = pd.DataFrame([['Random Forest (n=100)', acc, prec, rec, f1]],
                             columns=['Model', 'Accuracy', 'Precision', 'Recall', 'F1 Score'])

results = results.append(model_results, ignore_index=True)

# --------------------------------------------------------------- #
#### K-fold Cross Validation ####
# --------------------------------------------------------------- #

accuracies = cross_val_score(estimator=classifier, X=X_train, y=y_train,
                             cv=10)

print('Random Forest Classifier: %0.2f (+/- %0.2f)' %
      (accuracies.mean(), accuracies.std() * 2))

# --------------------------------------------------------------- #
#### Parameter Tuning ####
# --------------------------------------------------------------- #

# Applying Grid Search

# Round 1: Entropy
parameters = {'max_depth': [3, None],
              'max_features': [1, 5, 10],
              'min_samples_split': [2, 5, 10],
              'min_samples_leaf': [1, 5, 10],
              'bootstrap': [True, False],
              'criterion': ['entropy']}


grid_search = GridSearchCV(estimator=classifier,
                           param_grid=parameters,
                           scoring='accuracy',
                           cv=10,
                           n_jobs=1)

t0 = time.time()
grid_search = grid_search.fit(X_train, y_train)
t1 = time.time()

print('Time Took %0.2f seconds' % (t1 - t0))

rf_best_accuracy = grid_search.best_score_
rf_best_parameters = grid_search.best_params_
rf_best_accuracy, rf_best_parameters

# Round 2: Entropy
parameters = {'max_depth': [None],
              'max_features': [3, 5, 7],
              'min_samples_split': [8, 10, 12],
              'min_samples_leaf': [1, 2, 3],
              'bootstrap': [True],
              'criterion': ['entropy']}


grid_search = GridSearchCV(estimator=classifier,
                           param_grid=parameters,
                           scoring='accuracy',
                           cv=10,
                           n_jobs=1)

t0 = time.time()
grid_search = grid_search.fit(X_train, y_train)
t1 = time.time()

print('Time Took %0.2f seconds' % (t1 - t0))

rf_best_accuracy = grid_search.best_score_
rf_best_parameters = grid_search.best_params_
rf_best_accuracy, rf_best_parameters

# --------------------------------------------------------------- #
#### Predicting Test Set ####
# --------------------------------------------------------------- #

y_pred = grid_search.predict(X_test)


acc = accuracy_score(y_test, y_pred)
prec = precision_score(y_test, y_pred)
rec = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)

model_results = pd.DataFrame([['Random Forest (n=100, GSx2 + Entropy)', acc, prec, rec, f1]],
                             columns=['Model', 'Accuracy', 'Precision', 'Recall', 'F1 Score'])

results = results.append(model_results, ignore_index=True)


# Round 1: Gini
parameters = {'max_depth': [3, None],
              'max_features': [1, 5, 10],
              'min_samples_split': [2, 5, 10],
              'min_samples_leaf': [1, 5, 10],
              'bootstrap': [True, False],
              'criterion': ['gini']}


grid_search = GridSearchCV(estimator=classifier,
                           param_grid=parameters,
                           scoring='accuracy',
                           cv=10,
                           n_jobs=1)

t0 = time.time()
grid_search = grid_search.fit(X_train, y_train)
t1 = time.time()

print('Time Took %0.2f seconds' % (t1 - t0))

rf_best_accuracy = grid_search.best_score_
rf_best_parameters = grid_search.best_params_
rf_best_accuracy, rf_best_parameters

# Round 2: Gini
parameters = {'max_depth': [None],
              'max_features': [3, 5, 7],
              'min_samples_split': [8, 10, 12],
              'min_samples_leaf': [1, 2, 3],
              'bootstrap': [True],
              'criterion': ['gini']}


grid_search = GridSearchCV(estimator=classifier,
                           param_grid=parameters,
                           scoring='accuracy',
                           cv=10,
                           n_jobs=1)

t0 = time.time()
grid_search = grid_search.fit(X_train, y_train)
t1 = time.time()

print('Time Took %0.2f seconds' % (t1 - t0))

rf_best_accuracy = grid_search.best_score_
rf_best_parameters = grid_search.best_params_
rf_best_accuracy, rf_best_parameters

y_pred = grid_search.predict(X_test)


acc = accuracy_score(y_test, y_pred)
prec = precision_score(y_test, y_pred)
rec = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)

model_results = pd.DataFrame([['Random Forest (n=100, GSx2 + Entropy)', acc, prec, rec, f1]],
                             columns=['Model', 'Accuracy', 'Precision', 'Recall', 'F1 Score'])

results = results.append(model_results, ignore_index=True)

# --------------------------------------------------------------- #
#### Final Results ####
# --------------------------------------------------------------- #

final_results = pd.concate([y_test, users])
final_results['predictions'] = y_pred
final_results = final_results[['entry_id', 'e_signed', 'predictions']]
