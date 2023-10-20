# ------------------------------------------------------------------------------
# Libraries
# ------------------------------------------------------------------------------
library(recommenderlab)
library(ggplot2)
library(data.table)
library(reshape2)
library(dplyr)
library(tidyverse)
library(tidyr)

# ------------------------------------------------------------------------------
# Load the Datasets
# ------------------------------------------------------------------------------

df_movie_data <- read.csv("../../data/movies.csv", stringsAsFactors = FALSE)
df_rating_data <- read.csv("../../data/ratings.csv")

# ------------------------------------------------------------------------------
# Data Pre-processing
# ------------------------------------------------------------------------------

movie_genre <- as.data.frame(df_movie_data$genres, stringsAsFactors=FALSE)

movie_genre2 <- as.data.frame(tstrsplit(movie_genre[,1], '[|]', 
                                        type.convert=TRUE), 
                              stringsAsFactors=FALSE)


colnames(movie_genre2) <- c(1:10)

list_genre <- c("Action", "Adventure", "Animation", "Children", 
                "Comedy", "Crime","Documentary", "Drama", "Fantasy",
                "Film-Noir", "Horror", "Musical", "Mystery","Romance",
                "Sci-Fi", "Thriller", "War", "Western")
genre_mat1 <- matrix(0,10330,18)
genre_mat1[1,] <- list_genre
colnames(genre_mat1) <- list_genre

for (index in 1:nrow(movie_genre2)) {
  for (col in 1:ncol(movie_genre2)) {
    gen_col = which(genre_mat1[1,] == movie_genre2[index,col])
    genre_mat1[index+1,gen_col] <- 1
  }
}

#remove first row, which was the genre list
genre_mat2 <- as.data.frame(genre_mat1[-1,], stringsAsFactors=FALSE) 

for (col in 1:ncol(genre_mat2)) {
  #convert from characters to integers
  genre_mat2[,col] <- as.integer(genre_mat2[,col]) 
} 
str(genre_mat2)

# Create a search matrix
SearchMatrix <- cbind(df_movie_data[,1:2], genre_mat2[])
head(SearchMatrix)

# Create a rating Matrix

ratingMatrix <- dcast(df_rating_data, userId~movieId, value.var = "rating", na.rm=FALSE)
ratingMatrix <- as.matrix(ratingMatrix[,-1]) #remove userIds
#Convert rating matrix into a recommenderlab sparse matrix
ratingMatrix <- as(ratingMatrix, "realRatingMatrix")
ratingMatrix

# Using the recommendation model
recommendation_model <- recommenderRegistry$get_entries(dataType = "realRatingMatrix")
names(recommendation_model)

lapply(recommendation_model, "[[", "description")

recommendation_model$IBCF_realRatingMatrix$parameters