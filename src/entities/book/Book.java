package entities.book;

public abstract class Book {

    protected int id;
    protected String title;
    protected String author;
    protected boolean isAvailable;

    protected Book(int id, String title, String author, boolean isAvailable) {
        this.id = id;
        setTitle(title);
        setAuthor(author);
        this.isAvailable = isAvailable;
    }

    protected Book(String title, String author) {
        setTitle(title);
        setAuthor(author);
        this.isAvailable = true;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getAvailable() {
        return isAvailable;
    }


    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        this.author = author;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "Book{id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + isAvailable +
                '}';
    }
}
