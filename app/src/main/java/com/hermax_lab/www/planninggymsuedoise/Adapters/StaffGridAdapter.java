package com.hermax_lab.www.planninggymsuedoise.Adapters;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hermax_lab.www.planninggymsuedoise.Activities.CoursDetailsActivity;
import com.hermax_lab.www.planninggymsuedoise.Classes.Staff;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Guillaume on 5/10/16.
 */
public class StaffGridAdapter extends FirebaseListAdapter<Staff> {
    private final View mainView;



    List<Staff> staffs;
    private String Query;

    public StaffGridAdapter(com.google.firebase.database.Query mArrayList, Class<Staff> mModelClass, int mLayout, Activity activity, View mainView) {
        super(mArrayList, mModelClass, mLayout, activity);
        this.mainView = mainView;
        this.staffs= new ArrayList<>();
    }

    public StaffGridAdapter(ArrayList<Staff> mTeachers, Class<Staff> staffClass, int list_item_staff_cours, CoursDetailsActivity coursDetailsActivity, View currentFocus) {
        super(mTeachers,staffClass,list_item_staff_cours,coursDetailsActivity);
        this.mainView = currentFocus;
    }


    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    @Override
    protected void populateView(View view, Staff staff) {
            /* GridView */
            TextView tv_staffName = (TextView) view.findViewById(R.id.tv_name_staff);
            TextView tv_staffCity = (TextView) view.findViewById(R.id.tv_city_staff);
            TextView tv_staffBusiness=(TextView) view.findViewById(R.id.tv_business_staff);
            ImageView iv_staffImage = (CircleImageView) view.findViewById(R.id.iv_img_staff);
            tv_staffName.setText(staff.getStaff_name());
            tv_staffCity.setText(staff.getStaff_city());
            tv_staffBusiness.setText(staff.getStaff_business()+" %");
            int color;
            if (staff.getStaff_business()>=75) color=R.color.colorRunning;
            else if (staff.getStaff_business()>=50) color=R.color.colorStandard;
            else if (staff.getStaff_business()>=25) color =R.color.colorIntensif;
            else color=R.color.colorClubbing;
                tv_staffBusiness.setTextColor(getmActivity().getResources().getColor(color));
            ColorGenerator generator = ColorGenerator.MATERIAL;
             color = generator.getColor(staff.getStaff_name());
            TextDrawable circleLetter = TextDrawable.builder()
                    .buildRound(staff.getStaff_name().substring(0, 1), color);

           Picasso.with(this.getmActivity().getBaseContext())
                    .load(staff.getStaff_photo_large_url())
                    .error(circleLetter)
                    .resize(50, 50)
                    .centerCrop()
                    .into(iv_staffImage);


        }





}
