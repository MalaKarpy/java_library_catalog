import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Book {

  private int id;
  private String title;
  private Integer copies;

  public Book(String title, Integer copies) {
    this.title = title;
    this.copies = copies;
  }

  public int getBookId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Integer getCopies() {
    return copies;
  }

  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (title, copies) VALUES (:title, :copies)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("copies", this.copies)
        .executeUpdate()
        .getKey();
    }

  }

  public static List<Book> all() {
    String sql = "SELECT * FROM books";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }

  public void update(String title, Integer copies) {
    updateTitle(title);
    updateCopies(copies);
  }

  public void updateTitle(String title) {
    // update in memory
    this.title = title;

    // update in database
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET title=:title WHERE id=:id";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", title)
        .addParameter("id", this.id)
        .executeUpdate()
        .getKey();
    }
  }

  public void updateCopies(Integer copies) {
    // update in memory
    this.copies = copies;

    // update in database
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET copies=:copies WHERE id=:id";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("copies", copies)
        .addParameter("id", this.id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books where id=:id";
      Book course = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
      return course;
    }
  }

  public void addAuthor(Author author) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books_authors (book_id, author_id) VALUES (:book_id, :id)";
      con.createQuery(sql)
        .addParameter("book_id", this.getBookId())
        .addParameter("id", author.getAuthorId())
        .executeUpdate();
    }
  }

  public Integer getBookAuthorId(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT author_id FROM books_authors WHERE book_id=:book_id";
      Integer bookId = con.createQuery(sql)
        .addParameter("book_id", id)
        .executeAndFetchFirst(Integer.class);
        return bookId;
    }
  }

  public List<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT authors.* FROM books JOIN books_authors ON (books.id=books_authors.book_id) JOIN authors ON (books_authors.author_id=authors.id) WHERE books.id=:id";
      List<Author> authors = con.createQuery(sql)
        .addParameter("id", this.getBookId())
        .executeAndFetch(Author.class);
      return authors;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM books WHERE id=:id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM books_authors WHERE id = :id";
      con.createQuery(joinDeleteQuery)
        .addParameter("id", this.getBookId())
        .executeUpdate();
    }
  }

  public void clearAuthors() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM authors;";
      con.createQuery(sql)
        .executeUpdate();

    String joinDeleteQuery = "DELETE FROM books_authors;";
    con.createQuery(joinDeleteQuery)
      .executeUpdate();
    }
  }

  public static void removeBook(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM books WHERE id=:id;";
      con.createQuery(sql).addParameter("id",id).executeUpdate();
    }
  }
}
