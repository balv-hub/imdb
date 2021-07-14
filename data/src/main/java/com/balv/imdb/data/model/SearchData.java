package com.balv.imdb.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchData {
    @SerializedName("Search")
    public ArrayList<MovieNetworkObject> list;
    public String totalResults;
    @SerializedName("Response")
    public String response;
}
