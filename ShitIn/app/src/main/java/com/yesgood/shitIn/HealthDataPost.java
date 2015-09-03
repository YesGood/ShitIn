package com.yesgood.shitIn;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by hsuanlin on 2015/8/30.
 */

@ParseClassName("HealthData")
public class HealthDataPost extends ParseObject {
    public void setHealthStatus( String value )
    {
        put("status",value);
    }

    public String getHealthStatus()
    {
        return getString("status");
    }

    public void setType ( String value )
    {
        put("type",value);
    }

    public String getType()
    {
        return getString("type");
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

    public Date getFakeDate() {
        return getDate("fakeDate");
    }

    public void setFakeDate(Date value) {
        put("fakeDate", value);
    }

    public static ParseQuery<HealthDataPost> getQuery() {
        return ParseQuery.getQuery(HealthDataPost.class);
    }

}
