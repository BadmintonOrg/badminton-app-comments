package lib;

public class User {

    private Integer userId;
    private String name;
    private String surname;
    private String userEmail;
    private String password;
    private Integer organizationId;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOrganization() {
        return organizationId;
    }

    public void setOrganization(Integer organizationId) {
        this.organizationId = organizationId;
    }
}

