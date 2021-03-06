package com.yesgood.shitIn.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hsuanlin on 2015/8/30.
 */
public class CheckIn extends DialogFragment {

    private String toiletName;
    private String toiletObjectId;
    private TextView etName;
    private RatingBar rbRatingBar;
    private EditText etComment;
    private RadioGroup rgType;
    private RadioGroup rgStatus;
    private RelativeLayout rlRatingGroup;

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

        etName = (TextView) view.findViewById(R.id.etName);
        rbRatingBar = (RatingBar) view.findViewById(R.id.rbRatingBar);
        etComment = (EditText) view.findViewById(R.id.etComment);
        rgType = (RadioGroup) view.findViewById(R.id.rgType);
        rgStatus = (RadioGroup) view.findViewById(R.id.rgStatus);
        Button btSubmit = (Button) view.findViewById(R.id.btSubmit);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);
        ImageButton ibClose = (ImageButton) view.findViewById(R.id.ibClose);
        rlRatingGroup = (RelativeLayout) view.findViewById(R.id.rlRatingGroup);

        rgType.check(R.id.type_urinate);
        rgStatus.check(R.id.status_normal);
        etName.setText(this.toiletName);

        if(this.toiletObjectId == null )
        {
            rlRatingGroup.setVisibility(View.GONE);
        }

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

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlRatingGroup.setVisibility(View.GONE);
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

        if(rlRatingGroup.getVisibility() == View.VISIBLE ) {
            RatingDataPost ratingDataPost = new RatingDataPost();

            ratingDataPost.setUser(user);
            ratingDataPost.setToiletObjectId(this.toiletObjectId);
            ratingDataPost.setComment(etComment.getText().toString());
            ratingDataPost.setRating(rbRatingBar.getRating());

            ParseACL aclRating = new ParseACL();
            aclRating.setPublicReadAccess(true);

            ratingDataPost.setACL(aclRating);

            objects.add(ratingDataPost);
        }

        Calendar cal = GregorianCalendar.getInstance();

/*       for(int i = 0; i < 80 ; i++) {

            Random r = new Random();
            int past_day = r.nextInt(6) + 1;*/

            cal.setTime(new Date());
//            cal.add(Calendar.DAY_OF_YEAR, - past_day);
//            Date past7daysBeforeDate = cal.getTime();

            HealthDataPost healthDataPost = new HealthDataPost();

            healthDataPost.setFakeDate(cal.getTime());

            healthDataPost.setUser(user);
            healthDataPost.setType(HealthDataDefine.getTypeIndex(rgType.getCheckedRadioButtonId()));
            healthDataPost.setHealthStatus(HealthDataDefine.getStatusIndex(rgStatus.getCheckedRadioButtonId()));

/*            String[] typeArray = {"shit","urinate"};
            String[] statusArray = {"bad","normal","good"};
            healthDataPost.setType(typeArray[r.nextInt(2)]);
            healthDataPost.setHealthStatus(statusArray[r.nextInt(3)]);*/

            ParseACL aclHealth = new ParseACL();
            aclHealth.setReadAccess(user, true);

            healthDataPost.setACL(aclHealth);

            objects.add(healthDataPost);
//        }
            ParseObject.saveAllInBackground(objects, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    dialog.dismiss();
                }
            });

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        theme = R.style._FragmentTheme;
        setStyle(style, theme);
    }
}
