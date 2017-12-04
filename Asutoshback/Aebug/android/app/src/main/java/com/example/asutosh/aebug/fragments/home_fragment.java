package com.example.asutosh.aebug.fragments;

/**
 * Created by Asutosh on 22-08-2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asutosh.aebug.Adapter.projectAdapter;
import com.example.asutosh.aebug.AppUtils;
import com.example.asutosh.aebug.App_config.AppController;
import com.example.asutosh.aebug.ClickListener;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.Edit_issue_details;
import com.example.asutosh.aebug.HttpHandler;
import com.example.asutosh.aebug.MainActivity;
import com.example.asutosh.aebug.PaginationScrollListener;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.projectbean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;

import static com.android.volley.VolleyLog.TAG;
import static com.example.asutosh.aebug.Adapter.projectAdapter.prog;
import static com.example.asutosh.aebug.App_config.AppController.RoleID;
import static com.example.asutosh.aebug.App_config.AppController.RoleName;
import static com.example.asutosh.aebug.App_config.AppController.TeamId;

import static com.example.asutosh.aebug.App_config.AppController.homeback;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.statusBadge;
import static com.example.asutosh.aebug.App_config.AppController.userEmail;
import static com.example.asutosh.aebug.App_config.AppController.userName;
import static com.example.asutosh.aebug.LoginActivity.PREF_NAME;
import static com.example.asutosh.aebug.MainActivity.qBadgeView;
import static com.example.asutosh.aebug.MainActivity.v;

public class home_fragment extends Fragment implements ClickListener {
    RelativeLayout nointernet_lay;
    Button retry;
    private ProgressBar emProgress;

    private ProgressDialog pDialog;
    private ProgressDialog HpDialog;
    RecyclerView recyclerView;
    private List<projectbean> projectlist = new ArrayList<>();
    FloatingActionButton addproject;
    projectAdapter adapter;
    EditText searchproject;
    JSONObject jsonBody;
    int arryLenth;
    String loginUserID="";
    String RoleID="";
    String RoleName="";
    int index=1;
    boolean scrol=true;
    int postion;
    LinearLayoutManager llm;
    public static final String STATUS_PRE = "status";
    private SwipeRefreshLayout swipeContainer;

    public static home_fragment newInstance() {
        home_fragment fragment = new home_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences settings = getActivity().getSharedPreferences(PREF_NAME, 0);
        loginUserID=settings.getString("USERID", loginuserid);
        RoleID=settings.getString("ROLLID", RoleID);
        RoleName=settings.getString("ROLLNAME", RoleName);
        View rootView=inflater.inflate(R.layout.home_frag, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
        //setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //get the actionbar

        searchproject=(EditText)rootView.findViewById(R.id.searchpro);
        EditText yourEditText= (EditText) rootView.findViewById(R.id.searchpro);
        nointernet_lay = (RelativeLayout) rootView.findViewById(R.id.nointernet_lay);
        retry = (Button)rootView.findViewById(R.id.retry);
        emProgress = (ProgressBar)rootView.findViewById(R.id.eprogressBar);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        addproject=(FloatingActionButton) rootView.findViewById(R.id.addproject);
        if (RoleID.equals("1")){
            addproject.setVisibility(View.VISIBLE);
        }else {
            addproject.setVisibility(View.GONE);
        }



         addproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hi btn");
                final Dialog addDialog = new Dialog(getActivity());
                addDialog.setContentView(R.layout.add_project_popup);
                Button save=(Button)addDialog.findViewById(R.id.save_pro);
                final TextView pro_name=(TextView)addDialog.findViewById(R.id.pro_name);
                final TextView pro_description=(TextView)addDialog.findViewById(R.id.pro_description);
                Button cancel=(Button)addDialog.findViewById(R.id.cancel_pro);
                final TextView errorMsg=(TextView) addDialog.findViewById(R.id.errorMsg);

                GradientDrawable bgShape = (GradientDrawable) save.getBackground();
                bgShape.setColor(getResources().getColor(R.color.save));
                save.setTextColor(Color.WHITE);
                GradientDrawable bgShape1 = (GradientDrawable) cancel.getBackground();
                bgShape1.setColor(getResources().getColor(R.color.cancel));
                cancel.setTextColor(Color.WHITE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(v);
                        addDialog.dismiss();
                    }
                });
                pro_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        errorMsg.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String title=pro_name.getText().toString().trim();
                        final String descr=pro_description.getText().toString().trim();
                        boolean addp=true;
                        if (title.isEmpty()){
                            errorMsg.setText("Please Enter the Project Name !");
                            errorMsg.setVisibility(View.VISIBLE);
                        }else {

                            for (int i=0;i<projectlist.size();i++){
                                if (projectlist.get(i).getName().equalsIgnoreCase(title)){
                                    addp=false;
                                    break;
                                }else {
                                    addp=true;
                                }
                            }
                            if (addp==false){
                                errorMsg.setText(title+" already Exists !");
                                errorMsg.setVisibility(View.VISIBLE);
                            }else {
                                Boolean checkinternet2= checkConnection();
                                if (checkinternet2==true){
                                    hideKeyboard(v);
                                    errorMsg.setVisibility(View.GONE);
                                    addProjectPOST(title,descr);
                                    addDialog.dismiss();
                                }else {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.container), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.RED);
                                    snackbar.show();
                                }
                            }

                        }

                    }
                });
                addDialog.setCanceledOnTouchOutside(false);
                addDialog.show();
            }
        });
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapse_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout)rootView.findViewById(R.id.appbar);
        collapsingToolbarLayout.setTitle("Projects");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Projects");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("Projects");
                    //carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        recyclerView=(RecyclerView)rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

              //  recyclerView.setVisibility(View.GONE);
                postion=0;
                llm.scrollToPositionWithOffset(postion, postion);
                Boolean checkinternet1= checkConnection();
                if (checkinternet1==true){

                   // recyclerView.setVisibility(View.VISIBLE);
                   /* getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
                    projectlist.clear();
                    index=1;
                    nointernet_lay.setVisibility(View.GONE);
                    emProgress.setVisibility(View.GONE);
                    new GetProjects(index).execute();
                }else {
                    emProgress.setVisibility(View.GONE);
                    nointernet_lay.setVisibility(View.VISIBLE);
                  //  recyclerView.setVisibility(View.GONE);

                }
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean checkinternet2= checkConnection();
                        if (checkinternet2==true){
                            new GetProjects(index).execute();
                            emProgress.setVisibility(View.VISIBLE);
                   //         recyclerView.setVisibility(View.VISIBLE);
                        }else {
                            emProgress.setVisibility(View.VISIBLE);
                            onIntent();
                            nointernet_lay.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });


        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){

            nointernet_lay.setVisibility(View.GONE);
            emProgress.setVisibility(View.GONE);
            new GetProjects(index).execute();
        }else {
            emProgress.setVisibility(View.GONE);
            nointernet_lay.setVisibility(View.VISIBLE);

        }
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    new GetProjects(index).execute();
                    emProgress.setVisibility(View.VISIBLE);
                }else {
                    emProgress.setVisibility(View.VISIBLE);
                    onIntent();
                    nointernet_lay.setVisibility(View.VISIBLE);
                }
            }
        });


        //createList();
        adapter= new projectAdapter(home_fragment.this,getActivity(),getActivity(),projectlist,RoleID);
        adapter.setClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = llm.findLastVisibleItemPosition();
                postion=adapter.getItemCount()-5;
                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    if (scrol==true){
                        index=index+1;
                        new GetProjects(index).execute();
                    }else {

                    }
                }
            }
        });
        searchproject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    searchproject.setCursorVisible(true);
                }
            }
        });
        searchproject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
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

    public void callFromProjectAdapter(String id){
        DeleteProject(id);
    }
    private void DeleteProject(String projectID) {
        index=1;
        final ProgressDialog   pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("projectId", projectID);
                jsonBody.put("loggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.DeleteProject, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String Message=data.getString("status");
                                if (Message.matches("0")){
                                    SharedPreferences settings = getActivity().getSharedPreferences(STATUS_PRE, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    statusBadge++;
                                    editor.putInt("COUNT",statusBadge);
                                    editor.commit();
                                    //   new QBadgeView(getActivity()).bindTarget(v).setBadgeNumber(statusBadge);
                                    qBadgeView.setVisibility(View.VISIBLE);
                                    qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
                                    projectlist.clear();
                                    new GetProjects(index).execute();
                                    Toast.makeText(getActivity(),"Project deleted successfully !",Toast.LENGTH_SHORT).show();
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
    private void addProjectPOST(String title,String description) {
        index=1;
        HpDialog = new ProgressDialog(getActivity());
        HpDialog.setMessage("Please wait...");
        HpDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("ProjectName", title);
                jsonBody.put("Description", description);
                jsonBody.put("LoggedInUser", loginuserid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.CreateProject, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String status=data.getString("Status");


                                if (status.matches("0")){
                                    SharedPreferences settings = getActivity().getSharedPreferences(STATUS_PRE, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    statusBadge++;
                                    editor.putInt("COUNT",statusBadge);
                                    editor.commit();
                                 //   new QBadgeView(getActivity()).bindTarget(v).setBadgeNumber(statusBadge);
                                    qBadgeView.setVisibility(View.VISIBLE);
                                    qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
                                    projectlist.clear();
                                    HpDialog.hide();
                                    new GetProjects(index).execute();
                                    Toast.makeText(getActivity(),"Project Added successfully !",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(),"Some error occured while Adding project !",Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            HpDialog.hide();
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    HpDialog.hide();
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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private class GetProjects extends AsyncTask<Void, Void, Void> {

        int index1;
        public  GetProjects(int index){
            index1=index;
        }

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
            String jsonStr = sh.makeServiceCall(AppUtils.GetProjectList+loginuserid+"&pageIndex="+index+"&pageSize=15");
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
                    if (arryLenth==0){
                        scrol=false;
                    }
                    for (int i = 0; i < arryLenth; i++) {
                        JSONObject jsonObject1 = (JSONObject) obj.get(i);

                        String p_name=jsonObject1.getString("ProjectName").trim();
                        String projectId=jsonObject1.getString("ProjectId").trim();
                        String ticketCount=jsonObject1.optString("TicketCount").trim();
                        String teamCount = jsonObject1.optString("TeamCount").trim();
                        String ownerName = jsonObject1.getString("OwnerName").trim();
                        String description = jsonObject1.getString("Description").trim();
                        // tmp hash map for single contact
                        projectbean item = new projectbean();
                        item.setName(p_name);
                        item.setProjectId(projectId);
                        item.setTicketCount(ticketCount);
                        item.setTeamCount(teamCount);
                        item.setOnwerName(ownerName);
                        item.setDescription(description);
                        projectlist.add(item);

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
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            if (swipeContainer.isRefreshing()){
                swipeContainer.setRefreshing(false);
            }
            adapter= new projectAdapter(home_fragment.this,getActivity(),getActivity(),projectlist,RoleID);
            llm.scrollToPositionWithOffset(postion, postion);
            recyclerView.setAdapter(adapter);
            nointernet_lay.setVisibility(View.GONE);
        }
    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menupro, menu);
    }*/

    @Override
    public void itemClicked(View view, int position) {
       // Toast.makeText(getActivity(),"hi"+position,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroyView()
    {

        super.onDestroyView();
       // Toast.makeText(getActivity().getApplicationContext(),"hi des",Toast.LENGTH_SHORT).show();
        projectlist.clear();
        homeback=0;
//        pDialog.hide();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (homeback==1){

            index=1;
            postion=0;
            llm.scrollToPositionWithOffset(postion, postion);
            Boolean checkinternet3= checkConnection();
            if (checkinternet3==true){
                projectlist.clear();
                nointernet_lay.setVisibility(View.GONE);
                emProgress.setVisibility(View.GONE);

                new GetProjects(index).execute();
                adapter.notifyDataSetChanged();

            }else {
                emProgress.setVisibility(View.GONE);
                nointernet_lay.setVisibility(View.VISIBLE);

            }



            //Toast.makeText(getActivity().getApplicationContext(),"hi yest",Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(getActivity().getApplicationContext(),"hi resume",Toast.LENGTH_SHORT).show();
    }
}
