package com.yesgood.shitIn.fragments;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yesgood.shitIn.HealthDataDefine;
import com.yesgood.shitIn.HealthDataPost;
import com.yesgood.shitIn.R;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hsuanlin on 2015/9/2.
 */
public class HealthReport extends DialogFragment {

    private LinearLayout llBarChart;
    private TextView tvLastUrinate;
    private View view;

    public static HealthReport newInstance(){
        HealthReport frag = new HealthReport();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NO_FRAME, theme = 0;
        theme = R.style.AppTheme;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_report, container, false);

        llBarChart = (LinearLayout) view.findViewById(R.id.llBarChart);
        tvLastUrinate = (TextView) view.findViewById(R.id.tvLastUrinate);

        ParseQuery<HealthDataPost> query = HealthDataPost.getQuery();
        query.include("user");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addAscendingOrder("CreatedAt");
        query.setLimit(100);
        query.findInBackground(new FindCallback<HealthDataPost>() {
            @Override
            public void done(List<HealthDataPost> list, ParseException e) {
                ArrayList<String> dateKeys = new ArrayList<String>();
                HashMap<String, Integer> urinalData = new HashMap<String, Integer>();
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

                    if(urinalData.containsKey(keyStr)){
                        urinalData.put(keyStr, urinalData.get(keyStr) + 1);
                    }
                    else{
                        urinalData.put(keyStr, 1);
                    }
                }

                setBarChart(dateKeys,urinalData);
                if (lastTime != 0) {
                    tvLastUrinate.setText(getRelativeTimeAgo(lastTime));
                }
            }
        });


        return view;
    }

    private void setBarChart( ArrayList<String> keys, HashMap<String,Integer> map)
    {
        View vChart = getBarChart(view,"", "date", "counter", keys,map);
        llBarChart.removeAllViews();
        llBarChart.addView(vChart, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 800));
    }
    private View getBarChart(View layoutView, String chartTitle, String XTitle, String YTitle, ArrayList<String> keys, HashMap<String,Integer> map){

        XYSeries nSeries = new XYSeries("normal");
        XYSeries bSeries = new XYSeries("bad");
        XYSeries gSeries = new XYSeries("good");

        XYMultipleSeriesDataset Dataset = new XYMultipleSeriesDataset();
        Dataset.addSeries(nSeries);
        Dataset.addSeries(bSeries);
        Dataset.addSeries(gSeries);

        XYMultipleSeriesRenderer Renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer nRenderer = new XYSeriesRenderer();
        XYSeriesRenderer bRenderer = new XYSeriesRenderer();
        XYSeriesRenderer gRenderer = new XYSeriesRenderer();
        Renderer.addSeriesRenderer(nRenderer);
        Renderer.addSeriesRenderer(bRenderer);
        Renderer.addSeriesRenderer(gRenderer);

        //Renderer.setApplyBackgroundColor(true);
        //Renderer.setBackgroundColor(Color.BLACK);
        Renderer.setMarginsColor(Color.WHITE);
        Renderer.setTextTypeface(null, Typeface.BOLD);

        Renderer.setShowGrid(true);
        Renderer.setGridColor(Color.GRAY);

        Renderer.setChartTitle(chartTitle);
        Renderer.setLabelsColor(Color.BLACK);
        Renderer.setChartTitleTextSize(20);
        Renderer.setAxesColor(Color.BLACK);
        //Renderer.setBarSpacing(0.5);

        //Renderer.setXTitle(XTitle);
        //Renderer.setYTitle(YTitle);
        Renderer.setXLabelsColor(Color.BLACK);
        Renderer.setYLabelsColor(0, Color.BLACK);
        Renderer.setXLabelsAlign(Paint.Align.CENTER);
        Renderer.setYLabelsAlign(Paint.Align.CENTER);
        //Renderer.setXLabelsAngle(-25);
        Renderer.setLabelsTextSize(30);

        Renderer.setXLabels(0);
        Renderer.setYAxisMin(0);
        Renderer.setYAxisMax(20);

        //Renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);


        bRenderer.setColor(Color.RED);
        bRenderer.setChartValuesTextSize(20);
        bRenderer.setDisplayChartValues(true);
        bRenderer.setChartValuesTextSize(20);
        nRenderer.setColor(Color.BLUE);
        nRenderer.setChartValuesTextSize(20);
        nRenderer.setDisplayChartValues(true);
        nRenderer.setChartValuesTextSize(20);
        gRenderer.setColor(Color.GREEN);
        gRenderer.setChartValuesTextSize(20);
        gRenderer.setDisplayChartValues(true);
        gRenderer.setChartValuesTextSize(20);


/*        bSeries.add(0, 0);
        nSeries.add(0, 0);
        gSeries.add(0, 0);
        Renderer.addXTextLabel(0, "");*/
        int r =1;
        for(int i = 0; i < keys.size(); i++)
        {
            //Log.i("DEBUG", (r+1)+" "+xy[r][0]+"; "+xy[r][1]);
            String dateKey = keys.get(i);
            Renderer.addXTextLabel(r, dateKey);
            bSeries.add(r, map.containsKey(dateKey + "_" + HealthDataDefine.CONST_BAD) ? map.get(dateKey + "_" + HealthDataDefine.CONST_BAD) : 0);
            nSeries.add(r, map.containsKey(dateKey + "_" + HealthDataDefine.CONST_NORMAL) ? map.get(dateKey + "_" + HealthDataDefine.CONST_NORMAL) : 0);
            gSeries.add(r, map.containsKey(dateKey + "_" + HealthDataDefine.CONST_GOOD) ? map.get(dateKey + "_" + HealthDataDefine.CONST_GOOD) : 0);

            r++;
        }
/*        bSeries.add(r, 0);
        nSeries.add(r, 0);
        gSeries.add(r, 0);
        Renderer.addXTextLabel(r, "");*/


        View view = ChartFactory.getBarChartView(layoutView.getContext(), Dataset, Renderer, BarChart.Type.DEFAULT);
        return view;
    }

    private static String getRelativeTimeAgo(long dateMillis) {

        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }
}
