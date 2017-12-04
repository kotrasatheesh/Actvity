package com.example.asutosh.aebug.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asutosh.aebug.AppUtils;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.HttpHandler;
import com.example.asutosh.aebug.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

import static com.example.asutosh.aebug.App_config.AppController.loginuserid;

/**
 * Created by Asutosh on 03-09-2017.
 */

public class status_chart_fragment extends Fragment {
    RelativeLayout nointernet_lay;
    Button retry;
    private ProgressBar emProgress;

    private ProgressBar mProgress1;
    RelativeLayout preload;
    ArrayList<Integer> colors;
    Toolbar toolbar;
    private ProgressDialog pDialog;
    private static String TAg="MainActivity";
    private float[]yData={19,26,34,21};
    private Float[] percentages;
    private String[]xData={"a","b","c","d","e","f"};
    int arryLenth;
    PieChart pieChart;
    TextView urgentper,highper,mediumper,lowper,pend_per,startd_per,complted_per,resolved_per,reop_per,deferr;
    ProgressBar urgentpro,highpro,mediumpro,lowpro;
    public static status_chart_fragment newInstance() {
        status_chart_fragment fragment = new status_chart_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.activity_chart,container,false);
        toolbar = (Toolbar)rootview.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");
        urgentpro=(ProgressBar)rootview.findViewById(R.id.urgent);
        highpro=(ProgressBar)rootview.findViewById(R.id.highprogress);
        mediumpro=(ProgressBar)rootview.findViewById(R.id.mediumprogress);
        lowpro=(ProgressBar)rootview.findViewById(R.id.lowprogress);
        urgentper=(TextView)rootview.findViewById(R.id.urgentper);
        highper=(TextView)rootview.findViewById(R.id.highper);
        mediumper=(TextView)rootview.findViewById(R.id.mediumper);
        lowper=(TextView)rootview.findViewById(R.id.lowper);
        pend_per=(TextView)rootview.findViewById(R.id.pend_per);
        startd_per=(TextView)rootview.findViewById(R.id.startd_per);
        complted_per=(TextView)rootview.findViewById(R.id.complted_per);
        resolved_per=(TextView)rootview.findViewById(R.id.resolved_per);
        reop_per=(TextView)rootview.findViewById(R.id.reop_per);
        deferr=(TextView)rootview.findViewById(R.id.deferr);
        preload = (RelativeLayout)rootview. findViewById(R.id.preload);
        mProgress1 = (ProgressBar) rootview.findViewById(R.id.progressBar1);
        nointernet_lay = (RelativeLayout) rootview.findViewById(R.id.nointernet_lay);
        retry = (Button)rootview.findViewById(R.id.retry);
        emProgress = (ProgressBar)rootview.findViewById(R.id.eprogressBar);
        preload.setVisibility(View.VISIBLE);
        mProgress1.setVisibility(View.VISIBLE);
        Description hi=new Description();
        pieChart=(PieChart)rootview.findViewById(R.id.chart);
        pieChart.getDescription().setText("");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(1f);
        pieChart.setTransparentCircleAlpha(0);
     //   homeback=0;
        pieChart.setUsePercentValues(true);
        colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.i_pending));
        colors.add(getResources().getColor(R.color.i_started));
        colors.add(getResources().getColor(R.color.closed));
        colors.add(getResources().getColor(R.color.fixed));
        colors.add(getResources().getColor(R.color.i_reopend));
        colors.add(getResources().getColor(R.color.deferred));

        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){

            nointernet_lay.setVisibility(View.GONE);
            emProgress.setVisibility(View.GONE);
            new Getpriority().execute();
            new GetStatus().execute();
        }else {
            emProgress.setVisibility(View.GONE);
            nointernet_lay.setVisibility(View.VISIBLE);

        }
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    new Getpriority().execute();
                    new GetStatus().execute();
                    emProgress.setVisibility(View.VISIBLE);
                }else {
                    emProgress.setVisibility(View.VISIBLE);
                    onIntent();
                    nointernet_lay.setVisibility(View.VISIBLE);
                }
            }
        });
        return rootview;
    }
    private void onIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emProgress.setVisibility(View.GONE);
            }
        }, 3500);
    }
    private boolean checkConnection() {
        boolean checkintr=true;
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected==true){
            checkintr=true;
            showSnack(isConnected);
        }else {
            checkintr=false;
            showSnack(isConnected);
        }

        return checkintr;
    }

    // Showing the status in Snackbar

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.container), message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }


    private class Getpriority extends AsyncTask<Void, Void, Void> {
        String urgent;
        String high;
        String medium;
        String low;
        String total;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(getActivity());
   //         pDialog.setMessage("Please wait...");
  //          pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetPriorityCountByUserId+loginuserid);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                   /* // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");*/

                    // looping through All Contacts

                    JSONObject obj = null;

                    try {
                        obj = jsonObj.getJSONObject("data");
                        urgent=obj.getString("Urgent");
                        high=obj.getString("High");
                        medium=obj.optString("Medium");
                        low = obj.optString("Low");
                        total = obj.getString("Total");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int count=0;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();*/
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
         //   if (pDialog.isShowing())
        //        pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
          //  pieChart.setCenterText("Total "+reopen+"%");

            urgentpro.setProgress(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(urgent)))));
            highpro.setProgress(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(high)))));
            mediumpro.setProgress(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(medium)))));
            lowpro.setProgress(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(low)))));

            /*highpro.setProgress(Integer.parseInt(high));
            mediumpro.setProgress(Integer.parseInt(medium));
            lowpro.setProgress(Integer.parseInt(low));*/
            urgentper.setText(urgent+"%");
            highper.setText(high+"%");
            mediumper.setText(medium+"%");
            lowper.setText(low+"%");
        }
    }
    private class GetStatus extends AsyncTask<Void, Void, Void> {
        String pending="";
        String started="";
        String completed="";
        String resolved="";
        String reopen="";
        String Deferred="";
        Float pnding=0.f;
        Float srted=0.f;
        Float complted=0.f;
        Float reslved=0.f;
        Float repn=0.f;
        Float defer=0.f;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(getActivity());
       //     pDialog.setMessage("Please wait...");
      //      pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetStatusCountByUserId+loginuserid);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                   /* // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");*/

                    // looping through All Contacts

                    JSONObject obj = null;

                    try {
                        obj = jsonObj.getJSONObject("data");
                        pending =obj.getString("Pending");
                        started =obj.getString("Started");
                        completed =obj.optString("Closed");
                        resolved = obj.optString("Fixed");
                        reopen = obj.getString("ReOpened");
                        Deferred = obj.getString("Deferred");
                        pnding =Float.parseFloat(pending);
                        srted =Float.parseFloat(started);
                        complted =Float.parseFloat(completed);
                        reslved =Float.parseFloat(resolved);
                        repn =Float.parseFloat(reopen);
                        defer =Float.parseFloat(Deferred);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int count=0;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         /*   Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();*/
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
       //     if (pDialog.isShowing())
         //       pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            //  pieChart.setCenterText("Total "+reopen+"%");
            ArrayList<PieEntry> yentries = new ArrayList<>();
            ArrayList<String> xEntrys = new ArrayList<>();


                yentries.add(new PieEntry(pnding,0));
                yentries.add(new PieEntry(srted,1));
                yentries.add(new PieEntry(complted,2));
                yentries.add(new PieEntry(reslved,3));
                yentries.add(new PieEntry(repn,4));
                yentries.add(new PieEntry(defer,5));


                for (int i=0;i<xData.length;i++){
                    xEntrys.add(xData[i]);
                }

                PieDataSet pieDataSet=new PieDataSet(yentries,"Priority Status");
                pieDataSet.setSliceSpace(1f);
                pieDataSet.setValueTextSize(8);
                pieChart.setDrawSliceText(false);
                pieDataSet.setDrawValues(false);


                pieDataSet.setColors(colors);

                Legend legend=pieChart.getLegend();
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                legend.setEnabled(false);

                PieData pieData=new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            pend_per.setText(pending +"%");
            startd_per.setText(started +"%");
            complted_per.setText(completed +"%");
            resolved_per.setText(resolved +"%");
            reop_per.setText(reopen +"%");
            deferr.setText(Deferred +"%");
            preload.setVisibility(View.GONE);
            mProgress1.setVisibility(View.GONE);
            nointernet_lay.setVisibility(View.GONE);
        }
    }
}