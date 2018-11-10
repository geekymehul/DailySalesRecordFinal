package com.example.gargc.dailysalesrecord.Activity.FutureSales;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Activity.Sales.AddSalesActivity;
import com.example.gargc.dailysalesrecord.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PredictActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private DatabaseReference predictDatabase;
    private ImageView img1,img2;
    private String itemName;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

        img1 = (ImageView)findViewById(R.id.predict_img1);
        img2 = (ImageView)findViewById(R.id.predict_img2);
        itemName = getIntent().getStringExtra("name");
        tableLayout = findViewById(R.id.tableLayout);

        Log.i("intent",itemName);
        mAuth = FirebaseAuth.getInstance();
        predictDatabase = FirebaseDatabase.getInstance().getReference().child("FutureSales").child(itemName);

        predictDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Picasso.with(getApplicationContext()).load(dataSnapshot.child("ForecastSales").getValue().toString()).into(img1);
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("SeasonalSales").getValue().toString()).into(img2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        predictDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot = dataSnapshot.child("Data");

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String date = snapshot.child("Date").getValue().toString();
                    String sales = snapshot.child("Sales").getValue().toString();

                    Log.i("date",date);
                    Log.i("sales",sales);

                    TableRow tr_head=new TableRow(getApplicationContext());
                    tr_head.setBackgroundColor(Color.WHITE);        // part1
                    tr_head.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView Detail = new TextView(getApplicationContext());
                    Detail.setText(date);
                    Detail.setTextColor(Color.BLACK);
                    Detail.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,2f));
                    tr_head.addView(Detail);

                    TextView Quantity = new TextView(getApplicationContext());
                    Quantity.setText(sales);
                    Quantity.setTextColor(Color.BLACK);
                    Quantity.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f));
                    tr_head.addView(Quantity);

                    tableLayout.addView(tr_head, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
