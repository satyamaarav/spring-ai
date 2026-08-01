package com.spring.ai.entity;

public class Tut {

    private String title;
    private String message;
    private String createdYear;

    public Tut(String title, String message, String createdYear, String createdMonth, String createdDay) {
        this.title = title;
        this.message = message;
        this.createdYear = createdYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedYear() {
        return createdYear;
    }

    public void setCreatedYear(String createdYear) {
        this.createdYear = createdYear;
    }

    public Tut(){

    }
}
