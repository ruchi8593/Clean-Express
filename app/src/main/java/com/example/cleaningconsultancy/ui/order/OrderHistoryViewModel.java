package com.example.cleaningconsultancy.ui.order;

import androidx.lifecycle.ViewModel;

public class OrderHistoryViewModel {
    private String name;
    private String price;
    private String date;
    private String time;

    public OrderHistoryViewModel(String name, String price, String date, String time) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}