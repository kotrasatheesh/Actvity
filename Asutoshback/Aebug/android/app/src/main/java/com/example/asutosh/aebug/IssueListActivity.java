package com.example.asutosh.aebug;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asutosh.aebug.Adapter.AreaAdapter;
import com.example.asutosh.aebug.Adapter.AssignMemAdapter;
import com.example.asutosh.aebug.Adapter.issue_llist_Adapter;
import com.example.asutosh.aebug.App_config.AppController;
import com.example.asutosh.aebug.bean.IssueListBean;
import com.example.asutosh.aebug.bean.Areabean;
import com.example.asutosh.aebug.bean.member_assigned_bean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.asutosh.aebug.Adapter.issue_llist_Adapter.progre;
import static com.example.asutosh.aebug.Adapter.projectAdapter.PROJECT_NAME;
import static com.example.asutosh.aebug.App_config.AppController.addedArea;
import static com.example.asutosh.aebug.App_config.AppController.addedmem;
import static com.example.asutosh.aebug.App_config.AppController.homeback;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.statusBadge;
import static com.example.asutosh.aebug.LoginActivity.PREF_NAME;
import static com.example.asutosh.aebug.MainActivity.qBadgeView;
import static com.example.asutosh.aebug.MainActivity.v;
import static com.example.asutosh.aebug.fragments.home_fragment.STATUS_PRE;

/**
 * Created by Asutosh on 24-08-2017.
 */

public class IssueListActivity extends AppCompatActivity implements ClickListener,ConnectivityReceiver.ConnectivityReceiverListener {
    private ProgressBar mProgress1;
    private RelativeLayout conatiner;
    RelativeLayout preload;
    RelativeLayout nointernet_lay;
    public static int checkfilterpress=-1;
    private EndlessRecyclerViewScrollListener scrollListener;
    TextView noissue;
    LinearLayout linearLayout;
    JSONObject jsonBody;
    private static final String TAG = IssueListActivity.class.getSimpleName();
    Button fUrgent,fHigh,fMedium,fLow;
    TextView clearFilter;
    RelativeLayout popFilter;
    static String title="";
    static String description="";
    static Activity activity;
    static long totalSize = 0;
    private ProgressDialog pDialog;
    int arryLenth,arryLenth1,arryLenth2;
    static private String filePath = null;
    issue_llist_Adapter recyclerAdapter;
    RecyclerView recyclerView;
    private Toolbar mToolbar;
    private ImageButton mFabButton;
    public static BottomSheetDialog dialog;
    static ArrayList<Areabean> AreaSpinner = new ArrayList<>();
    static ArrayList<member_assigned_bean> assignmemSpinner = new ArrayList<>();
    ArrayList<String>AssignedMemberId;
    ArrayList<String>AssignedMemberName;
    static Spinner assign_name;
    static EditText input_title;
    static EditText input_description;
    static Spinner areas;
    Context context;
    Context context2;
    int indexforSearch=1;
    static String urgentclcik="";
    static String environmentclick="";
    private static int SELECT_FILE = 1;
    Bundle spinbundle;
    static ImageButton attachment;
    String priorityIdtoFilter="0";
    static ImageView attached;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    File finalFile;
    LinearLayoutManager linearLayoutManager;
    Bitmap thumbnail;
    Button retry;
    private ProgressBar emProgress;
    boolean scroll=true;
    boolean noissueShow=false;
    int postion;
    List<IssueListBean> issuelist = new ArrayList<IssueListBean>();
    String[] issues = {"Backgournd Color issue", "screen issue", "UI issue", "data base issue", "Backgournd Color issue", "Backgournd Color issue", "Backgournd Color issue",};
    static String ProjectId="";
    String ticketCount="";
    String projectnme="";
    String RoleID="";
    int refreshHide=0;
    boolean filtersclickd=false;
    boolean highclckd=false;
    boolean lowclckd=false;
    boolean urgntclckd=false;
    boolean mdumclckd=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //project id details
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
        ProjectId=settings.getString("PROJECTID",ProjectId );
        ticketCount=settings.getString("TICKETCOUNT",ticketCount );
        projectnme=settings.getString("PROJETCTNAME",null );
        System.out.println("projectId:  "+ProjectId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.issuelist_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(projectnme);
//        this.registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  homeback=1;

        // login credential
        SharedPreferences setting = getSharedPreferences(PREF_NAME, 0);
        RoleID=setting.getString("ROLLID", RoleID);

        popFilter=(RelativeLayout)findViewById(R.id.filterpop);
        noissue=(TextView)findViewById(R.id.noissue);
        fUrgent=(Button)findViewById(R.id.f_urgent);
        fHigh=(Button)findViewById(R.id.f_high);
        fMedium=(Button)findViewById(R.id.f_medium);
        fLow=(Button)findViewById(R.id.f_low);
        preload = (RelativeLayout) findViewById(R.id.preload);
        nointernet_lay = (RelativeLayout) findViewById(R.id.nointernet_lay);
        retry = (Button) findViewById(R.id.retry);
        emProgress = (ProgressBar)findViewById(R.id.eprogressBar);
        if (ticketCount.matches("0")){
            noissue.setText("No Tickets Available");
            noissue.setVisibility(View.VISIBLE);
        }else {
            noissue.setVisibility(View.GONE);
        }
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    new GetIssueList(1).execute();
                    emProgress.setVisibility(View.VISIBLE);
                }else {
                    emProgress.setVisibility(View.VISIBLE);
                    onIntent();
                    nointernet_lay.setVisibility(View.VISIBLE);
                }
            }
        });

        mProgress1 = (ProgressBar) findViewById(R.id.progressBar1);
        preload.setVisibility(View.VISIBLE);
        mProgress1.setVisibility(View.VISIBLE);
        clearFilter=(TextView)findViewById(R.id.clearFilter);
        conatiner=(RelativeLayout)findViewById(R.id.conatiner);

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtersclickd==true){
                    if (ticketCount.matches("0")){
                        noissue.setText("No Tickets Available");
                        noissue.setVisibility(View.VISIBLE);
                        System.out.println("not found the search !");
                    }else {
                        if (issuelist.size()==0){
                            noissue.setText("No result found !");
                            noissue.setVisibility(View.VISIBLE);
                            System.out.println("not found the search !");
                        }else {
                            noissue.setText("");
                            noissue.setVisibility(View.GONE);
                        }
                    }
                    issuelist.clear();
                    fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fHigh.setTextColor(Color.BLACK);
                    fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fMedium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    fUrgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    fLow.setTextColor(Color.BLACK);
                    // recyclerAdapter.Ifilter("clear");
                    priorityIdtoFilter="-1";
                    indexforSearch=1;
                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){
                        new GetIssueList(1).execute();
                        checkfilterpress=0;
                        scrollListener.restateClear();
                        scroll=true;
                        emProgress.setVisibility(View.VISIBLE);
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }

                }
                urgntclckd=false;
                filtersclickd=false;
                highclckd=false;
                mdumclckd=false;
                lowclckd=false;

            }
        });
        fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        fHigh.setTextColor(Color.BLACK);
        fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        fMedium.setTextColor(Color.BLACK);
        GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
        bgShape.setColor(getResources().getColor(R.color.colorWhite));
        fUrgent.setTextColor(Color.BLACK);
        GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
        bgShape1.setColor(getResources().getColor(R.color.colorWhite));
        fLow.setTextColor(Color.BLACK);

        activity=IssueListActivity.this;
        context=getApplicationContext();
        AssignedMemberId=new ArrayList<String>();
        AssignedMemberName=new ArrayList<String>();


        mFabButton = (ImageButton) findViewById(R.id.fabButton);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(),CreateTicket.class);
                startActivity(intent);
                IssueListActivity.this.finish();
                /*TutsPlusBottomSheetDialogFragment fragment = new TutsPlusBottomSheetDialogFragment(context);
                fragment.show(getSupportFragmentManager(), fragment.getTag());
                new getMemerbs().execute();
                new getAreas().execute();*/

            }
        });

        final GradientDrawable fbgShape = (GradientDrawable) mFabButton.getBackground();
        fbgShape.setColor(getResources().getColor(R.color.colorPrimary));
        fbgShape.setStroke(1, getResources().getColor(R.color.colorPrimary));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new issue_llist_Adapter(IssueListActivity.this,IssueListActivity.this,issuelist,RoleID);
        recyclerAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int clrPage,int currentpage,int urgentpage, int highpage,int mediumpage,int lowpage, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
             //   loadNextDataFromApi(page);
                //int postion=linearLayoutManager.findLastCompletelyVisibleItemPosition();

                postion=recyclerAdapter.getItemCount()-5;
                if (scroll==true){
                    if (checkfilterpress==0){
                        new GetIssueList(clrPage).execute();
                    }else if (checkfilterpress==1){
                        new GetIssueListByPrior(urgentpage).execute();
                    }else if (checkfilterpress==2){
                        new GetIssueListByPrior(highpage).execute();
                    }else if (checkfilterpress==3){
                        new GetIssueListByPrior(mediumpage).execute();
                    }else if (checkfilterpress==4){
                        new GetIssueListByPrior(lowpage).execute();
                    }
                    else if (checkfilterpress==-1){
                        new GetIssueList(currentpage).execute();
                    }

                }else {
                  //  Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();

                }

            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){
            new GetIssueList(1).execute();
            nointernet_lay.setVisibility(View.GONE);
            emProgress.setVisibility(View.GONE);
        }else {
            nointernet_lay.setVisibility(View.VISIBLE);

        }
        fUrgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"hi urg",Toast.LENGTH_SHORT).show();

                if (urgntclckd==false){
                    filtersclickd=true;
                    highclckd=false;
                    mdumclckd=false;
                    lowclckd=false;
                    fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fHigh.setTextColor(Color.BLACK);
                    fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fMedium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.input));
                    fUrgent.setTextColor(Color.WHITE);
                    GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    fLow.setTextColor(Color.BLACK);
                    //  recyclerAdapter.Ifilter("urgent");

                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){
                        priorityIdtoFilter="1";
                        checkfilterpress=1;
                        scrollListener.restateUrgent();
                        scroll=true;
                        issuelist.clear();
                        new GetIssueListByPrior(indexforSearch).execute();
                        emProgress.setVisibility(View.VISIBLE);
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }
                }
                urgntclckd=true;


            }
        });

        fHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //high();

                if (highclckd==false){
                    urgentclcik="2";
                    filtersclickd=true;
                    urgntclckd=false;
                    mdumclckd=false;
                    lowclckd=false;

                    fHigh.setBackgroundColor(getResources().getColor(R.color.input));
                    fHigh.setTextColor(Color.WHITE);
                    fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fMedium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    fUrgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    fLow.setTextColor(Color.BLACK);
                    //  recyclerAdapter.Ifilter("high");

                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){
                        priorityIdtoFilter="2";
                        checkfilterpress=2;
                        scrollListener.restateHigh();
                        scroll=true;
                        issuelist.clear();
                        new GetIssueListByPrior(indexforSearch).execute();
                        emProgress.setVisibility(View.VISIBLE);
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }
                }
                highclckd=true;



            }
        });
        fMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mdumclckd==false){
                    urgentclcik="3";
                    filtersclickd=true;
                    urgntclckd=false;
                    highclckd=false;
                    lowclckd=false;
                    fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fHigh.setTextColor(Color.BLACK);
                    fMedium.setBackgroundColor(getResources().getColor(R.color.input));
                    fMedium.setTextColor(Color.WHITE);
                    GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    fUrgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    fLow.setTextColor(Color.BLACK);
                    // recyclerAdapter.Ifilter("medium");

                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){
                        priorityIdtoFilter="3";
                        scrollListener.restateMedium();
                        scroll=true;
                        issuelist.clear();
                        checkfilterpress=3;
                        new GetIssueListByPrior(indexforSearch).execute();
                        emProgress.setVisibility(View.VISIBLE);
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }

                }
                mdumclckd=true;

            }
        });
        fLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lowclckd==false){
                    urgentclcik="4";
                    filtersclickd=true;
                    urgntclckd=false;
                    mdumclckd=false;
                    highclckd=false;
                    fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fHigh.setTextColor(Color.BLACK);
                    fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    fMedium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    fUrgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.input));
                    fLow.setTextColor(Color.WHITE);
                    //  recyclerAdapter.Ifilter("low");

                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){
                        priorityIdtoFilter="4";
                        checkfilterpress=4;
                        scrollListener.restateLow();
                        scroll=true;
                        issuelist.clear();
                        new GetIssueListByPrior(indexforSearch).execute();
                        emProgress.setVisibility(View.VISIBLE);
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }


                }
                lowclckd=true;


            }
        });
        //setting up our OnScrollListener
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,0);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
                layoutParams.setMargins(0, 0, 0, 0);
                popFilter.setLayoutParams(layoutParams);
                popFilter.animate().translationY(-popFilter.getHeight()).setInterpolator(new AccelerateInterpolator(2));

            }

            @Override
            public void onShow() {
                showViews();
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,240);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
                popFilter.setLayoutParams(layoutParams);
                popFilter.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }
        });
        if (getIntent().hasExtra("FULLIMG")) {
            //     ImageView previewThumbnail = new ImageView(this);

        }

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
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.conatiner), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
    private void onIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emProgress.setVisibility(View.GONE);
            }
        }, 3500);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void cllDeleteIssue(String id,int size){
        DeleteTicket(id,size);
    }
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
            }else{
                Snackbar snackbar = Snackbar.make(conatiner, "No Internet Connection !", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    };
    private void DeleteTicket(String TicketId, final int Size) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("ticketid", TicketId);
                jsonBody.put("loggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.DeleteTicket, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String Message=data.getString("status");
                                if (Message.matches("0")){
                                    //call the ticket list api

                                    if (Size==0){
                                        noissue.setText("No Tickets Available !");
                                        noissue.setVisibility(View.VISIBLE);
                                        mOptionsMenu.getItem(1).setEnabled(false);
                                        mOptionsMenu.getItem(2).setEnabled(false);
                                        popFilter.setVisibility(View.GONE);
                                        refreshHide=1;

                                    }else {
                                        refreshHide=0;
                                        mOptionsMenu.getItem(1).setEnabled(true);
                                        mOptionsMenu.getItem(2).setEnabled(true);
                                        popFilter.setVisibility(View.GONE);
                                        noissue.setVisibility(View.GONE);
                                    }
                                    //pi call ends here

                                    SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    statusBadge++;
                                    editor.putInt("COUNT",statusBadge);
                                    editor.commit();
                                    qBadgeView.setVisibility(View.VISIBLE);
                                    qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
                                    pDialog.hide();
                                    Toast.makeText(getApplicationContext(),"Ticket deleted !",Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            pDialog.hide();


                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }

            };


            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


    private class GetIssueListByPrior extends AsyncTask<Void, Void, Void> {

        int index;
        int total=0;
        public GetIssueListByPrior(int index1){
            index=index1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(IssueListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetTicketsByPriorityId+ProjectId+"&priorityId="+priorityIdtoFilter+"&pageIndex="+index+"&pageSize=15");

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
                        total=jsonObj.getInt("total");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int count = 0;
                    arryLenth = obj.length();

                    for (int i = 0; i < arryLenth; i++) {
                        count++;
                        JSONObject jsonObject1 = (JSONObject) obj.get(i);

                        String tickt_id = jsonObject1.getString("TicketId").trim();
                        String title = jsonObject1.getString("Title").trim();
                        String statusName = jsonObject1.optString("StatusName").trim();
                        String priorityName = jsonObject1.optString("PriorityName").trim();
                        String projectId = jsonObject1.optString("ProjectId").trim();
                        String description = jsonObject1.getString("Description").trim();
                        String createdOn = jsonObject1.getString("CreatedOn").trim();
                        String assigned = jsonObject1.getString("Assigned").trim();
                        String AssigneeId = jsonObject1.getString("AssigneeId").trim();
                        String EnteredBy = jsonObject1.getString("EnteredBy").trim();
                        String reporter = jsonObject1.getString("Reporter").trim();
                        String modifiedOn = jsonObject1.getString("ModifiedOn").trim();
                        String EnvironmentName = jsonObject1.getString("EnvironmentName").trim();


                        // tmp hash map for single contact
                        IssueListBean item = new IssueListBean();
                        item.setAssigneeID(AssigneeId);
                        item.setEnterdBy(EnteredBy);
                        item.setTicketId(tickt_id);
                        item.setEnvironment(EnvironmentName);
                        item.setIssueTitle(title);
                        item.setStatus(statusName);
                        item.setPriority(priorityName);
                        item.setProjectId(projectId);
                        item.setDescription(description);
                        item.setCreatedDate(createdOn);
                        item.setAssignedName(assigned);
                        item.setReporterNmae(reporter);
                        item.setModifiedDate(modifiedOn);
                        issuelist.add(item);
                        if (count==total){
                            scroll=false;
                        }
                        // setListViewHeightBasedOnChildren(listView);
//                        adapter.notifyDataSetChanged();

                        // adding contact to contact list
                        //frommelist.add(transitem);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    IssueListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*Toast.makeText(IssueListActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                IssueListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(IssueListActivity.this,
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
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            if (ticketCount.matches("0")){
                noissue.setText("No Tickets Available !");
                noissue.setVisibility(View.VISIBLE);
                System.out.println("not found the search !");
            }else {
                if (issuelist.size()==0){
                    noissue.setText("No result found !");
                    noissue.setVisibility(View.VISIBLE);
                    System.out.println("not found the search !");
                }else {
                    noissue.setText("");
                    noissue.setVisibility(View.GONE);
                }
            }

                recyclerAdapter = new issue_llist_Adapter(IssueListActivity.this,IssueListActivity.this,issuelist,RoleID);
                linearLayoutManager.scrollToPositionWithOffset(postion, postion);
                recyclerView.setAdapter(recyclerAdapter);

        }
    }
    private class GetIssueList extends AsyncTask<Void, Void, Void> {

        int index;
        int total=0;
        public GetIssueList(int index1){
            index=index1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(IssueListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetTicketsByProjectId+ProjectId+"&pageIndex="+index+"&pageSize=15");

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
                       total=jsonObj.getInt("total");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int count = 0;
                    arryLenth = obj.length();

                    for (int i = 0; i < arryLenth; i++) {
                        count++;
                        JSONObject jsonObject1 = (JSONObject) obj.get(i);

                        String tickt_id = jsonObject1.getString("TicketId").trim();
                        String title = jsonObject1.getString("Title").trim();
                        String statusName = jsonObject1.optString("StatusName").trim();
                        String priorityName = jsonObject1.optString("PriorityName").trim();
                        String projectId = jsonObject1.optString("ProjectId").trim();
                        String description = jsonObject1.getString("Description").trim();
                        String createdOn = jsonObject1.getString("CreatedOn").trim();
                        String assigned = jsonObject1.getString("Assigned").trim();
                        String AssigneeId = jsonObject1.getString("AssigneeId").trim();
                        String EnteredBy = jsonObject1.getString("EnteredBy").trim();
                        String reporter = jsonObject1.getString("Reporter").trim();
                        String modifiedOn = jsonObject1.getString("ModifiedOn").trim();
                        String EnvironmentName = jsonObject1.getString("EnvironmentName").trim();


                        // tmp hash map for single contact
                        IssueListBean item = new IssueListBean();
                        item.setTicketId(tickt_id);
                        item.setAssigneeID(AssigneeId);
                        item.setEnterdBy(EnteredBy);
                        item.setEnvironment(EnvironmentName);
                        item.setIssueTitle(title);
                        item.setStatus(statusName);
                        item.setPriority(priorityName);
                        item.setProjectId(projectId);
                        item.setDescription(description);
                        item.setCreatedDate(createdOn);
                        item.setAssignedName(assigned);
                        item.setReporterNmae(reporter);
                        item.setModifiedDate(modifiedOn);
                        issuelist.add(item);

                        if (count==total){
                            scroll=false;
                            noissueShow=true;
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    IssueListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*Toast.makeText(IssueListActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                IssueListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(IssueListActivity.this,
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
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            if (ticketCount.matches("0")){
                noissue.setText("No Tickets Available !");
                noissue.setVisibility(View.VISIBLE);
                System.out.println("not found the search !");
            }else {
                if (issuelist.size()==0){
                    noissue.setText("No result found !");
                    noissue.setVisibility(View.VISIBLE);
                    System.out.println("not found the search !");
                }else {
                    noissue.setText("");
                    noissue.setVisibility(View.GONE);
                }
            }
                recyclerAdapter = new issue_llist_Adapter(IssueListActivity.this,IssueListActivity.this,issuelist,RoleID);
                linearLayoutManager.scrollToPositionWithOffset(postion, postion);
                recyclerView.setAdapter(recyclerAdapter);
            preload.setVisibility(View.GONE);
            mProgress1.setVisibility(View.GONE);
        }
    }




    private class getAreas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(IssueListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String areas_str = sh.makeServiceCall(AppUtils.GetAreas);

            Log.e(TAG, "Response from url1: " + areas_str);

            if (areas_str != null) {
                try {
                    JSONArray jsonarray = new JSONArray(areas_str);

                    arryLenth1 = jsonarray.length();

                    Areabean item_spn1 = new Areabean();
                    item_spn1.setTitle("select");
                    AreaSpinner.add(item_spn1);
                    for (int i = 0; i < arryLenth1; i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        String tickt_id = jsonobject.getString("AreaId").trim();
                        String title = jsonobject.getString("AreaName").trim();

                        Areabean item_spn = new Areabean();

                        item_spn.setTitle(title);
                        item_spn.setTickt_id(tickt_id);
                        AreaSpinner.add(item_spn);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    IssueListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(IssueListActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                IssueListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(IssueListActivity.this,
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
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            AreaAdapter areaadapter = new AreaAdapter(IssueListActivity.this, 0, AreaSpinner);
            areas.setAdapter(areaadapter);

        }
    }
    private class getMemerbs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(IssueListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String mem_str = sh.makeServiceCall(AppUtils.GetAssignedMembers+ProjectId);

            Log.e(TAG, "Response from url2: " + mem_str);

            if (mem_str !=null) {
                try {

                    JSONArray jsonarray1 = new JSONArray(mem_str);

                    arryLenth2 = jsonarray1.length();
                    member_assigned_bean item = new member_assigned_bean();
                    item.setName("Assign members");
                    assignmemSpinner.add(item);
                    for (int i = 0; i < arryLenth2; i++) {
                        JSONObject jsonobject = jsonarray1.getJSONObject(i);

                        String AssignedMem_id = jsonobject.getString("AssigneeId").trim();
                        String AssignedMem_name = jsonobject.getString("AsigneeName").trim();

                        member_assigned_bean item2=new member_assigned_bean();
                        item2.setId(AssignedMem_id);
                        item2.setName(AssignedMem_name);
                        assignmemSpinner.add(item2);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    IssueListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(IssueListActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                IssueListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(IssueListActivity.this,
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
                pDialog.dismiss();

            AssignMemAdapter addmemberadapter = new AssignMemAdapter(IssueListActivity.this, 0, assignmemSpinner);
            assign_name.setAdapter(addmemberadapter);

        }
    }
    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public void itemClicked(View view, int position) {
     //   Toast.makeText(getApplicationContext(), "hi" + position, Toast.LENGTH_SHORT).show();
    }
    private Menu mOptionsMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mOptionsMenu=menu;
        getMenuInflater().inflate(R.menu.menu_issue, menu);
        mOptionsMenu.getItem(0).setVisible(true);
        mOptionsMenu.getItem(1).setVisible(true);
        mOptionsMenu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id==16908332){
            homeback=1;
            IssueListActivity.this.finish();
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ImageView iv = (ImageView)inflater.inflate(R.layout.iv_refresh, null);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.RESTART);
                rotation.restrictDuration(5000);
                iv.startAnimation(rotation);
                if (ticketCount.matches("0")){
                    noissue.setText("No Tickets Available !");
                    noissue.setVisibility(View.VISIBLE);
                    System.out.println("not found the search !");
                }else {
                    if (issuelist.size()==0){
                        noissue.setText("No result found !");
                        noissue.setVisibility(View.VISIBLE);
                        System.out.println("not found the search !");
                    }else {
                        noissue.setText("");
                        noissue.setVisibility(View.GONE);
                    }
                }
                mOptionsMenu.findItem(R.id.action_settings2).setActionView(iv);
                if (refreshHide==1){
                    mOptionsMenu.getItem(1).setEnabled(false);
                    mOptionsMenu.getItem(2).setEnabled(false);
                    popFilter.setVisibility(View.GONE);
                }else {
                    mOptionsMenu.getItem(1).setEnabled(true);
                    mOptionsMenu.getItem(2).setEnabled(true);
                }
                issuelist.clear();
                fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                fHigh.setTextColor(Color.BLACK);
                fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                fMedium.setTextColor(Color.BLACK);
                GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                fUrgent.setTextColor(Color.BLACK);
                GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                fLow.setTextColor(Color.BLACK);
                // recyclerAdapter.Ifilter("clear");
                priorityIdtoFilter="-1";
                indexforSearch=1;
                Boolean checkinternet3= checkConnection();
                if (checkinternet3==true){
                    new GetIssueList(1).execute();
                    checkfilterpress=0;
                    scrollListener.restateClear();
                    scroll=true;
                    emProgress.setVisibility(View.VISIBLE);
                }else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }
            }
        });
        if (ticketCount.matches("0")){
            mOptionsMenu.getItem(1).setEnabled(false);
            mOptionsMenu.getItem(2).setEnabled(false);
            popFilter.setVisibility(View.GONE);
        }else {
            mOptionsMenu.getItem(1).setEnabled(true);
            mOptionsMenu.getItem(2).setEnabled(true);
            if (item.getItemId()==R.id.action_settings){

                mOptionsMenu.getItem(1).setVisible(false);
                mOptionsMenu.getItem(2).setVisible(true);
                popFilter.setVisibility(View.VISIBLE);

            }
            if (item.getItemId()==R.id.action_settings1){
                mOptionsMenu.getItem(1).setVisible(true);
                mOptionsMenu.getItem(2).setVisible(false);
                popFilter.setVisibility(View.GONE);

            }
        }

        if (item.getItemId()==R.id.action_settings2){
          //  Toast.makeText(getApplicationContext(),"hifre",Toast.LENGTH_SHORT).show();

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
            rotation.setRepeatCount(Animation.ABSOLUTE);
            rotation.restrictDuration(5000);
            iv.startAnimation(rotation);
            mOptionsMenu.findItem(R.id.action_settings2).setActionView(iv);

            if (ticketCount.matches("0")){
                noissue.setText("No Tickets Available !");
                noissue.setVisibility(View.VISIBLE);
                System.out.println("not found the search !");
            }else {
                if (issuelist.size()==0){
                    noissue.setText("No result found !");
                    noissue.setVisibility(View.VISIBLE);
                    System.out.println("not found the search !");
                }else {
                    noissue.setText("");
                    noissue.setVisibility(View.GONE);
                }
            }

            if (refreshHide==1){
                mOptionsMenu.getItem(1).setEnabled(false);
                mOptionsMenu.getItem(2).setEnabled(false);
                popFilter.setVisibility(View.GONE);
            }else {
                mOptionsMenu.getItem(1).setEnabled(true);
                mOptionsMenu.getItem(2).setEnabled(true);
            }
            issuelist.clear();
            popFilter.setVisibility(View.GONE);
            fHigh.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            fHigh.setTextColor(Color.BLACK);
            fMedium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            fMedium.setTextColor(Color.BLACK);
            GradientDrawable bgShape = (GradientDrawable) fUrgent.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            fUrgent.setTextColor(Color.BLACK);
            GradientDrawable bgShape1 = (GradientDrawable) fLow.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            fLow.setTextColor(Color.BLACK);
            // recyclerAdapter.Ifilter("clear");
            priorityIdtoFilter="-1";
            indexforSearch=1;
            Boolean checkinternet3= checkConnection();
            if (checkinternet3==true){
                new GetIssueList(1).execute();
                checkfilterpress=0;
                scrollListener.restateClear();
                scroll=true;
                emProgress.setVisibility(View.VISIBLE);
            }else {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public static class TutsPlusBottomSheetDialogFragment extends BottomSheetDialogFragment {
        Button urgent, high, medium, low;
        Button production, User_acceptance, development;
        ImageButton img;
        Button save,cacelbtn;
        Context context2;
        public TutsPlusBottomSheetDialogFragment(Context context1){
            context2=context1;
        }

        private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };

        @Override
        public void setupDialog(final Dialog dialog, int style) {
            super.setupDialog(dialog, style);
            View contentView = View.inflate(getContext(), R.layout.bottom_sheet_view, null);
            dialog.setContentView(contentView);
            attachment=(ImageButton)dialog.findViewById(R.id.attachment);
            attached=(ImageView)dialog.findViewById(R.id.screenshot);
            attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GALLERYBUTTONCLICK(context2);
                }
            });
            input_title=(EditText)dialog.findViewById(R.id.input_title);
            input_description=(EditText)dialog.findViewById(R.id.input_descr);
            urgent = (Button) dialog.findViewById(R.id.urgent);
            high = (Button) dialog.findViewById(R.id.high);
            medium = (Button) dialog.findViewById(R.id.medium);
            low = (Button) dialog.findViewById(R.id.low);

            production = (Button) dialog.findViewById(R.id.production);
            User_acceptance = (Button) dialog.findViewById(R.id.User_acceptance);
            development = (Button) dialog.findViewById(R.id.development);
            ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
            save = (Button) dialog.findViewById(R.id.save_ticket);
            cacelbtn=(Button)dialog.findViewById(R.id.cancel_ticket);

            cacelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                    AreaSpinner.clear();
                    assignmemSpinner.clear();
                    addedArea.clear();
                    addedmem.clear();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String areas="";
                    String members="";
                    for (int i = 0; i< addedArea.size(); i++) {
                        if (addedArea.get(i).toString() != "hi") {
                            areas = areas + addedArea.get(i) + "|";
                         //   Toast.makeText(getActivity(), "selected areas " + areas, Toast.LENGTH_SHORT).show();
                        }
                    }
                        for (int j=0;j<addedmem.size();j++){
                            if (addedmem.get(j).toString()!="hi"){
                                members=members+addedmem.get(j)+"|";
                         //       Toast.makeText(getActivity(),"selected mem "+ members,Toast.LENGTH_SHORT).show();
                            }
                        }
                    new UploadFileToServer(areas,members).execute();
                    dialog.hide();
                }
            });
            assign_name = (Spinner) dialog.findViewById(R.id.assn_mem);
            assign_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(getActivity(),"selected"+ position,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            areas = (Spinner) dialog.findViewById(R.id.areasspn);
            high.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            high.setTextColor(Color.BLACK);
            medium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            medium.setTextColor(Color.BLACK);
            GradientDrawable bgShape = (GradientDrawable) urgent.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            urgent.setTextColor(Color.BLACK);
            GradientDrawable bgShape1 = (GradientDrawable) low.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            low.setTextColor(Color.BLACK);

            User_acceptance.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            User_acceptance.setTextColor(Color.BLACK);
            GradientDrawable bgShape2 = (GradientDrawable) production.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            production.setTextColor(Color.BLACK);
            GradientDrawable bgShape3 = (GradientDrawable) development.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            development.setTextColor(Color.BLACK);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                    AreaSpinner.clear();
                    assignmemSpinner.clear();
                    addedArea.clear();
                    addedmem.clear();
                }
            });
            urgent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urgentclcik="1";
                    high.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    high.setTextColor(Color.BLACK);
                    medium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    medium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) urgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.urgent));
                    urgent.setTextColor(Color.WHITE);
                    GradientDrawable bgShape1 = (GradientDrawable) low.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    low.setTextColor(Color.BLACK);

                }
            });
            high.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //high();
                    urgentclcik="2";
                    high.setBackgroundColor(getResources().getColor(R.color.high));
                    high.setTextColor(Color.BLACK);
                    medium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    medium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) urgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    urgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) low.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    low.setTextColor(Color.BLACK);
                }
            });
            medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urgentclcik="3";
                    high.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    high.setTextColor(Color.BLACK);
                    medium.setBackgroundColor(getResources().getColor(R.color.medium));
                    medium.setTextColor(Color.WHITE);
                    GradientDrawable bgShape = (GradientDrawable) urgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    urgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) low.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    low.setTextColor(Color.BLACK);
                }
            });
            low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urgentclcik="3";
                    high.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    high.setTextColor(Color.BLACK);
                    medium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    medium.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) urgent.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    urgent.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) low.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.low));
                    low.setTextColor(Color.WHITE);
                }
            });

            production.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    environmentclick="1";
                    User_acceptance.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    User_acceptance.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) production.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.production));
                    production.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) development.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    development.setTextColor(Color.BLACK);
                }
            });
            User_acceptance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    environmentclick="2";
                    User_acceptance.setBackgroundColor(getResources().getColor(R.color.acceptance));
                    User_acceptance.setTextColor(Color.WHITE);
                    GradientDrawable bgShape = (GradientDrawable) production.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    production.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) development.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                    development.setTextColor(Color.BLACK);

                }
            });
            development.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    environmentclick="3";
                    User_acceptance.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    User_acceptance.setTextColor(Color.BLACK);
                    GradientDrawable bgShape = (GradientDrawable) production.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.colorWhite));
                    production.setTextColor(Color.BLACK);
                    GradientDrawable bgShape1 = (GradientDrawable) development.getBackground();
                    bgShape1.setColor(getResources().getColor(R.color.developement));
                    development.setTextColor(Color.WHITE);

                }
            });
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();

            if (behavior != null && behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            }
        }
    }
    public static void GALLERYBUTTONCLICK(Context con) {
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {
            galleryIntent(con);
        }
    }
    private static void galleryIntent(Context context) {
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
           activity.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                //      onSelectFromGalleryResult(data);
                onSelectFromGalleryResultGallery(data);
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = IssueListActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void onSelectFromGalleryResultGallery(Intent data) {
        Uri uri = data.getData();
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(IssueListActivity.this.getContentResolver(), uri);
            Uri tempUri = getImageUri(IssueListActivity.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));
            filePath=getRealPathFromURI(tempUri);
        } catch (IOException e) {

            e.printStackTrace();
        }
        attached.setScaleType(ImageView.ScaleType.CENTER_CROP);
        attached.requestLayout();
        attached.getLayoutParams().height = 190;
        attached.setImageBitmap(thumbnail);
        //thumbnail=g.getBm();

        //imagecapture.setImageBitmap(bm);
    }
    private static class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        String s ;
        String s1 ;
        public UploadFileToServer(String num1, String num2) {
            super();
            s=num1;
            s1=num2;
            // Do something with these parameters
        }
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            // progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //  progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //   progressBar.setProgress(progress[0]);

            // updating percentage value
            //    txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
           /* Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte[] ba = bao.toByteArray();
            ba1 =Base64.encodeToString(byteArray, Base64.DEFAULT);*/
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            title=input_title.getText().toString().trim();
            description=input_description.getText().toString().trim();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppUtils.CreateTicket);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                File sourceFile = null;
                if (filePath != null) {
                    sourceFile = new File(filePath);
                    entity.addPart("file", new FileBody(sourceFile));
                }

                // Adding file data to http body

                // Extra parameters if you want to pass to server
                entity.addPart("Title", new StringBody(title));
                entity.addPart("Description", new StringBody(description));
                entity.addPart("PriorityId", new StringBody(urgentclcik));
                entity.addPart("ProjectId", new StringBody(ProjectId));
                entity.addPart("EnvironmentId", new StringBody(environmentclick));
                entity.addPart("TicketTypeId", new StringBody("1"));
                entity.addPart("Assigned", new StringBody(s1));
                entity.addPart("Areas", new StringBody(s));
                entity.addPart("LoggedInUser", new StringBody("160"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
//                    Toast.makeText(getActivity(),"Profile updated successfully",Toast.LENGTH_SHORT).show();
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            // showAlert(result);
           System.out.println("td"+title+" "+description);
           System.out.println("urgclick "+urgentclcik+" "+environmentclick);
           System.out.println("assede "+s+" "+s1);

            AreaSpinner.clear();
            assignmemSpinner.clear();
            addedArea.clear();
            addedmem.clear();
            super.onPostExecute(result);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}


