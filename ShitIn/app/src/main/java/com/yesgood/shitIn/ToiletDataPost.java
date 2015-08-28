package com.yesgood.shitIn;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a post.
 */
@ParseClassName("ToiletData")
public class ToiletDataPost extends ParseObject {
  public String getName() {
    return getString("name");
  }

  public void setName(String value) {
    put("name", value);
  }

  public int getType() {
    return getInt("type");
  }

    public void setType(int value) {
        put("type", value);
    }

  public void setFloor(int value) {
    put("floor", value);
  }

  public int getFloor() {
    return getInt("floor");
  }

  public void setBadyChanging(boolean value) {
    put("badyChanging", value);
  }

  public boolean getBadyChanging() {
    return getBoolean("floor");
  }

    public void setCloset(int value)
    {
        put("closet", value);
    }

    public int getCloset( )
    {
        return getInt("closet");
    }

    public void setUrinal(int value)
    {
        put("urinal", value);
    }

    public int getUrinal( )
    {
        return getInt("urinal");
    }

    public void setFee(int value)
    {
        put("fee", value);
    }

    public int getFee( )
    {
        return getInt("fee");
    }

    public void setToiletPaper(boolean value) {
        put("toiletPaper", value);
    }

    public boolean getToiletPaper() {
        return getBoolean("toiletPaper");
    }

  public ParseUser getUser() {
    return getParseUser("user");
  }

  public void setUser(ParseUser value) {
    put("user", value);
  }


    public ParseGeoPoint getLocation() {
    return getParseGeoPoint("location");
  }

  public void setLocation(ParseGeoPoint value) {
    put("location", value);
  }

  public static ParseQuery<ToiletDataPost> getQuery() {
    return ParseQuery.getQuery(ToiletDataPost.class);
  }
}
