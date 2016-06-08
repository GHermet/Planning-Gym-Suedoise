package com.hermax_lab.www.planninggymsuedoise.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermax_lab.www.planninggymsuedoise.Classes.Classroom;
import com.hermax_lab.www.planninggymsuedoise.Classes.Cours;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Guillaume on 5/17/16.
 */
public class SalleDetailsActivity extends AppCompatActivity  {
    private Classroom mClassroom;
    private ImageView mClassroomImageView;
    private WeekView mCalendarWeekView;
    private int classroom_id;
    private ArrayList<WeekViewEvent> EventList;
    private int class_id;
    private MaterialCalendarView mDatePicker;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_salles);

        mClassroomImageView = (ImageView) findViewById(R.id.iv_classroom_img);

        /* Date Picker */
        mDatePicker = (MaterialCalendarView) findViewById(R.id.calendarView);
        assert mDatePicker != null;
               mDatePicker.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mCalendarWeekView.goToDate(date.getCalendar());
            }
        });

        Intent in = getIntent();
        Bundle b = in.getExtras();
        classroom_id = b.getInt("classroom_id");
        class_id = b.getInt("class_id");
        System.out.println(String.valueOf(classroom_id));
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(FIREBASE.REF);
        EventList= new ArrayList<>();
        final Query classesQ = ref.child("DATABASE").child("class_list").child("FR");
        classesQ.addChildEventListener(new ChildEventListener() {
            public Calendar cEnd;
            public Calendar cStart;
            public WeekViewEvent weekViewEvent;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Cours mClass = dataSnapshot.getValue(Cours.class);
                if (mClass.getClassroom_id()==classroom_id){
                    Log.v("class",mClass.getClass_date());
                    String[] date = mClass.getClass_date().split("-");
                    int year = Integer.parseInt(date[0]);
                    int month = Integer.parseInt(date[1]);
                    int day = Integer.parseInt(date[2]);
                    String[] time = mClass.getClass_starttime().split(":");
                    int hour = Integer.parseInt(time[0]);
                    int min = Integer.parseInt(time[1]);
                    int duration = mClass.getClass_duration();
                    int min_end = duration % 60;
                       /* StartTime */
                        cStart = Calendar.getInstance();
                        cStart.set(year,month-1,day,hour,min);
                       /* EndTime */
                        cEnd = Calendar.getInstance();
                        cEnd.set(year,month-1,day,hour+((duration - min_end) / 60),min);
                       /* WeekEvent */
                        weekViewEvent = new WeekViewEvent();
                        weekViewEvent.setId(mClass.getClass_id());
                        weekViewEvent.setStartTime(cStart);
                        weekViewEvent.setEndTime(cEnd);
                       int color;
                       String level_text;
                    switch (mClass.getClass_level()){
                        case 31:
                            level_text = "Standard 75";
                            color = R.color.colorStandard_75;
                            break;
                        case 20:
                            level_text = "Basic";
                            color = R.color.colorBasic;
                            break;
                        case 45:
                            level_text = "Cardio Flex";
                            color = R.color.colorCardioFlex;
                            break;
                        case 46:
                            level_text = "Cardio Pump";
                            color = R.color.colorCardioPump;
                            break;
                        case 47:
                            level_text = "Circuit";
                            color = R.color.colorCircuit;
                            break;
                        case 65:
                            level_text = "Clubbing";
                            color = R.color.colorClubbing;
                            break;
                        case 95:
                            level_text = "Core";
                            color = R.color.colorCore;
                            break;
                        case 67:
                            level_text = "Dance Fit";
                            color = R.color.colorDanceFit;
                            break;
                        case 60:
                            level_text = "Famille";
                            color = R.color.colorFamille;
                            break;
                        case 96:
                            level_text = "Flex";
                            color = R.color.colorFlex;
                        case 40:
                            level_text = "Intensif";
                            color = R.color.colorIntensif;
                            break;
                        case 10:
                            level_text = "Junior";
                            color = R.color.colorJunior;
                            break;
                        case 50:
                            level_text = "Modéré";
                            color = R.color.colorModere;
                            break;
                        case 49:
                            level_text = "Running";
                            color = R.color.colorRunning;
                            break;
                        case 30:
                            level_text = "Standard";
                            color = R.color.colorStandard;
                            break;
                        default:
                            level_text = "Autre";
                            color = R.color.colorAccent;

                    }
                    weekViewEvent.setName(level_text+" - "+mClass.getClass_duration()+" min");
                    weekViewEvent.setColor(getResources().getColor(color));
                        EventList.add(weekViewEvent);


                }
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
        Query classroomQ = ref.child("DATABASE").child("classroom_list").child("FR").child(String.valueOf(classroom_id));
        classroomQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClassroom = dataSnapshot.getValue(Classroom.class);
                Log.v(getClass().getName(),mClassroom.getClassroom_name());

                /* Toolbar Setup */
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setTitle(mClassroom.getClassroom_name());
                    toolbar.setSubtitle(mClassroom.getClassroom_city());
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
                /* Load Classroom Photo */
                Picasso.with(getBaseContext())
                        .load(mClassroom.getClassroom_photo_large_url())
                        .error(R.drawable.background1)
                        .into(mClassroomImageView);


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

        Slidr.attach(this, config);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mCalendarWeekView = (WeekView) findViewById(R.id.weekView);
        assert mCalendarWeekView != null;
        mCalendarWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                long class_id = event.getId();
                Intent i = new Intent(getApplicationContext(), CoursDetailsActivity.class);
                Log.v("class_id", String.valueOf(class_id));
                i.putExtra("class_id",Integer.valueOf(String.valueOf(class_id)));
                startActivity(i);

            }
        });
        mCalendarWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });
        mCalendarWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
                int i=0;
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
                   if(i==3){
                        events = getEvents(newYear,newMonth);
                        i=0;
                    }
                    i++;
                   Log.v("event list "+String.valueOf(i), String.valueOf(events.size()));
                   return  events;
            }
        });
    }

    private ArrayList<WeekViewEvent> getEvents(final int newYear, final int newMonth) {
       ArrayList<WeekViewEvent> events = new ArrayList<>();
        for (WeekViewEvent event: EventList) {
             // if (!events.contains(event))
                  events.add(event);
            Log.v("added event",event.getId()+" "+event.getStartTime().get(Calendar.HOUR)+"-"+event.getEndTime().get(Calendar.HOUR));

        }
        return events;


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




}
