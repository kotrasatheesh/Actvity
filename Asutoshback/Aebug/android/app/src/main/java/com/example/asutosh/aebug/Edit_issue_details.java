package com.example.asutosh.aebug;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asutosh.aebug.Adapter.AreaPop_Adapter;
import com.example.asutosh.aebug.Adapter.IEdit_areaAdapter;
import com.example.asutosh.aebug.Adapter.IEdit_assnmemAdapter;
import com.example.asutosh.aebug.Adapter.mem_pop_Adapter;
import com.example.asutosh.aebug.App_config.AppController;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.example.asutosh.aebug.Adapter.AreaPop_Adapter.AREAPOPPRE;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.statusBadge;
import static com.example.asutosh.aebug.Issue_DetailsActivity.MYCOMMENTSHARE;
import static com.example.asutosh.aebug.MainActivity.qBadgeView;
import static com.example.asutosh.aebug.MainActivity.v;
import static com.example.asutosh.aebug.fragments.home_fragment.STATUS_PRE;

public class Edit_issue_details extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    Boolean validfile=true;
    private ProgressBar mProgress1;
    private AppBarLayout appbar;
    RelativeLayout preload;
    JSONObject jsonBody;
    RelativeLayout editLay;
    public static Dialog dialog1,dialog2;
    public static final String PREF_NAME = "Divid";
    ListView listView;
    TextView titlenameTag;
    ImageButton closepop;
    ImageView document;
    final Context context = this;
    ImageButton addmeBtn,addArBtn,addDocumentbtn;
    private ProgressDialog pDialog;
    public static List<member_assigned_bean> mermberList = new ArrayList<member_assigned_bean>();
    public  static  List<member_assigned_bean> mempopList = new ArrayList<member_assigned_bean>();
    public  static  List<Areabean> areapopList = new ArrayList<Areabean>();
    public  static List<String> membername = new ArrayList<String>();
    public  static List<String> memberId = new ArrayList<String>();
    public static List<Areabean> areaList = new ArrayList<Areabean>();
    IEdit_assnmemAdapter memAdapter;
    IEdit_areaAdapter areAdapter;
    mem_pop_Adapter mempopadapter;
    AreaPop_Adapter aREApOP_aDAPTER;
    public static ListView memberlistview,areaListview;
    private static final String TAG = Edit_issue_details.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private String filePath = null;
    File finalFile;
    Bitmap thumbnail;
    boolean deferdpre=false;
    private int SELECT_FILE = 2;
    String snd_priorityID="";
    String snd_statudID="";
    String snd_environmentId="";
    String snd_tickettypeid="";
    long totalSize = 0;
    String ticket_id="";
    String projetctId="";
    String title="";
    String description="";
    String priority_id="";
    String status_id="";
    String project_id="";
    String environment_id ="";
    String TicketTypeId="";
    String TicketType="";
    EditText edittile,edit_description;
    TextView Durgent,Dhigh,Dmedium,Dlow,Dpending,Dstarted, DClosed,Dreopen, Dfixed,deferre,Dproduction,D_UserAcceptance,D_Development,docName;
    SwitchCompat switch_select;
    RelativeLayout nointernet_lay;
    Button retry;
    Button Bug, CR, Support;
    private ProgressBar emProgress;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    View bottomSheetView;
    private Uri fileUri;
    private int REQUEST_CAMERA = 0;
    private static final int MY_PERMISSION_REQUEST_CONTACTS = 10;
    File destination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_issue_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        SharedPreferences settings = getApplicationContext().getSharedPreferences(MYCOMMENTSHARE, 0);
        memberlistview=(ListView)findViewById(R.id.memAssginListview);
        areaListview=(ListView)findViewById(R.id.areaListview);
        addmeBtn=(ImageButton)findViewById(R.id.addMembtn);
        addArBtn=(ImageButton)findViewById(R.id.addAreaBtn);
        document=(ImageView)findViewById(R.id.document);
        editLay=(RelativeLayout)findViewById(R.id.editLay);

        memAdapter = new IEdit_assnmemAdapter(this, mermberList);
        areAdapter = new IEdit_areaAdapter(this, areaList);
        areaListview.setAdapter(memAdapter);
        areaListview.setAdapter(areAdapter);
        ticket_id=settings.getString("TICKETID",ticket_id );
        setTitle("Ticket ID: " +ticket_id);
        title=settings.getString("TITLE",title );
        projetctId=settings.getString("PROJECTID",projetctId );
        description=settings.getString("DESCRIPTION",description );
        priority_id=settings.getString("PRIORITYID",priority_id );
        status_id=settings.getString("STATUSID",status_id );
        project_id=settings.getString("PROJECTID",project_id );
        environment_id =settings.getString("ENVIRONMENTID", environment_id);
        TicketTypeId=settings.getString("TICKETTYPEID",TicketTypeId );
        TicketType=settings.getString("TICKETTYPE",TicketType );
        System.out.println("TITLE:  "+ environment_id);

        edittile=(EditText)findViewById(R.id.edit_Title);
        Durgent=(TextView) findViewById(R.id.Durgent);
        Dhigh=(TextView) findViewById(R.id.Dhigh);
        Dmedium=(TextView) findViewById(R.id.Dmedium);
        Dlow=(TextView) findViewById(R.id.Dlow);
        Dpending=(TextView) findViewById(R.id.Dpending);
        Dstarted=(TextView) findViewById(R.id.Dstarted);
        DClosed =(TextView) findViewById(R.id.Dcompleted);
        Dreopen=(TextView) findViewById(R.id.Dreopen);
        Dfixed =(TextView) findViewById(R.id.Dresolved);
        deferre=(TextView) findViewById(R.id.deferre);
        Dproduction=(TextView) findViewById(R.id.Dproduction);
        D_UserAcceptance=(TextView) findViewById(R.id.D_UserAcceptance);
        D_Development=(TextView) findViewById(R.id.D_Development);
        edit_description=(EditText) findViewById(R.id.edit_description);
        //switch_select=(SwitchCompat) findViewById(R.id.switch_select);
        addDocumentbtn=(ImageButton) findViewById(R.id.addDocumentbtn);
        preload = (RelativeLayout) findViewById(R.id.preload);
        mProgress1 = (ProgressBar) findViewById(R.id.progressBar1);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        Bug = (Button) findViewById(R.id.Bug);
        CR = (Button) findViewById(R.id.CR);
        Support = (Button) findViewById(R.id.Support);
        nointernet_lay = (RelativeLayout) findViewById(R.id.nointernet_lay);
        retry = (Button) findViewById(R.id.retry);
        emProgress = (ProgressBar)findViewById(R.id.eprogressBar);

        preload.setVisibility(View.VISIBLE);
        mProgress1.setVisibility(View.VISIBLE);
        docName=(TextView) findViewById(R.id.docName);
        edittile.setText(title);
        edit_description.setText(description);

       /* if (TicketType.equalsIgnoreCase("bug")){
            snd_tickettypeid="1";
          //  switch_select.setChecked(false);
        }else if (TicketType.equalsIgnoreCase("CR")){
            snd_tickettypeid="2";
           // switch_select.setChecked(true);
        }*/
        if (TicketTypeId.equals("1")){
            snd_tickettypeid="1";
            CR.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            CR.setTextColor(Color.BLACK);
            GradientDrawable bgShape = (GradientDrawable) Bug.getBackground();
            bgShape.setColor(getResources().getColor(R.color.tickety));
            Bug.setTextColor(Color.WHITE);
            GradientDrawable bgShape1 = (GradientDrawable) Support.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Support.setTextColor(Color.BLACK);
        }else if (TicketTypeId.equals("2")){
            snd_tickettypeid="2";
            CR.setBackgroundColor(getResources().getColor(R.color.tickety));
            CR.setTextColor(Color.WHITE);
            GradientDrawable bgShape = (GradientDrawable) Bug.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Bug.setTextColor(Color.BLACK);
            GradientDrawable bgShape1 = (GradientDrawable) Support.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Support.setTextColor(Color.BLACK);

        }else if (TicketTypeId.equals("3")){
            snd_tickettypeid="3";
            CR.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            CR.setTextColor(Color.BLACK);
            GradientDrawable bgShape = (GradientDrawable) Bug.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Bug.setTextColor(Color.BLACK);
            GradientDrawable bgShape1 = (GradientDrawable) Support.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.tickety));
            Support.setTextColor(Color.WHITE);
        }
        addDocumentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // GALLERYBUTTONCLICK();
                bottomSheetView = getLayoutInflater().inflate(R.layout.choose_cam_gal, null);
                bottomSheetDialog = new BottomSheetDialog(Edit_issue_details.this);
                bottomSheetDialog.setContentView(bottomSheetView);
                RelativeLayout gallery=(RelativeLayout) bottomSheetDialog.findViewById(R.id.gallery);
                RelativeLayout document=(RelativeLayout) bottomSheetDialog.findViewById(R.id.document);
                bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.show();
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Managebuttonclick();

                    }
                });
                document.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        requestPermission();
                    }
                });



            }
        });




        Bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_tickettypeid="1";
                CR.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                CR.setTextColor(Color.BLACK);
                GradientDrawable bgShape = (GradientDrawable) Bug.getBackground();
                bgShape.setColor(getResources().getColor(R.color.tickety));
                Bug.setTextColor(Color.WHITE);
                GradientDrawable bgShape1 = (GradientDrawable) Support.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Support.setTextColor(Color.BLACK);
            }
        });
        CR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_tickettypeid="2";
                CR.setBackgroundColor(getResources().getColor(R.color.tickety));
                CR.setTextColor(Color.WHITE);
                GradientDrawable bgShape = (GradientDrawable) Bug.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                Bug.setTextColor(Color.BLACK);
                GradientDrawable bgShape1 = (GradientDrawable) Support.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Support.setTextColor(Color.BLACK);

            }
        });
        Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_tickettypeid="3";
                CR.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                CR.setTextColor(Color.BLACK);
                GradientDrawable bgShape = (GradientDrawable) Bug.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                Bug.setTextColor(Color.BLACK);
                GradientDrawable bgShape1 = (GradientDrawable) Support.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.tickety));
                Support.setTextColor(Color.WHITE);

            }
        });
        /*switch_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {

                    snd_tickettypeid="2";

                } else {

                    snd_tickettypeid="1";

                }

            }


        });*/
        if (priority_id.equals("1")){
            snd_priorityID="1";
            GradientDrawable bgShape = (GradientDrawable) Durgent.getBackground();
            bgShape.setColor(getResources().getColor(R.color.urgent));
            Durgent.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.urgent));

            GradientDrawable bgShape1 = (GradientDrawable) Dhigh.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dhigh.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dmedium.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dmedium.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) Dlow.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

        }else if (priority_id.equals("2")){
            snd_priorityID="2";
            GradientDrawable bgShape = (GradientDrawable) Dhigh.getBackground();
            bgShape.setColor(getResources().getColor(R.color.high));
            Dhigh.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.high));

            GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Durgent.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dmedium.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dmedium.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) Dlow.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));



        }else if (priority_id.equals("3")){
            snd_priorityID="3";
            GradientDrawable bgShape = (GradientDrawable) Dmedium.getBackground();
            bgShape.setColor(getResources().getColor(R.color.medium));
            Dmedium.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.medium));

            GradientDrawable bgShape2 = (GradientDrawable) Dhigh.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dhigh.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Durgent.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));


            GradientDrawable bgShape3 = (GradientDrawable) Dlow.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

        }else if (priority_id.equals("4")){
            snd_priorityID="4";
            GradientDrawable bgShape = (GradientDrawable) Dlow.getBackground();
            bgShape.setColor(getResources().getColor(R.color.low));
            Dlow.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.low));


            GradientDrawable bgShape3 = (GradientDrawable) Dmedium.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dmedium.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dhigh.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dhigh.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Durgent.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));
        }else {
            GradientDrawable bgShape = (GradientDrawable) Dlow.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));


            GradientDrawable bgShape3 = (GradientDrawable) Dmedium.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dmedium.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dhigh.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dhigh.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Durgent.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));
        }

        //status

        if (status_id.equals("1")){
            snd_statudID="1";
            GradientDrawable bgShape = (GradientDrawable) Dpending.getBackground();
            bgShape.setColor(getResources().getColor(R.color.i_pending));
            Dpending.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.i_pending));

            GradientDrawable bgShape1 = (GradientDrawable) Dstarted.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dstarted.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) DClosed.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            DClosed.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) Dreopen.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dreopen.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.colorWhite));
            Dfixed.setTextColor(Color.BLACK);
            bgShape4.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.colorWhite));
            deferre.setTextColor(Color.BLACK);
            bgShape6.setStroke(2,getResources().getColor(R.color.input));

        }else if (status_id.equals("2")){
            snd_statudID="2";
            GradientDrawable bgShape = (GradientDrawable) Dstarted.getBackground();
            bgShape.setColor(getResources().getColor(R.color.i_started));
            Dstarted.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.i_started));


            GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dpending.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) DClosed.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            DClosed.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) Dreopen.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dreopen.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.colorWhite));
            Dfixed.setTextColor(Color.BLACK);
            bgShape4.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.colorWhite));
            deferre.setTextColor(Color.BLACK);
            bgShape6.setStroke(2,getResources().getColor(R.color.input));



        }else if (status_id.equals("3")){
            snd_statudID="3";
            GradientDrawable bgShape = (GradientDrawable) DClosed.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            DClosed.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dstarted.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dpending.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) Dreopen.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            Dreopen.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.fixed));
            Dfixed.setTextColor(Color.WHITE);
            bgShape4.setStroke(2,getResources().getColor(R.color.fixed));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.colorWhite));
            deferre.setTextColor(Color.BLACK);
            bgShape6.setStroke(2,getResources().getColor(R.color.input));
        }else if (status_id.equals("5")){
            snd_statudID="5";
            GradientDrawable bgShape = (GradientDrawable) Dreopen.getBackground();
            bgShape.setColor(getResources().getColor(R.color.i_reopend));
            Dreopen.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.i_reopend));

            GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            DClosed.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dstarted.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dpending.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));


            GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.colorWhite));
            Dfixed.setTextColor(Color.BLACK);
            bgShape4.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.colorWhite));
            deferre.setTextColor(Color.BLACK);
            bgShape6.setStroke(2,getResources().getColor(R.color.input));

        }else if (status_id.equals("4")){
            snd_statudID="4";
            GradientDrawable bgShape = (GradientDrawable) Dfixed.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Dfixed.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape4 = (GradientDrawable) Dreopen.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.colorWhite));
            Dreopen.setTextColor(Color.BLACK);
            bgShape4.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.closed));
            DClosed.setTextColor(Color.WHITE);
            bgShape3.setStroke(2,getResources().getColor(R.color.closed));

            GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dstarted.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dpending.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.colorWhite));
            deferre.setTextColor(Color.BLACK);
            bgShape6.setStroke(2,getResources().getColor(R.color.input));

        }else if (status_id.equals("6")){

            snd_statudID="6";
            GradientDrawable bgShape = (GradientDrawable) Dfixed.getBackground();
            bgShape.setColor(getResources().getColor(R.color.gray));
            Dfixed.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.gray));

            GradientDrawable bgShape4 = (GradientDrawable) Dreopen.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.colorWhite));
            Dreopen.setTextColor(Color.BLACK);
            bgShape4.setStroke(2,getResources().getColor(R.color.gray));

            GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.gray));
            DClosed.setTextColor(Color.WHITE);
            bgShape3.setStroke(2,getResources().getColor(R.color.gray));

            GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.gray));
            Dstarted.setTextColor(Color.WHITE);
            bgShape2.setStroke(2,getResources().getColor(R.color.gray));

            GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.gray));
            Dpending.setTextColor(Color.WHITE);
            bgShape1.setStroke(2,getResources().getColor(R.color.gray));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.gray));
            deferre.setTextColor(Color.WHITE);
            bgShape6.setStroke(2,getResources().getColor(R.color.gray));


            //disable all other status bottons

            Dpending.setEnabled(false);
            Dstarted.setEnabled(false);
            Dfixed.setEnabled(false);
            Dreopen.setEnabled(true);
            DClosed.setEnabled(false);


            deferdpre=true;



        }else {
            GradientDrawable bgShape = (GradientDrawable) Dfixed.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Dfixed.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape4 = (GradientDrawable) Dreopen.getBackground();
            bgShape4.setColor(getResources().getColor(R.color.colorWhite));
            Dreopen.setTextColor(Color.BLACK);
            bgShape4.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
            bgShape3.setColor(getResources().getColor(R.color.colorWhite));
            DClosed.setTextColor(Color.BLACK);
            bgShape3.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dstarted.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dpending.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
            bgShape6.setColor(getResources().getColor(R.color.colorWhite));
            deferre.setTextColor(Color.BLACK);
            bgShape6.setStroke(2,getResources().getColor(R.color.input));
        }

        //environment

        if (environment_id.equals("1")){
            snd_environmentId="1";
            GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
            bgShape.setColor(getResources().getColor(R.color.production));
            Dproduction.setTextColor(Color.WHITE);
            bgShape.setStroke(2,getResources().getColor(R.color.production));

            GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            D_UserAcceptance.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            D_Development.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

        }else if (environment_id.equals("2")){
            snd_environmentId="2";
            GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Dproduction.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.acceptance));
            D_UserAcceptance.setTextColor(Color.WHITE);
            bgShape1.setStroke(2,getResources().getColor(R.color.acceptance));

            GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            D_Development.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));

        }else if (environment_id.equals("3")){
            snd_environmentId="3";
            GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Dproduction.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            D_UserAcceptance.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.developement));
            D_Development.setTextColor(Color.WHITE);
            bgShape2.setStroke(2,getResources().getColor(R.color.developement));

        }else {
            GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
            bgShape.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
            bgShape1.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape1.setStroke(2,getResources().getColor(R.color.input));

            GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
            bgShape2.setColor(getResources().getColor(R.color.colorWhite));
            Dlow.setTextColor(Color.BLACK);
            bgShape2.setStroke(2,getResources().getColor(R.color.input));
        }

        Durgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snd_priorityID="1";
                GradientDrawable bgShape = (GradientDrawable) Durgent.getBackground();
                bgShape.setColor(getResources().getColor(R.color.urgent));
                Durgent.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.urgent));

                GradientDrawable bgShape1 = (GradientDrawable) Dhigh.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Dhigh.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) Dmedium.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dmedium.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape3 = (GradientDrawable) Dlow.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dlow.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));
                UpdatePriority(snd_priorityID);
            }
        });
        Dhigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_priorityID="2";
                GradientDrawable bgShape = (GradientDrawable) Dhigh.getBackground();
                bgShape.setColor(getResources().getColor(R.color.high));
                Dhigh.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.high));

                GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Durgent.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) Dmedium.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dmedium.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape3 = (GradientDrawable) Dlow.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dlow.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));
                UpdatePriority(snd_priorityID);
            }
        });
        Dmedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_priorityID="3";
                GradientDrawable bgShape = (GradientDrawable) Dmedium.getBackground();
                bgShape.setColor(getResources().getColor(R.color.medium));
                Dmedium.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.medium));

                GradientDrawable bgShape2 = (GradientDrawable) Dhigh.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dhigh.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Durgent.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));


                GradientDrawable bgShape3 = (GradientDrawable) Dlow.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dlow.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));
                UpdatePriority(snd_priorityID);
            }
        });
        Dlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_priorityID="4";
                GradientDrawable bgShape = (GradientDrawable) Dlow.getBackground();
                bgShape.setColor(getResources().getColor(R.color.low));
                Dlow.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.low));


                GradientDrawable bgShape3 = (GradientDrawable) Dmedium.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dmedium.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) Dhigh.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dhigh.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) Durgent.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Durgent.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));
                UpdatePriority(snd_priorityID);
            }
        });

        Dpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_statudID="1";
                GradientDrawable bgShape = (GradientDrawable) Dpending.getBackground();
                bgShape.setColor(getResources().getColor(R.color.i_pending));
                Dpending.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.i_pending));

                GradientDrawable bgShape1 = (GradientDrawable) Dstarted.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Dstarted.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) DClosed.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                DClosed.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape3 = (GradientDrawable) Dreopen.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dreopen.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
                bgShape4.setColor(getResources().getColor(R.color.colorWhite));
                Dfixed.setTextColor(Color.BLACK);
                bgShape4.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
                bgShape6.setColor(getResources().getColor(R.color.colorWhite));
                deferre.setTextColor(Color.BLACK);
                bgShape6.setStroke(2,getResources().getColor(R.color.input));

                UpdateStatus(snd_statudID);


            }
        });
        Dstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_statudID="2";
                GradientDrawable bgShape = (GradientDrawable) Dstarted.getBackground();
                bgShape.setColor(getResources().getColor(R.color.i_started));
                Dstarted.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.i_started));


                GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Dpending.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) DClosed.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                DClosed.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape3 = (GradientDrawable) Dreopen.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dreopen.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
                bgShape4.setColor(getResources().getColor(R.color.colorWhite));
                Dfixed.setTextColor(Color.BLACK);
                bgShape4.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
                bgShape6.setColor(getResources().getColor(R.color.colorWhite));
                deferre.setTextColor(Color.BLACK);
                bgShape6.setStroke(2,getResources().getColor(R.color.input));

                UpdateStatus(snd_statudID);
            }
        });
        DClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_statudID="4";
                GradientDrawable bgShape = (GradientDrawable) DClosed.getBackground();
                bgShape.setColor(getResources().getColor(R.color.closed));
                DClosed.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.closed));

                GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dstarted.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Dpending.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape3 = (GradientDrawable) Dreopen.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                Dreopen.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
                bgShape4.setColor(getResources().getColor(R.color.colorWhite));
                Dfixed.setTextColor(Color.BLACK);
                bgShape4.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
                bgShape6.setColor(getResources().getColor(R.color.colorWhite));
                deferre.setTextColor(Color.BLACK);
                bgShape6.setStroke(2,getResources().getColor(R.color.input));

                UpdateStatus(snd_statudID);
            }
        });
        Dreopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_statudID="5";
                Dpending.setEnabled(true);
                Dstarted.setEnabled(true);
                Dfixed.setEnabled(true);
                Dreopen.setEnabled(true);
                DClosed.setEnabled(true);
                deferre.setEnabled(true);
                deferdpre=false;
                GradientDrawable bgShape = (GradientDrawable) Dreopen.getBackground();
                bgShape.setColor(getResources().getColor(R.color.i_reopend));
                Dreopen.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.i_reopend));

                GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                DClosed.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dstarted.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Dpending.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape4 = (GradientDrawable) Dfixed.getBackground();
                bgShape4.setColor(getResources().getColor(R.color.colorWhite));
                Dfixed.setTextColor(Color.BLACK);
                bgShape4.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
                bgShape6.setColor(getResources().getColor(R.color.colorWhite));
                deferre.setTextColor(Color.BLACK);
                bgShape6.setStroke(2,getResources().getColor(R.color.input));

                UpdateStatus(snd_statudID);
            }
        });
        Dfixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_statudID="3";
                GradientDrawable bgShape = (GradientDrawable) Dfixed.getBackground();
                bgShape.setColor(getResources().getColor(R.color.fixed));
                Dfixed.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.fixed));

                GradientDrawable bgShape4 = (GradientDrawable) Dreopen.getBackground();
                bgShape4.setColor(getResources().getColor(R.color.colorWhite));
                Dreopen.setTextColor(Color.BLACK);
                bgShape4.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.colorWhite));
                DClosed.setTextColor(Color.BLACK);
                bgShape3.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                Dstarted.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                Dpending.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
                bgShape6.setColor(getResources().getColor(R.color.colorWhite));
                deferre.setTextColor(Color.BLACK);
                bgShape6.setStroke(2,getResources().getColor(R.color.input));

                UpdateStatus(snd_statudID);


            }
        });

        deferre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //showing popup if not deferred

                if (deferdpre==false){

                    //check the empty title, empty description, empty member
                    /*String s="";
                    String s1="";
                    String dscript=edit_description.getText().toString().trim();
                    String title=edittile.getText().toString().trim();
                    for (int i=0;i<mermberList.size();i++){

                        s= s +mermberList.get(i).getId()+"|";
                        System.out.println("members=:  "+s);

                    }
                    for (int i=0;i<areaList.size();i++){
                        s1=s1+areaList.get(i).getTickt_id()+"|";
                        System.out.println("areas=:  "+s1);
                    }*/

                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){

               //         if(s==""||dscript.equals("")||title.equals("")){

                           /* if (s==""){
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Please assign atleast 1 member to the Ticket !", Snackbar.LENGTH_LONG);

                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.RED);
                                snackbar.show();
                            }else if (title.equals("")){
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Ticket Title can't be empty !", Snackbar.LENGTH_LONG);

                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.RED);
                                snackbar.show();
                            }else if (dscript.equals("")){
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Ticket Description can't be empty ! ", Snackbar.LENGTH_LONG);

                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.RED);
                                snackbar.show();
                            }*/

              //          }else {
                            //put the next actions for comment nad update the status

                            final Dialog popubdialog = new Dialog(Edit_issue_details.this);
                            popubdialog.setContentView(R.layout.write_deferred_comment);
                            popubdialog.show();
                            final EditText commnt=(EditText)popubdialog.findViewById(R.id.commnt);
                            final Button yes=(Button)popubdialog.findViewById(R.id.yes);
                            final Button no=(Button)popubdialog.findViewById(R.id.no);
                            final TextView errorempty=(TextView)popubdialog.findViewById(R.id.errorempty);
                            commnt.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    errorempty.setVisibility(View.GONE);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Boolean checkinternet2 = checkConnection();
                                    if (checkinternet2 == true) {
                                        String comment = commnt.getText().toString().trim();
                                        if (comment.equals("")) {
                                            //do nothing
                                            errorempty.setVisibility(View.VISIBLE);
                                            System.out.println("hic");
                                        } else {
                                            new PostCommnet(comment).execute();
                                            commnt.setText("");
                                            popubdialog.dismiss();

                                        }

                                    } else {
                                        Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                                        View sbView = snackbar.getView();
                                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                        textView.setTextColor(Color.RED);
                                        snackbar.show();
                                    }
                                }
                            });
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popubdialog.hide();
                                }
                            });


                            popubdialog.show();


              //          }
                    }else {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }


                }



            }
        });
        Dproduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_environmentId="1";
                GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
                bgShape.setColor(getResources().getColor(R.color.production));
                Dproduction.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.production));

                GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                D_UserAcceptance.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                D_Development.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));
                UpdateEnvironment(snd_environmentId);
            }
        });
        D_UserAcceptance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_environmentId="2";
                GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                Dproduction.setTextColor(Color.BLACK);
                bgShape.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.acceptance));
                D_UserAcceptance.setTextColor(Color.WHITE);
                bgShape1.setStroke(2,getResources().getColor(R.color.acceptance));

                GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.colorWhite));
                D_Development.setTextColor(Color.BLACK);
                bgShape2.setStroke(2,getResources().getColor(R.color.input));
                UpdateEnvironment(snd_environmentId);
            }
        });
        D_Development.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snd_environmentId="3";
                GradientDrawable bgShape = (GradientDrawable) Dproduction.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                Dproduction.setTextColor(Color.BLACK);
                bgShape.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape1 = (GradientDrawable) D_UserAcceptance.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                D_UserAcceptance.setTextColor(Color.BLACK);
                bgShape1.setStroke(2,getResources().getColor(R.color.input));

                GradientDrawable bgShape2 = (GradientDrawable) D_Development.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.developement));
                D_Development.setTextColor(Color.WHITE);
                bgShape2.setStroke(2,getResources().getColor(R.color.developement));
                UpdateEnvironment(snd_environmentId);
            }
        });

        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){
            new GetmemArea().execute();
            nointernet_lay.setVisibility(View.GONE);
            emProgress.setVisibility(View.GONE);
            appbar.setVisibility(View.VISIBLE);
        }else {
            appbar.setVisibility(View.GONE);
            nointernet_lay.setVisibility(View.VISIBLE);

        }
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    new GetmemArea().execute();
                    emProgress.setVisibility(View.VISIBLE);
                    appbar.setVisibility(View.VISIBLE);
                }else {
                    appbar.setVisibility(View.GONE);
                    emProgress.setVisibility(View.VISIBLE);
                    onIntent();
                    nointernet_lay.setVisibility(View.VISIBLE);
                }
            }
        });

        addmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    dialog1 = new Dialog(context);
                    dialog1.setContentView(R.layout.member_list_popup);
                    dialog1.show();
                    RelativeLayout relativeLayout = (RelativeLayout) dialog1.findViewById(R.id.rel11);
                    listView = (ListView) dialog1.findViewById(R.id.members);
                    titlenameTag = (TextView) dialog1.findViewById(R.id.titlenameTag);
                    titlenameTag.setText("Add Members");
                    closepop = (ImageButton) dialog1.findViewById(R.id.closepop);
                    closepop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mempopList.clear();
                            membername.clear();
                            memberId.clear();
                            dialog1.dismiss();
                        }
                    });

                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    relativeLayout.setBackgroundResource(R.drawable.dialog_corners);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 18);
                    myAnim.setInterpolator(interpolator);
                    relativeLayout.startAnimation(myAnim);
                    dialog1.setCanceledOnTouchOutside(false);
                    new getMemerbs().execute();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.editLay), "Sorry! You need internet to Access Members !", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }



            }
        });
        addArBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    dialog2 = new Dialog(context);
                    dialog2.setContentView(R.layout.member_list_popup);
                    dialog2.show();
                    RelativeLayout relativeLayout = (RelativeLayout) dialog2.findViewById(R.id.rel11);
                    listView = (ListView) dialog2.findViewById(R.id.members);
                    titlenameTag = (TextView) dialog2.findViewById(R.id.titlenameTag);
                    titlenameTag.setText("Add Area");
                    closepop = (ImageButton) dialog2.findViewById(R.id.closepop);
                    closepop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            areapopList.clear();
                            dialog2.dismiss();
                        }
                    });

                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    relativeLayout.setBackgroundResource(R.drawable.dialog_corners);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 18);
                    myAnim.setInterpolator(interpolator);
                    relativeLayout.startAnimation(myAnim);
                    dialog2.setCanceledOnTouchOutside(false);
                    new getAreas().execute();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.editLay), "Sorry! You need internet to Access Areas !", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }

            }
        });

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
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.editLay), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            showFileChooser();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void Managebuttonclick() {
        if (ActivityCompat.checkSelfPermission(Edit_issue_details.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Edit_issue_details.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSION_REQUEST_CONTACTS);
        }else {
            reardcontacts();
        }

    }
    private void reardcontacts() {
        if (ActivityCompat.checkSelfPermission(Edit_issue_details.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Edit_issue_details.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file=getOutputMediaFile(1);
            // fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            filePath= file.getPath();

            // Toast.makeText(getActivity(),filePath,Toast.LENGTH_LONG).show();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent
            startActivityForResult(intent, REQUEST_CAMERA);
        }

    }
    private  File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }
    private void GALLERYBUTTONCLICK() {

            if (ActivityCompat.checkSelfPermission(Edit_issue_details.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Edit_issue_details.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }else {
                galleryIntent();
            }
    }
    BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback(){
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState){
                        case BottomSheetBehavior.STATE_COLLAPSED:

                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:

                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:

                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:

                            bottomSheetDialog.dismiss();
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:

                            break;
                        default:

                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
        }
    }
    private void galleryIntent() {
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }

    }
    private void onCaptureImageResultGallery(Intent data) {
        thumbnail = (Bitmap)data.getExtras().get("data");
        // filePath = data.getStringExtra("data");
        //   filePath=BitMapToString(thumbnail);
        //  filePath=fileUri.getPath();
        /*Bitmap displayImage = null;
        byte[] image=getBytes(thumbnail);
        database.addEntry(image);
        System.out.println("image------"+database);*/
        Uri selectedFileURI = data.getData();
//        File file = new File(selectedFileURI.getPath().toString());
        //  filePath=file.toString();
        Uri tempUri = getImageUri(Edit_issue_details.this, thumbnail);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        finalFile = new File(getRealPathFromURI(tempUri));
        filePath=getRealPathFromURI(tempUri);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // imageView =new ImageView(getActivity());
        document.setVisibility(View.VISIBLE);
        document.setScaleType(ImageView.ScaleType.CENTER_CROP);
        document.requestLayout();
        document.getLayoutParams().height = 190;
        document.setImageBitmap(thumbnail);
        docName.setText(destination.getName());
        bottomSheetDialog.dismiss();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                //      onSelectFromGalleryResult(data);
                onSelectFromGalleryResultGallery(data);
            }else  if (requestCode == REQUEST_CAMERA) {
                // onCaptureImageResult(data);
                onCaptureImageResultGallery(data);
                bottomSheetDialog.dismiss();
            }
            if (requestCode == 1) {
                Uri selectedFileURI = data.getData();
                File file = new File(selectedFileURI.getPath().toString());
                filePath=file.toString();
                Log.d("", "File : " + file.getName());
                //  StringTokenizer  tokens = new StringTokenizer(uploadedFileName, ":");
                //  String   first = tokens.nextToken();
                // String    file_1 = tokens.nextToken().trim();
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(Edit_issue_details.this.getContentResolver(), selectedFileURI);

                    if (filePath.contains(".docx")||filePath.contains(".pdf")
                            ||filePath.contains(".xls")||filePath.contains(".pptx")
                            ||filePath.contains(".xlsx")||filePath.contains(".txt")||filePath.contains(".png")||filePath.contains(".jpg")
                            ||filePath.contains(".jpeg")){
                        validfile=true;

                    }else {
                        validfile=false;
                        filePath=null;
                    }

                    if ( validfile==false){
                        bottomSheetDialog.dismiss();
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Sorry! not a valid file format", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                        document.setVisibility(View.GONE);
                        docName.setText("");
                    }else {
                        document.setVisibility(View.VISIBLE);
                        if (filePath.contains(".jpg")||filePath.contains(".jpeg")||filePath.contains(".png")){
                            document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            document.requestLayout();
                            document.getLayoutParams().height = 190;
                            document.setImageBitmap(thumbnail);
                            docName.setText(file.getName());
                        }else {
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.docs);
                            document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            document.requestLayout();
                            document.getLayoutParams().height = 190;
                            document.setImageBitmap(icon);
                            docName.setText(file.getName());

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("file: naem :"+ filePath);
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
        Cursor cursor = Edit_issue_details.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void onSelectFromGalleryResultGallery(Intent data) {
        Uri uri = data.getData();
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(Edit_issue_details.this.getContentResolver(), uri);
            Uri tempUri = getImageUri(Edit_issue_details.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));
            filePath=getRealPathFromURI(tempUri);
        } catch (IOException e) {

            e.printStackTrace();
        }
        document.setScaleType(ImageView.ScaleType.CENTER_CROP);
        document.requestLayout();
        document.getLayoutParams().height = 190;
        document.setImageBitmap(thumbnail);
        //thumbnail=g.getBm();

        //imagecapture.setImageBitmap(bm);
    }
    public void memFromAdapter(){

        SharedPreferences setting =getApplicationContext(). getSharedPreferences(
                PREF_NAME, 0);

        String name = setting.getString("POPNAME", null);
        String id = setting.getString("POPID", null);
        member_assigned_bean timerbeanitem=new member_assigned_bean();

        timerbeanitem.setName(name);
        timerbeanitem.setId(id);
        mermberList.add(timerbeanitem);
        memAdapter = new IEdit_assnmemAdapter(Edit_issue_details.this, mermberList);
        memberlistview.setAdapter(memAdapter);
        setListViewHeightBasedOnChildren(memberlistview);
        memAdapter.notifyDataSetChanged();
    }
    public void areaFromAdapter(){

        SharedPreferences setting =getApplicationContext(). getSharedPreferences(
                AREAPOPPRE, 0);

        String name = setting.getString("POPTITLE", null);
        String id = setting.getString("POPTICKETID", null);
        Areabean item=new Areabean();
        item.setTitle(name);
        item.setTickt_id(id);
        areaList.add(item);
        areAdapter = new IEdit_areaAdapter(Edit_issue_details.this, areaList);
        areaListview.setAdapter(areAdapter);
        setListViewHeightBasedOnChildren(areaListview);
        areAdapter.notifyDataSetChanged();
    }
    private void UpdateEnvironment(String environmentId) {
       /* pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("ticketId", ticket_id);
                jsonBody.put("environmentId", environmentId);
                jsonBody.put("loggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.ChangeEnvironment, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String Message=data.getString("status");
                                if (Message.matches("0")){
                                    SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    statusBadge++;
                                    editor.putInt("COUNT",statusBadge);
                                    editor.commit();
                                    qBadgeView.setVisibility(View.VISIBLE);
                                    qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
                            //        pDialog.hide();
                                    // Toast.makeText(getApplicationContext(),"Ticket deleted !",Toast.LENGTH_SHORT).show();
                                    Snackbar snackbar = Snackbar
                                            .make(editLay, "Environment Updated successfully !", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                   //         pDialog.hide();


                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
            //        pDialog.hide();
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
    private void UpdateStatus(String statusID) {
     /*   pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("ticketId", ticket_id);
                jsonBody.put("statusId", statusID);
                jsonBody.put("loggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.Changestatus, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String Message=data.getString("status");
                                if (Message.matches("0")){
                                    SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    statusBadge++;
                                    editor.putInt("COUNT",statusBadge);
                                    editor.commit();
                                    qBadgeView.setVisibility(View.VISIBLE);
                                    qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
                           //         pDialog.hide();
                                   // Toast.makeText(getApplicationContext(),"Ticket deleted !",Toast.LENGTH_SHORT).show();
                                    Snackbar snackbar = Snackbar
                                            .make(editLay, "Status Updated successfully !", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                       //     pDialog.hide();


                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
              //      pDialog.hide();
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
    private class PostCommnet extends AsyncTask<Void, Integer, String> {
        String commentpost;
        String commentget;
        String PHOTO;
        String NAME;

        public PostCommnet(String com) {
            super();
            commentpost=com;
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

        }

        @Override
        protected String doInBackground(Void... params) {

            return addcommentpost();
        }

        @SuppressWarnings("deprecation")
        private String addcommentpost() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppUtils.CreateComment);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body

                // Extra parameters if you want to pass to server
                entity.addPart("LoggedInUser", new StringBody(loginuserid));
                entity.addPart("TicketId", new StringBody(ticket_id));
                entity.addPart("CommentText", new StringBody(commentpost));
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
                    responseString = "Error occurred! Http Status Code:"
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

            /*String s="";
            String s1="";
            String dscript=edit_description.getText().toString().trim();
            String title=edittile.getText().toString().trim();
            for (int i=0;i<mermberList.size();i++){

                s= s +mermberList.get(i).getId()+"|";
                System.out.println("members=:  "+s);

            }
            for (int i=0;i<areaList.size();i++){
                s1=s1+areaList.get(i).getTickt_id()+"|";
                System.out.println("areas=:  "+s1);
            }*/

            Boolean checkinternet2= checkConnection();
            if (checkinternet2==true){

                    UpdateStatus("6");
                   // new SaveIssueDetails(s,s1).execute();
                GradientDrawable bgShape = (GradientDrawable) Dfixed.getBackground();
                bgShape.setColor(getResources().getColor(R.color.gray));
                Dfixed.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(R.color.gray));

                GradientDrawable bgShape4 = (GradientDrawable) Dreopen.getBackground();
                bgShape4.setColor(getResources().getColor(R.color.colorWhite));
                Dreopen.setTextColor(Color.BLACK);
                bgShape4.setStroke(2,getResources().getColor(R.color.gray));

                GradientDrawable bgShape3 = (GradientDrawable) DClosed.getBackground();
                bgShape3.setColor(getResources().getColor(R.color.gray));
                DClosed.setTextColor(Color.WHITE);
                bgShape3.setStroke(2,getResources().getColor(R.color.gray));

                GradientDrawable bgShape2 = (GradientDrawable) Dstarted.getBackground();
                bgShape2.setColor(getResources().getColor(R.color.gray));
                Dstarted.setTextColor(Color.WHITE);
                bgShape2.setStroke(2,getResources().getColor(R.color.gray));

                GradientDrawable bgShape1 = (GradientDrawable) Dpending.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.gray));
                Dpending.setTextColor(Color.WHITE);
                bgShape1.setStroke(2,getResources().getColor(R.color.gray));

                GradientDrawable bgShape6 = (GradientDrawable) deferre.getBackground();
                bgShape6.setColor(getResources().getColor(R.color.gray));
                deferre.setTextColor(Color.WHITE);
                bgShape6.setStroke(2,getResources().getColor(R.color.gray));


                //disable all other status bottons

                Dpending.setEnabled(false);
                Dstarted.setEnabled(false);
                Dfixed.setEnabled(false);
                Dreopen.setEnabled(true);
                DClosed.setEnabled(false);


                deferdpre=true;

            }else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
            }
            super.onPostExecute(result);
        }

    }
    private void UpdatePriority(String priorityID) {
      //  pDialog = new ProgressDialog(this);
      //  pDialog.setMessage("Loading...");
      //  pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("ticketId", ticket_id);
                jsonBody.put("priorityId", priorityID);
                jsonBody.put("loggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.ChangePriority, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String Message=data.getString("status");
                                if (Message.matches("0")){
                                    SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    statusBadge++;
                                    editor.putInt("COUNT",statusBadge);
                                    editor.commit();
                                    qBadgeView.setVisibility(View.VISIBLE);
                                    qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
                                //    pDialog.hide();
                                    // Toast.makeText(getApplicationContext(),"Ticket deleted !",Toast.LENGTH_SHORT).show();
                                    Snackbar snackbar = Snackbar
                                            .make(editLay, "Priority Updated successfully !", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        //    pDialog.hide();


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
    private class getAreas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Edit_issue_details.this);
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

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        String tickt_id = jsonobject.getString("AreaId").trim();
                        String title = jsonobject.getString("AreaName").trim();

                        Areabean item=new Areabean();
                        item.setTitle(title);
                        item.setTickt_id(tickt_id);
                        areapopList.add(item);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Edit_issue_details.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  Toast.makeText(Edit_issue_details.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Edit_issue_details.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(Edit_issue_details.this,
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

            AreaPop_Adapter addareaadapter = new AreaPop_Adapter(Edit_issue_details.this, areapopList,Edit_issue_details.this);
            listView.setAdapter(addareaadapter);
            addareaadapter.notifyDataSetChanged();

        }
    }

    private class getMemerbs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Edit_issue_details.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String mem_str = sh.makeServiceCall(AppUtils.GetAssignedMembers+project_id);

            Log.e(TAG, "Response from url2: " + mem_str);

            if (mem_str !=null) {
                try {

                    JSONArray jsonarray1 = new JSONArray(mem_str);

                    for (int i = 0; i < jsonarray1.length(); i++) {
                        JSONObject jsonobject = jsonarray1.getJSONObject(i);

                        String AssignedMem_id = jsonobject.getString("AssigneeId").trim();
                        String AssignedMem_name = jsonobject.getString("AsigneeName").trim();
                        membername.add(AssignedMem_name);
                        memberId.add(AssignedMem_id);
                        member_assigned_bean item2=new member_assigned_bean();

                        item2.setId(AssignedMem_id);
                        item2.setName(AssignedMem_name);
                        mempopList.add(item2);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Edit_issue_details.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(Edit_issue_details.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Edit_issue_details.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(Edit_issue_details.this,
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

            mem_pop_Adapter addmemberadapter = new mem_pop_Adapter(Edit_issue_details.this, mempopList,Edit_issue_details.this);
            listView.setAdapter(addmemberadapter);
            addmemberadapter.notifyDataSetChanged();

        }
    }

    private class SaveIssueDetails extends AsyncTask<Void, Integer, String> {
        ProgressDialog dialog;
        String members ;
        String areas ;
        public SaveIssueDetails(String string,String string1) {
            super();
            members=string;
            areas=string1;
            // Do something with these parameters
        }
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Edit_issue_details.this);
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
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
            String title=edittile.getText().toString().trim();
            String description=edit_description.getText().toString().trim();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppUtils.EditTicket);
           /* ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
*/
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
                entity.addPart("LoggedInUser", new StringBody(loginuserid));
                entity.addPart("TicketId", new StringBody(ticket_id));
                entity.addPart("PriorityId", new StringBody(snd_priorityID));
                entity.addPart("StatusId", new StringBody(snd_statudID));
                entity.addPart("ProjectId", new StringBody(projetctId));
                entity.addPart("EnvironmentId", new StringBody(snd_environmentId));
                entity.addPart("TicketTypeId", new StringBody(snd_tickettypeid));
                entity.addPart("Assigned", new StringBody(members));
                entity.addPart("Areas", new StringBody(areas));

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

            if (dialog.isShowing())
                dialog.dismiss();
            SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
            SharedPreferences.Editor editor = settings.edit();
            statusBadge++;
            editor.putInt("COUNT",statusBadge);
            editor.commit();
            qBadgeView.setVisibility(View.VISIBLE);
            qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
            Toast.makeText(getApplicationContext(),"Ticket Updated Successfully !",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),Issue_DetailsActivity.class);
            startActivity(intent);

            if (areaList.size()>0){
                areaList.clear();
            }
            if (mermberList.size()>0){
                mermberList.clear();
            }
            super.onPostExecute(result);
        }
        /*private void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }*/

    }
    private class GetmemArea extends AsyncTask<Void, Void, Void> {

        String AssigneeId = "";
        String AsigneeName ="";
        String AreaId ="";
        String AreaName ="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Edit_issue_details.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetTicketById+ticket_id);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                   /* // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");*/

                    // looping through All Contacts

                    JSONObject obj = null;
                    JSONArray attachment_obj = null;
                    JSONArray assignee_obj = null;
                    JSONArray area_obj = null;

                    try {
                        obj = jsonObj.getJSONObject("data");
                        attachment_obj = obj.getJSONArray("attachment");
                        assignee_obj = obj.getJSONArray("assignee");
                        area_obj = obj.getJSONArray("area");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < assignee_obj.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) assignee_obj.get(i);

                        AssigneeId = jsonObject1.getString("AssigneeId");
                        AsigneeName = jsonObject1.getString("AsigneeName");
                        member_assigned_bean timerbeanitem=new member_assigned_bean();

                        timerbeanitem.setName(AsigneeName);
                        timerbeanitem.setId(AssigneeId);
                        mermberList.add(timerbeanitem);


                    }
                    for (int i = 0; i < area_obj.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) area_obj.get(i);

                        AreaId = jsonObject1.getString("AreaId");
                        AreaName = jsonObject1.getString("AreaName");
                        Areabean item=new Areabean();

                        item.setTickt_id(AreaId);
                        item.setTitle(AreaName);
                        areaList.add(item);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Edit_issue_details.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(Edit_issue_details.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Edit_issue_details.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(Edit_issue_details.this,
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

            nointernet_lay.setVisibility(View.GONE);
            memAdapter = new IEdit_assnmemAdapter(Edit_issue_details.this, mermberList);
            memberlistview.setAdapter(memAdapter);
            setListViewHeightBasedOnChildren(memberlistview);
            memAdapter.notifyDataSetChanged();

            areAdapter = new IEdit_areaAdapter(Edit_issue_details.this, areaList);
            areaListview.setAdapter(areAdapter);
            setListViewHeightBasedOnChildren(areaListview);
            areAdapter.notifyDataSetChanged();
            preload.setVisibility(View.GONE);
            mProgress1.setVisibility(View.GONE);
            /**
             * Updating parsed JSON data into ListView
             * */
        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
    private Menu mOptionsMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu=menu;
        getMenuInflater().inflate(R.menu.menupro, menu);
        mOptionsMenu.getItem(0).setVisible(false);
        mOptionsMenu.getItem(1).setVisible(true);
        mOptionsMenu.getItem(2).setVisible(false);
        return true;
    }
    boolean canEdit = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.action_settings1){
            //update();

            String s="";
            String s1="";
            String dscript=edit_description.getText().toString().trim();
            String title=edittile.getText().toString().trim();
            for (int i=0;i<mermberList.size();i++){

                s= s +mermberList.get(i).getId()+"|";
                System.out.println("members=:  "+s);

            }
            for (int i=0;i<areaList.size();i++){
                s1=s1+areaList.get(i).getTickt_id()+"|";
                System.out.println("areas=:  "+s1);
            }

            Boolean checkinternet2= checkConnection();
            if (checkinternet2==true){

                if(s==""||dscript.equals("")||title.equals("")){

                    if (s==""){
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Please assign atleast 1 member to the Ticket !", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }else if (title.equals("")){
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Ticket Title can't be empty !", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }else if (dscript.equals("")){
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Ticket Description can't be empty ! ", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }

                }else new SaveIssueDetails(s,s1).execute();
            }else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.editLay), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
            }

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (areaList.size()>0){
            areaList.clear();
        }
        if (mermberList.size()>0){
            mermberList.clear();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (areaList.size()>0){
            areaList.clear();
        }
        if (mermberList.size()>0){
            mermberList.clear();
        }
    }
}
