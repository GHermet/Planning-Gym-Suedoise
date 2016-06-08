package com.hermax_lab.www.planninggymsuedoise.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hermax_lab.www.planninggymsuedoise.Adapters.StaffGridAdapter;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.Classes.Staff;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Guillaume on 5/10/16.
 */
public class StaffFragment extends Fragment {

    public StaffGridAdapter mStaffGridAdapter;
    public GridView mStaffGrid;
    private TextView tv_staffName_Details;
    private TextView tv_staffCity_Details;
    private CircleImageView iv_staffImage_Details;
    private TextView tv_staffMobile_Details;
    private TextView tv_staffEmail_Details;
    private TextView tv_staffAdress_Details;
    private TextView tv_staffJob_Details;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mSearchStaff;
    private BaseRoundCornerProgressBar progressGlobal;
    private DatabaseReference ref;
    private LinearLayout ll_staffEmail_Details;
    private SharedPreferences sharedPref;
    private List<Staff> mStaffList;

    public static StaffFragment newInstance() {
        return new StaffFragment();
    }

    public List<Staff> getmStaffList() {
        return mStaffList;
    }

    public void setmStaffList(List<Staff> mStaffList) {
        this.mStaffList = mStaffList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           return inflater.inflate(R.layout.fragment_staff, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.v("pref changed",key);
            }
        });
        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(FIREBASE.REF);
        setmStaffList(new ArrayList<Staff>());
       mStaffGrid = (GridView) view.findViewById(R.id.grid_staff);

         /* Staff Details Card */
        final CardView cv_staffDetails = (CardView) view.findViewById(R.id.cv_staff_details);
        tv_staffName_Details = (TextView) view.findViewById(R.id.tv_name_staff_details);
        tv_staffCity_Details = (TextView) view.findViewById(R.id.tv_city_staff_details);
        iv_staffImage_Details = (CircleImageView) view.findViewById(R.id.iv_img_staff_details);
        tv_staffMobile_Details = (TextView) view.findViewById(R.id.tv_mobile_staff_details);
        tv_staffEmail_Details = (TextView) view.findViewById(R.id.tv_email_staff_details);
        ll_staffEmail_Details = (LinearLayout) getActivity().findViewById(R.id.ll_mail);
        tv_staffAdress_Details = (TextView) view.findViewById(R.id.tv_adress_staff_details);
        tv_staffJob_Details = (TextView) view.findViewById(R.id.tv_job_staff_details);

        final ImageView iv_close_details = (ImageView) view.findViewById(R.id.iv_close_details);
        iv_close_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_staffDetails.setVisibility(View.GONE);
            }
        });

                mStaffGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              final Staff mStaff= (Staff) parent.getAdapter().getItem(position);

                tv_staffName_Details.setText(mStaff.getStaff_name());
                tv_staffCity_Details.setText(mStaff.getStaff_city());
                tv_staffEmail_Details.setText(mStaff.getStaff_email());
                tv_staffMobile_Details.setText(mStaff.getStaff_mobile());
                tv_staffAdress_Details.setText(mStaff.getStaff_adress());
                tv_staffJob_Details.setText(mStaff.getStaff_job());
                ll_staffEmail_Details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", mStaff.getStaff_email(), null));
                        getContext().startActivity(Intent.createChooser(emailIntent, "Envoyer un email à "+mStaff.getStaff_name()));
                    }
                });
                ColorGenerator generator = ColorGenerator.MATERIAL; // generate random color
                int color = generator.getColor(mStaff.getStaff_name());
                TextDrawable circleLetter = TextDrawable.builder()
                        .buildRound(mStaff.getStaff_name().substring(0,1), color);
                Picasso.with(getContext())
                        .load(mStaff.getStaff_photo_large_url())
                        .resize(100, 100)
                        .centerCrop()
                        .error(circleLetter)
                        .into(iv_staffImage_Details);
                View v = getActivity().getCurrentFocus();
                /* Hide Keyboard */
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);}
                cv_staffDetails.setVisibility(View.VISIBLE);


                  /* Staff Details GridView Intensités */
                final GridView mIntensiteGridView = (GridView) getActivity().findViewById(R.id.gridview_intensites);
                final ArrayList<Integer> intensite_ids = mStaff.getStaff_intensites();
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
                        View view =  getLayoutInflater(savedInstanceState).inflate(R.layout.grid_item_intensite, parent, false);
                        ImageView iv_itensite = (ImageView) view.findViewById(R.id.iv_intensite);
                        Picasso.with(getContext())
                                .load(intensite_URLs.get(position))
                                .centerCrop()
                                .resize(40,40)
                                .into(iv_itensite);
                        return view;
                    }
                };
                for (final Integer intensite_id:intensite_ids) {
                    Log.v("intensite id", String.valueOf(intensite_id));
                    Query QintensiteURL = ref.child("appdata").child("ListeLogo");
                    QintensiteURL.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().contains(String.valueOf(intensite_id))) {
                                String intensite_URL = dataSnapshot.getValue(String.class);
                                Log.v("intensite URL", intensite_URL);
                                intensite_URLs.add(intensite_URL);
                                mIntensiteGridAdapter.notifyDataSetChanged();
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
                }
                mIntensiteGridView.setAdapter(mIntensiteGridAdapter);
                mIntensiteGridAdapter.notifyDataSetChanged();


            }
        });
        cv_staffDetails.setVisibility(View.GONE);

        /* Staff GridList */
        final Query staff = ref.child("Intervenant").orderByChild("staff_business");
        staff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Staff mStaff = dataSnapshot.getValue(Staff.class);
                mStaffList.add(mStaff);
                Log.v("added staff", mStaff.getStaff_name());

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
        mStaffGridAdapter = new StaffGridAdapter(staff,Staff.class,R.layout.grid_item_staff, getActivity(),view);
        mStaffGrid.setAdapter(mStaffGridAdapter);
        mStaffGridAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mStaffGrid.setSelection(mStaffGridAdapter.getCount() - 1);
            }
        });



        /* Staff Search */
        mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
        mSearchView.setQueryHint("Rechercher par Nom...");
        mSearchStaff = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mStaffGridAdapter.getmModels().clear();
                mStaffGrid.setAdapter(mStaffGridAdapter);
                mStaffGridAdapter.notifyDataSetChanged();
                Object[] Staffs = getmStaffList().toArray();
                for (Object staff : Staffs) {
                    if (((Staff) staff).getStaff_name().toLowerCase().contains(newText.toLowerCase())) {
                        mStaffGridAdapter.getmModels().add((Staff) staff);
                        Log.v("added to results",((Staff) staff).getStaff_name());
                    }

                }
                mStaffGrid.setAdapter(mStaffGridAdapter);
                mStaffGridAdapter.notifyDataSetChanged();
                System.gc();
                return false;
            }
        };
        mSearchView.setOnQueryTextListener(mSearchStaff);





    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStaffGridAdapter.cleanup();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        SearchView mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
        //mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setQueryHint("Rechercher par Nom...");
        mSearchView.setOnQueryTextListener(mSearchStaff);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
