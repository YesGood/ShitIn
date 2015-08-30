package com.yesgood.shitIn.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yesgood.shitIn.R;
import com.yesgood.shitIn.ToiletDataDefine;
import com.yesgood.shitIn.ToiletDataPost;

/**
 * Created by hsuanlin on 2015/8/28.
 */
public class ToiletDetail extends DialogFragment {
    private ToiletDataPost post;

    public static ToiletDetail newInstance(){
        ToiletDetail frag = new ToiletDetail();
        return frag;
    }

    public void setToiletData( ToiletDataPost data )
    {
        post = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toilet_detail, container, false );

        ImageView ivType = (ImageView) view.findViewById(R.id.ivTypeImage);
        TextView tvFloor = (TextView) view.findViewById(R.id.tvShowFloor);
        TextView tvBadyChanging = (TextView) view.findViewById(R.id.tvShowBabyChanging);
        TextView tvCloset = (TextView) view.findViewById(R.id.tvShowCloset);
        TextView tvUrinal = (TextView) view.findViewById(R.id.tvShowUrinal);
        TextView tvFee = (TextView) view.findViewById(R.id.tvShowFee);
        TextView tvPaper = (TextView) view.findViewById(R.id.tvShowPaper);
        TextView tvCreator = (TextView) view.findViewById(R.id.tvShowCreator);

        ivType.setImageResource(ToiletDataDefine.getTypeImageResID(post.getType()));
        tvFloor.setText(Integer.toString(post.getFloor()));
        tvBadyChanging.setText(ToiletDataDefine.getBabyChangingText(getActivity(), post.getBadyChanging()));
        tvCloset.setText(ToiletDataDefine.getClosetText(getActivity(), post.getCloset()));
        tvUrinal.setText(ToiletDataDefine.getUrinalText(getActivity(), post.getUrinal()));
        tvFee.setText(Integer.toString(post.getFee()));
        tvPaper.setText(ToiletDataDefine.getPaperText(getActivity(), post.getToiletPaper()));
        tvCreator.setText(post.getUser().getUsername());

        return view;
    }
}
