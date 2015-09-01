package com.yesgood.shitIn;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by hsuanlin on 2015/8/27.
 */
public class ToiletDataDefine {

    private static Integer[] type_index = {R.id.type_man, R.id.type_woman, R.id.type_all, R.id.type_withchildren, R.id.type_accessible};
    private static Integer[] type_image = {R.mipmap.ic_men_on, R.mipmap.ic_woman_on, R.mipmap.ic_both_on, R.mipmap.ic_family_on, R.mipmap.ic_handicap_on};
    private static Integer[] type_image_off = {R.mipmap.ic_men_off, R.mipmap.ic_woman_off, R.mipmap.ic_both_off, R.mipmap.ic_family_off, R.mipmap.ic_handicap_off};

    public static int getTypeIndex(int id) {
        return Arrays.asList(type_index).indexOf(id);
    }

    public static int getTypeImageResID(int typeIndex) {
        return getTypeImageResID(typeIndex, true);
    }

    public static int getTypeImageResID(int typeIndex, boolean isOn) {
        if (isOn)
            return type_image[typeIndex];
        else
            return type_image_off[typeIndex];
    }

    public static Integer[] getType_index() {
        return type_index;
    }

    public static String getBabyChangingText(Context context, boolean flag) {
        return flag == true ? context.getResources().getString(R.string.yes_text) : context.getResources().getString(R.string.no_text);
    }

    public static String getPaperText(Context context, boolean flag) {
        return flag == true ? context.getResources().getString(R.string.yes_text) : context.getResources().getString(R.string.no_text);
    }

    public static String getClosetText(Context context, int value) {
        String[] closet_array = context.getResources().getStringArray(R.array.closet_numeric_array);
        return closet_array.length > value ? closet_array[value] : "";
    }

    public static String getUrinalText(Context context, int value) {
        String[] urinal_array = context.getResources().getStringArray(R.array.urinal_numeric_array);
        return urinal_array.length > value ? urinal_array[value] : "";
    }
}

