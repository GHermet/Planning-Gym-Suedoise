package com.hermax_lab.www.planninggymsuedoise.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermax_lab.www.planninggymsuedoise.Adapters.ViewPagerAdapter;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.Classes.Region;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TabsActivity extends AppCompatActivity {

    public Set<String> intensity_filters;
    public SharedPreferences sharedPreferences;
    private BottomSheetBehavior<View> mBottomSheetBehavior;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_tabs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_filters:
                Filters();
                break;
            case R.id.menu_settings:
               goToSettings();
                break;
            case R.id.menu_logout:
                unAuthUser();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Filters(){
        if (mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED) showFilters();
        else if (mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) hideFilters();
    }

    private void showFilters() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void hideFilters(){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        /* Shared Preferences Instance */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /* Firebase Instance */
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(FIREBASE.REF);
        /* Filtres setup */
        View bottomSheet = findViewById( R.id.bottom_sheet);
        assert bottomSheet != null;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        /* Filtre Regions */
        final Spinner mRegionsSpinner = (Spinner) findViewById(R.id.sp_regions);
        final ArrayList<Region> mRegionsArray = new ArrayList<>();
        final List<String> mRegionsSpinnerList = new ArrayList<>();
        final ArrayAdapter<String> mRegionsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mRegionsSpinnerList);
        mRegionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Query QRegions = ref.child("DATABASE").child("region_list");
        QRegions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> mRegionSnaps = dataSnapshot.getChildren();
                for (DataSnapshot mRegionSnap:mRegionSnaps) {
                    Region mRegion = mRegionSnap.getValue(Region.class);
                    mRegionsArray.add(mRegion);
                    mRegionsSpinnerList.add(mRegion.getRegion_name());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        assert mRegionsSpinner != null;
        mRegionsSpinner.setAdapter(mRegionsAdapter);
    mRegionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      mRegionsSpinner.setSelection(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mRegionsSpinner.setSelection(0);

    }
});

        final Spinner mProblemeSpinner = (Spinner) findViewById(R.id.sp_probleme);
        final Spinner mTypeProblemeSpinner = (Spinner) findViewById(R.id.sp_type_probleme);



        /* Filtre intensités */
        final GridView mIntensiteGridView = (GridView) findViewById(R.id.gridview_intensites);
        assert mIntensiteGridView != null;
        intensity_filters = new HashSet<>();
        mIntensiteGridView.setHapticFeedbackEnabled(true);
        mIntensiteGridView.setFocusableInTouchMode(true);
        mIntensiteGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String intensite_id = (String) parent.getAdapter().getItem(position);
                ImageView iv = (ImageView) view;
                if (!intensity_filters.contains(intensite_id)){

                    iv.setColorFilter(null);
                    intensity_filters.add(intensite_id);
                    sharedPreferences.edit().putStringSet("intens_filter", intensity_filters).commit();
                    Log.v("intens selected",intensite_id);
                }
                else{

                    iv.setColorFilter(R.color.cardview_dark_background);
                    intensity_filters.remove(intensite_id);
                    sharedPreferences.edit().putStringSet("intens_filter", intensity_filters).commit();
                    Log.v("intens removed",intensite_id);
                }

            }
        });
        final ArrayList<String> intensite_URLs = new ArrayList<>();
        final BaseAdapter mIntensiteGridAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return intensite_URLs.size();
            }

            @Override
            public Object getItem(int position) {
                return intensite_URLs.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =  getLayoutInflater().inflate(R.layout.grid_item_intensite, parent, false);
                ImageView iv_intensite = (ImageView) view.findViewById(R.id.iv_intensite);
                iv_intensite.setColorFilter(R.color.black_overlay);
                Picasso.with(getApplication())
                        .load(intensite_URLs.get(position))
                        .centerCrop()
                        .resize(40,40)
                        .into(iv_intensite);
                return view;
            }
        };
            Query QintensiteURL = ref.child("appdata").child("ListeLogo");
            QintensiteURL.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String intensite_URL = dataSnapshot.getValue(String.class);
                        Log.v("intensite URL", intensite_URL);
                        intensite_URLs.add(intensite_URL);
                        mIntensiteGridAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        mIntensiteGridView.setAdapter(mIntensiteGridAdapter);
        mIntensiteGridAdapter.notifyDataSetChanged();

        /* ViewPager Setup */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        assert mViewPager != null;
        mViewPager.setAdapter(mViewPagerAdapter);
        SmartTabLayout smartTabLayout = (SmartTabLayout) findViewById(R.id.smartTabLayout);
        assert smartTabLayout != null;
        smartTabLayout.setViewPager(mViewPager);

        /* Toolbar setup  */
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle("Planning Gym Suedoise");
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.main_tabs_menu);
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /* Internet Check */
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

    private void unAuthUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        /* Unauthenticate the client */
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        TabsActivity.this.finish();

    }

    private void goToSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



}