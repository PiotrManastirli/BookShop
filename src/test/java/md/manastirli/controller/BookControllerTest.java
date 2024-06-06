package md.manastirli.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import md.manastirli.model.Book;
import md.manastirli.service.BookService;
import org.jboss.logging.annotations.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
class BookControllerTest {

    @InjectMock
    BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Message("serviciul extern intoarece acelasi raspuns pentru orice chemare")
    public void testFindBooksByTitle() {
        List<Book> books = Arrays.asList(
                new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 9.99),
                new Book(2L, "The Great Gatsb", "F. Scott Fitzgerald", 14.99)
        );

        when(bookService.findBooksByTitle("The Great Gatsby")).thenReturn(books);

        given()
                .when().get("/book/title/The Great Gatsby")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].title", is("The Great Gatsby"));
//                .body("[1].title", is("The Great Gatsby"));

        verify(bookService, times(1)).findBooksByTitle("The Great Gatsby");
    }

    @Test
    @Message("Serviciul extern intoarce raspunsuri diferite pentru chemari consecutive ")
    public void testFindBooksByTitleWithDifferentResponses() {
        List<Book> books1 = Arrays.asList(
                new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 9.99)
        );

        List<Book> books2 = Arrays.asList(
                new Book(2L, "The Great Gatsby", "F. Scott Fitzgerald", 14.99),
                new Book(3L, "The Great Gatsby", "F. Scott Fitzgerald", 19.99)
        );

        when(bookService.findBooksByTitle("The Great Gatsby"))
                .thenReturn(books1)
                .thenReturn(books2);

        // First call
        given()
                .when().get("/book/title/The Great Gatsby")
                .then()
                .statusCode(200)
                .body("$.size()", is(1))
                .body("[0].title", is("The Great Gatsby"))
                .body("[0].price", is(9.99f));

        // Second call
        given()
                .when().get("/book/title/The Great Gatsby")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].title", is("The Great Gatsby"))
                .body("[0].price", is(14.99f))
                .body("[1].title", is("The Great Gatsby"))
                .body("[1].price", is(19.99f));

        verify(bookService, times(2)).findBooksByTitle("The Great Gatsby");
    }

    @Test
    public void testExternalServiceReturnsDifferentResponsesBasedOnParameters() {
        when(bookService.findBooksByPriceBelow(50.0)).thenReturn(Arrays.asList(
                new Book(1L, "Book1", "Author1", 40.0),
                new Book(2L, "Book2", "Author2", 45.0)
        ));

        when(bookService.findBooksByPriceBelow(100.0)).thenReturn(Arrays.asList(
                new Book(3L, "Book3", "Author3", 80.0),
                new Book(4L, "Book4", "Author4", 90.0)
        ));

        given()
                .when().get("/book/price-below/50.0")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].title", is("Book1"))
                .body("[0].author", is("Author1"))
                .body("[0].price", is(40.0f))
                .body("[1].title", is("Book2"))
                .body("[1].author", is("Author2"))
                .body("[1].price", is(45.0f));

        given()
                .when().get("/book/price-below/100.0")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].title", is("Book3"))
                .body("[0].author", is("Author3"))
                .body("[0].price", is(80.0f))
                .body("[1].title", is("Book4"))
                .body("[1].author", is("Author4"))
                .body("[1].price", is(90.0f));

        verify(bookService, times(1)).findBooksByPriceBelow(50.0);
        verify(bookService, times(1)).findBooksByPriceBelow(100.0);
    }

    @Test
    public void testExternalServiceThrowsError() {
        Mockito.when(bookService.findBooksByTitle("Invalid")).thenThrow(new RuntimeException("No books found with title Invalid"));
        given()
                .when().get("/book/title/Invalid")
                .then()
                .statusCode(500);
        Mockito.verify(bookService, times(1)).findBooksByTitle("Invalid");
    }


}