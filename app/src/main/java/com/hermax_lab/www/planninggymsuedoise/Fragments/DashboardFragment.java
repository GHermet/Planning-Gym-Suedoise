package com.hermax_lab.www.planninggymsuedoise.Fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermax_lab.www.planninggymsuedoise.Activities.CoursDetailsActivity;
import com.hermax_lab.www.planninggymsuedoise.Adapters.CoursGridAdapter;
import com.hermax_lab.www.planninggymsuedoise.Classes.Cours;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Guillaume on 5/10/16.
 */
public class DashboardFragment extends Fragment {

    private CoursGridAdapter mCoursTodoDashboardAdapter;
    private CoursGridAdapter mCoursInProgressDashboardAdapter;
    private CoursGridAdapter mCoursDoneDashboardAdapter;
    private ArrayList<Cours> mCoursDone;
    private ArrayList<Cours> mCoursInProgress;
    private ArrayList<Cours> mCoursTodo;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        final RoundCornerProgressBar mProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.pb_dashboard);
        mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
        mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
        if (mCoursTodo.size()>1||mCoursInProgress.size()>1||mCoursDone.size()>1){
            int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
            mProgressBar.setMax(max);
        }
        if (mCoursDone.size()>1){
            int progress = mCoursDone.size();
            mProgressBar.setProgress(progress);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridView mCoursTodoGrid = (GridView) view.findViewById(R.id.grid_todo);
        final ListView mCoursInProgressList = (ListView) view.findViewById(R.id.list_in_progress);
        final ListView mCoursDoneList = (ListView) view.findViewById(R.id.list_done);
        final RoundCornerProgressBar mProgressBar = (RoundCornerProgressBar) view.findViewById(R.id.pb_dashboard);

        final DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(FIREBASE.REF);

        /* Cours TODO */

        mCoursTodoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cours mClass = (Cours) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), CoursDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(mClass.getClass_id()));
                i.putExtra("class_id",mClass.getClass_id());
                startActivity(i);
            }
        });

        final Query coursTODO = ref.child("DATABASE").child("DASHBOARD").child("TODO");
        mCoursTodo = new ArrayList<Cours>();
        coursTODO.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  String coursTodoId = dataSnapshot.getKey();
                 Log.v("cours id todo", String.valueOf(coursTodoId));
                    final Query QcoursTodo = ref.child("DATABASE").child("class_list").child("FR").child(coursTodoId);
                    QcoursTodo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Cours mClass = dataSnapshot.getValue(Cours.class);
                                //  Log.v("cours", mClass.getClass_date());
                                mCoursTodo.add(mClass);
                                mCoursTodoDashboardAdapter = new CoursGridAdapter(mCoursTodo, Cours.class, R.layout.grid_item_cours, getActivity());
                                mCoursTodoGrid.setAdapter(mCoursTodoDashboardAdapter);
                                mCoursTodoDashboardAdapter.notifyDataSetChanged();
                                mCoursTodoDashboardAdapter.registerDataSetObserver(new DataSetObserver() {
                                    @Override
                                    public void onChanged() {
                                        super.onChanged();
                                        mCoursTodoGrid.setSelection(mCoursTodoDashboardAdapter.getCount() - 1);
                                    }
                                });

                            }
                            mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
                            mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
                            int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
                            mProgressBar.setMax(max);
                            int progress = mCoursDone.size();
                            mProgressBar.setProgress(progress);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (mCoursTodoDashboardAdapter!=null)
                    mCoursTodoDashboardAdapter.notifyDataSetChanged();


            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String coursTodoId = dataSnapshot.getKey();
                for (int i = 0; i <mCoursTodo.size() ; i++) {
                    if (Objects.equals(String.valueOf(mCoursTodo.get(i).getClass_id()), coursTodoId)){
                        mCoursTodo.remove(i);
                    mCoursTodoDashboardAdapter = new CoursGridAdapter(mCoursTodo, Cours.class, R.layout.grid_item_cours, getActivity());
                    mCoursTodoGrid.setAdapter(mCoursTodoDashboardAdapter);
                    mCoursTodoDashboardAdapter.notifyDataSetChanged();
                    }
                    mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
                    mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
                    int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
                    mProgressBar.setMax(max);
                    int progress = mCoursDone.size();
                    mProgressBar.setProgress(progress);


                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (mCoursTodoDashboardAdapter!=null)
                mCoursTodoDashboardAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        /* Cours In Progress */
        mCoursInProgressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cours mClass = (Cours) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), CoursDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(mClass.getClass_id()));
                i.putExtra("class_id",mClass.getClass_id());
                startActivity(i);
            }
        });
        final Query coursIN_PROGRESS = ref.child("DATABASE").child("DASHBOARD").child("INPROGRESS");
        mCoursInProgress = new ArrayList<>();
        coursIN_PROGRESS.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    String coursinProgressId = dataSnapshot.getKey();
                    Log.v("cours id in progress", String.valueOf(coursinProgressId));
                    final Query QcoursInProgress = ref.child("DATABASE").child("class_list").child("FR").child(coursinProgressId);
                    QcoursInProgress.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                            Cours mClass = dataSnapshot.getValue(Cours.class);
                                mCoursInProgress.add(mClass);
                                mCoursInProgressDashboardAdapter = new CoursGridAdapter(mCoursInProgress, Cours.class, R.layout.grid_item_cours, getActivity());
                                mCoursInProgressList.setAdapter(mCoursInProgressDashboardAdapter);
                                mCoursInProgressDashboardAdapter.notifyDataSetChanged();
                                mCoursInProgressDashboardAdapter.registerDataSetObserver(new DataSetObserver() {
                                    @Override
                                    public void onChanged() {
                                        super.onChanged();
                                        mCoursInProgressList.setSelection(mCoursInProgressDashboardAdapter.getCount() - 1);
                                    }
                                });
                            }
                            mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
                            mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
                            int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
                            mProgressBar.setMax(max);
                            int progress = mCoursDone.size();
                            mProgressBar.setProgress(progress);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if ( mCoursInProgressDashboardAdapter!=null)
                    mCoursInProgressDashboardAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String coursInProgressId = dataSnapshot.getKey();
                for (int i = 0; i <mCoursInProgress.size() ; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(String.valueOf(mCoursInProgress.get(i).getClass_id()), coursInProgressId)){
                            mCoursInProgress.remove(i);
                            mCoursInProgressDashboardAdapter.notifyDataSetChanged();
                            mCoursInProgressDashboardAdapter = new CoursGridAdapter(mCoursInProgress, Cours.class, R.layout.grid_item_cours, getActivity());
                            mCoursInProgressList.setAdapter(mCoursInProgressDashboardAdapter);
                        }
                        mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
                        mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
                        int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
                        mProgressBar.setMax(max);
                        int progress = mCoursDone.size();
                        mProgressBar.setProgress(progress);

                    }


                }


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if ( mCoursInProgressDashboardAdapter!=null)
                    mCoursInProgressDashboardAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        /* Cours Done */
        mCoursDoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cours mClass = (Cours) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), CoursDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(mClass.getClass_id()));
                i.putExtra("class_id",mClass.getClass_id());
                startActivity(i);
            }
        });
        final Query coursDONE = ref.child("DATABASE").child("DASHBOARD").child("DONE");
        mCoursDone = new ArrayList<>();
        coursDONE.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String coursDoneId = dataSnapshot.getKey();
                    Log.v("cours id done", coursDoneId);
                    final Query QcoursDone = ref.child("DATABASE").child("class_list").child("FR").child(coursDoneId);
                    QcoursDone.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Cours mClass = dataSnapshot.getValue(Cours.class);
                                mCoursDone.add(mClass);
                                mCoursDoneDashboardAdapter = new CoursGridAdapter(mCoursDone, Cours.class, R.layout.grid_item_cours, getActivity());
                                mCoursDoneList.setAdapter(mCoursDoneDashboardAdapter);
                                mCoursDoneDashboardAdapter.notifyDataSetChanged();
                                mCoursDoneDashboardAdapter.registerDataSetObserver(new DataSetObserver() {
                                    @Override
                                    public void onChanged() {
                                        super.onChanged();
                                        mCoursDoneList.setSelection(mCoursDoneDashboardAdapter.getCount() - 1);
                                    }
                                });


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (mCoursDoneDashboardAdapter != null)
                    mCoursDoneDashboardAdapter.notifyDataSetChanged();
                mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
                mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
                int max = mCoursTodo.size() + mCoursInProgress.size() + mCoursDone.size();
                mProgressBar.setMax(max);
                int progress = mCoursDone.size();
                mProgressBar.setProgress(progress);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String coursDoneId = dataSnapshot.getKey();
                for (int i = 0; i <mCoursDone.size() ; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(String.valueOf(mCoursDone.get(i).getClass_id()), coursDoneId)){
                            mCoursDone.remove(i);
                        mCoursDoneDashboardAdapter = new CoursGridAdapter(mCoursDone, Cours.class, R.layout.grid_item_cours, getActivity());
                        mCoursDoneList.setAdapter(mCoursDoneDashboardAdapter);
                        mCoursDoneDashboardAdapter.notifyDataSetChanged();
                        }
                        mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
                        mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
                        int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
                        mProgressBar.setMax(max);
                        int progress = mCoursDone.size();
                        mProgressBar.setProgress(progress);

                    }

                }



            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (mCoursDoneDashboardAdapter!=null)
                    mCoursDoneDashboardAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        //final RoundCornerProgressBar mProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.pb_dashboard);
        mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
        mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
        if (mCoursTodo.size()>1||mCoursInProgress.size()>1||mCoursDone.size()>1){
            int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
            mProgressBar.setMax(max);
        }
        if (mCoursDone.size()>1){
            int progress = mCoursDone.size();
            mProgressBar.setProgress(progress);
        }




    }


    @Override
    public void onResume() {
        super.onResume();

        final RoundCornerProgressBar mProgressBar = (RoundCornerProgressBar) getActivity().findViewById(R.id.pb_dashboard);
        mProgressBar.setProgressColor(Color.parseColor("#56d2c2"));
        mProgressBar.setProgressBackgroundColor(Color.LTGRAY);
        if (mCoursTodo.size()>1||mCoursInProgress.size()>1||mCoursDone.size()>1){
            int max = mCoursTodo.size()+mCoursInProgress.size()+mCoursDone.size();
            mProgressBar.setMax(max);
        }
       if (mCoursDone.size()>1){
           int progress = mCoursDone.size();
           mProgressBar.setProgress(progress);
       }

        //mSearchView.setVisibility(View.INVISIBLE);

    }
}