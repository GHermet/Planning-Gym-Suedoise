package com.hermax_lab.www.planninggymsuedoise.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hermax_lab.www.planninggymsuedoise.Activities.CoursDetailsActivity;
import com.hermax_lab.www.planninggymsuedoise.Adapters.CoursGridAdapter;
import com.hermax_lab.www.planninggymsuedoise.Classes.Cours;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Guillaume on 5/10/16.
 */
public class CoursFragment extends Fragment {

    private CoursGridAdapter mCoursGridAdapter;
    private ArrayList<Cours> cours;
    private GridView mIntensiteGridView;
    private SharedPreferences sharedPref;
    private Set<String> intensPref;
    private DatabaseReference ref;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mSearchCours;

    public CoursFragment() {
    }

    public static CoursFragment newInstance() {
        return new CoursFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cours, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridView mCoursGrid = (GridView) view.findViewById(R.id.grid_cours);
         ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(FIREBASE.REF);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        intensPref= new HashSet<>();
        cours = new ArrayList<>();
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.v("preferences changed",key);
                switch (key){
                    case "intens_filter":
                        intensPref = sharedPref.getStringSet("intens_filter",new HashSet<String>());
                        final Query Qcours = ref.child("DATABASE").child("class_list").child("FR").orderByChild("NotePonderation");
                        Qcours.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Cours mClass = dataSnapshot.getValue(Cours.class);
                                if (intensPref.contains(String.valueOf(mClass.getClass_level()))){
                                    cours.add(mClass);
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
                        mCoursGridAdapter = new CoursGridAdapter(cours,Cours.class, R.layout.grid_item_cours, getActivity());
                        mIntensiteGridView.setAdapter(mCoursGridAdapter);
                        mCoursGridAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });


         final Query cours = ref.child("DATABASE").child("class_list").child("FR");
         mCoursGridAdapter = new CoursGridAdapter(cours,Cours.class, R.layout.grid_item_cours, getActivity());
        mCoursGrid.setAdapter(mCoursGridAdapter);
        mCoursGridAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mCoursGrid.setSelection(mCoursGridAdapter.getCount() - 1);
            }
        });

        mCoursGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getView() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);}
                Cours mClass = (Cours) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), CoursDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(mClass.getClass_id()));
                i.putExtra("class_id",mClass.getClass_id());
                startActivity(i);

            }
        });

        mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
        mSearchCours = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCoursGridAdapter.getmModels().clear();
                Object[] Cours = getCours().toArray();
                for (Object cours : Cours) {
                    if (((com.hermax_lab.www.planninggymsuedoise.Classes.Cours) cours).getClass_date().toLowerCase().contains(newText.toLowerCase())) {
                        mCoursGridAdapter.getmModels().add((Cours) cours);
                        Log.v("added to results", ((Cours) cours).getClass_date());
                    }

                }
                mCoursGrid.setAdapter(mCoursGridAdapter);
                mCoursGridAdapter.notifyDataSetChanged();
                System.gc();
                return false;
            }
        };
       // mSearchView.setOnQueryTextListener(mSearchCours);



    }

    @Override
    public void onResume() {
        super.onResume();
        SearchView mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
       // mSearchView.setVisibility(View.INVISIBLE);
        //mSearchView.setQueryHint("Rechercher par Date...");
        //mSearchView.setOnQueryTextListener(mSearchCours);
    }

    public List<Cours> getCours() {
        return cours;
    }

    public void setCours(ArrayList<Cours> cours) {
        this.cours = cours;
    }

}
