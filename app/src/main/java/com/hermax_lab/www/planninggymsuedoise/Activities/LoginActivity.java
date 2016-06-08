package com.hermax_lab.www.planninggymsuedoise.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.R;

public class LoginActivity extends AppCompatActivity {
    private String message=null;
    private Button bt_login;
    private EditText et_username;
    private EditText et_password;
    private ProgressBar progressView;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        bt_login = (Button) findViewById(R.id.bt_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        progressView = (ProgressBar) findViewById(R.id.progressView);
          assert et_password != null;
         et_password.setTypeface(Typeface.DEFAULT);
         et_password.setTransformationMethod(new PasswordTransformationMethod());
         assert progressView != null;
         progressView.setVisibility(View.INVISIBLE);
         bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               username = String.valueOf(et_username.getText());
               password = String.valueOf(et_password.getText());
                    if (username.equals("")&& password.equals("")){
                        et_username.setError("Veuillez entrer votre identifiant");
                        et_password.setError("Veuillez entrer votre mot de passe");
                    }
                    else if (username.equals("")) et_username.setError("Veuillez entrer votre identifiant");
                    else if  (password.equals("")) et_password.setError("Veuillez entrer votre mot de passe");
                    else{
                        /* Hide SoftKeyboard */
                        View view = getCurrentFocus();
                        if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            /* Asynchronous Login */
                        new AsyncLogin().onPreExecute();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncLogin().execute();
                            }
                        }, 3000);

                    }


                }
            }
        });



    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /* Check Internet Connection */
        if (!FIREBASE.isOnline(getBaseContext())){
            System.out.println("No Internet");
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
            assert coordinatorLayout != null;
            Snackbar snackbar= Snackbar.make(coordinatorLayout,"Connection Lost", Snackbar.LENGTH_INDEFINITE).setAction("Connect", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    private class AsyncLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            /* Show CircularProgressBar */
            progressView.setVisibility(View.VISIBLE);
            bt_login.setClickable(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) { // Success Callback
                            message = null;
                            Log.v("login success", authResult.getUser().getProviderData().toString());
                            Intent intent = new Intent(LoginActivity.this, TabsActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() { // Failure Callback
                @Override
                public void onFailure(@NonNull Exception e) {
                    message = e.getMessage();
                    Log.e("login failed", message);
                    Handler handler = new Handler(getApplicationContext().getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            /* Hide CircularProgressBar */
                            progressView.setVisibility(View.INVISIBLE);
                            bt_login.setClickable(true);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

                    return null;
        }}}
