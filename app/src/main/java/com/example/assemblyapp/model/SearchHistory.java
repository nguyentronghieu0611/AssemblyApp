package com.example.assemblyapp.model;

public class SearchHistory {
    private int id;
    private int user_id;
    private String search_txt;

    public SearchHistory(int id, int user_id, String search_txt) {
        this.id = id;
        this.user_id = user_id;
        this.search_txt = search_txt;
    }

    public SearchHistory(int user_id, String search_txt) {
        this.user_id = user_id;
        this.search_txt = search_txt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSearch_txt() {
        return search_txt;
    }

    public void setSearch_txt(String search_txt) {
        this.search_txt = search_txt;
    }
}
