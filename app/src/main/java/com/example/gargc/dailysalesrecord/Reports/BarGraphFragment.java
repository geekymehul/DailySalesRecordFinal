package com.example.gargc.dailysalesrecord.Reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gargc.dailysalesrecord.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarGraphFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference productDatabase;
    private ArrayList<String> productList;
    private ArrayList<Integer> productSales;
    private HashMap<String,Integer> map;
    BarChart barChart;
    public BarGraphFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bar_graph, container, false);

        mAuth = FirebaseAuth.getInstance();
        productList = new ArrayList<>();
        productSales = new ArrayList<>();
        map = new HashMap<>();
        productDatabase = FirebaseDatabase.getInstance().getReference().child("Products").child(mAuth.getCurrentUser().getUid());
        barChart = (BarChart) view.findViewById(R.id.barchart);

        productDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String name = snapshot.child("name").getValue().toString();
                    productList.add(name);
                    map.put(name,0);
                }

                Log.i("map",map.toString());
                Log.i("productlist",productList.toString());

                productDatabase = FirebaseDatabase.getInstance().getReference().child("Sales").child(mAuth.getCurrentUser().getUid());
                productDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            for (DataSnapshot shot : snapshot.getChildren())
                            {
                                DataSnapshot cur = shot.child("ProductsSold");
                                for (DataSnapshot p : cur.getChildren())
                                {
                                    String pn = p.child("ProductName").getValue().toString();
                                    float pq = Float.parseFloat(p.child("Quantity").getValue().toString());
                                    float k = map.get(pn);
                                    k = k+pq;
                                    int q = (int)k;
                                    map.put(pn,q);
                                }
                            }
                        }

                        Log.i("map",map.toString());

                        productList = new ArrayList<String>(map.keySet());
                        productSales = new ArrayList<Integer>(map.values());

                        Log.i("array",productList.toString());
                        Log.i("array",productSales.toString());

                        //-------------------BAR GRAPH--------------------
                        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
                        int c=0;
                        for(int i=0;i<productSales.size();i++)
                            bargroup1.add(new BarEntry(productSales.get(i),c++));

                        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "actual sales");
                        BarData data = new BarData(productList,barDataSet1);
                        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

                        barChart.setDescription("Product-wise sales");
                        barChart.setData(data);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

}
