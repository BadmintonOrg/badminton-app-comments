package lib;

public class Comment {
    private Integer id;
    private Integer user;
    private Integer court;
    private String content;
    private boolean profanity;

    public boolean getProfanity() {
        return profanity;
    }

    public void setProfanity(boolean prof) {
        this.profanity = prof;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getCourt() {
        return court;
    }

    public void setCourt(Integer court) {
        this.court = court;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
