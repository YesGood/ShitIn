package com.yesgood.shitIn.fragments;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yesgood.shitIn.R;
import com.yesgood.shitIn.ToiletDataDefine;
import com.yesgood.shitIn.ToiletDataPost;


/**
 * Created by hsuanlin on 2015/8/27.
 */
public class CreateNewToilet extends DialogFragment {
    private Location location;
    private EditText etName;
    private RadioGroup rgType;
    private EditText etFloor;
    private RadioGroup rgBabyChanging;
    private Spinner closetSpinner;
    private Spinner urinalSpinner;
    private EditText etFee;
    private RadioGroup rgPaper;
    private RadioGroup rgPrivate;

    public static CreateNewToilet newInstance(){
        CreateNewToilet frag = new CreateNewToilet();
        return frag;
    }

    public void setLocation( Location vaule )
    {
        location = vaule;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_create_new_toilet, container, false);

        etName = (EditText) view.findViewById(R.id.etName);
        rgType = (RadioGroup) view.findViewById(R.id.rgType);
        etFloor = (EditText) view.findViewById(R.id.etFloor);
        rgBabyChanging = (RadioGroup) view.findViewById(R.id.rgBadyChanging);
        etFee = (EditText) view.findViewById(R.id.etFee);
        rgPaper = (RadioGroup) view.findViewById(R.id.rgPaper);
        rgPrivate = (RadioGroup) view.findViewById(R.id.rgPrivate);
        closetSpinner = (Spinner) view.findViewById(R.id.spCloset);
        urinalSpinner = (Spinner) view.findViewById(R.id.spUrinal);
        TextView locationText = (TextView) view.findViewById(R.id.tvLocationData);
        Button btSubmit = (Button) view.findViewById(R.id.btSubmit);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);

        locationText.setText(location.toString());
        ArrayAdapter<CharSequence> closetAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.closet_numeric_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> urinalAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.urinal_numeric_array, android.R.layout.simple_spinner_item);


        rgType.check(R.id.type_man);
        rgBabyChanging.check(R.id.bady_changeing_no);
        rgPaper.check(R.id.paper_yes);
        rgPrivate.check(R.id.private_no);

        closetSpinner.setAdapter(closetAdapter);
        urinalSpinner.setAdapter(urinalAdapter);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etFloor.length() == 0 || etFee.length() == 0 || etName.length() == 0)
                {
                    Toast.makeText(getActivity(),R.string.answer_all_questions,Toast.LENGTH_SHORT).show();
                    return;
                }
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
    private void post () {
        String name = etName.getText().toString();
        int type = ToiletDataDefine.getTypeIndex(rgType.getCheckedRadioButtonId());
        int floor = Integer.parseInt(etFloor.getText().toString());
        boolean babyChanging = ToiletDataDefine.getBabyChangingIndex(rgBabyChanging.getCheckedRadioButtonId());
        int fee =Integer.parseInt(etFee.getText().toString());
        boolean paper = ToiletDataDefine.getPaperIndex(rgPaper.getCheckedRadioButtonId());
        boolean pirvate = ToiletDataDefine.getPrivateIndex(rgPrivate.getCheckedRadioButtonId());
        int closetNum = closetSpinner.getSelectedItemPosition();
        int urinalNum = urinalSpinner.getSelectedItemPosition();
        ParseUser user = ParseUser.getCurrentUser();

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.progress_post));
        dialog.show();

        // Create a post.
        ToiletDataPost post = new ToiletDataPost();

        // Set the location to the current user's location
        ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        post.setLocation(geoPoint);
        post.setName(name);
        post.setType(type);
        post.setFloor(floor);
        post.setBadyChanging(babyChanging);
        post.setFee(fee);
        post.setToiletPaper(paper);
        post.setCloset(closetNum);
        post.setUrinal(urinalNum);
        post.setUser(user);

        //post.setUser(ParseUser.getCurrentUser());
        ParseACL acl = new ParseACL();

        // Give public read access
        if( !pirvate) {
            acl.setPublicReadAccess(true);
        }
        else
        {
            acl.setReadAccess(user,true);
        }
        post.setACL(acl);

        // Save the post
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
            }
        });
    }

}
