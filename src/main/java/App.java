import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

import java.util.HashMap;
import java.util.Iterator;
import java.time.LocalDateTime;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    // Main page
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Index for books
    get("/books", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allBooks", Book.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    // Index for authors
    get("/authors", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allAuthors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Show individual course
    get("/book/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Book showBook = Book.find(id);
      model.put("showBook", showBook);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    // Show individual author
    get("/author/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Author showAuthor = Author.find(id);
      model.put("showAuthor", showAuthor);
      model.put("template", "templates/author.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    // Show empty new course form
    get("/books/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/book-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Show empty new author form
    get("/authors/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allBooks", Book.all());
      model.put("template", "templates/author-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Create book
    post("/books", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String bookTitle = request.queryParams("bookTitle");
      Integer bookCopies = Integer.parseInt(request.queryParams("bookCopies"));
      String authorName = request.queryParams("authorName");


      Book newBook = new Book(bookTitle, bookCopies);
      Author newAuthor = new Author(authorName);

      newAuthor.save();
      newBook.save();
      newBook.addAuthor(newAuthor);



      model.put("allBooks", Book.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    // Create author
    post("/authors", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String authorName = request.queryParams("authorName");
      Integer authorId = Integer.parseInt(request.queryParams("authorId"));//check

      Book newBook = Book.find(authorId);
      Author newAuthor = new Author(authorName);

      newAuthor.save();
      newBook.addAuthor(newAuthor);

      model.put("allAuthors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Edit book
    get("/books/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Book editBook = Book.find(id);
      Author editAuthor = Author.find(editBook.getBookAuthorId());
      model.put("editAuthor", editAuthor);
      model.put("editBook", editBook);
      model.put("template", "templates/book-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Edit author
    get("/authors/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Author editAuthor = Author.find(id);
      model.put("allBooks", Book.all());
      model.put("editAuthor", editAuthor);
      model.put("template", "templates/author-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Update book
    post("/books/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String newBookTitle = request.queryParams("editBookTitle");
      Integer newBookCopies = Integer.parseInt(request.queryParams("editBookCopies"));
      String newBookAuthor = request.queryParams("editAuthorName");

      int id = Integer.parseInt(request.params(":id"));
      Book editBook = Book.find(id); //this is fine
      Author editAuthor = Author.find(id);

      editAuthor.update(newBookAuthor, editBook);

      editBook.update(newBookTitle, newBookCopies);
      editBook.addAuthor(editAuthor);

      model.put("editAuthor", editAuthor);
      model.put("allBooks", Book.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Update author
    post("/authors/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String newAuthorName = request.queryParams("editAuthorName");
      Integer newBooks = Integer.parseInt(request.queryParams("editBooks"));
      int id = Integer.parseInt(request.params(":id"));

      Book newBook = Book.find(newBooks);
      Author editAuthor = Author.find(id);

      editAuthor.update(newAuthorName, newBook);

      model.put("allAuthors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Delete course
    get("/books/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Book deleteBook = Book.find(id);
      deleteBook.delete();
      model.put("allBooks", Book.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Delete author
    get("/authors/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Author deleteAuthor = Author.find(id);
      deleteAuthor.delete();
      model.put("allAuthors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }//close
}//close
