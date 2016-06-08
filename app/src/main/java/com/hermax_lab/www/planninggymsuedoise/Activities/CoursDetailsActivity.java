package com.hermax_lab.www.planninggymsuedoise.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermax_lab.www.planninggymsuedoise.Adapters.StaffGridAdapter;
import com.hermax_lab.www.planninggymsuedoise.Classes.Classroom;
import com.hermax_lab.www.planninggymsuedoise.Classes.Cours;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.Classes.Staff;
import com.hermax_lab.www.planninggymsuedoise.Dialogs.StaffDialog;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Guillaume on 5/17/16.
 */
public class CoursDetailsActivity extends AppCompatActivity {

    private MapView mMapView;
    private ListView mTeachersListView;
    private Cours mClass;
    private ArrayList<Staff> mTeachers;
    private ArrayList<Staff> mHosts;
    private ImageView mClassroomImageView;
    private TextView mClassDate;
    private TextView mClassStartTime;
    private TextView mClassDuration;
    private TextView mClassroomName;
    private TextView mClassroomCapacity;
    private ImageView mClassLogo;
    private ListView mHostsListView;
    private TextView mClassroomLocation;
    private TextView mClassroomCity;
    private AppCompatButton mPlanningButton;
    private DatabaseReference ref;
    private DatabaseReference teachersQ;
    private DatabaseReference hostsQ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* View */
        setContentView(R.layout.activity_details_cours);
        mPlanningButton = (AppCompatButton) findViewById(R.id.button_planning);
        mClassDate= (TextView) findViewById(R.id.tv_class_date);
        mClassLogo = (ImageView) findViewById(R.id.iv_class_logo);
        mClassStartTime = (TextView) findViewById(R.id.tv_class_starttime);
        mClassDuration = (TextView) findViewById(R.id.tv_class_duration);
        mClassroomName = (TextView) findViewById(R.id.tv_classroom_name);
        mClassroomLocation = (TextView) findViewById(R.id.tv_classroom_address);
        mClassroomCity = (TextView) findViewById(R.id.tv_classroom_city);
        mClassroomCapacity = (TextView) findViewById(R.id.tv_classroom_capacity);
        mClassroomImageView = (ImageView) findViewById(R.id.iv_classroom_img);
        mTeachersListView = (ListView) findViewById(R.id.lv_class_teachers);
        mHostsListView = (ListView) findViewById(R.id.lv_class_hosts);
        mMapView = (MapView) findViewById(R.id.mapview);
        assert mMapView != null;
        mMapView.setAccessToken(getResources().getString(R.string.accessToken));
        mMapView.onCreate(savedInstanceState);
        mMapView.setStyle(Style.MAPBOX_STREETS);
                /* MapView  */


        /* Bundle */
        Intent in = getIntent();
        Bundle b = in.getExtras();
        final int class_id = b.getInt("class_id");
        System.out.println(String.valueOf(class_id));

        /* Firebase */
        ref = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE.REF);
         /* Populate View */
        final Query classQ = ref.child("DATABASE").child("class_list").child("FR").child(String.valueOf(class_id));
        classQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClass = dataSnapshot.getValue(Cours.class);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
                DateFormat date2Day = new SimpleDateFormat("EEEE dd MMM yyyy",Locale.FRANCE);
                try {
                    Date date = format.parse(mClass.getClass_date());
                    mClass.setClass_date(date2Day.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.getCause();

                }
                /* Toolbar */
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setTitle(mClass.getClass_date());
                    setSupportActionBar(toolbar);
                    toolbar.inflateMenu(R.menu.main_tabs_menu);
                    ActionBar actionBar = getSupportActionBar();
                    assert actionBar != null;
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                    actionBar.setDisplayShowTitleEnabled(true);
                    actionBar.setDisplayUseLogoEnabled(true);
                    actionBar.setHomeButtonEnabled(false);
                }


                assert mClassDate != null;
                mClassDate.setText(mClass.getClass_date());
                assert mClassDuration != null;
                mClassDuration.setText(mClass.getClass_duration()+" min");
                assert mClassStartTime != null;
                mClassStartTime.setText(mClass.getClass_starttime());
                Picasso.with(getBaseContext())
                        .load(mClass.getClass_level_icon_URL())
                        .error(R.mipmap.ic_close)
                        .into(mClassLogo);

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        setMarker(mapboxMap);


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });



        /* Populate Teachers */
        mTeachers= new ArrayList<>();
        Query teachersListQ = ref.child("DATABASE").child("class_list").child("FR").child(String.valueOf(class_id)).child("teacher_list");
        teachersListQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> teacher_ids = dataSnapshot.getChildren();
                for (DataSnapshot teacher_id: teacher_ids){
                    Log.v("teacher id", String.valueOf(teacher_id.getValue(Integer.class)));
                    teachersQ = ref.child("Intervenant").child(String.valueOf(teacher_id.getValue(Integer.class)));
                    teachersQ.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Staff teacher = dataSnapshot.getValue(Staff.class);
                                mTeachers.add(teacher);
                                Log.v("teacher", teacher.getStaff_name());
                                final StaffGridAdapter mTeachersGridAdapter = new StaffGridAdapter(mTeachers, Staff.class, R.layout.grid_item_staff, CoursDetailsActivity.this, getCurrentFocus());
                                mTeachersListView.setAdapter(mTeachersGridAdapter);
                                mTeachersGridAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                }

                mTeachersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Staff mTeacher = (Staff) parent.getAdapter().getItem(position);
                        final StaffDialog dialog = new StaffDialog(CoursDetailsActivity.this,mTeacher);
                        dialog.setTitle(mClassroomName.getText());
                        dialog.show();
                    }
                });





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        /* Populate Hosts*/
        mHosts= new ArrayList<>();
        Query HostsListQ = ref.child("DATABASE").child("class_list").child("FR").child(String.valueOf(class_id)).child("host_list");
        HostsListQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> hosts_ids = dataSnapshot.getChildren();
                for (DataSnapshot hosts_id: hosts_ids){
                    Log.v("host id", String.valueOf(hosts_id.getValue(Integer.class)));
                    hostsQ = ref.child("Intervenant").child(String.valueOf(hosts_id.getValue(Integer.class)));
                    hostsQ.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                            Staff host = dataSnapshot.getValue(Staff.class);
                            mHosts.add(host);
                            Log.v("host", host.getStaff_name());
                            final StaffGridAdapter mHostsGridAdapter = new StaffGridAdapter(mHosts, Staff.class, R.layout.grid_item_staff, CoursDetailsActivity.this, getCurrentFocus());
                            mHostsListView.setAdapter(mHostsGridAdapter);
                            mHostsGridAdapter.notifyDataSetChanged();
                        }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }


                mHostsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Staff mHost = (Staff) parent.getAdapter().getItem(position);
                        final StaffDialog dialog = new StaffDialog(CoursDetailsActivity.this,mHost);
                        dialog.setTitle(mClassroomName.getText());
                        dialog.show();

                    }
                });




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






    SlidrConfig config = new SlidrConfig.Builder()
            .primaryColor(getResources().getColor(R.color.colorPrimary))
            .position(SlidrPosition.TOP)
            .sensitivity(1f)
            .scrimStartAlpha(0.8f)
            .scrimEndAlpha(0f)
            .velocityThreshold(2400)
            .distanceThreshold(0.25f)
            .edge(true)
            .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
            .listener(new SlidrListener() {
                @Override
                public void onSlideStateChanged(int state) {

                }

                @Override
                public void onSlideChange(float percent) {

                }

                @Override
                public void onSlideOpened() {

                }

                @Override
                public void onSlideClosed() {

                }
            })
            .build();

    Slidr.attach(this,config);
}

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    private void setMarker(final MapboxMap mapboxMap) {
        final Query classroomQ = ref.child("DATABASE").child("classroom_list").child("FR").child(String.valueOf(mClass.getClassroom_id()));
        classroomQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Classroom mClassroom = dataSnapshot.getValue(Classroom.class);
                mClassroomCapacity.setText(mClassroom.getClassroom_capacity()+" places");
                mClassroomName.setText(mClassroom.getClassroom_name());
                mClassroomLocation.setText(mClassroom.getClassroom_address());
                mClassroomCity.setText(mClassroom.getClassroom_city());
                mPlanningButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), SalleDetailsActivity.class);
                        Log.v(getClass().getName(), String.valueOf(mClassroom.getClassroom_id()));
                        i.putExtra("classroom_id",mClassroom.getClassroom_id());
                        i.putExtra("class_id",mClass.getClass_id());
                        startActivity(i);
                    }
                });

                Picasso.with(getBaseContext())
                        .load(mClassroom.getClassroom_photo_large_url())
                        .error(R.drawable.background1)
                        .into(mClassroomImageView);

                float lat = mClassroom.getClassroom_gps_lat();
                float lon = mClassroom.getClassroom_gps_lon();
                String name = mClassroom.getClassroom_name();
                String zip = mClassroom.getClassroom_zip();
                    mapboxMap.addMarker(new com.mapbox.mapboxsdk.annotations
                            .MarkerOptions()
                            .title(name)
                            .snippet(zip)
                            .position(new LatLng(lat, lon)));
                    CameraPosition cm = mapboxMap.getCameraPosition();
                    cm.target.setLatitude(lat);
                    cm.target.setLongitude(lon);
                    mapboxMap.setCameraPosition(cm);
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull com.mapbox.mapboxsdk.annotations.Marker marker) {
               /* Intent i = new Intent(getApplicationContext(), SalleDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(mClass.getClassroom_id()));
                i.putExtra("classroom_id",mClass.getClassroom_id());
                startActivity(i);
                */
                return false;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }



}
