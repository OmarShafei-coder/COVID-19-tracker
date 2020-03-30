package com.omarshafei.covid_19tracker.ui.world;

public class Module {

    private String imageUrl;
    private String countryName;
    private String cases;
    private String deaths;
    private String recovered;

    Module(String imageUrl, String countryName, String cases, String deaths, String recovered) {
        this.imageUrl = imageUrl;
        this.countryName = countryName;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    String getImageUrl() {
        return imageUrl;
    }

    String getCountryName() {
        return countryName;
    }

    String getCases() {
        return cases;
    }

    String getDeaths() {
        return deaths;
    }

    String getRecovered() {
        return recovered;
    }
}