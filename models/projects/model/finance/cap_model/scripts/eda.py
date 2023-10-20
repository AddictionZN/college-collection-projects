# -*- coding: utf-8 -*-
"""

@author: bevan
@project: Capital Asset Pricing Model (CAPM)
    
"""

# --------------------------------------------------------------- #
#### Importing Libraries ####
# --------------------------------------------------------------- #

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import plotly.express as px
import plotly.figure_factory as ff
import plotly.graph_objects as go
import funcModels

from copy import copy
from scipy import stats

# --------------------------------------------------------------- #
#### EDA ####
# --------------------------------------------------------------- #

stock_dataset = pd.read_csv('../data/stock.csv')

stock_dataset = stock_dataset.sort_values(by = ['Date'])

funcModels.interactive_plot(stock_dataset, 'Prices')

funcModels.interactive_plot(funcModels.normalize(stock_dataset), 'Normalized Prices')

# --------------------------------------------------------------- #
#### Daily Returns ####
# --------------------------------------------------------------- #

stock_dataset_return = funcModels.daily_return(stock_dataset)

# --------------------------------------------------------------- #
#### Calculate Beta for single stock ####
# --------------------------------------------------------------- #

stock_dataset_return.plot(kind = 'scatter', x = 'sp500', y = 'AAPL')

beta, alpha = np.polyfit(stock_dataset_return['sp500'],stock_dataset_return['AAPL'], 1)

print('Beta for {} stock is {} and alpha is {}'.format('AAPL',beta, alpha))

stock_dataset_return.plot(kind = 'scatter', x = 'sp500', y = 'AAPL')

plt.plot(stock_dataset_return['sp500'], beta * stock_dataset_return['sp500'] + alpha)






