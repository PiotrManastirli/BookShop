package md.manastirli.repository;

import jakarta.enterprise.context.ApplicationScoped;
import md.manastirli.dto.AddBook;
import md.manastirli.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookRepository {

    private final List<Book> books;

    public BookRepository() {
        this.books = new ArrayList<>(Arrays.asList(
                new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 9.99),
                new Book(2L, "To Kill a Mockingbird", "Harper Lee", 14.99),
                new Book(3L, "1984", "George Orwell", 19.99),
                new Book(4L, "Pride and Prejudice", "Jane Austen", 24.99),
                new Book(5L, "The Catcher in the Rye", "J.D. Salinger", 29.99),
                new Book(6L, "The Hobbit", "J.R.R. Tolkien", 34.99),
                new Book(7L, "The Lord of the Rings", "J.R.R. Tolkien", 39.99),
                new Book(8L, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", 44.99),
                new Book(9L, "To the Lighthouse", "Virginia Woolf", 49.99),
                new Book(10L, "Brave New World", "Aldous Huxley", 54.99),
                new Book(11L, "The Odyssey", "Homer", 59.99),
                new Book(12L, "The Divine Comedy", "Dante Alighieri", 64.99),
                new Book(13L, "Don Quixote", "Miguel de Cervantes", 69.99),
                new Book(14L, "War and Peace", "Leo Tolstoy", 74.99),
                new Book(15L, "Crime and Punishment", "Fyodor Dostoevsky", 79.99),
                new Book(16L, "The Brothers Karamazov", "Fyodor Dostoevsky", 84.99),
                new Book(17L, "Anna Karenina", "Leo Tolstoy", 89.99),
                new Book(18L, "Moby-Dick", "Herman Melville", 94.99),
                new Book(19L, "The Picture of Dorian Gray", "Oscar Wilde", 99.99),
                new Book(20L, "Wuthering Heights", "Emily Brontë", 104.99)
        ));
    }

    public List<Book> findAll() {
        return books;
    }

    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public Book save(AddBook addBook) {
        Long newId = generateId();
        Book book = new Book(newId, addBook.getTitle(), addBook.getAuthor(), addBook.getPrice());
        books.add(book);
        return book;
    }

    public Book update(Book book) {
        int index = findIndexById(book.getId());
        if (index != -1) {
            Book existingBook = books.get(index);
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPrice(book.getPrice());
            return existingBook;
        } else {
            throw new RuntimeException("Book not found with id: " + book.getId());
        }
    }

    public String deleteById(Long id) {
        int index = findIndexById(id);
        if (index != -1) {
            books.remove(index);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
        return "Book deleted: " + id;
    }

    private Long generateId() {
        Long maxId = books.stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    private int findIndexById(Long id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
