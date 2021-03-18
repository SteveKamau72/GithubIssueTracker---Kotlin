package com.githubissuetracker.structure

data class IssueStructure(
    val title: String,
    val state: String,
    val createdAt: String,
    val author: String,
    val url: String,
    val commentsCount: Int
)
