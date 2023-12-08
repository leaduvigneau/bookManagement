package com.jicay.bookmanagement.domain.usecase

import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort

class BookUseCase(
    private val bookPort: BookPort
) {
    fun getAllBooks(): List<Book> {
        return bookPort.getAllBooks().sortedBy {
            it.name.lowercase()
        }
    }

    fun addBook(book: Book) {
        bookPort.createBook(book)
    }

    fun reserveBook(bookTitle: String): String {
        // Check if the book exists
        val bookExists = bookPort.doesBookExist(bookTitle)
        if (!bookExists) {
            return "Book not found."
        }

        // Check if the book is already reserved
        if (bookPort.isBookReserved(bookTitle)) {
            return "Book is already reserved."
        }

        // Reserve the book
        bookPort.reserveBookByTitle(bookTitle, true)
        return "Book reserved successfully."
    }


}