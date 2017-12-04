package com.example.asutosh.aebug.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.example.asutosh.aebug.Adapter.ActivityAdapter;
import com.example.asutosh.aebug.Adapter.notification_recycler_Adapter;
import com.example.asutosh.aebug.AppUtils;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.EndlessRecyclerViewScrollListener;
import com.example.asutosh.aebug.HttpHandler;
import com.example.asutosh.aebug.IssueListActivity;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.ActivityBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

import static com.android.volley.VolleyLog.TAG;
import static com.example.asutosh.aebug.Adapter.issue_llist_Adapter.progre;
import static com.example.asutosh.aebug.Adapter.notification_recycler_Adapter.progresss;
import static com.example.asutosh.aebug.Adapter.projectAdapter.prog;
//import static com.example.asutosh.aebug.App_config.AppController.homeback;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.statusBadge;
import static com.example.asutosh.aebug.MainActivity.qBadgeView;
import static com.example.asutosh.aebug.MainActivity.v;
import static com.example.asutosh.aebug.fragments.home_fragment.STATUS_PRE;

/**
 * Created by Asutosh on 13-09-2017.
 */

public class notification_frag extends Fragment {

    RelativeLayout nointernet_lay;
    Button retry;
    private ProgressBar emProgress;
    Toolbar toolbar;
    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView recyclerView;
    notification_recycler_Adapter adapter;
    LinearLayoutManager llm;
    int index=1;
    List<ActivityBean> Alist=new ArrayList<ActivityBean>();
    boolean scroll=true;
    int postion;
    public static notification_frag newInstance() {

        notification_frag fragment = new notification_frag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.notification_frag,container,false);
        toolbar = (Toolbar)rootview.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Notifications");
        SharedPreferences settings = getActivity().getSharedPreferences(STATUS_PRE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("COUNT");
        editor.apply();
      //  homeback=0;
        statusBadge=0;
        qBadgeView.setVisibility(View.GONE);
        //new QBadgeView(getActivity()).bindTarget(v).setBadgeNumber(statusBadge);
        recyclerView =(RecyclerView)rootview.findViewById(R.id.noti_recyclerview);
        nointernet_lay = (RelativeLayout) rootview.findViewById(R.id.nointernet_lay);
        retry = (Button)rootview.findViewById(R.id.retry);
        emProgress = (ProgressBar)rootview.findViewById(R.id.eprogressBar);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter=new notification_recycler_Adapter(getActivity(),Alist);
        recyclerView.setAdapter(adapter);

        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){

            nointernet_lay.setVisibility(View.GONE);
            emProgress.setVisibility(View.GONE);
            new GetActivities(1).execute();
        }else {
            emProgress.setVisibility(View.GONE);
            nointernet_lay.setVisibility(View.VISIBLE);

        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    new GetActivities(1).execute();
                    emProgress.setVisibility(View.VISIBLE);
                }else {
                    emProgress.setVisibility(View.VISIBLE);
                    onIntent();
                    nointernet_lay.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = llm.findLastVisibleItemPosition();
                postion=adapter.getItemCount()-5;
                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    if (scroll==true){
                        index=index+1;
                        new GetActivities(index).execute();
                    }else {
                    }


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
    private class GetActivities extends AsyncTask<Void, Void, Void> {

        int index;
        public GetActivities(int index1){
            index=index1;
        }

        ProgressDialog  pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.ActivityCount+loginuserid+"&pageIndex="+index+"&pageSize=15");

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                   /* // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");*/

                    // looping through All Contacts

                    JSONArray obj = null;

                    try {
                        obj = jsonObj.getJSONArray("data");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (obj.length()==0){
                        scroll=false;
                    }
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) obj.get(i);

                        String ActivityId = jsonObject1.getString("ActivityId").trim();
                        String ActivityBy = jsonObject1.getString("ActivityBy").trim();
                        String Description = jsonObject1.optString("Description").trim();
                        String When = jsonObject1.optString("When").trim();
                        String ProjectId = jsonObject1.optString("ProjectId").trim();
                        String ProjectName = jsonObject1.getString("ProjectName").trim();


                        // tmp hash map for single contact
                        ActivityBean item = new ActivityBean();
                        item.setActivty_id(ActivityId);
                        item.setActivty_by(ActivityBy);
                        item.setDescription(Description);
                        item.setTime(When);
                        item.setProj_id(ProjectId);
                        item.setProj_name(ProjectName);
                        Alist.add(item);

                        // setListViewHeightBasedOnChildren(recyclerView);
//                        adapter.notifyDataSetChanged();

                        // adding contact to contact list
                        //frommelist.add(transitem);
                    }
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
            if (pDialog.isShowing())


            adapter=new notification_recycler_Adapter(getActivity(),Alist);
            llm.scrollToPositionWithOffset(postion, postion);
            recyclerView.setAdapter(adapter);
            nointernet_lay.setVisibility(View.GONE);

        }
    }

}