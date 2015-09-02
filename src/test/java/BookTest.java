import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class BookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Book.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfTitlesAretheSame() {
    Book firstBook = new Book("Seven Habits",5);
    Book secondBook = new Book("Seven Habits",5);
    assertTrue(firstBook.equals(secondBook));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Book myBook = new Book("Seven Habits", 5);
    myBook.save();
    assertTrue(Book.all().get(0).equals(myBook));
  }

  @Test
  public void updateTitle_updatesBookTitleInDatabase_true() {
    Book myBook = new Book("Seven Habits",5);
    myBook.save();
    myBook.updateTitle("Eight Habits");
    assertEquals("Eight Habits", Book.all().get(0).getTitle());
  }

  @Test
  public void updateNumber_updatesBookNumberInDatabase_true() {
    Book myBook = new Book("Seven Habits",5);
    myBook.save();
    myBook.updateCopies(4);
    assertEquals((Integer) 4, myBook.getCopies());
  }

  @Test
  public void find_findBookInDatabase_true() {
    Book myBook = new Book("Seven Habits",5);
    myBook.save();
    Book savedBook = Book.find(myBook.getBookId());
    assertTrue(myBook.equals(savedBook));
  }


  @Test
  public void addAuthors_addsAuthorToBook() {
    Book myBook = new Book("Seven Habits",5);
    myBook.save();

    Author myAuthor = new Author("Steven Covey");
    myAuthor.save();

    myBook.addAuthor(myAuthor);
    Author savedAuthor = myBook.getAuthors().get(0);
    assertTrue(myAuthor.equals(savedAuthor));
  }
  //

  @Test
  public void delete_deletesAllBooksForAuthor() {
    Book myBook = new Book("Seven Habits",5);
    myBook.save();

    Author myAuthor = new Author("Momo");
    myAuthor.save();

    myBook.addAuthor(myAuthor);
    myBook.delete();
    assertEquals(myAuthor.getBooks().size(), 0);
  }

  @Test
  public void clearAuthors_clearsAllAuthorsFromDatabase() {
    Book myBook = new Book("Seven Habits",5);
    myBook.save();
    Author firstAuthor = new Author("Barack Obame");
    firstAuthor.save();
    Author secondAuthor = new Author("Perry Eising");
    secondAuthor.save();
    myBook.clearAuthors();
    assertTrue(myBook.getAuthors().size()==0);
  }
}
