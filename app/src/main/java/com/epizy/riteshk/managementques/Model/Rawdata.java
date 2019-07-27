package com.epizy.riteshk.managementques.Model;

public class Rawdata {
    private String profilepicURL;
    private String coverUrl;
    private String description;
    private String myDescription;

    public Rawdata(String profilepicURL, String coverUrl, String description, String myDescription) {

        this.profilepicURL = profilepicURL;
        this.coverUrl = coverUrl;
        this.description = description;
        this.myDescription = myDescription;
    }
    public Rawdata(){}


    public String getProfilepicURL() {
        return profilepicURL;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getMyDescription() {
        return myDescription;
    }
}
