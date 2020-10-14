package com.example.giaothongapp.model;

public class MarkUser {
    private int user_id;
    private int error_id;
    private String time;

    public MarkUser(int user_id, int error_id, String time) {
        this.user_id = user_id;
        this.error_id = error_id;
        this.time = time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getError_id() {
        return error_id;
    }

    public void setError_id(int error_id) {
        this.error_id = error_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
