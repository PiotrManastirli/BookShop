package md.manastirli.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import md.manastirli.dto.AddBook;
import md.manastirli.model.Book;
import md.manastirli.repository.BookRepository;
import md.manastirli.service.BookService;

import java.util.List;
import java.util.Optional;

@Path("/book")
public class BookController {

    @Inject
    BookService bookService;

    @Inject
    BookRepository bookRepository;

    @GET
    @Path("/find-all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            return Response.ok(bookOptional.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found with id: " + id)
                    .build();
        }
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(AddBook book) {
        Book addedBook = bookRepository.save(book);
        return Response.status(Response.Status.CREATED)
                .entity(addedBook)
                .build();
    }

    @PUT
    @Path("/update")
    public Response updateBook(Book book) {
        if (book.getId() == null) {
            throw new BadRequestException("Book ID must be provided for update.");
        }
        Book updatedBook = bookRepository.update(book);
        if (updatedBook == null) {
            throw new NotFoundException("Book not found with id: " + book.getId());
        }
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        String result = bookRepository.deleteById(id);
        if (result == null) {
            throw new NotFoundException("Book not found with id: " + id);
        }
        return Response.ok(result).build();
    }

    @GET
    @Path("/price-below/{price}")
    public Response findBooksByPriceBelow(@PathParam("price") double price) {
        List<Book> books = bookService.findBooksByPriceBelow(price);
        return Response.ok(books).build();
    }

    @GET
    @Path("/price-above/{price}")
    public Response findBooksByPriceAbove(@PathParam("price") double price) {
        List<Book> books = bookService.findBooksByPriceAbove(price);
        return Response.ok(books).build();
    }

    @GET
    @Path("/title/{title}")
    public Response findBooksByTitle(@PathParam("title") String title) {
        List<Book> books = bookService.findBooksByTitle(title);
        return Response.ok(books).build();
    }

    @GET
    @Path("/author/{author}")
    public Response findBooksByAuthor(@PathParam("author") String author) {
        List<Book> books = bookService.findBooksByAuthor(author);
        return Response.ok(books).build();
    }

}
