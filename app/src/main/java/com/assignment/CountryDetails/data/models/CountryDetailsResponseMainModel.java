package com.assignment.CountryDetails.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CountryDetailsResponseMainModel {

    @SerializedName("title")
    private String title;

    @SerializedName("rows")
    private List<CountryDetailsRow> rows = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CountryDetailsRow> getRows() {
        return rows;
    }

    public void setRows(List<CountryDetailsRow> rows) {
        this.rows = rows;
    }
}
