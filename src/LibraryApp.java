import edu.aitu.oop3.db.DatabaseConnection;
import repositories.*;
import entities.*;
import services.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class LibraryApp {

    public void run() {

        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            return;
        }

        Scanner sc = new Scanner(System.in);

        BookRepository bookRepo = new BookRepository();
        MemberRepository memberRepo = new MemberRepository();
        LoanService loanService = new LoanService();
        FineCalculator fineCalculator = new FineCalculator();

        int choice = -1;

        while (choice != 0) {

            System.out.println("\n1. Add book");
            System.out.println("2. Show all books");
            System.out.println("3. Add member");
            System.out.println("4. Show members");
            System.out.println("5. Borrow book");
            System.out.println("6. Return book");
            System.out.println("7. Show active loans");
            System.out.println("0. Exit");
            System.out.print("Choose (or type back): ");

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
                    System.out.print("Enter title: ");
                    String title = sc.nextLine();
                    if (title.equalsIgnoreCase("back")) break;

                    System.out.print("Enter author: ");
                    String author = sc.nextLine();
                    if (author.equalsIgnoreCase("back")) break;

                    try {
                        bookRepo.insertBook(connection, title, author, true);
                        System.out.println("Book added.");
                    } catch (SQLException e) {
                        System.out.println("Failed to add book.");
                    }
                    break;

                case 2:
                    try {
                        List<Book> books = bookRepo.allAvailableBooks(connection);
                        if (books.isEmpty()) {
                            System.out.println("No books found.");
                        } else {
                            for (Book b : books) {
                                System.out.println(b);
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Failed to load books.");
                    }
                    break;

                case 3:
                    System.out.print("Enter member name: ");
                    String name = sc.nextLine();
                    if (name.equalsIgnoreCase("back")) break;

                    System.out.print("Enter member email: ");
                    String email = sc.nextLine();
                    if (email.equalsIgnoreCase("back")) break;

                    try {
                        memberRepo.insertMember(connection, name, email);
                        System.out.println("Member added.");
                    } catch (SQLException e) {
                        System.out.println("Failed to add member.");
                    }
                    break;

                case 4:
                    try {
                        List<Member> members = memberRepo.allMembers(connection);
                        if (members.isEmpty()) {
                            System.out.println("No members found.");
                        } else {
                            for (Member m : members) {
                                System.out.println(m);
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Failed to load members.");
                    }
                    break;

                case 5:
                    System.out.print("Enter member ID: ");
                    String memberInput = sc.nextLine();
                    if (memberInput.equalsIgnoreCase("back")) break;

                    System.out.print("Enter book ID: ");
                    String bookInput = sc.nextLine();
                    if (bookInput.equalsIgnoreCase("back")) break;

                    try {
                        int memberId = Integer.parseInt(memberInput);
                        int bookId = Integer.parseInt(bookInput);

                        Member m = memberRepo.findMemberById(connection, memberId);
                        Book b = bookRepo.findBookById(connection, bookId);

                        if (m == null || b == null) {
                            System.out.println("Member or book not found.");
                            break;
                        }

                        loanService.insertLoan(connection, m, b);
                        System.out.println("Book loaned.");

                    } catch (Exception e) {
                        System.out.println("Failed to borrow book.");
                    }
                    break;

                case 6:
                    System.out.print("Enter member ID: ");
                    String mem = sc.nextLine();
                    if (mem.equalsIgnoreCase("back")) break;

                    System.out.print("Enter book ID: ");
                    String bk = sc.nextLine();
                    if (bk.equalsIgnoreCase("back")) break;

                    try {
                        int memberId = Integer.parseInt(mem);
                        int bookId = Integer.parseInt(bk);

                        Member m = memberRepo.findMemberById(connection, memberId);
                        Book b = bookRepo.findBookById(connection, bookId);

                        if (m == null || b == null) {
                            System.out.println("Member or book not found.");
                            break;
                        }

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
                    break;

                case 7:
                    System.out.print("Enter member ID: ");
                    String memID = sc.nextLine();
                    if (memID.equalsIgnoreCase("back")) break;

                    try {
                        int memberId = Integer.parseInt(memID);
                        Member m = memberRepo.findMemberById(connection, memberId);

                        if (m == null) {
                            System.out.println("Member not found.");
                            break;
                        }

                        List<Loan> loans = loanService.findMemberActiveLoan(connection, m);

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
}
