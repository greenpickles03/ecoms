package com.black.ecoms.dto;

public class GenerateCodeRequest {

    private String email;

    public GenerateCodeRequest() {
    }

    public GenerateCodeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

