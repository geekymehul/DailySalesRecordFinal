package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gargc.dailysalesrecord.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SalesItemInfoActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private DatabaseReference salesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item_info);
    }

}
