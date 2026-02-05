package entities.book;

public class EBook extends Book {

    public EBook(String title, String author) {
        super(title, author);
    }

    public EBook(int id, String title, String author, boolean isAvailable) {
        super(id, title, author, isAvailable);
    }

    @Override
    public String getType() {
        return "ebook";
    }
}
