package com.omarshafei.covid_19tracker.ui.world;

public class Module {

    private String imageUrl;
    private String countryName;
    private String cases;
    private String deaths;
    private String recovered;

    public Module(String imageUrl, String countryName, String cases, String deaths, String recovered) {
        this.imageUrl = imageUrl;
        this.countryName = countryName;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCases() {
        return cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getRecovered() {
        return recovered;
    }
}