import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Author {

  private int id;
  private String name;

  public Author(String name) {
    this.name = name;
  }

  public int getAuthorId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object otherAuthor){
    if (!(otherAuthor instanceof Author)) {
      return false;
    } else {
      Author newAuthor = (Author) otherAuthor;
      return this.getName().equals(newAuthor.getName()) &&
             this.getAuthorId() == newAuthor.getAuthorId();
    }
  }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO authors (name) VALUES (:name)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Author> all() {
    String sql = "SELECT * FROM authors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Author.class);
    }
  }

  public static Author find(int id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM authors where id=:id";
    Author author = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Author.class);
    return author;
    }
  }


  public void updateName(String name) {
    this.name = name;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE authors SET name=:name WHERE id=:id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }


  public void updateBook(Book newBook) {
    try(Connection con = DB.sql2o.open()) {
      Integer newBookId = newBook.getBookId();
      String sql = "UPDATE books_authors SET book_id = :book_id, author_id = :author_id WHERE id = :id";
      con.createQuery(sql)
        .addParameter("book_id", newBookId)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM authors WHERE id = :id;";
      con.createQuery(deleteQuery)
        .addParameter("id", id)
        .executeUpdate();

      String joinDeleteQuery = "DELETE FROM books_authors WHERE author_id = :author_id";
      con.createQuery(joinDeleteQuery)
        .addParameter("author_id", this.getAuthorId())
        .executeUpdate();
    }
  }

  public void addBook(Book book) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books_authors (book_id, id) VALUES (:book_id, :id)";
      con.createQuery(sql)
        .addParameter("book_id", book.getBookId())
        .addParameter("id", this.getAuthorId())
        .executeUpdate();
    }
  }

  public List<Book> getBooks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.* FROM authors JOIN books_authors USING (id) JOIN books USING (id) WHERE authors.id=:id";
      List<Book> books = con.createQuery(sql)
        .addParameter("id", this.getAuthorId())
        .executeAndFetch(Book.class);
      return books;
    }
  }

}
