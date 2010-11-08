package se.telescopesoftware.betpals.domain;

public class UserSearchForm {

    private Long userId;
    private String username;
    private String nameOrSurname;
    private String email;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameOrSurname() {
        return nameOrSurname;
    }

    public void setNameOrSurname(String nameOrSurname) {
        this.nameOrSurname = nameOrSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
