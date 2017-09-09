package com.example.carlos.fokus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eceybrito on 09/09/2017.
 */

public class Spots {

    @SerializedName("spot")
    @Expose
    private List<Spot> spot = null;

    public List<Spot> getSpot() {
        return spot;
    }

    public void setSpot(List<Spot> spot) {
        this.spot = spot;
    }

}
