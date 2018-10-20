package com.example.gargc.dailysalesrecord.Reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartFragment extends Fragment
{
    private DatabaseReference salesDatabase;
    private ArrayList<Entry> records;
    private FirebaseAuth mAuth;
    private LineChart chart;
    private int c = 0;
    private ArrayList<String> labels;

    public LineChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);

        mAuth = FirebaseAuth.getInstance();
        records = new ArrayList<>();
        labels = new ArrayList<>();
        salesDatabase = FirebaseDatabase.getInstance().getReference().child("SalesInfo").child(mAuth.getCurrentUser().getUid());

        salesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    int data = Integer.parseInt(snapshot.child("total").getValue()+"");
                    String date = snapshot.child("date").getValue()+"";

                    Log.i("going","going");
                    Log.i("data",data+"");

                    records.add(new Entry(c,data));
                    labels.add(date);
                    c++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chart = view.findViewById(R.id.linechart);

        LineDataSet dataSet = new LineDataSet(records, "sales");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        // Setting Data
        LineData data = new LineData(labels,dataSet);
        chart.setData(data);
        chart.animateX(2500);


        return view;
    }

}
