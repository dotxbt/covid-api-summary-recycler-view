package com.example.covidsummary;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Sabituddin Bigbang
 * @since 13 March 2021
 * @apiNote Model for Gson Serialiization
 */
public class ItemModel {
    @SerializedName("ID")
    private String id;

    @SerializedName("Country")
    private String country;

    @SerializedName("CountryCode")
    private String countryCode;

    @SerializedName("Slug")
    private String slug;

    @SerializedName("NewConfirmed")
    private int newConfirmed;

    @SerializedName("TotalConfirmed")
    private int totalConfirmed;

    @SerializedName("NewDeaths")
    private int newDeaths;

    @SerializedName("TotalDeaths")
    private int totalDeaths;

    @SerializedName("NewRecovered")
    private int newRecovered;

    @SerializedName("TotalRecovered")
    private int totalRecovered;

    @SerializedName("Date")
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        this.newRecovered = newRecovered;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String toString() {
        return id + ", "+ country+","+countryCode+","+slug+","+newConfirmed+","+totalConfirmed+","+newDeaths+","+totalDeaths+","+newRecovered+","+totalRecovered+","+date.toString();
    }
}
