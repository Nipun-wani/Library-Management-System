package com.example.bookinventory.service;

import com.example.bookinventory.issued.IssuedBook;
import com.example.bookinventory.model.Book;
import com.example.bookinventory.repository.BookRepository;
import com.example.bookinventory.repository.IssuedBookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IssuedBookService {

    private final IssuedBookRepository issuedBookRepository;
    private final BookRepository bookRepository;

    public IssuedBookService(IssuedBookRepository issuedBookRepository, BookRepository bookRepository) {
        this.issuedBookRepository = issuedBookRepository;
        this.bookRepository = bookRepository;
    }

    // Save issued record and reduce quantity
    public boolean issueBook(Long bookId, String personName, String mobile, LocalDate issueDate) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (book.getQuantity() > 0) {
                // Reduce quantity
                book.setQuantity(book.getQuantity() - 1);
                bookRepository.save(book);

                // Save issued record
                IssuedBook issuedBook = new IssuedBook(bookId, personName, mobile, issueDate);
                issuedBookRepository.save(issuedBook);
                return true;
            }
        }
        return false;
    }

    // List all issued books
    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookRepository.findAll();
    }

    //Return Book
    public void returnIssuedBook(Long issueId) {
        Optional<IssuedBook> optionalIssued = issuedBookRepository.findById(issueId);

        if (optionalIssued.isPresent()) {
            IssuedBook issuedBook = optionalIssued.get();

            // Step A: Increase quantity
            Optional<Book> optionalBook = bookRepository.findById(issuedBook.getBookId());
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                book.setQuantity(book.getQuantity() + 1);
                bookRepository.save(book);
            }

            // Step B: Remove issued record
            issuedBookRepository.deleteById(issueId);
        }
    }

}
