package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Model.SalesItemContent;
import com.example.gargc.dailysalesrecord.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SalesItemInfoActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private DatabaseReference salesDatabase;
    private String date;
    private RecyclerView salesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item_info);

        date = getIntent().getStringExtra("sales_id");

        mAuth = FirebaseAuth.getInstance();
        salesDatabase = FirebaseDatabase.getInstance().getReference().child("Sales").child(mAuth.getCurrentUser().getUid()).child(date);

        salesList = (RecyclerView)findViewById(R.id.sales_single_item_list);
        salesList.setLayoutManager(new LinearLayoutManager(this));
        salesList.setHasFixedSize(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<SalesItemContent,ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SalesItemContent, ItemViewHolder>
                (
                        SalesItemContent.class,
                        R.layout.single_sales_item_info,
                        ItemViewHolder.class,
                        salesDatabase
                ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, SalesItemContent model, int position)
            {
                final String customer = getRef(position).getKey();

                viewHolder.profit.setText("Profit Earned: "+model.getProfit());
                viewHolder.paid.setText("Paid : "+model.getPaid());
                viewHolder.status.setText("Status : "+model.getStatus());
                viewHolder.total.setText("Total : "+model.getTotal());
                viewHolder.disc.setText("Discount : "+model.getDiscount());
                viewHolder.discPer.setText("Discount : "+model.getDiscountPercentage()+"%");
                viewHolder.tax.setText("Tax : "+model.getTax());
                viewHolder.taxPer.setText("Tax : "+model.getTaxPercentage()+"%");
                viewHolder.customer.setText("Customer : "+model.getCustomer());

                viewHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent viewIntent = new Intent(SalesItemInfoActivity.this,CustomerSaleReportActivity.class);
                        viewIntent.putExtra("date",date);
                        viewIntent.putExtra("customer",customer);
                        startActivity(viewIntent);
                    }
                });

            }
        };
        salesList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView customer,status,total,paid,disc,discPer,tax,taxPer,profit;
        Button viewDetails;

        public ItemViewHolder(View itemView)
        {
            super(itemView);

            customer = (TextView)itemView.findViewById(R.id.single_sales_item_customer);
            status = (TextView)itemView.findViewById(R.id.single_sales_item_status);
            total = (TextView)itemView.findViewById(R.id.single_sales_item_total);
            paid = (TextView)itemView.findViewById(R.id.single_sales_item_paid);
            disc = (TextView)itemView.findViewById(R.id.single_sales_item_disc);
            discPer = (TextView)itemView.findViewById(R.id.single_sales_item_disc_per);
            tax = (TextView)itemView.findViewById(R.id.single_sales_item_tax);
            taxPer = (TextView)itemView.findViewById(R.id.single_sales_item_tax_per);
            profit = (TextView)itemView.findViewById(R.id.single_sales_item_profit);
            viewDetails = (Button)itemView.findViewById(R.id.single_sales_item_view_details);
        }

    }

}
