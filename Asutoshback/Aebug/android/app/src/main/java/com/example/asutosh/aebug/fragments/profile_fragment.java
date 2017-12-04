package com.example.asutosh.aebug.fragments;

/**
 * Created by Asutosh on 22-08-2017.
 */

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
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.asutosh.aebug.AndroidMultiPartEntity;
import com.example.asutosh.aebug.AppUtils;
import com.example.asutosh.aebug.App_config.AppController;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.Edit_issue_details;
import com.example.asutosh.aebug.HttpHandler;
import com.example.asutosh.aebug.Issue_DetailsActivity;
import com.example.asutosh.aebug.LoginActivity;
import com.example.asutosh.aebug.MainActivity;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.projectbean;
import com.example.asutosh.aebug.bean.recylcer_data_model;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.VolleyLog.TAG;
import static com.example.asutosh.aebug.AppUtils.ProfileUpload;
//import static com.example.asutosh.aebug.App_config.AppController.homeback;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.userEmail;
import static com.example.asutosh.aebug.App_config.AppController.userName;
import static com.example.asutosh.aebug.LoginActivity.PREF_NAME;

public class profile_fragment extends Fragment {

    RelativeLayout nointernet_lay;
    Button retry;
    private ProgressBar emProgress;

    private Menu mOptionsMenu;
    static String previewProfile="";
    ImageButton changepic,editProfilebtn,editDone;
    String canEdit="";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    Toolbar toolbar;
    File destination;
    String pic="0";
    File finalFile;
    JSONObject jsonBody;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CircleImageView profilepic;
    EditText fname,lname;
    TextView changepass;
    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    long totalSize = 0;
    int arryLenth;
    Bitmap photo = null;
    Bitmap thumbnail;
    Button save;
    private Uri fileUri;
    private String filePath = null;
    private static final int MY_PERMISSION_REQUEST_CONTACTS = 10;
    public static profile_fragment newInstance() {
        profile_fragment fragment = new profile_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_frag, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");




        changepic=(ImageButton)rootView.findViewById(R.id.fabButton);
        editProfilebtn=(ImageButton)rootView.findViewById(R.id.editprof);
        editDone=(ImageButton)rootView.findViewById(R.id.editDone);
        profilepic=(CircleImageView)rootView.findViewById(R.id.profile_image);
        nointernet_lay = (RelativeLayout) rootView.findViewById(R.id.nointernet_lay);
        retry = (Button)rootView.findViewById(R.id.retry);
        emProgress = (ProgressBar)rootView.findViewById(R.id.eprogressBar);
        //homeback=0;
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", previewProfile);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                PreviewFrag newFragment = new PreviewFrag();
                newFragment.show(ft, "slideshow");
            }
        });
        fname=(EditText)rootView.findViewById(R.id.fname);
        lname=(EditText)rootView.findViewById(R.id.lname);
        changepass=(TextView)rootView.findViewById(R.id.changepas);

        fname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        Boolean checkinternet1= checkConnection();
        if (checkinternet1==true){

            nointernet_lay.setVisibility(View.GONE);
            emProgress.setVisibility(View.GONE);
            new GetContacts().execute();
        }else {
            emProgress.setVisibility(View.GONE);
            nointernet_lay.setVisibility(View.VISIBLE);

        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){
                    new GetContacts().execute();
                    emProgress.setVisibility(View.VISIBLE);
                    getActivity().getMenuInflater().inflate(R.menu.menu_signout, mOptionsMenu);
                }else {
                    emProgress.setVisibility(View.VISIBLE);
                    onIntent();
                    nointernet_lay.setVisibility(View.VISIBLE);
                }
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog4 = new Dialog(getActivity());
                dialog4.setContentView(R.layout.change_pass_pop);
                dialog4.show();
                final EditText pas=(EditText)dialog4.findViewById(R.id.password);
                final EditText cpas=(EditText)dialog4.findViewById(R.id.confiromPass);
                final TextView errormsg=(TextView)dialog4.findViewById(R.id.errormsg);
                Button submtPass=(Button)dialog4.findViewById(R.id.submtPass);
                submtPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pass=pas.getText().toString().trim();
                        String cpass=cpas.getText().toString().trim();

                        pas.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                errormsg.setVisibility(View.GONE);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        cpas.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                errormsg.setVisibility(View.GONE);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        if (pass.isEmpty()||pass.length()<4){
                            if (pass.isEmpty()){
                                errormsg.setText("Enter a new password !");
                                errormsg.setVisibility(View.VISIBLE);
                            }else {
                                errormsg.setText("Password must be greatere than 4 characters !");
                                errormsg.setVisibility(View.VISIBLE);
                            }

                        }else if (cpass.isEmpty()){
                            errormsg.setText("Confirm the password !");
                            errormsg.setVisibility(View.VISIBLE);
                        }else if (pass.matches(cpass)){
                            errormsg.setVisibility(View.GONE);

                            Boolean checkinternet2= checkConnection();
                            if (checkinternet2==true){

                                Postpassword(cpass);
                                dialog4.dismiss();
                            }else {
                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.container), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.RED);
                                snackbar.show();
                            }



                        }else {
                            errormsg.setText("Password doesn't match !");
                            errormsg.setVisibility(View.VISIBLE);
                        }
                        /*if (pass.matches(cpass)&&pass!=""){
                            errormsg.setVisibility(View.INVISIBLE);
                            dialog4.dismiss();
                            Postpassword(cpass);
                        }else {
                            errormsg.setVisibility(View.VISIBLE);
                        }*/
                    }
                });

            }
        });
        save=(Button)rootView.findViewById(R.id.save_prof);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkinternet2= checkConnection();
                if (checkinternet2==true){

                    new UploadFileToServer().execute();
                    editProfilebtn.setVisibility(View.VISIBLE);
                    changepic.setVisibility(View.INVISIBLE);
                    fname.setEnabled(false);
                    lname.setEnabled(false);
                    editDone.setVisibility(View.INVISIBLE);
                }else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.container), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }


            }
        });
        editProfilebtn.setVisibility(View.VISIBLE);
        changepic.setVisibility(View.INVISIBLE);
        fname.setEnabled(false);
        lname.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        editDone.setVisibility(View.INVISIBLE);
        editProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canEdit="true";
                changepic.setVisibility(View.VISIBLE);
                fname.setEnabled(true);
                lname.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                editDone.setVisibility(View.VISIBLE);
                checkVisibility();
            }
        });
        editDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canEdit="false";
                Boolean checkinternet1= checkConnection();
                if (checkinternet1==true){

                    nointernet_lay.setVisibility(View.GONE);
                    emProgress.setVisibility(View.GONE);
                    new GetContacts().execute();
                }else {
                    emProgress.setVisibility(View.GONE);
                    nointernet_lay.setVisibility(View.VISIBLE);

                }

                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean checkinternet2= checkConnection();
                        if (checkinternet2==true){
                            new GetContacts().execute();
                            emProgress.setVisibility(View.VISIBLE);
                            getActivity().getMenuInflater().inflate(R.menu.menu_signout, mOptionsMenu);
                        }else {
                            emProgress.setVisibility(View.VISIBLE);
                            onIntent();
                            nointernet_lay.setVisibility(View.VISIBLE);
                        }
                    }
                });
                editProfilebtn.setVisibility(View.GONE);
                changepic.setVisibility(View.INVISIBLE);
                fname.setEnabled(false);
                lname.setEnabled(false);
                save.setVisibility(View.INVISIBLE);
                editDone.setVisibility(View.INVISIBLE);
                checkVisibility();
            }
        });

        GradientDrawable bgShape = (GradientDrawable)changepic.getBackground();
        bgShape.setColor(Color.parseColor("#5c97bd"));
        bgShape.setStroke(1,Color.parseColor("#5c97bd"));
        changepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
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
    private void checkVisibility() {

        if (canEdit.equals("true")){
            editProfilebtn.setVisibility(View.GONE);
            editDone.setVisibility(View.VISIBLE);
        }
        else if (canEdit.equals("false")){
            editProfilebtn.setVisibility(View.VISIBLE);
            editDone.setVisibility(View.GONE);
        }
    }
    public static class PreviewFrag extends DialogFragment {
        private String TAG = PreviewFrag.class.getSimpleName();
        private ArrayList<recylcer_data_model> images;
        private ImageView imageView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fullimage, container, false);
            imageView = (ImageView) v.findViewById(R.id.full);

            if (previewProfile.equals("")){
                Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.mipmap.default_user_icon);
                imageView.setImageBitmap(Icon);
            }else {
                Picasso.with(getActivity()).invalidate("");
                Picasso.with(getActivity())
                        .load(previewProfile)
                        .placeholder(R.mipmap.default_user_icon)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .fit()
                        .into(imageView);
            }

            return v;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        }
    }

    private void Postpassword(String password) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("loggedInUser", loginuserid);
                jsonBody.put("Password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.UpdatePassword, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String status=data.getString("Status");


                                if (status.matches("0")){

                                    Toast.makeText(getActivity(),"Password Updated !",Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            //   System.out.println("id..." + loginuserid);
                            pDialog.hide();

                        /*    SharedPreferences setting = getApplicationContext().getSharedPreferences(
                                    MyPREFERENCES, 0);
                            SharedPreferences.Editor editor = setting.edit();
                            editor.putString("wid", String.valueOf(loginuserid));
                            // editor.putString("ptus", String.valueOf(pstatus));
                            editor.commit();*/
                            // System.out.println("id..." + Wo);

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
    private void select() {
        final CharSequence[] items = {"Take Photo", "Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Managebuttonclick();
                } else if (items[item].equals("Gallery")) {
                    // boolean result = Utility.checkPermission(Record_Dailies.this);
                    GALLERYBUTTONCLICK();
                /*    userChoosenTask = "Gallery";
                    if (result)
                        galleryIntent();*/

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void GALLERYBUTTONCLICK() {
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {
            galleryIntent();
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
            else if (requestCode == REQUEST_CAMERA) {
                // onCaptureImageResult(data);
                onCaptureImageResultGallery(data);
            }
        }
    }//have to chec

    private void onCaptureImageResultGallery(Intent data) {
        thumbnail = (Bitmap)data.getExtras().get("data");
        // filePath = data.getStringExtra("data");
        //   filePath=BitMapToString(thumbnail);
        //  filePath=fileUri.getPath();
        /*Bitmap displayImage = null;
        byte[] image=getBytes(thumbnail);
        database.addEntry(image);
        System.out.println("image------"+database);*/
        Uri tempUri = getImageUri(getActivity(), thumbnail);

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
        profilepic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        profilepic.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResultGallery(Intent data) {
        Uri uri = data.getData();
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            Uri tempUri = getImageUri(getActivity(), thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));
            filePath=getRealPathFromURI(tempUri);

        } catch (IOException e) {

            e.printStackTrace();
        }

        //create imageview here and setbg
        profilepic.setImageBitmap(thumbnail);
        //thumbnail=g.getBm();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void galleryIntent() {
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }

    }

    private void Managebuttonclick() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},MY_PERMISSION_REQUEST_CONTACTS);
        }else {
            reardcontacts();
        }

    }

    private void reardcontacts() {
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        String profilePhoto="";
        String firstName="";
        String lastName="";
        String email="";
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
            String jsonStr = sh.makeServiceCall(AppUtils.UserProfile+loginuserid);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                  //  JSONObject emp=(new JSONObject(jsonStr)).getJSONObject("data");
                   /* // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");*/

                    // looping through All Contacts

                    JSONObject obj = null;

                    try {
                        obj=(new JSONObject(jsonStr)).getJSONObject("data");

                        firstName=obj.getString("FirstName");
                        lastName=obj.getString("LastName");
                        email=obj.getString("Email");
                        profilePhoto=obj.getString("ProfilePhoto");
                        previewProfile=profilePhoto;

                    } catch (JSONException e) {
                        e.printStackTrace();
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
            nointernet_lay.setVisibility(View.GONE);
            if(profilePhoto.equals("")){
                //do nothing
                Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.mipmap.default_user_icon);
                profilepic.setImageBitmap(Icon);
            }else {
                if (getActivity()!=null){
                    Picasso.with(getActivity()).invalidate("");
                    Picasso.with(getActivity())
                            .load(profilePhoto)
                            .placeholder(R.mipmap.default_user_icon)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .fit()
                            .centerCrop().into(profilepic);

                }

            }
            fname.setText(firstName);
            lname.setText(lastName);
           // toolbar.setTitle(email);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        mOptionsMenu=menu;
        boolean checkinternet2=checkConnection();
        if (checkinternet2==true){

            inflater.inflate(R.menu.menu_signout, menu);
        }else {
            //do nothing
        }

        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings1:
                final SharedPreferences settings =this.getActivity(). getSharedPreferences(
                        PREF_NAME, 0);
                final SharedPreferences.Editor editor = settings.edit();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Are you sure, you want to Signout !");

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        editor.remove("hasLoggedIn");
                        editor.apply();
                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        // Write your code here to invoke YES event
                        // Toast.makeText(get, "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

// Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //  Toast.makeText(geta, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

// Showing Alert Message
                alertDialog.show();

                return true;

        }
        return onOptionsItemSelected(item);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
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
            String fs=fname.getText().toString().trim();
            String ls=lname.getText().toString().trim();
           // About=abt.getText().toString().trim();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ProfileUpload);
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
                entity.addPart("firstName", new StringBody(fs));
                entity.addPart("lastName", new StringBody(ls));
                entity.addPart("loggedInUser", new StringBody(loginuserid));
               /// entity.addPart("aboutMe", new StringBody(About));

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
            Toast.makeText(getActivity(),"Profile updated successfully",Toast.LENGTH_SHORT).show();
            // showing the server response in an alert dialog
            // showAlert(result);
            save.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }


    }
}
