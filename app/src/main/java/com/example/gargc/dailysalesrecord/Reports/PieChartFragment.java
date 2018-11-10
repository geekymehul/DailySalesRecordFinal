package com.example.gargc.dailysalesrecord.Reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gargc.dailysalesrecord.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment {


    public PieChartFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        PieChart pieChart = (PieChart) view.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(11f, 0));
        yvalues.add(new Entry(12f, 1));
        yvalues.add(new Entry(9f, 2));

        PieDataSet dataSet = new PieDataSet(yvalues, "Products");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Technology");
        xVals.add("Furniture");
        xVals.add("Office Supplies");

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setDrawHoleEnabled(false);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setDescription("Sales contribution of each product");

        return view;
    }

}
