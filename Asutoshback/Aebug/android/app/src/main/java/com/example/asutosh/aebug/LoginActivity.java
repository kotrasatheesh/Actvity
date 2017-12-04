package com.example.asutosh.aebug;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asutosh.aebug.App_config.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.userEmail;
import static com.example.asutosh.aebug.App_config.AppController.userName;
import static com.example.asutosh.aebug.App_config.AppController.RoleID;
import static com.example.asutosh.aebug.App_config.AppController.RoleName;
import static com.example.asutosh.aebug.App_config.AppController.TeamId;


public class LoginActivity extends AppCompatActivity {
   TextView forgotpas;
    Button login;
    EditText username,pass;
    String un="";
    RelativeLayout rellog;
    String pas="";
    public static final String PREF_NAME = "Divid";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String Username="";
    public  static String image="";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    JSONObject jsonBody;
    private ProgressDialog pDialog;
    private static final String TAG = LoginActivity.class.getSimpleName();
    //UI Initiation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        rellog=(RelativeLayout)findViewById(R.id.loginlay);
        InitUi();
        forgotpas=(TextView)findViewById(R.id.forgotPass);
        forgotpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,forgotPassword.class);
                startActivity(intent);
            }
        });

    }
    private void InitUi() {
        username=(EditText)findViewById(R.id.usernamee);
        pass=(EditText)findViewById(R.id.passe);
        login=(Button)findViewById(R.id.login);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un=username.getText().toString().trim();
                pas=pass.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (!un.matches(emailPattern) || un.isEmpty()) {
                    username.setError("Invalid username !");
                    return;
                }
                if(!un.isEmpty() && pas.isEmpty())
                {
                    pass.setError("Invalid password !");
                    return;
                }
                if(un.isEmpty() && !pas.isEmpty())
                {
                    pass.setError("Invalid username !");
                    return;
                }
                if (pas.length()<4 || pas.isEmpty()) {
                    pass.setError("Invalid password !");

                }
                else if (!un.isEmpty() && !pas.isEmpty()){
                    Boolean checkinternet= isNetworkConnected();
                    if (checkinternet==true){
                        Loginvalidate(un,pas);
                    }else {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                LoginActivity.this).create();

                        // Setting Dialog Title
                        //alertDialog.setTitle("Alert Dialog");

                        // Setting Dialog Message
                        alertDialog.setMessage("No internet connection !");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                            }
                        });
                        alertDialog.show();
                        // Showing Alert Message


                    }

                }
            }
        });

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void Loginvalidate(String un, String pas) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("email", un);
                jsonBody.put("password", pas);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.Login, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String process=data.getString("IsLoginSuccess");
                                loginuserid=data.getString("UserID");
                                userName=data.getString("DisplayName");
                                userEmail=data.getString("Email");
                                RoleID=data.getString("RoleId");
                                RoleName=data.getString("RoleName");
                                TeamId=data.getString("TeamId");


                                if (process.matches("true")){
                                    Intent i=new Intent(LoginActivity.this,MainActivity.class);
                                    SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("USERID", loginuserid);
                                    editor.putString("USERNAME", userName);
                                    editor.putString("USEREMAIL", userEmail);
                                    editor.putString("ROLLID", RoleID);
                                    editor.putString("ROLLNAME", RoleName);
                                    editor.putString("TEAMID", TeamId);
                                    editor.putBoolean("hasLoggedIn",true);
                                    editor.commit();
                                    System.out.println("userId..." + loginuserid);
                                    System.out.println("userName..." + userName);
                                    System.out.println("userName..." + RoleID);
                                    pDialog.hide();
                                    if (pDialog != null && pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(i);
                                }
                                else if (process.matches("false")){
                                    Snackbar snackbar1 = Snackbar.make(rellog, "Wrong user name or password !", Snackbar.LENGTH_INDEFINITE);
                                    snackbar1.show();
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
    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
