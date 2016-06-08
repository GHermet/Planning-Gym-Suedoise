package com.hermax_lab.www.planninggymsuedoise.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Check if any User is logged */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // user authenticated -> goto TabActivity
            Log.i("Authentification",user.toString());
            Toast.makeText(getApplicationContext(), FIREBASE.LOGGED_IN, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,TabsActivity.class);
            i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            // no user authenticated -> goto LoginActivity
            Log.i("Authentification","no user authenticated");
            Toast.makeText(getApplicationContext(), FIREBASE.LOGGED_OUT, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        MainActivity.this.finish();

    }

    @Override
    public View onCreateView(final View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }
}
