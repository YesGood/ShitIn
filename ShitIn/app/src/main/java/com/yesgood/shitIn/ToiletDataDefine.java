package com.yesgood.shitIn;

import java.util.Arrays;

/**
 * Created by hsuanlin on 2015/8/27.
 */
public class ToiletDataDefine {

    private static Integer[] type_index = {R.id.type_man,R.id.type_woman,R.id.type_all,R.id.type_withchildren,R.id.type_accessible};
    private static Integer[] type_image = {R.mipmap.ic_men_on,R.mipmap.ic_woman_on,R.mipmap.ic_both_on,R.mipmap.ic_family_on,R.mipmap.ic_handicap_on};
    private static Integer[] type_image_off = {R.mipmap.ic_men_off,R.mipmap.ic_woman_off,R.mipmap.ic_both_off,R.mipmap.ic_family_off,R.mipmap.ic_handicap_off};

    public static int getTypeIndex( int id )
    {
        return Arrays.asList(type_index).indexOf(id);
    }

    public static int getTypeImageResID(int typeIndex)
    {
        return getTypeImageResID(typeIndex, true);
    }

    public static int getTypeImageResID(int typeIndex, boolean isOn)
    {
        if(isOn)
            return type_image[typeIndex];
        else
            return type_image_off[typeIndex];
    }

    public static Integer[] getType_index() {
        return type_index;
    }
}

