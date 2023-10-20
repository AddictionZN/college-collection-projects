# ------------------------------------------------------------------------------
# Loading the R Scripts
# ------------------------------------------------------------------------------
projectFolder <- paste(getwd(), "/", sep = '')

source(paste0(projectFolder,"1. Movie Recommendation/data preparation.R"))

# ------------------------------------------------------------------------------
# Collaborative Filtering System
# ------------------------------------------------------------------------------
sampled_data<- sample(x = c(TRUE, FALSE),
                      size = nrow(movie_ratings),
                      replace = TRUE,
                      prob = c(0.8, 0.2))

train_data <- movie_ratings[sampled_data, ]
test_data <- movie_ratings[!sampled_data, ]

recommendation_system <- recommenderRegistry$get_entries(dataType ="realRatingMatrix")
recommendation_system$IBCF_realRatingMatrix$parameters

recommen_model <- Recommender(data = train_data,
                              method = "IBCF",
                              parameter = list(k = 30))

recommen_model

model_info <- getModel(recommen_model)

class(model_info$sim)

top_items <- 20
image(model_info$sim[1:top_items, 1:top_items],
      main = "Heatmap of the first rows and columns")

sum_rows <- rowSums(model_info$sim > 0)
table(sum_rows)

sum_cols <- colSums(model_info$sim > 0)
qplot(sum_cols, fill=I("steelblue"), col=I("red"))+ ggtitle("Distribution of the column count")

# ------------------------------------------------------------------------------
# Using the recommendation system
# ------------------------------------------------------------------------------

top_recommendations <- 10 # the number of items to recommend to each user
predicted_recommendations <- predict(object = recommen_model,
                                     newdata = test_data,
                                     n = top_recommendations)
predicted_recommendations

user1 <- predicted_recommendations@items[[1]] # recommendation for the first user
movies_user1 <- predicted_recommendations@itemLabels[user1]
movies_user2 <- movies_user1
for (index in 1:10){
  movies_user2[index] <- as.character(subset(df_movie_data,
                                             df_movie_data$movieId == movies_user1[index])$title)
}

movies_user2

recommendation_matrix <- sapply(predicted_recommendations@items,
                                function(x){ as.integer(colnames(movie_ratings)[x]) }) # matrix with the recommendations for each user

recommendation_matrix[,1:4]