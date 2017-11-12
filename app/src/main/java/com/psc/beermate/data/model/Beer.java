package com.psc.beermate.data.model;

import com.google.gson.annotations.SerializedName;

public class Beer {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("first_brewed")
    private String firstBrewed;

    public Beer() {
    }

    public Beer(int id, String name, String description, String imageUrl, String firstBrewed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.firstBrewed = firstBrewed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFirstBrewed() {

        return firstBrewed;
    }
}
