package com.example.halodoc.model

data class StoryText(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
)