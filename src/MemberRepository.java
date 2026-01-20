import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class MemberRepository {
    public static void main(String[] args) {


    }
    public static void createTableIfNeeded(Connection connection) throws SQLException {
        String sql = """
 create table if not exists members (
 id serial primary key,
 name varchar(100) not null,
 email varchar(100) not null unique
 
 );
 """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table members is ready.");
        }
    }
    public  void insertMember(Connection connection, String name, String email) throws SQLException {
       if(!emailExist(connection, email)) {
           String sql = "insert into members (name, email) values (?, ?) ";
           try (PreparedStatement stmt = connection.prepareStatement(sql)) {
               stmt.setString(1, name);
               stmt.setString(2, email);
               int rows = stmt.executeUpdate();
               System.out.println("Inserted rows: " + rows);
           }
       }else{
           System.out.println("Such email already exists!");

       }
    }

    public Boolean emailExist(Connection connection, String email) throws SQLException {
        if(findMemberByEmail(connection, email)!= null){
            return true;
        }
        return false;
    }

    public List<Member> allMembers(Connection connection) throws SQLException {
        String sql = "select id, name, email from members order by id";
        ArrayList<Member> members = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                members.add(new Member(id,name, email));
            }
        }
        return members;
    }

    public Member findMemberById(Connection connection, int id) throws SQLException {
        String sql = "select id, name, email from members where id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    Member member = new Member(id,name,email);
                    return member;
                }
            }

        }
        return null;
    }

    public Member findMemberByEmail(Connection connection, String email) throws SQLException {
        String sql = "select id, name, email from members where email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1,email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Member member = new Member(id,name,email);
                    return member;
                }
            }

        }
        return null;
    }


}