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
    fig = px.line(title=title)
    for i in df.columns[1:]:
        fig.add_scatter(x=df['Date'], y=df[i], name=i)
    fig.show()

# --------------------------------------------------------------- #
#### Daily Returns Function ####
# --------------------------------------------------------------- #


def daily_return(df):
    df_daily_returns = df.copy()
    for i in df.columns[1:]:
        for j in range(1, len(df)):
            df_daily_returns[i][j] = (
                (df[i][j] - df[i][j-1]) / df[i][j-1]) * 100

        df_daily_returns[i][0] = 0
    return df_daily_returns

# --------------------------------------------------------------- #
#### Individual Stock Function ####
# --------------------------------------------------------------- #


def individual_stock(price_df, vol_df, name):
    return pd.DataFrame({'Date': price_df['Date'],
                         'Close': price_df[name],
                         'Volume': vol_df[name]})

# --------------------------------------------------------------- #
#### Trading Window Function ####
# --------------------------------------------------------------- #


def trading_window(data):
    n = 1
    data['Target'] = data[['Close']].shift(-n)
    return data

# --------------------------------------------------------------- #
#### Show Data Plot Function ####
# --------------------------------------------------------------- #


def show_plot(data, title):
    plt.figure(figsize=(13, 5))
    plt.plot(data, linewidth=3)
    plt.title(title)
    plt.grid()
