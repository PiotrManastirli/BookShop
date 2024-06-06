package md.manastirli.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import md.manastirli.model.Book;
import md.manastirli.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

    public List<Book> findBooksByPriceBelow(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        List<Book> books = bookRepository.findAll().stream()
                .filter(book -> book.getPrice() < price)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new RuntimeException("No books found with price below " + price);
        }
        return books;
    }

    public List<Book> findBooksByPriceAbove(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        List<Book> books = bookRepository.findAll().stream()
                .filter(book -> book.getPrice() > price)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            throw new RuntimeException("No books found with price above " + price);
        }

        return books;
    }

    public List<Book> findBooksByTitle(String title) {
        List<Book> books = bookRepository.findAll().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            throw new RuntimeException("No books found with title " + title);
        }

        return books;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> books = bookRepository.findAll().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            throw new RuntimeException("No books found with author " + author);
        }

        return books;
    }
}
