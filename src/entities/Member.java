package entities;

public class Member {
    private int id;
    private String name;

    private String email;

    public Member(int id, String name, String email) {
        this.id = id;
        setName(name);
        setEmail(email);
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {

        return email;
     }


    public void setName(String name) {
        if (name == null || name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("entities.Member name cannot contain numbers.");
        }
        this.name = name;
    }

     public void setEmail(String email) {
         if (email == null || !((email.matches(".*@\\w+\\." + "\\w+")))) {
             throw new IllegalArgumentException("entities.Member email format is incorrect.");
         }
         this.email = email;
     }

    @Override
    public String toString() {
        return "entities.Member{id=" + id + ", name= " + name + ", email " + email + "'} " ;
    }
}