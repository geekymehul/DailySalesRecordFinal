package com.example.gargc.dailysalesrecord.Reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gargc.dailysalesrecord.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarGraphFragment extends Fragment {


    public BarGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_graph, container, false);
    }

}
