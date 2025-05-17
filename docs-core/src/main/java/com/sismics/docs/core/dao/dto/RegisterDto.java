package com.sismics.docs.core.dao.dto;

public class RegisterDto {
    /**
     * User ID.
     */
    private String id;

    /**
     * Username.
     */
    private String username;

    /**
     * Email address.
     */
    private String email;

    /**
     * Creation date of this user.
     */
    private Long createTimestamp;

    /**
     * Disable date of this user.
     */

    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getState(){
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

}
