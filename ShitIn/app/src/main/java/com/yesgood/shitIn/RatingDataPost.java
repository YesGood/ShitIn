package com.yesgood.shitIn;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by hsuanlin on 2015/8/30.
 */

/**
 * Data model for a post.
 */
@ParseClassName("RatingData")
public class RatingDataPost extends ParseObject {

    public void setRating( double value )
    {
        put("rating", value);
    }

    public double getRating()
    {
        return getDouble("rating");
    }

    public void setComment( String comment )
    {
        String s = comment;
        if( comment.length() > R.integer.COMMENT_MAX_LEN) {
            s = comment.substring(0, R.integer.COMMENT_MAX_LEN);
        }
        put("comment",s);
    }

    public String getComment( )
    {
        return getString("comment");
    }

    public void setToiletObjectId( String value )
    {
        put("toiletObjectId",value);
    }

    public String getToiletObjectId()
    {
        return getString("toiletObjectId");
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

    public static ParseQuery<RatingDataPost> getQuery() {
        return ParseQuery.getQuery(RatingDataPost.class);
    }
}

