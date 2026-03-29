package com.black.ecoms.dto;

public class ChangePasswordRequest {

    private String email;
    private String newPassword;
    private String requestCode;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String email, String newPassword, String requestCode) {
        this.email = email;
        this.newPassword = newPassword;
        this.requestCode = requestCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
}
