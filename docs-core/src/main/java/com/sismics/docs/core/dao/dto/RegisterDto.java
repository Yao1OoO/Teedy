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
    private Long disableTimestamp;

    /**
     * Storage quota.
     */
    private Long storageQuota;

    /**
     * Storage current usage.
     */
    private Long storageCurrent;

    /**
     * TOTP key.
     */
    private String totpKey;

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

    public Long getDisableTimestamp() {
        return disableTimestamp;
    }

    public RegisterDto setDisableTimestamp(Long disableTimestamp) {
        this.disableTimestamp = disableTimestamp;
        return this;
    }

    public Long getStorageQuota() {
        return storageQuota;
    }

    public void setStorageQuota(Long storageQuota) {
        this.storageQuota = storageQuota;
    }

    public Long getStorageCurrent() {
        return storageCurrent;
    }

    public void setStorageCurrent(Long storageCurrent) {
        this.storageCurrent = storageCurrent;
    }

    public String getTotpKey() {
        return totpKey;
    }

    public void setTotpKey(String totpKey) {
        this.totpKey = totpKey;
    }

    public String getState(){
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

}
