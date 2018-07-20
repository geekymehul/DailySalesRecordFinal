package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Activity.EditProductActivity;
import com.example.gargc.dailysalesrecord.Activity.ProductActivity;
import com.example.gargc.dailysalesrecord.Model.SalesContent;
import com.example.gargc.dailysalesrecord.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SalesActivity extends AppCompatActivity
{

    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private DatabaseReference salesDatabase;
    private RecyclerView salesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        mAuth = FirebaseAuth.getInstance();
        salesDatabase = FirebaseDatabase.getInstance().getReference().child("SalesInfo").child(mAuth.getCurrentUser().getUid());

        salesList = (RecyclerView)findViewById(R.id.sales_list);
        salesList.setLayoutManager(new LinearLayoutManager(this));
        salesList.setHasFixedSize(true);


        floatingActionButton=(FloatingActionButton) findViewById(R.id.addSale);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SalesActivity.this,AddSalesActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<SalesContent,SalesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SalesContent, SalesViewHolder>
                (
                        SalesContent.class,
                        R.layout.single_sales_info,
                        SalesViewHolder.class,
                        salesDatabase
                ) {
            @Override
            protected void populateViewHolder(SalesViewHolder viewHolder, SalesContent model, int position)
            {
                final String sales_key = getRef(position).getKey();

                viewHolder.date.setText("Date : "+model.getDate());
                viewHolder.paid.setText("Paid : "+model.getPaid());
                viewHolder.total.setText("Total :"+model.getTotal());
                viewHolder.discount.setText("Discount :  "+model.getDiscount());
                viewHolder.tax.setText("Tax : "+model.getTax());
                viewHolder.shipping.setText("Shipping : "+model.getShipping());
                viewHolder.profit.setText("Profit : "+model.getProfit());

                viewHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent viewIntent = new Intent(SalesActivity.this,SalesItemInfoActivity.class);
                        viewIntent.putExtra("product_id",sales_key);
                        startActivity(viewIntent);
                    }
                });
            }
        };

        salesList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class SalesViewHolder extends RecyclerView.ViewHolder
    {
        TextView date,paid,profit,tax,discount,shipping,total;
        Button viewDetails;

        public SalesViewHolder(View itemView)
        {
            super(itemView);

            date = (TextView)itemView.findViewById(R.id.sales_info_date);
            paid = (TextView)itemView.findViewById(R.id.sales_info_paid);
            profit = (TextView)itemView.findViewById(R.id.sales_info_Profit);
            tax = (TextView)itemView.findViewById(R.id.sales_info_tax);
            discount = (TextView)itemView.findViewById(R.id.sales_info_discount);
            shipping = (TextView)itemView.findViewById(R.id.sales_info_shipping);
            total = (TextView)itemView.findViewById(R.id.sales_info_total);
            viewDetails = (Button)itemView.findViewById(R.id.sales_info_btn);
        }

    }

}
