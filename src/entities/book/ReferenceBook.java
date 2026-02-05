package entities.book;

public class ReferenceBook extends Book {

    public ReferenceBook(String title, String author) {
        super(title, author);
    }

    public ReferenceBook(int id, String title, String author, boolean isAvailable) {
        super(id, title, author, isAvailable);
    }

    @Override
    public String getType() {
        return "reference";
    }
}
