package com.yesgood.shitIn;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by hsuanlin on 2015/8/26.
 */
@ParseClassName("CheckInData")
public class CheckInDataPost extends ParseObject {
    public int getRating() {
        return getInt("rating");
    }

    public void setRating(int value) {
        put("rating", value);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String value) {
        put("content", value);
    }

    public int getHealthStatus() {
        return getInt("healthStatus");
    }

    public void setHealthStatus(int value) {
        put("healthStatus", value);
    }
}
