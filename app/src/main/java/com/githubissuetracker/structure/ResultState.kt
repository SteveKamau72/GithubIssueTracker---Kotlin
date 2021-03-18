package com.githubissuetracker.structure

sealed class ResultState {
    object Loading : ResultState()
    data class Success(val boolean: Boolean, val message: String) : ResultState()
}
