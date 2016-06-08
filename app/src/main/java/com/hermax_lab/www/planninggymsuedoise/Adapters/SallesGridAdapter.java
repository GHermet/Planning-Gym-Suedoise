package com.hermax_lab.www.planninggymsuedoise.Adapters;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.hermax_lab.www.planninggymsuedoise.Classes.Classroom;
import com.hermax_lab.www.planninggymsuedoise.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Guillaume on 5/13/16.
 */
public class SallesGridAdapter extends FirebaseListAdapter<Classroom> {
    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public SallesGridAdapter(Query mRef, Class<Classroom> mModelClass, int mLayout, Activity activity) {
        super(mRef, mModelClass, mLayout, activity);

    }

    @Override
    protected void populateView(View view, Classroom salles) {
         /* GridView Salles */
        final TextView tv_sallesName = (TextView) view.findViewById(R.id.tv_name_salles);
        final TextView tv_sallesCity = (TextView) view.findViewById(R.id.tv_city_salles);
        final  ImageView iv_staffImage = (ImageView) view.findViewById(R.id.iv_img_salles);
        tv_sallesName.setText(salles.getClassroom_name());
        tv_sallesCity.setText(salles.getClassroom_city());

        Picasso.with(this.getmActivity().getBaseContext())
                .load(salles.getClassroom_photo_url())
                .error(R.mipmap.ic_launcher)
                .resize(50, 50)
                .centerCrop()
                .into(iv_staffImage);
    }
}
