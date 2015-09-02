package com.yesgood.shitIn;

import java.util.HashMap;

/**
 * Created by hsuanlin on 2015/8/30.
 */
public class HealthDataDefine {
    public static final String CONST_SHIT = "shit";
    public static final String CONST_URINATE = "urinate";
    public static final String CONST_BAD = "bad";
    public static final String CONST_NORMAL = "normal";
    public static final String CONST_GOOD = "good";

    private static HashMap<Integer,String> type_map;
    private static HashMap<Integer,String> status_map;

    static
    {
        type_map = new HashMap<Integer,String>();
        type_map.put(R.id.type_shit, CONST_SHIT);
        type_map.put(R.id.type_urinate, CONST_URINATE);

        status_map = new HashMap<Integer,String>();
        status_map.put(R.id.status_bad,CONST_BAD);
        status_map.put(R.id.status_normal,CONST_NORMAL);
        status_map.put(R.id.status_good, CONST_GOOD);
    }

    public static String getTypeIndex(Integer id)
    {
        if(type_map.containsKey(id))
        {
            return type_map.get(id);
        }

        return "";
    }

    public static String getStatusIndex( Integer id)
    {
        if(status_map.containsKey(id))
        {
            return status_map.get(id);
        }

        return "";
    }


}
