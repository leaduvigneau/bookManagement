package com.jicay.bookmanagement.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookDTOUseCaseTest {

    @InjectMockKs
    private lateinit var bookUseCase: BookUseCase

    @MockK
    private lateinit var bookPort: BookPort

    @Test
    fun `get all books should returns all books sorted by name`() {
        every { bookPort.getAllBooks() } returns listOf(
            Book("Les Misérables", "Victor Hugo"),
            Book("Hamlet", "William Shakespeare")
        )

        val res = bookUseCase.getAllBooks()

        assertThat(res).containsExactly(
            Book("Hamlet", "William Shakespeare"),
            Book("Les Misérables", "Victor Hugo")
        )
    }

    @Test
    fun `add book`() {
        justRun { bookPort.createBook(any()) }

        val book = Book("Les Misérables", "Victor Hugo")

        bookUseCase.addBook(book)

        verify(exactly = 1) { bookPort.createBook(book) }
    }
    @Test
    fun `reserve book when book does not exist`() {
        every { bookPort.doesBookExist("NonExistingBook") } returns false

        val result = bookUseCase.reserveBook("NonExistingBook")

        assertThat(result).isEqualTo("Book not found.")
    }

    @Test
    fun `reserve book when book is already reserved`() {
        every { bookPort.doesBookExist("ExistingBook") } returns true
        every { bookPort.isBookReserved("ExistingBook") } returns true

        val result = bookUseCase.reserveBook("ExistingBook")

        assertThat(result).isEqualTo("Book is already reserved.")
    }

    @Test
    fun `successfully reserve a book`() {
        every { bookPort.doesBookExist("AvailableBook") } returns true
        every { bookPort.isBookReserved("AvailableBook") } returns false
        justRun { bookPort.reserveBookByTitle("AvailableBook", true) }

        val result = bookUseCase.reserveBook("AvailableBook")

        assertThat(result).isEqualTo("Book reserved successfully.")

        verify(exactly = 1) { bookPort.reserveBookByTitle("AvailableBook", true) }
    }

}