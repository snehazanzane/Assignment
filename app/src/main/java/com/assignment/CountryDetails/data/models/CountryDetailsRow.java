package com.assignment.CountryDetails.data.models;

import com.google.gson.annotations.SerializedName;

public class CountryDetailsRow {
    /*
    {
	"title":"Beavers",
	"description":"Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
	"imageHref":"http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
	}
     */

    @SerializedName("title")
    private String title="";

    @SerializedName("description")
    private String description="";

    @SerializedName("imageHref")
    private String imageHref="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }
}
