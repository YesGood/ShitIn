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
import com.yesgood.shitIn.HealthDataPost;
import com.yesgood.shitIn.R;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hsuanlin on 2015/9/2.
 */
public class HealthReport extends DialogFragment {

    private LinearLayout llBarChart;
    private TextView tvLastUrinate;
    private View view;

    private String[][] Top10ErrCode={{"ADFU1","20"},{"MBPW2","19"},{"ABCDE","17"},{"BLFU1","16"},{"LCVD3","15"},{"ADDK1","11"},{"CMFU3","8"},{"LCCR2","7"},{"QBLE1","5"},{"SPNS1","2"}};

    public static HealthReport newInstance(){
        HealthReport frag = new HealthReport();
        return frag;
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
        query.findInBackground(new FindCallback<HealthDataPost>() {
            @Override
            public void done(List<HealthDataPost> list, ParseException e) {
                LinkedHashMap<String,Integer> urinalData = new LinkedHashMap<String, Integer>();
                long lastTime = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                for(HealthDataPost post : list){
                    Date createDate = post.getCreatedAt();
                    String dateStr = sdf.format(createDate);
                    lastTime = createDate.getTime();
                    if( urinalData.containsKey(dateStr))
                    {
                        urinalData.put(dateStr,urinalData.get(dateStr) + 1);
                    }
                    else
                    {
                        urinalData.put(dateStr,1);
                    }
                }

                setBarChart(urinalData);
                if( lastTime != 0) {
                    tvLastUrinate.setText(getRelativeTimeAgo(lastTime));
                }
            }
        });


        return view;
    }

    private void setBarChart( LinkedHashMap<String,Integer> map)
    {
        View vChart = getBarChart(view,"", "date", "counter", map);
        llBarChart.removeAllViews();
        llBarChart.addView(vChart, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, 400));
    }
    private View getBarChart(View layoutView, String chartTitle, String XTitle, String YTitle, LinkedHashMap<String,Integer> map){

        XYSeries Series = new XYSeries(YTitle);

        XYMultipleSeriesDataset Dataset = new XYMultipleSeriesDataset();
        Dataset.addSeries(Series);

        XYMultipleSeriesRenderer Renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer yRenderer = new XYSeriesRenderer();
        Renderer.addSeriesRenderer(yRenderer);

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
        Renderer.setBarSpacing(0.5);

        //Renderer.setXTitle(XTitle);
        //Renderer.setYTitle(YTitle);
        Renderer.setXLabelsColor(Color.BLACK);
        Renderer.setYLabelsColor(0, Color.BLACK);
        Renderer.setXLabelsAlign(Paint.Align.CENTER);
        Renderer.setYLabelsAlign(Paint.Align.CENTER);
        Renderer.setXLabelsAngle(-25);
        Renderer.setLabelsTextSize(20);

        Renderer.setXLabels(0);
        Renderer.setYAxisMin(0);


        yRenderer.setColor(Color.RED);
        yRenderer.setChartValuesTextSize(20);
        yRenderer.setDisplayChartValues(true);

        int r =0;
        for(Map.Entry<String,Integer> entry: map.entrySet())
        {
            //Log.i("DEBUG", (r+1)+" "+xy[r][0]+"; "+xy[r][1]);
            Renderer.addXTextLabel(r, entry.getKey());
            Series.add(r, entry.getValue());
            r++;
        }
        Series.add(r, 0);
        Renderer.addXTextLabel(r, "");


        View view = ChartFactory.getBarChartView(layoutView.getContext(), Dataset, Renderer, BarChart.Type.DEFAULT);
        return view;
    }

    private static String getRelativeTimeAgo(long dateMillis) {

        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }
}
