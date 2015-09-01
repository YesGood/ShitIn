package com.yesgood.shitIn.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yesgood.shitIn.HealthDataDefine;
import com.yesgood.shitIn.HealthDataPost;
import com.yesgood.shitIn.R;
import com.yesgood.shitIn.RatingDataPost;

import java.util.ArrayList;

/**
 * Created by hsuanlin on 2015/8/30.
 */
public class CheckIn extends DialogFragment {

    private String toiletName;
    private String toiletObjectId;
    private EditText etName;
    private RatingBar rbRatingBar;
    private EditText etComment;
    private RadioGroup rgType;
    private RadioGroup rgStatus;

    public static CheckIn newInstance(){
        CheckIn frag = new CheckIn();
        return frag;
    }

    public void setToiletName(String toiletName) {
        this.toiletName = toiletName;
    }

    public void setToiletObjectId(String toiletObjectId) {
        this.toiletObjectId = toiletObjectId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shitit, container, false);

        etName = (EditText) view.findViewById(R.id.etName);
        rbRatingBar = (RatingBar) view.findViewById(R.id.rbRatingBar);
        etComment = (EditText) view.findViewById(R.id.etComment);
        rgType = (RadioGroup) view.findViewById(R.id.rgType);
        rgStatus = (RadioGroup) view.findViewById(R.id.rgStatus);
        Button btSubmit = (Button) view.findViewById(R.id.btSubmit);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);

        rgType.check(R.id.type_urinate);
        rgStatus.check(R.id.status_normal);
        etName.setText(this.toiletName);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
                dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void post(){
        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<ParseObject> objects = new ArrayList<>();

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.progress_post));
        dialog.show();

        RatingDataPost ratingDataPost = new RatingDataPost();

        ratingDataPost.setUser(user);
        ratingDataPost.setToiletObjectId(this.toiletObjectId);
        ratingDataPost.setComment(etComment.getText().toString());
        ratingDataPost.setRating(rbRatingBar.getRating());

        ParseACL aclRating = new ParseACL();
        aclRating.setPublicReadAccess(true);

        ratingDataPost.setACL(aclRating);

        objects.add(ratingDataPost);

        HealthDataPost healthDataPost = new HealthDataPost();

        healthDataPost.setUser(user);
        healthDataPost.setToiletObjectId(this.toiletObjectId);
        healthDataPost.setType(HealthDataDefine.getTypeIndex(rgType.getCheckedRadioButtonId()));
        healthDataPost.setHealthStatus(HealthDataDefine.getStatusIndex(rgStatus.getCheckedRadioButtonId()));

        ParseACL aclHealth = new ParseACL();
        aclHealth.setReadAccess(user, true);

        healthDataPost.setACL(aclHealth);

        objects.add(healthDataPost);

        ParseObject.saveAllInBackground(objects, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
            }
        });
    }
}
