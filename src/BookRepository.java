import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class BookRepository {
    public static void main(String[] args) {


    }
    public static void createTableIfNeeded(Connection connection) throws SQLException {
        String sql = """
 create table if not exists books (
 id serial primary key,
 title varchar(100) not null,
 author varchar(100) not null,
 is_available boolean default true not null
 
 );
 """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table books is ready.");
        }
    }
    public  void insertBook(Connection connection, String title, String author, boolean is_available) throws SQLException {
        String sql = "insert into books (title, author, is_available) values (?, ?, ?) ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setBoolean(3, is_available);
            int rows = stmt.executeUpdate();
            System.out.println("Inserted rows: " + rows);
        }
    }

    public List<Book> allAvailableBooks(Connection connection) throws SQLException {
        String sql = "select id, title, author, is_available from books where is_available is true order by id";
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                Boolean is_available = rs.getBoolean("is_available");
                books.add(new Book(id,title, author, is_available));
            }
        }
        return books;
    }


    public List<Book> allBooks(Connection connection) throws SQLException {
        String sql = "select id, title, author, is_available from books order by id";
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                Boolean is_available = rs.getBoolean("is_available");
                books.add(new Book(id,title, author, is_available));
            }
        }
        return books;
    }

    public Book findBookById(Connection connection, int id) throws SQLException {
        String sql = "select id, title, author, is_available from books where id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    boolean is_available = rs.getBoolean("is_available");

                    Book book = new Book(id,title, author, is_available);
                    return book;
                }
            }
            
        }
        return null;
    }

    public Book findBookByTitle(Connection connection, String title) throws SQLException {
        String sql = "select id, title, author, is_available from books where title = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    int id = rs.getInt("id");
                    String author = rs.getString("author");
                    boolean is_available = rs.getBoolean("is_available");

                    Book book = new Book(id,title, author, is_available);
                    return book;
                }
            }

        }
        return null;
    }

    public  void borrowBook(Connection connection, String title) throws SQLException {
        String sql = "update books set is_available = false where title = ? and is_available = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, title);
            int booksAffected = stmt.executeUpdate();


        }
    }


    public  void returnBook(Connection connection, String title) throws SQLException {
        String sql = "update books set is_available = true where title = ? and is_available = false";
        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, title);
            int booksAffected = stmt.executeUpdate();


        }
    }


}