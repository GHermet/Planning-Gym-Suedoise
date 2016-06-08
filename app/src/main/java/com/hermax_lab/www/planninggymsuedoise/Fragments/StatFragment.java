package com.hermax_lab.www.planninggymsuedoise.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.ChartData;
import com.hermax_lab.www.planninggymsuedoise.R;

/**
 * Created by Guillaume on 5/10/16.
 */
public class StatFragment extends Fragment {

    private BarChart barChart1;
    private BarChart barChart2;

    public static StatFragment newInstance() {
        return new StatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stat, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* Chart1
        BarChart mBarChart1 = (BarChart) getActivity().findViewById(R.id.barChart1);
        ArrayList<BarEntry> valsComp1 = new ArrayList<BarEntry>();
        BarDataSet setComp1 = new BarDataSet(valsComp1, "Company 1");
        setComp1.setColors(new int[] { R.color.colorAccent, R.color.colorPrimary }, getContext());
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(setComp1);
        BarData data = new BarData(xVals, dataSets);
        mBarChart1.setData(data);
        mBarChart1.invalidate(); // refresh
        */

        /* Chart 2
        barChart2 = (BarChart) getActivity().findViewById(R.id.barChart2); */



    }

    public void setData(ChartData data) {

    }
    @Override
    public void onResume() {
        super.onResume();
        SearchView mSearchView = (SearchView) getActivity().findViewById(R.id.sv_tab_activity);
       // mSearchView.setVisibility(View.INVISIBLE);

    }


}
