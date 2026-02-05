package entities.book;

public class PrintedBook extends Book {

    public PrintedBook(String title, String author) {
        super(title, author);
    }

    public PrintedBook(int id, String title, String author, boolean isAvailable) {
        super(id, title, author, isAvailable);
    }

    @Override
    public String getType() {
        return "printed";
    }
}
