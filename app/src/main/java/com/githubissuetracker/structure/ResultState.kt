package com.githubissuetracker.structure

sealed class ResultState {
    object Loading : ResultState()
    data class Success(val success: Boolean, val request: String, val message: String) :
        ResultState()
}
