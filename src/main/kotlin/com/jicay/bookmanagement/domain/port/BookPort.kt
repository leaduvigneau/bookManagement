package com.jicay.bookmanagement.domain.port

import com.jicay.bookmanagement.domain.model.Book

interface BookPort {
    fun getAllBooks(): List<Book>
    fun createBook(book: Book)
    fun doesBookExist(bookTitle: String) : Boolean
    fun isBookReserved(bookTitle: String) : Boolean
    fun reserveBookByTitle(bookTitle: String, isReserved: Boolean)
}