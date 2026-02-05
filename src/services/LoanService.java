package services;

import cfg.FinePolicy;
import edu.aitu.oop3.db.DatabaseConnection;
import entities.Book;
import entities.Loan;
import entities.Member;
import repositories.BookRepository;
import repositories.MemberRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;




public class LoanService {
    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {
            createTableIfNeeded(connection);
            System.out.println("Table created");
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        }

    }
    public static void createTableIfNeeded(Connection connection) throws SQLException {
        String sql = """    
 create table if not exists loans (
 id serial primary key,
 book_id int not null,
     foreign key (book_id) references books (id),
 member_id int not null,
     foreign key (member_id) references members (id),
 borrow_date date not null,
 due_date date not null,
 return_date date,
 fine int default 0
 
 );
 """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table loans is ready.");
        }
    }

    private final BookRepository bookRepository = new BookRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final FinePolicy.FineCalculator fineCalculator = new FinePolicy.FineCalculator();


    public  void insertLoan(Connection connection, Member member, Book book) throws SQLException {
        boolean fineExist = anyFine(connection, member.getId());
        if(bookExist(connection, book)   ) {
            if( memberExist(connection, member) && !fineExist){
                String sql = "insert into loans (book_id, member_id, borrow_date, due_date) values (?, ?, ?, ?) ";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, book.getId());
                    stmt.setInt(2, member.getId());
                    stmt.setDate(3, Date.valueOf(LocalDate.now()));
                    stmt.setDate(4, Date.valueOf(LocalDate.now().plusDays(7)));
                    int rows = stmt.executeUpdate();
                    System.out.println("Inserted rows: " + rows);

                }

                bookRepository.borrowBook(connection, book.getId());

            }else {
                throw new IllegalArgumentException("entities.Member not found");
            }
        }else{
            throw new IllegalArgumentException("entities.Book not found or unavailable");
        }


    }

    public boolean bookExist(Connection connection, Book book) throws SQLException {
        return bookRepository.findBookById(connection, book.getId()) != null && (bookRepository.findBookById(connection, book.getId()).getAvailable());

    }

    public boolean memberExist(Connection connection, Member member) throws SQLException {
        return memberRepository.findMemberById(connection, member.getId()) != null;
    }

    public ArrayList<Loan> findMemberActiveLoan(Connection connection, Member member) throws SQLException{
        ArrayList<Loan> loans = new ArrayList<>();
        if( memberExist(connection, member)){
            String sql = "select * from loans where member_id = ? and return_date is null";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, member.getId());
                try( ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        int memberId = rs.getInt("member_id");
                        int loanId = rs.getInt("id");
                        LocalDate borrowDate = rs.getObject("borrow_date", LocalDate.class);
                        LocalDate returnDate = rs.getObject("return_date", LocalDate.class);
                        int fine = rs.getInt("fine");
                        loans.add(new Loan(loanId, memberRepository.findMemberById(connection, memberId), bookRepository.findBookById(connection, bookId), borrowDate, returnDate, fine)) ;
                    }
                    return loans;
                }

            }
        }else {
            throw new IllegalArgumentException("entities.Member not found");
        }
    }

    public Loan findActiveLoan(Connection connection, Member member, Book book) throws SQLException{
        if( memberExist(connection, member)){
            String sql = "select * from loans where book_id = ? and member_id = ? and return_date is null";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, book.getId());
                stmt.setInt(2, member.getId());
                try( ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()){
                        int bookId = rs.getInt("book_id");
                        int memberId = rs.getInt("member_id");
                        int loanId = rs.getInt("id");
                        LocalDate borrowDate = rs.getObject("borrow_date", LocalDate.class);
                        LocalDate returnDate = rs.getObject("return_date", LocalDate.class);
                        int fine = rs.getInt("fine");
                        Loan loan = new Loan(loanId, memberRepository.findMemberById(connection, memberId), bookRepository.findBookById(connection, bookId), borrowDate, returnDate, fine);
                        return loan;

                    }
                }
            }
        }else {
            throw new IllegalArgumentException("entities.Member not found");
        }
        throw new IllegalArgumentException("No such loan record!");
    }


    public void returnProcess(Connection connection, Loan loan, LocalDate returnDate) throws  SQLException {
        int fine = fineCalculator.calculateFineSoFar(loan);

        String sql = "update loans set return_date = ? , fine = ?  where id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setInt(2, fine);
            stmt.setInt(3, loan.getId());
            int loanUpdate = stmt.executeUpdate();

        }

        sql = "update books set is_available = true where id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loan.getBook().getId());
            int bookIsAvailable = stmt.executeUpdate();
        }

    }

    public boolean anyFine(Connection connection, int memberId) throws  SQLException{

        List<Loan> loans = new ArrayList<>();

        String sql = "select * from loans where member_id = ? and return_date is null";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, memberId);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int bookId = rs.getInt("book_id");
                    memberId = rs.getInt("member_id");
                    LocalDate borrowDate = rs.getObject("borrow_date", LocalDate.class);
                    LocalDate returnDate = rs.getObject("return_date", LocalDate.class);
                    int fine = rs.getInt("fine");

                    loans.add(new Loan(id, memberRepository.findMemberById(connection, memberId), bookRepository.findBookById(connection, bookId), borrowDate, returnDate, fine));
                }
            }
        }

        int totalFine = 0;

        for (Loan loan : loans) {
            totalFine += fineCalculator.calculateFineSoFar(loan);
        }

        return totalFine > 0;
    }
}