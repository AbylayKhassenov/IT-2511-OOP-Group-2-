package entities;

import java.time.LocalDate;


public class Loan {
    private int id;
    private Book book;
    private Member member;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int fine;



    public Loan(int _id, Member member, Book book, LocalDate borrowDate, LocalDate _returnDate, int _fine){

        id = _id;
        setBook(book);
        setMember(member);
        setBorrowDate(borrowDate);
        returnDate = _returnDate;
        fine = _fine;
    }



    public int getId() {
        return id;
    }

    public Book getBook(){
        return book;
    }

    public Member getMember(){
        return member;
    }

    public LocalDate getBorrowDate(){
        return borrowDate;
    }

    public LocalDate getDueDate(){
        return  dueDate;
    }

    public  LocalDate getReturnDate(){ return  returnDate ;}

    public int getFine(){return  fine;}

    public void setBook(Book _book) {
        book = _book;
    }

    public void setMember(Member _member){
        member = _member;
    }

    public void setBorrowDate(LocalDate _borrowDate){
        borrowDate = _borrowDate;
    }

    private void setDueDate(LocalDate _dueDate) { dueDate = _dueDate;   }

    @Override
    public String toString() {  return "entities.Loan{" + "id=" + id + ", member=" + member.getName() + ", book=" + book.getTitle() + ", borrowed=" + borrowDate + ", returned=" + returnDate + ", fine=" + fine + '}';}









}


