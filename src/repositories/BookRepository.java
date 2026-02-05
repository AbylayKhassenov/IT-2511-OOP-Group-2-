package repositories;

import entities.book.Book;
import factory.BookFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class BookRepository implements CrudRepository<Book, Integer>{
    public static void main(String[] args) {


    }
    public static void createTableIfNeeded(Connection connection) throws SQLException {
        String sql = """
 create table if not exists books (
 id serial primary key,
 title varchar(100) not null,
 author varchar(100) not null,
 is_available boolean default true not null,
 type varchar (20) not null
 
 );
 """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table books is ready.");
        }
    }

    @Override
    public void insert(Connection connection, Book book) {
        try {
            insertBook(
                    connection,
                    book.getTitle(),
                    book.getAuthor(),
                    book.getAvailable(),
                    book.getType()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting book", e);
        }
    }

    public  void insertBook(Connection connection, String title, String author, boolean is_available, String type) throws SQLException {
        String sql = "insert into books (title, author, is_available, type) values (?, ?, ?, ?) ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setBoolean(3, is_available);
            stmt.setString(4, type);
            int rows = stmt.executeUpdate();
            System.out.println("Inserted rows: " + rows);
        }
    }

    public List<Book> allAvailableBooks(Connection connection) throws SQLException {
        String sql = "select id, title, author, is_available, type from books where is_available is true order by id";
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean is_available = rs.getBoolean("is_available");
                String type = rs.getString("type");
                books.add(BookFactory.createBook(id,title, author, is_available, type));
            }
        }
        return books;
    }

    @Override
    public List<Book> findAll(Connection connection) {
        try {
            return allBooks(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding books", e);
        }
    }

    public List<Book> allBooks(Connection connection) throws SQLException {
        String sql = "select id, title, author, is_available, type from books order by id";
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean is_available = rs.getBoolean("is_available");
                String type = rs.getString("type");
                books.add(BookFactory.createBook(id, title, author, is_available,type));
            }
        }
        return books;
    }


    @Override
    public Book findById(Connection connection, Integer id) {
        try {
            return findBookById(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding book by id", e);
        }
    }


    public Book findBookById(Connection connection, int id) throws SQLException {
        String sql = "select id, title, author, is_available, type from books where id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    boolean is_available = rs.getBoolean("is_available");
                    String type = rs.getString("type");

                    Book book = BookFactory.createBook(id,title, author, is_available, type);
                    return book;
                }
            }
            
        }
        return null;
    }

    public Book findBookByTitle(Connection connection, String title) throws SQLException {
        String sql = "select id, title, author, is_available, type from books where title = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    int id = rs.getInt("id");
                    String author = rs.getString("author");
                    boolean is_available = rs.getBoolean("is_available");
                    String type = rs.getString("type");

                    Book book = BookFactory.createBook(id,title, author, is_available, type);
                    return book;
                }
            }

        }
        return null;
    }

    public  void borrowBook(Connection connection, int id) throws SQLException {
        String sql = "update books set is_available = false where id = ? and is_available = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, id);
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