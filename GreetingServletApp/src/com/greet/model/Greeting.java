package com.greet.model;

public class Greeting {

    private int id;
    private String message;
    private String imagePath;
    private int createdById;
    private String createdByName;

    public Greeting() {
    }
    public Greeting(int id, String message, String imagePath,
                    int createdById, String createdByName) {
        this.id = id;
        this.message = message;
        this.imagePath = imagePath;
        this.createdById = createdById;
        this.createdByName = createdByName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}