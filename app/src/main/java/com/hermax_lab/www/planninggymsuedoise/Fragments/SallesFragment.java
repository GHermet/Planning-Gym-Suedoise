package com.hermax_lab.www.planninggymsuedoise.Fragments;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.hermax_lab.www.planninggymsuedoise.Activities.SalleDetailsActivity;
import com.hermax_lab.www.planninggymsuedoise.Adapters.SallesGridAdapter;
import com.hermax_lab.www.planninggymsuedoise.Classes.Classroom;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 5/10/16.
 */
public class SallesFragment extends Fragment {

    private static MapView mMapView;
    private SallesGridAdapter mSallesGridAdapter;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mSearchSalles;
    private GridView mSallesGrid;
    private List<Classroom> mClassrooms;

    public static SallesFragment newInstance() {
        return new SallesFragment();
    }

    public List<Classroom> getmClassrooms() {
        return mClassrooms;
    }

    public void setmClassrooms(List<Classroom> mClassrooms) {
        this.mClassrooms = mClassrooms;
    }

    private void setMarkers(MapboxMap mapboxMap) {
        for(Classroom mClassroom : mClassrooms){
            float lat = mClassroom.getClassroom_gps_lat();
            float lon = mClassroom.getClassroom_gps_lon();
            String name = mClassroom.getClassroom_name();
            int id = mClassroom.getClassroom_id();
            mapboxMap.addMarker( new com.mapbox.mapboxsdk.annotations
                                .MarkerOptions()
                                .title(name)
                                .snippet(String.valueOf(id))
                                .position(new com.mapbox.mapboxsdk.geometry.LatLng(lat,lon)));

        }

        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull com.mapbox.mapboxsdk.annotations.Marker marker) {
                int classroom_id = Integer.valueOf(marker.getSnippet());
                Intent i = new Intent(getActivity(), SalleDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(classroom_id));
                i.putExtra("classroom_id",classroom_id);
                startActivity(i);

                return false;
            }
        });
    }

    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salles, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* MapView setup */
        mMapView = ((MapView) getActivity().findViewById(R.id.mapview));
        mMapView.onCreate(savedInstanceState);
        mMapView.setStyle(Style.MAPBOX_STREETS);
        setmClassrooms(new ArrayList<Classroom>());
        mSallesGrid = (GridView) view.findViewById(R.id.grid_salles);
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(FIREBASE.REF);
         Query salles = ref.child("DATABASE").child("classroom_list").child("FR").orderByChild("classroom_name");
        salles.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Classroom mClassroom = dataSnapshot.getValue(Classroom.class);
                    mClassrooms.add(mClassroom);
                    Log.v("added classroom", mClassroom.getClassroom_gps_lat() + " " + mClassroom.getClassroom_gps_lon());
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


        mMapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                setMarkers(mapboxMap);
            }
        });

        mSallesGridAdapter = new SallesGridAdapter(salles,Classroom.class, R.layout.grid_item_salles, getActivity());
        mSallesGrid.setAdapter(mSallesGridAdapter);
        System.out.print("mSallesGridAdapter set up");
        mSallesGridAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mSallesGrid.setSelection(mSallesGridAdapter.getCount() - 1);

            }
        });


        mSallesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getView() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);}
                Classroom mClassroom = (Classroom) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), SalleDetailsActivity.class);
                Log.v(getClass().getName(), String.valueOf(mClassroom.getClassroom_id()));
                i.putExtra("classroom_id",mClassroom.getClassroom_id());
                startActivity(i);
            }
        });


        mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
        mSearchView.setQueryHint("Rechercher par Nom...");
        mSearchSalles = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSallesGridAdapter.getmModels().clear();
                mSallesGrid.setAdapter(mSallesGridAdapter);
                mSallesGridAdapter.notifyDataSetChanged();
                Object[] Salles = getmClassrooms().toArray();
                for (Object classroom : Salles) {
                    if (((Classroom) classroom).getClassroom_name().toLowerCase().contains(newText.toLowerCase())) {
                        mSallesGridAdapter.getmModels().add((Classroom) classroom);
                        Log.v("added to results",((Classroom) classroom).getClassroom_name());
                    }

                }
                Salles=null;
                mSallesGrid.setAdapter(mSallesGridAdapter);
                mSallesGridAdapter.notifyDataSetChanged();
                System.gc();
                return false;
            }
        };
        mSearchView.setOnQueryTextListener(mSearchSalles);

          /* Map Fragment Setup */



    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        SearchView mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
       // mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setQueryHint("Rechercher par Nom...");
        mSearchView.setOnQueryTextListener(mSearchSalles);
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

