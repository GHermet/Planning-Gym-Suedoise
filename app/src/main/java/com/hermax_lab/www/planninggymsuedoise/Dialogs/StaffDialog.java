package com.hermax_lab.www.planninggymsuedoise.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hermax_lab.www.planninggymsuedoise.Classes.FIREBASE;
import com.hermax_lab.www.planninggymsuedoise.Classes.Staff;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Guillaume on 6/6/16.
 */
public class StaffDialog extends Dialog {
    private final Staff mStaff;

    public StaffDialog(Context context,Staff staff) {
        super(context);
        this.mStaff = staff;
        setContentView(R.layout.dialog_staff_details_cours);
         /* Details Card */
        final CardView cv_staffDetails = (CardView) findViewById(R.id.cv_staff_details);
        TextView tv_staffName_Details = (TextView) findViewById(R.id.tv_name_staff_details);
        TextView tv_staffCity_Details = (TextView) findViewById(R.id.tv_city_staff_details);
        CircleImageView iv_staffImage_Details = (CircleImageView) findViewById(R.id.iv_img_staff_details);
        TextView tv_staffMobile_Details = (TextView) findViewById(R.id.tv_mobile_staff_details);
        final TextView tv_staffEmail_Details = (TextView) findViewById(R.id.tv_email_staff_details);
        final LinearLayout ll_staffEmail_Details = (LinearLayout) findViewById(R.id.ll_mail);
        ll_staffEmail_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", mStaff.getStaff_email(), null));
                getContext().startActivity(Intent.createChooser(emailIntent, "Envoyer un email à "+mStaff.getStaff_name()));
            }
        });
        TextView tv_staffAdress_Details = (TextView) findViewById(R.id.tv_adress_staff_details);
        TextView tv_staffJob_Details = (TextView) findViewById(R.id.tv_job_staff_details);
        final ImageView iv_close_details = (ImageView) findViewById(R.id.iv_close_details);
        iv_close_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_staffName_Details.setText(mStaff.getStaff_name());
        tv_staffCity_Details.setText(mStaff.getStaff_city());
        tv_staffEmail_Details.setText(mStaff.getStaff_email());
        tv_staffMobile_Details.setText(mStaff.getStaff_mobile());
        tv_staffAdress_Details.setText(mStaff.getStaff_adress());
        tv_staffJob_Details.setText(mStaff.getStaff_job());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT // generate random color
        int color = generator.getColor(mStaff.getStaff_name());
        TextDrawable circleLetter = TextDrawable.builder()
                .buildRound(mStaff.getStaff_name().substring(0,1), color);
        Picasso.with(getContext())
                .load(mStaff.getStaff_photo_large_url())
                .resize(100, 100)
                .centerCrop()
                .error(circleLetter)
                .into(iv_staffImage_Details);

          /* GridView Intensités */
        final GridView mIntensiteGridView = (GridView) findViewById(R.id.gridview_intensites);
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
                View view =  getLayoutInflater().inflate(R.layout.grid_item_intensite, parent, false);
                ImageView iv_intensite = (ImageView) view.findViewById(R.id.iv_intensite);
                Picasso.with(getContext())
                        .load(intensite_URLs.get(position))
                        .centerCrop()
                        .resize(40,40)
                        .into(iv_intensite);
                return view;
            }
        };
        for (final Integer intensite_id:intensite_ids) {
            Log.v("intensite id", String.valueOf(intensite_id));
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(FIREBASE.REF);
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
}
