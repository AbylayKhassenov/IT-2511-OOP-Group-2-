public class Member {
    private int id;
    private String name;

    public Member(int id, String name) {
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
            throw new IllegalArgumentException("Member ID must be a positive number.");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Member name cannot contain numbers.");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "Member{id=" + id + ", name='" + name + "'}";
    }
}