package com.example.cleaningconsultancy.ui.search;

import androidx.lifecycle.ViewModel;

public class SearchViewModel {
    private String currentUserId;
    private String cleanerId;
    private String name;
    private String email;
    private String zipcode;
    private String phoneNumber;
    private String availableFrom;
    private String availableTo;
    private Integer price;
    private String description;
    private boolean isCleaner;

    public SearchViewModel(String currentUserId, String cleanerId, String name, String email, String zipcode, String phoneNumber, String availableFrom, String availableTo, Integer price, String description, boolean isCleaner) {
        this.currentUserId = currentUserId;
        this.cleanerId = cleanerId;
        this.name = name;
        this.email = email;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.price = price;
        this.description = description;
        this.isCleaner = isCleaner;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(String cleanerId) {
        this.cleanerId = cleanerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(String availableTo) {
        this.availableTo = availableTo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsCleaner() {
        return isCleaner;
    }

    public void setIsCleaner(Boolean isCleaner) {
        this.isCleaner = isCleaner;
    }
}