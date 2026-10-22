package za.co.smartpantry;

public class SupportTicket {
    public long id;
    public String userEmail;
    public String category;
    public String subject;
    public String message;
    public String status;
    public String createdAt;

    public SupportTicket(long id, String userEmail, String category, String subject, String message, String status, String createdAt) {
        this.id = id;
        this.userEmail = userEmail;
        this.category = category;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }
}
