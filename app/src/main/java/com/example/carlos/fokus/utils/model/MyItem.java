package com.example.carlos.fokus.utils.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Carlos on 12/09/2017.
 */


public class MyItem implements ClusterItem, IMyItem {
    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private float mIdIcon;
    private  Object mObject;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
        mIdIcon = 0;
        mObject = null;
    }

    public MyItem(double lat, double lng, String title, String snippet, float idIcon,Object object) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mIdIcon = idIcon;
        mObject = object;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() { return mTitle; }

    @Override
    public String getSnippet() { return mSnippet; }

    @Override
    public float getIdIcon(){
        return mIdIcon;
    }

    @Override
    public Object getOBject(){
        return mObject;
    }

    @Override
    public void setIdIcon(float idIcon){
        mIdIcon = idIcon;
    }

    @Override
    public void setObject(Object object){
        mObject = object;
    }


    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }
}