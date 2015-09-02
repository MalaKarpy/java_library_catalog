import java.time.LocalDateTime;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class AuthorTest {
  @Rule
  public DatabaseRule database= new DatabaseRule();

  // @Test
  // public void all_emptyAtFirst() {
  //   assertEquals(Author.all().size(), 0);
  // }

  @Test
  public void equals_returnsTrueIfAuthorNamesAretheSame() {
    Author myAuthor = new Author("Momo");
    Author secondAuthor = new Author("Momo");
    assertTrue(myAuthor.equals(secondAuthor));
  }

  //
  @Test
  public void all_savesIntoDatabase_true() {
    Author myAuthor = new Author("Momo");
    myAuthor.save();
    assertEquals(Author.all().get(0).getName(), "Momo");
  }

  //
  // // @Test
  // // public void find_findsAuthorsInDatabase_true() {
  // //   Author myAuthor = new Author("Momo");
  // //   myAuthor.save();
  // //   Author savedAuthor = Author.find(myAuthor.getAuthorId());
  // //   assertEquals(savedAuthor.getName(), "Momo Ozawa");
  // // }
  //
  // //
  // @Test
  // public void delete_deletesAllAuthorsInBook() {
  //   Book myBook = new Book("Proofs", 2, 2);
  //   myBook.save();
  //
  //   Author myAuthor = new Author("Momo");
  //   myAuthor.save();
  //
  //   myAuthor.addBook(myBook);
  //   myAuthor.delete();
  //   assertEquals(myBook.getAuthors().size(), 0);
  // }
  //
  // @Test
  // public void addCourse_addsCourseToAuthor() {
  //   Book myBook = new Book("Proofs", 307);
  //   myBook.save();
  //
  //   Author myAuthor = new Author("Momo", "Ozawa", "09/28/2010");
  //   myAuthor.save();
  //
  //   myAuthor.addBook(myBook);
  //   Book savedCourse = myAuthor.getCourses().get(0);
  //   assertTrue(myBook.equals(savedCourse));
  // }
  //
  // @Test
  // public void getCourses_returnsAllCourses_ArrayList() {
  //   Book myBook = new Book("Proofs", 307);
  //   myBook.save();
  //
  //   Author myAuthor = new Author("Momo", "Ozawa", "09/28/2010");
  //   myAuthor.save();
  //
  //   myAuthor.addBook(myBook);
  //   List savedCourses = myAuthor.getCourses();
  //   assertEquals(savedCourses.size(), 1);
  // }

}
