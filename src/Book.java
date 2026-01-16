public class Book {
    private int id;
    private String name;

    public Book(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Book ID must be a positive number.");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Book name cannot contain numbers.");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", name='" + name + "'}";
    }
}