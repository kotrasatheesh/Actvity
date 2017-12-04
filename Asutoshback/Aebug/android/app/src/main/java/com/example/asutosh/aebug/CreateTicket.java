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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asutosh.aebug.Adapter.CArea_pop_Adapter;
import com.example.asutosh.aebug.Adapter.Cmem_pop_Adapter;
import com.example.asutosh.aebug.Adapter.Create_areaAdapter;
import com.example.asutosh.aebug.Adapter.Create_asMemAdapter;
import com.example.asutosh.aebug.bean.Areabean;
import com.example.asutosh.aebug.bean.member_assigned_bean;
import com.example.asutosh.aebug.fragments.SlideshowDialogFragment;

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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static com.example.asutosh.aebug.Adapter.AreaPop_Adapter.AREAPOPPRE;
import static com.example.asutosh.aebug.Adapter.projectAdapter.PROJECT_NAME;
import static com.example.asutosh.aebug.App_config.AppController.addedArea;
import static com.example.asutosh.aebug.App_config.AppController.addedmem;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.statusBadge;
import static com.example.asutosh.aebug.MainActivity.qBadgeView;
import static com.example.asutosh.aebug.MainActivity.v;
import static com.example.asutosh.aebug.fragments.home_fragment.STATUS_PRE;

public class CreateTicket extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    Boolean validfile=true;
    String snd_tickettypeid="";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private int SELECT_FILE = 2;
    File finalFile;
    Bitmap thumbnail;
    long totalSize = 0;
    private String filePath = null;
    String environmentclick="";
    String priorityCHk ="";
    Button urgent, high, medium, low;
    Button Bug, CR, Support;
    Button production, User_acceptance, development;
    public static final String PREF_NAME = "Divid";
    public static Dialog Cdialog,Cdialog2;
    public static List<member_assigned_bean> CmermberList = new ArrayList<member_assigned_bean>();
    public  static List<member_assigned_bean> CmemberpopList = new ArrayList<member_assigned_bean>();
    public  static  List<Areabean> CareapopList = new ArrayList<Areabean>();
    public static List<Areabean> CareaList = new ArrayList<Areabean>();
    private static final String TAG = Edit_issue_details.class.getSimpleName();
    SwitchCompat switch_select;
    private ProgressDialog pDialog;
    final Context context = this;
    ImageButton addmeBtn,addArBtn;
    Create_asMemAdapter CmemAdapter;
    Create_areaAdapter CareAdapter;
    ImageButton closepop,adddocumentbtnn;
    ListView listView;
    TextView titlenameTag,nomem;
    String ProjectId="";
    LinearLayout btnlay;
    RelativeLayout checkid;
    LinearLayout btnlay1;
    View sep1,sep2,sep,sepp,sepp1;
    public static ListView memberlistview1,areaListview1;
    EditText input_title,input_descr;
    TextView docNamee;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    ImageView document;
    RelativeLayout container1;
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
        setContentView(R.layout.activity_create_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
        ProjectId=settings.getString("PROJECTID",ProjectId );
        System.out.println("projectId:  "+ProjectId);
       // switch_select=(SwitchCompat) findViewById(R.id.switch_select);
        btnlay=(LinearLayout)findViewById(R.id.btnlay);
        //checkid=(RelativeLayout)findViewById(R.id.checkid);
        btnlay1=(LinearLayout)findViewById(R.id.btnlay1);
        sep=(View)findViewById(R.id.sep);
        sep1=(View)findViewById(R.id.sep1);
        sep2=(View)findViewById(R.id.sep2);
        sepp=(View)findViewById(R.id.sepp);
        sepp1=(View)findViewById(R.id.sepp1);
        memberlistview1=(ListView)findViewById(R.id.memAssginListview1);
        areaListview1=(ListView)findViewById(R.id.areaListview1);
        addmeBtn=(ImageButton)findViewById(R.id.addMembtn);
        addArBtn=(ImageButton)findViewById(R.id.addAreaBtn);
        document=(ImageView)findViewById(R.id.document);
        container1=(RelativeLayout)findViewById(R.id.container1);
        adddocumentbtnn=(ImageButton)findViewById(R.id.adddocumentbtnn);
        adddocumentbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GALLERYBUTTONCLICK();
                bottomSheetView = getLayoutInflater().inflate(R.layout.choose_cam_gal, null);
                bottomSheetDialog = new BottomSheetDialog(CreateTicket.this);
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
        Bug = (Button) findViewById(R.id.Bug);
        CR = (Button) findViewById(R.id.CR);
        Support = (Button) findViewById(R.id.Support);
        urgent = (Button) findViewById(R.id.urgent);
        high = (Button) findViewById(R.id.high);
        medium = (Button) findViewById(R.id.medium);
        low = (Button) findViewById(R.id.low);
        input_title = (EditText) findViewById(R.id.input_title);
        input_descr = (EditText) findViewById(R.id.input_descr);
        docNamee = (TextView) findViewById(R.id.docNamee);

        production = (Button) findViewById(R.id.production);
        User_acceptance = (Button) findViewById(R.id.User_acceptance);
        development = (Button) findViewById(R.id.development);
        CmemAdapter = new Create_asMemAdapter(this, CmermberList);
        CareAdapter = new Create_areaAdapter(this, CareaList);


        GradientDrawable bgShape0 = (GradientDrawable) Bug.getBackground();
        bgShape0.setColor(getResources().getColor(R.color.colorWhite));
        Bug.setTextColor(Color.BLACK);
        CR.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        CR.setTextColor(Color.BLACK);
        GradientDrawable bgShape01 = (GradientDrawable) Support.getBackground();
        bgShape01.setColor(getResources().getColor(R.color.colorWhite));
        Support.setTextColor(Color.BLACK);

        GradientDrawable bgShape012 = (GradientDrawable) btnlay.getBackground();
        bgShape012.setColor(getResources().getColor(R.color.colorWhite));
        bgShape012.setStroke(3,getResources().getColor(R.color.input));

        GradientDrawable bgShape0123 = (GradientDrawable) btnlay1.getBackground();
        bgShape0123.setColor(getResources().getColor(R.color.colorWhite));
        bgShape0123.setStroke(3,getResources().getColor(R.color.input));

        sep.setBackgroundColor(getResources().getColor(R.color.input));
        sep1.setBackgroundColor(getResources().getColor(R.color.input));
        sep2.setBackgroundColor(getResources().getColor(R.color.input));
        sepp.setBackgroundColor(getResources().getColor(R.color.input));
        sepp1.setBackgroundColor(getResources().getColor(R.color.input));

        GradientDrawable bgShape = (GradientDrawable) urgent.getBackground();
        bgShape.setColor(getResources().getColor(R.color.colorWhite));
        urgent.setTextColor(Color.BLACK);
        high.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        high.setTextColor(Color.BLACK);
        medium.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        medium.setTextColor(Color.BLACK);
        GradientDrawable bgShape1 = (GradientDrawable) low.getBackground();
        bgShape1.setColor(getResources().getColor(R.color.colorWhite));
        low.setTextColor(Color.BLACK);


        GradientDrawable bgShape2 = (GradientDrawable) production.getBackground();
        bgShape2.setColor(getResources().getColor(R.color.colorWhite));
        production.setTextColor(Color.BLACK);
        User_acceptance.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        User_acceptance.setTextColor(Color.BLACK);
        GradientDrawable bgShape3 = (GradientDrawable) development.getBackground();
        bgShape3.setColor(getResources().getColor(R.color.colorWhite));
        development.setTextColor(Color.BLACK);
        addmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    Cdialog = new Dialog(context);
                    Cdialog.setContentView(R.layout.member_list_popup);
                    Cdialog.show();
                    RelativeLayout relativeLayout = (RelativeLayout) Cdialog.findViewById(R.id.rel11);
                    listView = (ListView) Cdialog.findViewById(R.id.members);
                    titlenameTag = (TextView) Cdialog.findViewById(R.id.titlenameTag);
                    nomem = (TextView) Cdialog.findViewById(R.id.nomem);
                    titlenameTag.setText("Add Members");
                    closepop = (ImageButton) Cdialog.findViewById(R.id.closepop);
                    closepop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CmemberpopList.clear();
                            Cdialog.dismiss();
                        }
                    });

                    Cdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    relativeLayout.setBackgroundResource(R.drawable.dialog_corners);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 18);
                    myAnim.setInterpolator(interpolator);
                    relativeLayout.startAnimation(myAnim);
                    Cdialog.setCanceledOnTouchOutside(false);
                    new getMemerbs().execute();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.container1), "Sorry! You need internet to Access Members !", Snackbar.LENGTH_LONG);

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
                    Cdialog2 = new Dialog(context);
                    Cdialog2.setContentView(R.layout.member_list_popup);
                    Cdialog2.show();
                    RelativeLayout relativeLayout = (RelativeLayout) Cdialog2.findViewById(R.id.rel11);
                    listView = (ListView) Cdialog2.findViewById(R.id.members);
                    titlenameTag = (TextView) Cdialog2.findViewById(R.id.titlenameTag);
                    titlenameTag.setText("Add Areas");
                    closepop = (ImageButton) Cdialog2.findViewById(R.id.closepop);
                    closepop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CareapopList.clear();
                            Cdialog2.dismiss();
                        }
                    });

                    Cdialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    relativeLayout.setBackgroundResource(R.drawable.dialog_corners);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 18);
                    myAnim.setInterpolator(interpolator);
                    relativeLayout.startAnimation(myAnim);
                    Cdialog2.setCanceledOnTouchOutside(false);
                    new getAreas().execute();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.container1), "Sorry! You need internet to Access Areas !", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }

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


        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priorityCHk ="1";

                GradientDrawable bgShape012 = (GradientDrawable) btnlay.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.urgent));

                sep.setBackgroundColor(getResources().getColor(R.color.urgent));
                sep1.setBackgroundColor(getResources().getColor(R.color.urgent));
                sep2.setBackgroundColor(getResources().getColor(R.color.urgent));

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
                priorityCHk ="2";

                GradientDrawable bgShape012 = (GradientDrawable) btnlay.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.high));

                sep.setBackgroundColor(getResources().getColor(R.color.high));
                sep1.setBackgroundColor(getResources().getColor(R.color.high));
                sep2.setBackgroundColor(getResources().getColor(R.color.high));

                high.setBackgroundColor(getResources().getColor(R.color.high));
                high.setTextColor(Color.WHITE);
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
                priorityCHk ="3";

                GradientDrawable bgShape012 = (GradientDrawable) btnlay.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.medium));

                sep.setBackgroundColor(getResources().getColor(R.color.medium));
                sep1.setBackgroundColor(getResources().getColor(R.color.medium));
                sep2.setBackgroundColor(getResources().getColor(R.color.medium));

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
                priorityCHk ="4";

                GradientDrawable bgShape012 = (GradientDrawable) btnlay.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.low));

                sep.setBackgroundColor(getResources().getColor(R.color.low));
                sep1.setBackgroundColor(getResources().getColor(R.color.low));
                sep2.setBackgroundColor(getResources().getColor(R.color.low));

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

                GradientDrawable bgShape012 = (GradientDrawable) btnlay1.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.production));

                sepp.setBackgroundColor(getResources().getColor(R.color.production));
                sepp1.setBackgroundColor(getResources().getColor(R.color.production));

                User_acceptance.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                User_acceptance.setTextColor(Color.BLACK);
                GradientDrawable bgShape = (GradientDrawable) production.getBackground();
                bgShape.setColor(getResources().getColor(R.color.production));
                production.setTextColor(Color.WHITE);
                GradientDrawable bgShape1 = (GradientDrawable) development.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.colorWhite));
                development.setTextColor(Color.BLACK);
            }
        });
        User_acceptance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environmentclick="2";
                GradientDrawable bgShape012 = (GradientDrawable) btnlay1.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.acceptance));

                sepp.setBackgroundColor(getResources().getColor(R.color.acceptance));
                sepp1.setBackgroundColor(getResources().getColor(R.color.acceptance));

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

                GradientDrawable bgShape012 = (GradientDrawable) btnlay1.getBackground();
                bgShape012.setColor(getResources().getColor(R.color.colorWhite));
                bgShape012.setStroke(3,getResources().getColor(R.color.developement));
                sepp.setBackgroundColor(getResources().getColor(R.color.developement));
                sepp1.setBackgroundColor(getResources().getColor(R.color.developement));

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
       /* switch_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    snd_tickettypeid="2";
                } else {
                    snd_tickettypeid="1";
                }

            }


        });*/
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
                    .make(findViewById(R.id.container1), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
    private class getMemerbs extends AsyncTask<Void, Void, Void> {

        int siz;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CreateTicket.this);
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
                    siz=jsonarray1.length();
                    for (int i = 0; i < jsonarray1.length(); i++) {
                        JSONObject jsonobject = jsonarray1.getJSONObject(i);

                        String AssignedMem_id = jsonobject.getString("AssigneeId").trim();
                        String AssignedMem_name = jsonobject.getString("AsigneeName").trim();
                        member_assigned_bean item2=new member_assigned_bean();

                        item2.setId(AssignedMem_id);
                        item2.setName(AssignedMem_name);
                        CmemberpopList.add(item2);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    CreateTicket.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(CreateTicket.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                CreateTicket.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      /*  Toast.makeText(CreateTicket.this,
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

            if (siz==0){
                nomem.setVisibility(View.VISIBLE);
            }else {
                nomem.setVisibility(View.GONE);
            }
            Cmem_pop_Adapter addmemberadapter = new Cmem_pop_Adapter(CreateTicket.this, CmemberpopList,CreateTicket.this);
            listView.setAdapter(addmemberadapter);
            addmemberadapter.notifyDataSetChanged();

        }
    }
    public void memFromAdapter(){

        SharedPreferences setting =getApplicationContext(). getSharedPreferences(
                PREF_NAME, 0);

        String name = setting.getString("POPNAME", null);
        String id = setting.getString("POPID", null);
        member_assigned_bean memItem=new member_assigned_bean();

        memItem.setName(name);
        memItem.setId(id);
        CmermberList.add(memItem);
        CmemAdapter = new Create_asMemAdapter(CreateTicket.this, CmermberList);
        memberlistview1.setAdapter(CmemAdapter);
        setListViewHeightBasedOnChildren(memberlistview1);
        CmemAdapter.notifyDataSetChanged();
    }

    public void areaFromAdapter(){

        SharedPreferences setting =getApplicationContext(). getSharedPreferences(
                AREAPOPPRE, 0);

        String name = setting.getString("POPTITLE", null);
        String id = setting.getString("POPTICKETID", null);
        Areabean item=new Areabean();
        item.setTitle(name);
        item.setTickt_id(id);
        CareaList.add(item);
        CareAdapter = new Create_areaAdapter(CreateTicket.this, CareaList);
        areaListview1.setAdapter(CareAdapter);
        setListViewHeightBasedOnChildren(areaListview1);
        CareAdapter.notifyDataSetChanged();
    }

    public  void setListViewHeightBasedOnChildren(ListView listView) {
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
    private class getAreas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CreateTicket.this);
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
                        CareapopList.add(item);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    CreateTicket.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(CreateTicket.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                CreateTicket.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      /*  Toast.makeText(CreateTicket.this,
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

            CArea_pop_Adapter addareaadapter = new CArea_pop_Adapter(CreateTicket.this, CareapopList,CreateTicket.this);
            listView.setAdapter(addareaadapter);
            addareaadapter.notifyDataSetChanged();

        }
    }
    private Menu mOptionsMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu=menu;
        getMenuInflater().inflate(R.menu.menupro, menu);
        mOptionsMenu.getItem(0).setVisible(false);
        mOptionsMenu.getItem(1).setVisible(false);
        mOptionsMenu.getItem(2).setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==16908332){
            Intent intent=new Intent(CreateTicket.this,IssueListActivity.class);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.action_settings2){
            //update();
            String s="";
            String s1="";

            for (int i=0;i<CmermberList.size();i++){
                s= s +CmermberList.get(i).getId()+"|";
                System.out.println("members=:  "+s);

            }

            for (int i=0;i<CareaList.size();i++){
                s1=s1+CareaList.get(i).getTickt_id()+"|";
                System.out.println("areas=:  "+s1);
            }

            boolean z= validate();
            if (z==true){
                if (s.matches("")){
                    if (s.matches("")){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicket.this);
                        alertDialog.setMessage("Please assign atleast 1 member to the Ticket !");

                        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }

                }else {

                    Boolean checkinternet2= checkConnection();
                    if (checkinternet2==true){
                        CareaList.clear();
                        CmermberList.clear();
                        new UploadFileToServer(s,s1).execute();
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.container1), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }


                }

            }



       //     Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean  validate(){
        boolean validate=true;
        String Title=input_title.getText().toString().trim();
        String description=input_descr.getText().toString().trim();

        if (priorityCHk.matches("")){
            validate=false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicket.this);
            alertDialog.setMessage("Please select the priority !");

            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    dialog.cancel();
                }
            });
            alertDialog.show();
        }else if (environmentclick.matches("")){
            validate=false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicket.this);
            alertDialog.setMessage("Please select the enviornment !");

            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    dialog.cancel();
                }
            });
            alertDialog.show();
        }else if (Title.isEmpty()){
            validate=false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicket.this);
            alertDialog.setMessage("Please enter the title !");

            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    dialog.cancel();
                }
            });
            alertDialog.show();

        }else if (description.isEmpty()){
            validate=false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicket.this);
            alertDialog.setMessage("Please enter the description !");

            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    dialog.cancel();
                }
            });
            alertDialog.show();
        }else if (snd_tickettypeid.matches("")){
            validate=false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicket.this);
            alertDialog.setMessage("Please select the Ticket type !");

            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
        return validate;
    }
    private void Managebuttonclick() {
        if (ActivityCompat.checkSelfPermission(CreateTicket.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateTicket.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSION_REQUEST_CONTACTS);
        }else {
            reardcontacts();
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
    private void reardcontacts() {
        if (ActivityCompat.checkSelfPermission(CreateTicket.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateTicket.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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
    private void GALLERYBUTTONCLICK() {

        if (ActivityCompat.checkSelfPermission(CreateTicket.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateTicket.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {
            galleryIntent();
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
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            showFileChooser();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
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
        Uri tempUri = getImageUri(CreateTicket.this, thumbnail);

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
        docNamee.setText(destination.getName());
        bottomSheetDialog.dismiss();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                //      onSelectFromGalleryResult(data);
                onSelectFromGalleryResultGallery(data);
                bottomSheetDialog.dismiss();
            }else  if (requestCode == REQUEST_CAMERA) {
                // onCaptureImageResult(data);
                onCaptureImageResultGallery(data);
                bottomSheetDialog.dismiss();
            }
            if (requestCode == 1) {
                bottomSheetDialog.dismiss();
                Uri selectedFileURI = data.getData();
                File file = new File(selectedFileURI.getPath().toString());
                filePath=file.toString();
                Log.d("", "File : " + file.getName());
              //  StringTokenizer  tokens = new StringTokenizer(uploadedFileName, ":");
            //  String   first = tokens.nextToken();
           // String    file_1 = tokens.nextToken().trim();
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(CreateTicket.this.getContentResolver(), selectedFileURI);

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
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.container1), "Sorry! not a valid file format", Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                        document.setVisibility(View.GONE);
                        docNamee.setText("");
                    }else {
                        document.setVisibility(View.VISIBLE);
                        if (filePath.contains(".jpg")||filePath.contains(".jpeg")||filePath.contains(".png")){
                            document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            document.requestLayout();
                            document.getLayoutParams().height = 190;
                            document.setImageBitmap(thumbnail);
                            docNamee.setText(file.getName());
                        }else {
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.docs);
                            document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            document.requestLayout();
                            document.getLayoutParams().height = 190;
                            document.setImageBitmap(icon);
                            docNamee.setText(file.getName());

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
        Cursor cursor = CreateTicket.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void onSelectFromGalleryResultGallery(Intent data) {
        Uri uri = data.getData();
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(CreateTicket.this.getContentResolver(), uri);
            Uri tempUri = getImageUri(CreateTicket.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

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
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container1), "Sorry! not a valid file format", Snackbar.LENGTH_LONG);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
                document.setVisibility(View.GONE);
                docNamee.setText("");
            }else {
                document.setVisibility(View.VISIBLE);
                if (filePath.contains(".jpg")||filePath.contains(".jpeg")||filePath.contains(".png")){
                    document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    document.requestLayout();
                    document.getLayoutParams().height = 190;
                    document.setImageBitmap(thumbnail);
                    docNamee.setText(finalFile.getName());
                }else {
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.docs);
                    document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    document.requestLayout();
                    document.getLayoutParams().height = 190;
                    document.setImageBitmap(icon);
                    docNamee.setText(finalFile.getName());

                }
            }


            System.out.println("new : "+filePath);
        } catch (IOException e) {

            e.printStackTrace();
        }

        if ( validfile==false){
            bottomSheetDialog.dismiss();
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.container1), "Sorry! not a valid file format", Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }else {
            document.setScaleType(ImageView.ScaleType.CENTER_CROP);
            document.requestLayout();
            document.getLayoutParams().height = 190;
            document.setImageBitmap(thumbnail);
        }




        //thumbnail=g.getBm();

        //imagecapture.setImageBitmap(bm);
    }
    private  class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog dialog;
        String members ;
        String areas ;
        String Title;
        String description;
        String status="";
        public UploadFileToServer(String num1, String num2) {
            super();
            members=num1;
            areas=num2;
            // Do something with these parameters
        }
        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(CreateTicket.this);
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Title=input_title.getText().toString().trim();
            description=input_descr.getText().toString().trim();
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
                entity.addPart("Title", new StringBody(Title));
                entity.addPart("Description", new StringBody(description));
                entity.addPart("PriorityId", new StringBody(priorityCHk));
                entity.addPart("ProjectId", new StringBody(ProjectId));
                entity.addPart("EnvironmentId", new StringBody(environmentclick));
               // entity.addPart("TicketTypeId", new StringBody("1"));
                entity.addPart("Assigned", new StringBody(members));
                entity.addPart("Areas", new StringBody(areas));
                entity.addPart("LoggedInUser", new StringBody(loginuserid));
                entity.addPart("TicketTypeId", new StringBody(snd_tickettypeid));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    JSONObject data = new JSONObject(String.valueOf(responseString));
                    status=data.getString("Status");
//                    Toast.makeText(getActivity(),"Profile updated successfully",Toast.LENGTH_SHORT).show();
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            if (dialog.isShowing())
                dialog.dismiss();
            if (status.equals("0")){

                // badge count pref
                SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
                SharedPreferences.Editor editor = settings.edit();
                statusBadge++;
                editor.putInt("COUNT",statusBadge);
                editor.commit();
                qBadgeView.setVisibility(View.VISIBLE);
                qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);

                Toast.makeText(getApplicationContext(),"Ticket created successfully !",Toast.LENGTH_SHORT).show();
                //no ticket preff
                SharedPreferences settings1 = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
                SharedPreferences.Editor editor1 = settings1.edit();
                editor1.putString("TICKETCOUNT", "1");
                editor1.apply();
                Intent intent=new Intent(getApplicationContext(),IssueListActivity.class);
                startActivity(intent);
            }
            finish();
            // showing the server response in an alert dialog
            // showAlert(result);
            super.onPostExecute(result);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CareaList.size()>0){
            CareaList.clear();
        }
        if (CmermberList.size()>0){
            CmermberList.clear();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (CareaList.size()>0){
            CareaList.clear();
        }
        if (CmermberList.size()>0){
            CmermberList.clear();
        }
        Intent intent=new Intent(CreateTicket.this,IssueListActivity.class);
        startActivity(intent);
    }
}
