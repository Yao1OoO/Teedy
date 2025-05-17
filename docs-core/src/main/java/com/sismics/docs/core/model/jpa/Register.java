package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "T_REGISTER")
public class Register {
    @Id
    @Column(name = "R_ID")
    private String id;
    /**
     * User's username.
     */
    @Column(name = "R_USERNAME_C", nullable = false, length = 50)
    private String username;

    /**
     * User's password.
     */
    @Column(name = "R_PASSWORD_C", nullable = false, length = 100)
    private String password;

    /**
     * Email address.
     */
    @Column(name = "R_EMAIL_C", nullable = false, length = 100)
    private String email;

    /**
     * Creation date.
     */
    @Column(name = "R_CREATEDATE_D", nullable = false)
    private Date createDate;

    /**
     * Deletion date.
     */
    @Column(name = "R_DELETEDATE_D")
    private Date deleteDate;

    /**
     * Disable date.
     */
    @Column(name = "R_DISABLEDATE_D")
    private Date disableDate;

    @Column(name = "R_STATE")
    private String state;

    public String getId() {
        return id;
    }

    public Register setId(String id) {
        this.id = id;
        return this;
    }


    public String getUsername() {
        return username;
    }

    public Register setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Register setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Register setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Register setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public Register setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
        return this;
    }

    public Date getDisableDate() {
        return disableDate;
    }

    public Register setDisableDate(Date disableDate) {
        this.disableDate = disableDate;
        return this;
    }

    public String getState(){
        return state;
    }

    public Register setState(String state){
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("email", email)
                .toString();
    }

    public String toMessage() {
        return username;
    }

}
