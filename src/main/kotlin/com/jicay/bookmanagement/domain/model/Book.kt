package com.jicay.bookmanagement.domain.model

data class Book(
        val name: String,
        val author: String,
        var isReserved: Boolean = false // Default value as false, indicating not reserved
)