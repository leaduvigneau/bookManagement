package com.jicay.bookmanagement.infrastructure.driven.adapter

import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BookPort {
    override fun getAllBooks(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM BOOK", MapSqlParameterSource()) { rs, _ ->
                Book(
                    name = rs.getString("title"),
                    author = rs.getString("author"),
                    isReserved = rs.getBoolean("is_reserved")
                )
            }
    }

    override fun createBook(book: Book) {
        namedParameterJdbcTemplate
            .update("INSERT INTO BOOK (title, author) values (:title, :author)", mapOf(
                "title" to book.name,
                "author" to book.author
            ))
    }
    override fun isBookReserved(bookTitle: String): Boolean {
        val count = namedParameterJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM BOOK WHERE title = :title AND is_reserved = TRUE",
                MapSqlParameterSource().addValue("title", bookTitle),
                Int::class.java
        )
        return (count ?: 0) > 0
    }

    override fun doesBookExist(bookTitle: String): Boolean {
        val count = namedParameterJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM BOOK WHERE title = :title",
                MapSqlParameterSource().addValue("title", bookTitle),
                Int::class.java
        )
        return (count ?: 0) > 0
    }
    override fun reserveBookByTitle(bookTitle: String, isReserved: Boolean) {
        namedParameterJdbcTemplate.update(
                "UPDATE BOOK SET is_reserved = :isReserved WHERE title = :title",
                MapSqlParameterSource()
                        .addValue("title", bookTitle)
                        .addValue("isReserved", isReserved)
        )
    }
}