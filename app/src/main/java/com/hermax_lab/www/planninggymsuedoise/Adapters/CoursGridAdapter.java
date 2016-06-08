package com.hermax_lab.www.planninggymsuedoise.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hermax_lab.www.planninggymsuedoise.Classes.Cours;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Guillaume on 5/13/16.
 */
public class CoursGridAdapter extends FirebaseListAdapter<Cours> {
    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
 *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public CoursGridAdapter(ArrayList<Cours> mRef, Class<Cours> mModelClass, int mLayout, Activity activity) {
        super(mRef, mModelClass, mLayout, activity);

    }

    public CoursGridAdapter(Query cours, Class<Cours> mModelClass, int grid_item_cours, FragmentActivity activity) {
        super(cours,mModelClass,grid_item_cours,activity);
    }

    @Override
    protected void populateView(View view, final Cours cours) {
        /* GridView */
        TextView tv_coursDate = (TextView) view.findViewById(R.id.tv_name_cours);
        TextView tv_coursCapacity = (TextView) view.findViewById(R.id.tv_capacity_cours);
        TextView tv_coursDuration =(TextView) view.findViewById(R.id.tv_duration_cours);
        tv_coursDuration.setText(String.valueOf(cours.getClass_duration()));
        ImageView iv_staffImage = (ImageView) view.findViewById(R.id.iv_img_cours);
        ImageView iv_coursStatus= (ImageView) view.findViewById(R.id.iv_class_status);
        iv_coursStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] tab = {"Todo","In Progress","Done"};
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getmActivity());

                        alertDialog.setSingleChoiceItems(tab, 0, null)
                                .setTitle("Ajouter ce cours à liste...")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        HashMap<String, Object> hashMap = new HashMap<>();

                                        switch (selectedPosition){
                                            case 0:
                                                //hashMap.put(String.valueOf(cours.getClass_id()),cours);
                                               // mDatabase.child("DATABASE").child("DASHBOARD").child("TODO").updateChildren(hashMap);
                                                break;
                                            case 1:
                                                //mDatabase.child("DATABASE").child("DASHBOARD").child("INPROGRESS").updateChildren(hashMap);
                                                break;
                                            case 2:
                                                //mDatabase.child("DATABASE").child("DASHBOARD").child("DONE").updateChildren(hashMap);
                                                break;
                                        }

                                    }
                                }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.FRANCE);
        DateFormat date2Day = new SimpleDateFormat("EEE dd MMM",Locale.FRANCE);
        try {
            Date date = format.parse(cours.getClass_date());
            cours.setClass_date(date2Day.format(date));
            tv_coursDate.setText(cours.getClass_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_coursCapacity.setText(cours.getClass_bookable_capacity()+" places");
        tv_coursCapacity.setTextColor(getmActivity().getResources().getColor(R.color.colorAccent));
        Picasso.with(this.getmActivity().getBaseContext())
                .load(cours.getClass_level_icon_URL())
                .error(R.mipmap.ic_launcher)
                .resize(50, 50)
                .centerInside()
                .into(iv_staffImage);

    }


}
