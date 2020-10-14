package com.example.giaothongapp.model;

public class Error {
    private int id;
    private String name;
    private String description;
    private byte[] image;

    public Error(int id, String name, String description,byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Error(String name, String description, byte[] image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
