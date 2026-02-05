import cfg.FinePolicy;
import edu.aitu.oop3.db.DatabaseConnection;
import entities.book.Book;
import repositories.*;
import entities.*;
import services.*;
import factory.BookFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class LibraryApp {

    private Scanner sc = new Scanner(System.in);

    private BookRepository bookRepo = new BookRepository();
    private MemberRepository memberRepo = new MemberRepository();
    private LoanService loanService = new LoanService();
    private FinePolicy.FineCalculator fineCalculator =
            new FinePolicy.FineCalculator();

    public void run() {

        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            return;
        }

        int choice = -1;

        while (choice != 0) {

            printMenu();
            String menuInput = sc.nextLine();

            if (menuInput.equalsIgnoreCase("back")) {
                continue;
            }

            try {
                choice = Integer.parseInt(menuInput);
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {

                case 1:
                    addBook(connection);
                    break;

                case 2:
                    showAllBooks(connection);
                    break;

                case 3:
                    addMember(connection);
                    break;

                case 4:
                    showMembers(connection);
                    break;

                case 5:
                    borrowBook(connection);
                    break;

                case 6:
                    returnBook(connection);
                    break;

                case 7:
                    showActiveLoans(connection);
                    break;

                case 0:
                    System.out.println("Goodbye.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close database connection.");
        }

        sc.close();
    }



    private void printMenu() {
        System.out.println("\n1. Add book");
        System.out.println("2. Show all books");
        System.out.println("3. Add member");
        System.out.println("4. Show members");
        System.out.println("5. Borrow book");
        System.out.println("6. Return book");
        System.out.println("7. Show active loans");
        System.out.println("0. Exit");
        System.out.print("Choose (or type back): ");
    }


    private void addBook(Connection connection) {

        System.out.print("Enter title: ");
        String title = sc.nextLine();
        if (title.equalsIgnoreCase("back")) return;

        System.out.print("Enter author: ");
        String author = sc.nextLine();
        if (author.equalsIgnoreCase("back")) return;

        System.out.print(
                "Choose the book type (1 - printed; 2 - eBook; 3 - reference): "
        );
        String typeChoice = sc.nextLine();
        if (typeChoice.equalsIgnoreCase("back")) return;

        String type;

        switch (typeChoice) {
            case "1":
                type = "printed";
                break;
            case "2":
                type = "ebook";
                break;
            case "3":
                type = "reference";
                break;
            default:
                System.out.println("Invalid type");
                return;
        }

        Book book = BookFactory.createBook(title, author, type);
        bookRepo.insert(connection, book);
        System.out.println("Book added.");
    }



    private void showAllBooks(Connection connection) {

        List<Book> books = bookRepo.findAll(connection);

        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }


    private void addMember(Connection connection) {

        System.out.print("Enter member name: ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("back")) return;

        System.out.print("Enter member email: ");
        String email = sc.nextLine();
        if (email.equalsIgnoreCase("back")) return;

        Member member = new Member(name, email);
        memberRepo.insert(connection, member);
    }


    private void showMembers(Connection connection) {

        List<Member> members = memberRepo.findAll(connection);

        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            for (Member m : members) {
                System.out.println(m);
            }
        }
    }


    private void borrowBook(Connection connection) {

        System.out.print("Enter member ID: ");
        String memberInput = sc.nextLine();
        if (memberInput.equalsIgnoreCase("back")) return;

        System.out.print("Enter book ID: ");
        String bookInput = sc.nextLine();
        if (bookInput.equalsIgnoreCase("back")) return;

        int memberId = Integer.parseInt(memberInput);
        int bookId = Integer.parseInt(bookInput);

        Member m = memberRepo.findById(connection, memberId);
        Book b = bookRepo.findById(connection, bookId);

        if (m == null || b == null) {
            System.out.println("Member or book not found.");
            return;
        }

        try {
            loanService.insertLoan(connection, m, b);
            System.out.println("Book loaned.");
        } catch (Exception e) {
            System.out.println("Failed to borrow book.");
        }
    }


    private void returnBook(Connection connection) {

        System.out.print("Enter member ID: ");
        String mem = sc.nextLine();
        if (mem.equalsIgnoreCase("back")) return;

        System.out.print("Enter book ID: ");
        String bk = sc.nextLine();
        if (bk.equalsIgnoreCase("back")) return;

        int memberId = Integer.parseInt(mem);
        int bookId = Integer.parseInt(bk);

        Member m = memberRepo.findById(connection, memberId);
        Book b = bookRepo.findById(connection, bookId);

        if (m == null || b == null) {
            System.out.println("Member or book not found.");
            return;
        }

        try {
            Loan loan = loanService.findActiveLoan(connection, m, b);
            loanService.returnProcess(connection, loan, LocalDate.now());

            int fine = fineCalculator.calculateFineSoFar(loan);

            System.out.println("Book returned.");
            if (fine == 0) {
                System.out.println("No fine.");
            } else {
                System.out.println("Total fine: " + fine);
            }

        } catch (Exception e) {
            System.out.println("Failed to return book.");
        }
    }


    private void showActiveLoans(Connection connection) {

        System.out.print("Enter member ID: ");
        String memID = sc.nextLine();
        if (memID.equalsIgnoreCase("back")) return;

        int memberId = Integer.parseInt(memID);
        Member m = memberRepo.findById(connection, memberId);

        if (m == null) {
            System.out.println("Member not found.");
            return;
        }

        try {
            List<Loan> loans =
                    loanService.findMemberActiveLoan(connection, m);

            if (loans.isEmpty()) {
                System.out.println("No active loans.");
            } else {
                for (Loan l : loans) {
                    System.out.println(l);
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to load loans.");
        }
    }
}
