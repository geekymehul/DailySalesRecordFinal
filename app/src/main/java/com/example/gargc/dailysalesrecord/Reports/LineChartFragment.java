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

        chart = view.findViewById(R.id.linechart);

        salesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    float d = Float.parseFloat(snapshot.child("total").getValue()+"");
                    int data = (int)d;
                    String date = snapshot.child("date").getValue()+"";

                    Log.i("going","going");
                    Log.i("data",data+"");

                    records.add(new Entry(data,c));

                    Log.i("records",""+records.toString());

                    Log.i("this","1");

                    labels.add(date);
                    c++;
                }

                Log.i("size",records.size()+"");

                Log.i("this","2");
                LineDataSet dataSet = new LineDataSet(records, "sales");
                dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                // Setting Data
                LineData data = new LineData(labels,dataSet);
                chart.setData(data);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*records.add(new Entry(1200,1));
        records.add(new Entry(890,2));
        records.add(new Entry(1050,3));
        records.add(new Entry(1320,4));
        records.add(new Entry(1900,5));
        records.add(new Entry(670,6));
        records.add(new Entry(1100,7));
        records.add(new Entry(1670,9));

        labels.add("10");
        labels.add("12");
        labels.add("15");
        labels.add("18");
        labels.add("20");
        labels.add("23");
        labels.add("28");
        labels.add("31");
*/

        //chart.animateX(2500);


        return view;
    }

}
