package com.yesgood.shitIn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class HealthReportActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too
     * memory intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RelativeLayout layout;
        private int MAX_PAST_DAY = 7;
        private int QUERY_LIMIT = 100;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_pager_charts, container, false);
            layout = (RelativeLayout) rootView;
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNum) {
                case 1:
                    try {
                        generateHealthDataPostData(HealthDataDefine.CONST_URINATE);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        generateHealthDataPostData(HealthDataDefine.CONST_SHIT);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    break;

            }

            return rootView;
        }

        private void generateHealthDataPostData(String type) throws java.text.ParseException {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, - (MAX_PAST_DAY-1));
            Date past7daysBeforeDate = cal.getTime();
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            String currentDateandTime = sdf.format(past7daysBeforeDate);
            try {
                past7daysBeforeDate = sdf.parse(currentDateandTime);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            ParseQuery<HealthDataPost> query = HealthDataPost.getQuery();
            query.include("user");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("type", type);
            //query.whereGreaterThan("CreatedAt",past7daysBeforeDate.toString());
            query.addAscendingOrder("CreatedAt");
            query.setLimit(QUERY_LIMIT);
            query.findInBackground(new FindCallback<HealthDataPost>() {
                @Override
                public void done(List<HealthDataPost> list, ParseException e) {
                    ArrayList<String> dateKeys = new ArrayList<String>();
                    HashMap<String, Integer> statusMap = new HashMap<String, Integer>();
                    long lastTime = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                    for (HealthDataPost post : list) {
                        Date createDate = post.getCreatedAt();
                        String dateStr = sdf.format(createDate);
                        String keyStr = dateStr + "_" + post.getHealthStatus();
                        lastTime = createDate.getTime();
                        if (!dateKeys.contains(dateStr)) {
                            dateKeys.add(dateStr);
                        }

                        if(statusMap.containsKey(keyStr)){
                            statusMap.put(keyStr, statusMap.get(keyStr) + 1);
                        }
                        else{
                            statusMap.put(keyStr, 1);
                        }
                    }

                    List<AxisValue> axisValues = new ArrayList<AxisValue>();
                    List<Column> columns = new ArrayList<Column>();
                    List<SubcolumnValue> values;
                    LinkedHashMap<String,Integer> statusDetail = new LinkedHashMap<String, Integer>();

                    statusDetail.put(HealthDataDefine.CONST_BAD, getResources().getColor(R.color.REPORT_COLOR_BAD));
                    statusDetail.put(HealthDataDefine.CONST_NORMAL, getResources().getColor(R.color.REPORT_COLOR_NORMAL));
                    statusDetail.put(HealthDataDefine.CONST_GOOD, getResources().getColor(R.color.REPORT_COLOR_GOOD));

                    for(int i = 0; i < dateKeys.size(); i++)
                    {
                        String dateKey = dateKeys.get(i);
                        values = new ArrayList<SubcolumnValue>();

                        for(LinkedHashMap.Entry<String,Integer> entry: statusDetail.entrySet())
                        {
                            String statusKey = dateKey + "_" + entry.getKey();
                            if(statusMap.containsKey( statusKey)) {
                                values.add(new SubcolumnValue((float) statusMap.get(statusKey), entry.getValue()));
                            }
                        }

                        axisValues.add(new AxisValue(i).setLabel(dateKeys.get(i)));
                        Column column = new Column(values);
                        column.setHasLabels(true);
                        columns.add(column);
                    }

                    ColumnChartData data = new ColumnChartData(columns);

                    data.setStacked(true);
                    data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
                    data.setAxisYLeft(new Axis().setHasLines(true));
                    //data.setAxisYLeft(Axis.generateAxisFromRange(0f, 20f, 1f).setHasLines(true));


                    ColumnChartView columnChartView = new ColumnChartView(getActivity());
                    columnChartView.setColumnChartData(data);
                    columnChartView.setZoomType(ZoomType.HORIZONTAL);

                    /** Note: Chart is within ViewPager so enable container scroll mode. **/
                    columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

                    layout.addView(columnChartView);


                }
            });

        }

        private ColumnChartData generateColumnChartData() {
            int numSubcolumns = 1;
            int numColumns = 12;
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                }

                columns.add(new Column(values));
            }

            ColumnChartData data = new ColumnChartData(columns);

            data.setAxisXBottom(new Axis().setName("Axis X"));
            data.setAxisYLeft(new Axis().setName("Axis Y").setHasLines(true));
            return data;

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pee";
                case 1:
                    return "Poo";
            }
            return null;
        }
    }

}