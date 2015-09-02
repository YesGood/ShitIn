package com.yesgood.shitIn.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.yesgood.shitIn.R;
import com.yesgood.shitIn.RatingDataPost;
import com.yesgood.shitIn.ToiletDataDefine;
import com.yesgood.shitIn.ToiletDataPost;

/**
 * Created by hsuanlin on 2015/8/28.
 */
public class ToiletDetail extends DialogFragment {
    private static int MAX_POST_SEARCH_RESULTS = 10;

    // Map fragment
    private SupportMapFragment mapFragment;
    private ToiletDataPost post;
    // Adapter for the Parse query
    private ParseQueryAdapter<RatingDataPost> postsQueryAdapter;

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

        TextView tvDetailShowName = (TextView) view.findViewById(R.id.tvDetailShowName);
        ImageView ivType = (ImageView) view.findViewById(R.id.ivTypeImage);
        TextView tvFloor = (TextView) view.findViewById(R.id.tvShowFloor);
        TextView tvBadyChanging = (TextView) view.findViewById(R.id.tvShowBabyChanging);
        TextView tvCloset = (TextView) view.findViewById(R.id.tvShowCloset);
        TextView tvUrinal = (TextView) view.findViewById(R.id.tvShowUrinal);
        TextView tvFee = (TextView) view.findViewById(R.id.tvShowFee);
        TextView tvPaper = (TextView) view.findViewById(R.id.tvShowPaper);
        TextView tvCreator = (TextView) view.findViewById(R.id.tvShowCreator);
        Button btShitIn = (Button) view.findViewById(R.id.btShitIn);
        Button btCancel = (Button) view.findViewById(R.id.btLeave);
        ListView lvRatingList = (ListView) view.findViewById( R.id.lvRatingList );

        btShitIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CheckIn checkIn = CheckIn.newInstance();
                checkIn.setToiletName(post.getName());
                checkIn.setToiletObjectId(post.getObjectId());
                checkIn.show(fm,"test");
                dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvDetailShowName.setText(post.getName());
        ivType.setImageResource(ToiletDataDefine.getTypeImageResID(post.getType()));
        tvFloor.setText(Integer.toString(post.getFloor()));
        tvBadyChanging.setText(ToiletDataDefine.getBabyChangingText(getActivity(), post.getBadyChanging()));
        tvCloset.setText(ToiletDataDefine.getClosetText(getActivity(), post.getCloset()));
        tvUrinal.setText(ToiletDataDefine.getUrinalText(getActivity(), post.getUrinal()));
        tvFee.setText(Integer.toString(post.getFee()));
        tvPaper.setText(ToiletDataDefine.getPaperText(getActivity(), post.getToiletPaper()));
        tvCreator.setText(post.getUser().getUsername());

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<RatingDataPost> factory =
                new ParseQueryAdapter.QueryFactory<RatingDataPost>() {
                    public ParseQuery<RatingDataPost> create() {
                        ParseQuery<RatingDataPost> query = RatingDataPost.getQuery();
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.whereEqualTo("toiletObjectId", post.getObjectId());
                        query.setLimit(MAX_POST_SEARCH_RESULTS);

                        return query;
                    }
                };

        // Set up the query adapter
        postsQueryAdapter = new ParseQueryAdapter<RatingDataPost>(getActivity().getApplicationContext(), factory) {
            @Override
            public View getItemView(RatingDataPost post, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.rating_item, null);
                }

                TextView tvName = (TextView) view.findViewById(R.id.tvCreator);
                RatingBar rbRating = (RatingBar) view.findViewById(R.id.rbRating);
                TextView tvCreatedTime = (TextView) view.findViewById(R.id.tvCreatedTime);
                TextView tvComment = (TextView) view.findViewById(R.id.tvComment);

                tvName.setText(post.getUser().getUsername());
                rbRating.setRating(((float) post.getRating()));
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                tvCreatedTime.setText(df.format("yyyy/MM/dd",post.getCreatedAt()));
                tvComment.setText(post.getComment().toString());

                return view;
            }
        };

        postsQueryAdapter.setAutoload(false);
        postsQueryAdapter.setObjectsPerPage(6);
        postsQueryAdapter.setPaginationEnabled(true);
        lvRatingList.setAdapter(postsQueryAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if( post != null )
        {
            postsQueryAdapter.loadObjects();;
        }

    }
}
