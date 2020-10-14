package com.example.giaothongapp.model;

import com.example.giaothongapp.model.Error;
public class AmercementLevel {
    private int error_id;
    private String vehical;
    private String amercement;

    public AmercementLevel(int error_id, String vehical, String amercement) {
        this.error_id = error_id;
        this.vehical = vehical;
        this.amercement = amercement;
    }

    public int getError_id() {
        return error_id;
    }

    public void setError_id(int error_id) {
        this.error_id = error_id;
    }

    public String getVehical() {
        return vehical;
    }

    public void setVehical(String vehical) {
        this.vehical = vehical;
    }

    public String getAmercement() {
        return amercement;
    }

    public void setAmercement(String amercement) {
        this.amercement = amercement;
    }
}
