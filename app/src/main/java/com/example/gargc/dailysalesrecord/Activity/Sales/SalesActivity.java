package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gargc.dailysalesrecord.R;

public class SalesActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Log.i("check","true3");

        floatingActionButton=(FloatingActionButton) findViewById(R.id.addSale);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SalesActivity.this,AddSalesActivity.class);
                startActivity(intent);

            }
        });
    }
}
