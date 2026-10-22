package za.co.smartpantry;

public class User {
    public long id;
    public String fullName;
    public String email;
    public String createdAt;

    public User(long id, String fullName, String email, String createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = createdAt;
    }
}
