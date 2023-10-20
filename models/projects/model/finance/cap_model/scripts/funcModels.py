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

from copy import copy

# --------------------------------------------------------------- #
#### Normalize Function ####
# --------------------------------------------------------------- #

def normalize(df):
    x = df.copy()
    for i in x.columns[1:]:
        x[i] = x[i] / x[i][0]
    return x

# --------------------------------------------------------------- #
#### Interactive Plot Function ####
# --------------------------------------------------------------- #

def interactive_plot(df, title):
    fig = px.line(title = title)
    for i in df.columns[1:]:
        fig.add_scatter(x = df['Date'], y = df[i], name = i)
    fig.show()
    
# --------------------------------------------------------------- #
#### Daily Returns Function ####
# --------------------------------------------------------------- #

def daily_return(df):
    df_daily_returns = df.copy()
    for i in df.columns[1:]:
        for j in range(1, len(df)):
            df_daily_returns[i][j] = ((df[i][j] - df[i][j-1]) / df[i][j-1]) * 100
        
        df_daily_returns[i][0] = 0
    return df_daily_returns
    
    