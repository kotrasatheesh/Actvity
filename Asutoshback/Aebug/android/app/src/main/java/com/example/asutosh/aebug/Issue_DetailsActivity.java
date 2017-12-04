package com.example.asutosh.aebug;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.asutosh.aebug.Adapter.GalleryAdapter;
import com.example.asutosh.aebug.Adapter.RecyclerView_Adapter;
import com.example.asutosh.aebug.Adapter.commentAdapter;
import com.example.asutosh.aebug.App_config.AppController;
import com.example.asutosh.aebug.bean.commentBean;
import com.example.asutosh.aebug.bean.recylcer_data_model;
import com.example.asutosh.aebug.fragments.SlideshowDialogFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.color.transparent;
import static com.android.volley.VolleyLog.TAG;
import static com.example.asutosh.aebug.App_config.AppController.RoleID;
import static com.example.asutosh.aebug.App_config.AppController.RoleName;
import static com.example.asutosh.aebug.App_config.AppController.TeamId;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.LoginActivity.PREF_NAME;

public class Issue_DetailsActivity extends AppCompatActivity {
    String canEdit="";
    private Menu mOptionsMenu;
    private ProgressBar mProgress1;
    RelativeLayout preload;
    JSONObject jsonBody;
    String ticketIdfromIssueList="";
    public static final String ISSUEtICKET = "myTicket";
    String TicketId="";
    String CommentedBy="";
    String ProfilePhoto="";
    static long totalSize = 0;
    public static final String MYCOMMENTSHARE = "Mycommnet" ;
    EditText commentEditText;
    TextView sendbtn,docs;
    commentAdapter commentAdapter;
    public static ListView CommnetListV;
    RecyclerView recyclerView;
    TextView T_id,T_type,T_date,Dpriority,Dstatus,Darea,issue_title,D_description;
    private ProgressDialog pDialog;
    RecyclerView_Adapter recyclerViewAdapter;
    int arryLenth;
    String EnteredBy="";
    final Context context=this;
    ViewGroup.LayoutParams params;
    RelativeLayout recyclerLay;
    RelativeLayout issueDetailLay;
    private ArrayList<recylcer_data_model> imageslide;
    List<recylcer_data_model> documentImg=new ArrayList<recylcer_data_model>();
    List<String> assignedmemberID=new ArrayList<String>();
    private List<commentBean> commnetList =new ArrayList<commentBean>();
    RelativeLayout nointernet_lay;
    Button retry;
    private ProgressBar emProgress;
    private AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ticket id

        final SharedPreferences settings =Issue_DetailsActivity.this.getSharedPreferences(ISSUEtICKET, 0);
        ticketIdfromIssueList = settings.getString("TICKETID", String.valueOf(ticketIdfromIssueList));
        System.out.println("ticketIdfromIssueList: "+ticketIdfromIssueList);

        setContentView(R.layout.activity_issue__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences settings1 = getSharedPreferences(PREF_NAME, 0);
        RoleID=settings1.getString("ROLLID", RoleID);
        RoleName=settings1.getString("ROLLNAME", RoleName);

        T_id=(TextView)findViewById(R.id.ticketID);
        T_type=(TextView)findViewById(R.id.TicketType);
        T_date=(TextView)findViewById(R.id.reported_date);
        issue_title=(TextView)findViewById(R.id.issue_title);
        D_description=(TextView)findViewById(R.id.D_description);
        Dpriority=(TextView)findViewById(R.id.Dpriority);
        Dstatus=(TextView)findViewById(R.id.Dstatus);
        Darea=(TextView)findViewById(R.id.Darea);
        CommnetListV = (ListView)findViewById(R.id.commentListview);
        commentEditText = (EditText) findViewById(R.id.addcomment);
        sendbtn = (TextView) findViewById(R.id.sendbtn);
        sendbtn.setTextColor(Color.GRAY);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String comment=commentEditText.getText().toString().trim();
                if (comment.equals("")){
                    //do nothing
                    sendbtn.setTextColor(Color.GRAY);
                }else {
                    sendbtn.setTextColor(getResources().getColor(R.color.blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerLay=(RelativeLayout) findViewById(R.id.recyLay);
        issueDetailLay=(RelativeLayout) findViewById(R.id.issueDetailLay);
        imageslide = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_loadImg);
        preload = (RelativeLayout) findViewById(R.id.preload);
        mProgress1 = (ProgressBar) findViewById(R.id.progressBar1);
        nointernet_lay = (RelativeLayout) findViewById(R.id.nointernet_lay);
        retry = (Button) findViewById(R.id.retry);
        emProgress = (ProgressBar)findViewById(R.id.eprogressBar);
        appbar = (AppBarLayout)findViewById(R.id.appbar);




        preload.setVisibility(View.VISIBLE);
        mProgress1.setVisibility(View.VISIBLE);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);


        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200,200);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerView_Adapter(Issue_DetailsActivity.this, documentImg,imageslide);
        recyclerView.setAdapter(recyclerViewAdapter);// set adapter on recyclerview
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (documentImg.get(position).getImageURl().contains(".docx")||documentImg.get(position).getImageURl().contains(".pdf")
                        ||documentImg.get(position).getImageURl().contains(".xls")||documentImg.get(position).getImageURl().contains(".pptx")
                        ||documentImg.get(position).getImageURl().contains(".xlsx")||documentImg.get(position).getImageURl().contains(".txt")){

                     Intent intent=new Intent(Issue_DetailsActivity.this,WebView_activity.class);
                     intent.putExtra("URL",documentImg.get(position).getImageURl().toString());
                     startActivity(intent);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", imageslide);
                    bundle.putInt("position", position);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }



            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    String comment=commentEditText.getText().toString().trim();
                    if (comment.equals("")){
                        //do nothing

                        System.out.println("hic");
                    }else {
                        new PostCommnet(comment).execute();
                        commentEditText.setText("");
                        emProgress.setVisibility(View.VISIBLE);
                    }

                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.issueDetailLay), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }



            }
        });
        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){
            new GetTicketDetails().execute();
            new GetComment().execute();
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
                    new GetTicketDetails().execute();
                    new GetComment().execute();
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
                    .make(findViewById(R.id.issueDetailLay), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
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
                entity.addPart("TicketId", new StringBody(TicketId));
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

            commentEditText.setText("");
            commnetList.clear();
            new GetComment().execute();
            super.onPostExecute(result);
        }

    }



    private class GetTicketDetails extends AsyncTask<Void, Void, Void> {

        String Title="";
        String Description="";
        String TicketTypeId="";
        String PriorityId="";
        String StatusId="";
        String ProjectId="";

        String Assigned="";
        String StatusName="";
        String ProjectName="";
        String PriorityName="";
        String TicketType="";
        String EnteredOn="";
        String Areas="";
        String file="";
        String LoggedInUser="";
        String AreasIds="";
        String AssignedToIds="";
        String EnvironmentId="";
        String EnvironmentName="";
        String AttachmentId = "";
        String FileName = "";
        String FilePath = "";
        String AssigneeId = "";
        String AsigneeName ="";
        String CreatedOn ="";
        String AreaId ="";
        String AreaName ="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Issue_DetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetTicketById+ticketIdfromIssueList);

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
                        TicketId=obj.getString("TicketId");
                        Title =obj.getString("Title");
                        Description =obj.getString("Description");
                        TicketTypeId =obj.getString("TicketTypeId");
                        PriorityId =obj.getString("PriorityId");
                        StatusId =obj.getString("StatusId");
                        ProjectId =obj.getString("ProjectId");
                        EnteredBy =obj.getString("EnteredBy");
                        Assigned =obj.getString("Assigned");
                        StatusName =obj.getString("StatusName");
                        PriorityName =obj.getString("PriorityName");
                        ProjectName =obj.getString("ProjectName");
                        TicketType =obj.getString("TicketType");
                        EnteredOn =obj.getString("EnteredOn");
                        Areas =obj.getString("Areas");
                        file =obj.getString("file");
                        LoggedInUser =obj.getString("LoggedInUser");
                        AreasIds =obj.getString("AreasIds");
                        AssignedToIds =obj.getString("AssignedToIds");
                        EnvironmentId =obj.getString("EnvironmentId");
                        EnvironmentName =obj.getString("EnvironmentName");
                        CreatedOn =obj.getString("CreatedOn");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int count = 0;
                    arryLenth = attachment_obj.length();
                    for (int i = 0; i < arryLenth; i++) {
                        JSONObject jsonObject1 = (JSONObject) attachment_obj.get(i);
                         AttachmentId = jsonObject1.getString("AttachmentId");
                         FileName = jsonObject1.getString("FileName");
                         FilePath = jsonObject1.optString("FilePath");

                        recylcer_data_model item= new recylcer_data_model();
                        item.setImageURl(FilePath);
                        recylcer_data_model image= new recylcer_data_model();
                        image.setImageslide(FilePath);
                        imageslide.add(image);
                        documentImg.add(item);

                    }
                    for (int i = 0; i < assignee_obj.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) assignee_obj.get(i);

                        AssigneeId = jsonObject1.getString("AssigneeId");
                        AsigneeName = jsonObject1.getString("AsigneeName");
                        assignedmemberID.add(AssigneeId);
                    }
                    for (int i = 0; i < area_obj.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) area_obj.get(i);

                        AreaId = jsonObject1.getString("AreaId");
                        AreaName = jsonObject1.getString("AreaName");

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Issue_DetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* Toast.makeText(Issue_DetailsActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Issue_DetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Toast.makeText(Issue_DetailsActivity.this,
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
            T_id.setText(TicketId);
            T_type.setText(TicketType);
            T_date.setText(EnteredOn);
            Dpriority.setText(PriorityName);
            Dstatus.setText(StatusName);
            issue_title.setText(Title);
            D_description.setText(Description);
            Darea.setText(EnvironmentName);
            for (int i=0;i<assignedmemberID.size();i++){
                if (assignedmemberID.get(i).toString().equals(TeamId)){
                    System.out.println("print match:"+assignedmemberID.get(i).toString());
                    canEdit="true";
                }
            }
            if (EnteredBy.equals(loginuserid)||RoleID.equals("1")|| canEdit=="true"){
                mOptionsMenu.getItem(0).setVisible(true);
                mOptionsMenu.getItem(1).setVisible(false);
                mOptionsMenu.getItem(2).setVisible(false);
            }
            if (PriorityId.equalsIgnoreCase("1")){
                GradientDrawable bgShape = (GradientDrawable) Dpriority.getBackground();
                bgShape.setColor(getResources().getColor(R.color.urgent));
                Dpriority.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));

            }else if (PriorityId.equalsIgnoreCase("2")){
                GradientDrawable bgShape = (GradientDrawable) Dpriority.getBackground();
                bgShape.setColor(getResources().getColor(R.color.high));
                Dpriority.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (PriorityId.equalsIgnoreCase("3")){
                GradientDrawable bgShape = (GradientDrawable) Dpriority.getBackground();
                bgShape.setColor(getResources().getColor(R.color.medium));
                Dpriority.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (PriorityId.equalsIgnoreCase("4")){
                GradientDrawable bgShape = (GradientDrawable) Dpriority.getBackground();
                bgShape.setColor(getResources().getColor(R.color.low));
                Dpriority.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else {
                GradientDrawable bgShape = (GradientDrawable) Dpriority.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                Dpriority.setTextColor(Color.BLACK);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }

            if (StatusId.equals("1")){
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.i_pending));
                Dstatus.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (StatusId.equals("2")){
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.i_started));
                Dstatus.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (StatusId.equals("3")){
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.fixed));
                Dstatus.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (StatusId.equals("4")){
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.closed));
                Dstatus.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (StatusId.equals("5")){
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.i_reopend));
                Dstatus.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (StatusId.equals("6")){
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.deferred));
                Dstatus.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }
            else {
                GradientDrawable bgShape = (GradientDrawable) Dstatus.getBackground();
                bgShape.setColor(getResources().getColor(R.color.colorWhite));
                Dstatus.setTextColor(Color.BLACK);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }
            if (EnvironmentId.equals("1")){
                GradientDrawable bgShape = (GradientDrawable) Darea.getBackground();
                bgShape.setColor(getResources().getColor(R.color.production));
                Darea.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (EnvironmentId.equals("2")){
                GradientDrawable bgShape = (GradientDrawable) Darea.getBackground();
                bgShape.setColor(getResources().getColor(R.color.acceptance));
                Darea.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }else if (EnvironmentId.equals("3")){
                GradientDrawable bgShape = (GradientDrawable) Darea.getBackground();
                bgShape.setColor(getResources().getColor(R.color.developement));
                Darea.setTextColor(Color.WHITE);
                bgShape.setStroke(2,getResources().getColor(transparent));
            }
            recyclerViewAdapter = new RecyclerView_Adapter(Issue_DetailsActivity.this, documentImg,imageslide);
            recyclerView.setAdapter(recyclerViewAdapter);// set adapter on recyclerview
            recyclerViewAdapter.notifyDataSetChanged();
            SharedPreferences setting = Issue_DetailsActivity.this.getSharedPreferences(
                    MYCOMMENTSHARE, 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("TICKETID", TicketId);
            editor.putString("TITLE", Title);
            editor.putString("DESCRIPTION", Description);
            editor.putString("PRIORITYID", PriorityId);
            editor.putString("STATUSID", StatusId);
            editor.putString("PROJECTID", ProjectId);
            editor.putString("ENVIRONMENTID", EnvironmentId);
            editor.putString("TICKETTYPEID", TicketTypeId);
            editor.putString("TICKETTYPE", TicketType);
            editor.putString("PROJECTID", ProjectId);
            editor.commit();
            System.out.println("TicketId: "+TicketId);
            System.out.println("Title: "+Title);
            System.out.println("Description: "+Description);
            System.out.println("PriorityId: "+PriorityId);
            System.out.println("StatusId: "+StatusId);
            System.out.println("ProjectId: "+ProjectId);
            System.out.println("EnvironmentId: "+EnvironmentId);
            System.out.println("TicketTypeId: "+TicketTypeId);
            preload.setVisibility(View.GONE);
            mProgress1.setVisibility(View.GONE);
            /**
             * Updating parsed JSON data into ListView
             * */
        }
    }

    public void cllgetComment(String string){
        String s=string;
        System.out.println("Commnetid:  "+s);
        DeleteComment(s);
    }
    private void DeleteComment(String commentID) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("Id", commentID);
                jsonBody.put("loggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.DeleteComment, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String Message=data.getString("status");
                                if (Message.matches("0")){
                                    Toast.makeText(getApplicationContext(),"Comment deleted !",Toast.LENGTH_SHORT).show();
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
    private class GetComment extends AsyncTask<Void, Void, Void> {

        String CommentId="";
        String TicketId="";
        String CommentText="";
        String CommentedOn="";
        String CommentedById="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Issue_DetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppUtils.GetComment+ticketIdfromIssueList);

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
                    int count=0;
                    arryLenth=obj.length();
                    for (int i = 0; i < arryLenth; i++) {
                        JSONObject jsonObject1 = (JSONObject) obj.get(i);

                        CommentId=jsonObject1.getString("CommentId").trim();
                        TicketId=jsonObject1.getString("TicketId").trim();
                        CommentText=jsonObject1.optString("CommentText").trim();
                        CommentedBy = jsonObject1.optString("CommentedBy").trim();
                        CommentedOn = jsonObject1.getString("CommentedOn").trim();
                        ProfilePhoto = jsonObject1.getString("ProfilePhoto").trim();
                        CommentedById = jsonObject1.getString("CommentedById").trim();
                        // tmp hash map for single contact
                        commentBean item = new commentBean();
                        item.setCommentId(CommentId);
                        item.setTicketId(TicketId);
                        item.setCommentText(CommentText);
                        item.setCommentedBy(CommentedBy);
                        item.setCommentedOn(CommentedOn);
                        item.setProfilePhoto(ProfilePhoto);
                        item.setCommentedById(CommentedById);
                        commnetList.add(item);

                        // setListViewHeightBasedOnChildren(listView);
//                        adapter.notifyDataSetChanged();

                        // adding contact to contact list
                        //frommelist.add(transitem);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Issue_DetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  Toast.makeText(Issue_DetailsActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Issue_DetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      /*  Toast.makeText(Issue_DetailsActivity.this,
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
            nointernet_lay.setVisibility(View.GONE);
            commentAdapter = new commentAdapter(Issue_DetailsActivity.this,Issue_DetailsActivity.this, commnetList);
            CommnetListV.setAdapter(commentAdapter);

            setListViewHeightBasedOnChildren(CommnetListV);
            commentAdapter.notifyDataSetChanged();
            SharedPreferences setting = Issue_DetailsActivity.this.getSharedPreferences(
                    MYCOMMENTSHARE, 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("PROFILEPHOTO", ProfilePhoto);
            editor.putString("NAME", CommentedBy);
            editor.commit();
        }

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu=menu;
        getMenuInflater().inflate(R.menu.menupro, menu);
        mOptionsMenu.getItem(0).setVisible(false);
        mOptionsMenu.getItem(1).setVisible(false);
        mOptionsMenu.getItem(2).setVisible(false);
                return true;
            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId() == R.id.action_settings){

            for (int i=0;i<assignedmemberID.size();i++){
                if (assignedmemberID.get(i).toString().equals(TeamId)){
                    System.out.println("print match:"+assignedmemberID.get(i).toString());
                    canEdit="true";
                }
            }
            if (EnteredBy.equals(loginuserid)||RoleID.equals("1")|| canEdit=="true"){
                Intent intent=new Intent(getApplicationContext(),Edit_issue_details.class);
                startActivity(intent);
            }

        }


        return super.onOptionsItemSelected(item);
    }

}
