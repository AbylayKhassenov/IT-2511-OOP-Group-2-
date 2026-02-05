package factory;

import entities.book.Book;
import entities.book.EBook;
import entities.book.PrintedBook;
import entities.book.ReferenceBook;

public class BookFactory {

    public static Book createBook(String title, String author,String type) {

        if (type == null) {
            throw new IllegalArgumentException("Book type cannot be null");
        }


        if (type.equals("printed")) {
            return new PrintedBook(title, author);
        }else if (type.equals("ebook")) {
            return new EBook(title, author);
        }else if(type.equals("reference")) {
            return new ReferenceBook(title, author);
        }else{
        throw new IllegalArgumentException(type + " book tpye  does not exist! ");
        }
    }

    public static Book createBook(int id, String title, String author, boolean isAvailable, String type) {
        return switch (type) {
            case "printed" -> new PrintedBook(id, title, author, isAvailable);
            case "ebook" -> new EBook(id, title, author, isAvailable);
            case "reference" -> new ReferenceBook(id, title, author, isAvailable);
            default -> throw new IllegalArgumentException("Unknown book type: " + type);
        };

    }
}
