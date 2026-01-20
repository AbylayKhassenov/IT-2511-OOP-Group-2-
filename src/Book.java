public class Book {
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;
    private static int idGen = 0;

    public Book(int id, String title, String author, Boolean isAvailable) {
        this.id = id;
        setTitle(title);
        setAuthor(author);
        setAvailable(isAvailable);

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public boolean getAvailable(){ return isAvailable; }


    public void setTitle(String title){
        if(title == null || title.isEmpty()){
            throw new IllegalArgumentException("title value can not be equal to null!");

        }
        this.title = title;
    }


    public void setAuthor(String author){
        if(author == null || author.isEmpty()){
            throw new IllegalArgumentException("author value can not be equal to null!");

        }
        this.author = author;
    }

    public void setAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }



    @Override
    public String toString() {
        return "Book{id=" + id + ", name='" + title + "'}";
    }
}