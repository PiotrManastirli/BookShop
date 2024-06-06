package md.manastirli.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import md.manastirli.model.Book;
import md.manastirli.repository.BookRepository;
import md.manastirli.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
public class BookController {

    @Inject
    BookService bookService;

    @Spy
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindBooksByTitle() {
        List<Book> expectedBooks = Arrays.asList(
                new Book(1L, "Title1", "Author1", 10.0),
                new Book(2L, "Title2", "Author2", 20.0)
        );
        doReturn(expectedBooks).when(bookRepository).findAll();

        List<Book> actualBooks = bookService.findBooksByTitle("Title1");

        assertEquals(1, actualBooks.size());
        assertEquals("Title1", actualBooks.get(0).getTitle());
        assertEquals("Author1", actualBooks.get(0).getAuthor());
        assertEquals(10.0, actualBooks.get(0).getPrice(), 0.01);

        verify(bookRepository, times(1)).findAll();
    }
}
