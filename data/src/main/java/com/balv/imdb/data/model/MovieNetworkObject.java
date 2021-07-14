package com.balv.imdb.data.model;

import com.google.gson.annotations.SerializedName;

public class MovieNetworkObject {
        @SerializedName("Title")
        public String title;
        @SerializedName("Year")
        public String year;
        public String imdbID;
        @SerializedName("Type")
        public String type;
        @SerializedName("Poster")
        public String poster;
}
