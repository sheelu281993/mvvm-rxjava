package com.example.halodoc.model

data class Url(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
)