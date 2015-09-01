package com.yesgood.shitIn.fragments;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yesgood.shitIn.R;
import com.yesgood.shitIn.ToiletDataPost;

import static com.yesgood.shitIn.ToiletDataDefine.getFloorText;
import static com.yesgood.shitIn.ToiletDataDefine.getTypeImageResID;
import static com.yesgood.shitIn.ToiletDataDefine.getTypeIndex;
import static com.yesgood.shitIn.ToiletDataDefine.getType_index;


/**
 * Created by hsuanlin on 2015/8/27.
 */
public class CreateNewToilet extends DialogFragment {
    private Location location;
    private EditText etName;
    private RadioGroup rgType;
    private NumberPicker npFloor;
    private NumberPicker npCloset;
    private NumberPicker npUrinal;
    private Button btSubmit;
    private Button btCancel;
    private CheckBox cbBabyChanging;
    private CheckBox cbToiletPaper;
    private CheckBox cbFee;
    private CheckBox cbPrivate;
    public final static int MAX_FLOOR = 101;
    public final static int MIN_FLOOR = -10;

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
        npFloor = (NumberPicker) view.findViewById(R.id.npFloor);
        npCloset = (NumberPicker) view.findViewById(R.id.npCloset);
        npUrinal = (NumberPicker) view.findViewById(R.id.npUrinal);

        btSubmit = (Button) view.findViewById(R.id.btSubmit);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        cbBabyChanging = (CheckBox) view.findViewById(R.id.cbBabyChanging);
        cbFee = (CheckBox) view.findViewById(R.id.cbFee);
        cbToiletPaper = (CheckBox) view.findViewById(R.id.cbToiletPaper);
        cbPrivate = (CheckBox) view.findViewById(R.id.cbPrivate);
        setupView();

        return view;
    }

    private void setupView()
    {
       // locationText.setText(location.toString());



        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Integer[] ar = getType_index();
                for (int i = 0; i < ar.length; i++) {
                    int id = ar[i];

                    RadioButton rb = (RadioButton) group.findViewById(id);

                    rb.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(
                                    getTypeImageResID(
                                            getTypeIndex(id), (checkedId == id)
                                    )
                            ), null, null);

                }
            }
        });
        rgType.check(R.id.type_man);

        String[] closets = getResources().getStringArray( R.array.closet_numeric_array);
        npCloset.setMinValue(0);
        npCloset.setMaxValue(closets.length - 1);
        npCloset.setDisplayedValues(closets);
        npCloset.setValue(0);
        npCloset.setWrapSelectorWheel(false);
        npCloset.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        String[] urinals = getResources().getStringArray( R.array.urinal_numeric_array);
        npUrinal.setMaxValue(0);
        npUrinal.setMaxValue(urinals.length - 1);
        npUrinal.setDisplayedValues(urinals);
        npUrinal.setValue(0);
        npUrinal.setWrapSelectorWheel(false);
        npUrinal.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        npFloor.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                int floor = index + MIN_FLOOR;
                return getFloorText(floor);
            }
        });
        npFloor.setMinValue(0);
        npFloor.setMaxValue(MAX_FLOOR - MIN_FLOOR);

        npFloor.setValue(-MIN_FLOOR);
        npFloor.setWrapSelectorWheel(false);
        npFloor.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.length() == 0)
                {
                    Toast.makeText(getActivity(), R.string.answer_all_questions, Toast.LENGTH_SHORT).show();
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
    }
    private void post () {
        String name = etName.getText().toString();
        int type = getTypeIndex(rgType.getCheckedRadioButtonId());
        int floor = npFloor.getValue() + MIN_FLOOR;
        boolean babyChanging = cbBabyChanging.isChecked();
        int fee = (cbFee.isChecked())?1:0;
        boolean paper = cbToiletPaper.isChecked();
        boolean pirvate = cbPrivate.isChecked();
        int closetNum = npCloset.getValue();
        int urinalNum = npUrinal.getValue();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        theme = R.style.AppTheme;
        setStyle(style, theme);
    }
}
