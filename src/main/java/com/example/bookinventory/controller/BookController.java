package com.example.bookinventory.controller;


import com.example.bookinventory.model.Book;
import com.example.bookinventory.service.BookService;
import com.example.bookinventory.service.IssuedBookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final IssuedBookService issuedBookService;


    public BookController(BookService bookService, IssuedBookService issuedBookService) {
        this.bookService = bookService;
        this.issuedBookService = issuedBookService;
    }


    // Load All Books Page
    @GetMapping
    public String viewBooks(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }
        model.addAttribute("books", bookService.getAllBooks());
        return "view-books";
    }


    // Load Add Book Page
    @GetMapping("/add")
    public String addBookForm(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }
        model.addAttribute("book", new Book());
        return "add-book";
    }


    // Save Book
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/books";
    }

    // Issue Book
    @GetMapping("/issue/{id}")
    public String showIssueForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }
        model.addAttribute("bookId", id);
        return "issue-book";
    }




    // Delete Book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/issued")
    public String viewIssuedBooks(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }
        model.addAttribute("issuedBooks", issuedBookService.getAllIssuedBooks());
        return "view-issued";
    }

    @PostMapping("/issue/save")
    public String saveIssuedBook(
            @RequestParam Long bookId,
            @RequestParam String personName,
            @RequestParam String mobile,
            @RequestParam String issueDate,
            HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }

        issuedBookService.issueBook(
                bookId,
                personName,
                mobile,
                java.time.LocalDate.parse(issueDate)
        );

        return "redirect:/books";
    }

    // Return Book
    @GetMapping("/return/{issueId}")
    public String returnBook(@PathVariable Long issueId, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/";
        }

        issuedBookService.returnIssuedBook(issueId);
        return "redirect:/books/issued";
    }





}
