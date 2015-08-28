package com.yesgood.shitIn;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by hsuanlin on 2015/8/27.
 */
public class ToiletDataDefine {

    private static Integer[] type_index = {R.id.type_man,R.id.type_woman,R.id.type_all,R.id.type_withchildren,R.id.type_accessible};
    private static Integer[] type_image = {R.mipmap.ic_toilet_man,R.mipmap.ic_toilet_woman,R.mipmap.ic_toilet_all,R.mipmap.ic_toilet_withchildren,R.mipmap.ic_toilet_accessible};
    private static Integer[] baby_changing_index = {R.id.bady_changeing_yes, R.id.bady_changeing_no};
    private static Integer[] paper_index = {R.id.paper_yes, R.id.paper_no};
    private static Integer[] private_index = {R.id.private_yes, R.id.private_no};

    public static int getTypeIndex( int id )
    {
        return Arrays.asList(type_index).indexOf(id);
    }

    public static boolean getBabyChangingIndex( int id )
    {
        return Arrays.asList(baby_changing_index).indexOf(id) == 0 ? true : false;
    }

    public static boolean getPaperIndex( int id )
    {
        return Arrays.asList(paper_index).indexOf(id) == 0 ? true: false;
    }

    public static String getBabyChangingText( Context context, boolean flag )
    {
        return flag == true ? context.getResources().getString(R.string.yes_text) : context.getResources().getString(R.string.no_text);
    }

    public static String getPaperText( Context context, boolean flag )
    {
        return flag == true ? context.getResources().getString(R.string.yes_text) : context.getResources().getString(R.string.no_text);
    }

    public static String getClosetText( Context context, int value )
    {
        String[] closet_array = context.getResources().getStringArray(R.array.closet_numeric_array);
        return closet_array.length > value ? closet_array[value] : "";
    }

    public static String getUrinalText( Context context, int value )
    {
        String[] urinal_array = context.getResources().getStringArray(R.array.urinal_numeric_array);
        return urinal_array.length > value ? urinal_array[value] : "";
    }

    public static boolean getPrivateIndex( int id )
    {
        return Arrays.asList(private_index).indexOf(id) == 0 ? true : false;
    }

    public static int getTypeImageResID(int typeIndex)
    {
        return type_image[typeIndex];
    }
}

