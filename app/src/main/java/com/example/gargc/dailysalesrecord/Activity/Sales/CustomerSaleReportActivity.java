package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Model.CustomerProductSold;
import com.example.gargc.dailysalesrecord.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerSaleReportActivity extends AppCompatActivity
{
    private String date,customer;
    private RecyclerView itemList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,custDatabase,saleDatabase;
    private TextView tvDate,tvCustomer,tvEmail,tvPhone,tvCompany,tvSubtotal,tvTotal,tvTax,tvDisc;
    private Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sale_report);

        date = getIntent().getStringExtra("date");
        customer = getIntent().getStringExtra("customer");

        Log.i("getintent","date : "+date+" customer : "+customer);

        itemList = (RecyclerView)findViewById(R.id.customer_sales_item_list);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        //itemList.setHasFixedSize(true);

        tvDate = (TextView)findViewById(R.id.customer_sale_due_date);
        tvCustomer = (TextView)findViewById(R.id.customer_sale_name);
        tvEmail = (TextView)findViewById(R.id.customer_sale_email);
        tvPhone = (TextView)findViewById(R.id.customer_sale_phone);
        tvCompany = (TextView)findViewById(R.id.customer_sale_user_name);
        btnGenerate = (Button)findViewById(R.id.customer_sales_btn_invoice);
        tvTax = (TextView)findViewById(R.id.customer_sales_tax);
        tvTotal = (TextView)findViewById(R.id.customer_sales_total);
        tvSubtotal = (TextView)findViewById(R.id.customer_sales_subtotal);
        tvDisc = (TextView)findViewById(R.id.customer_sales_discount);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Sales").child(mAuth.getCurrentUser().getUid())
                .child(date).child(customer).child("ProductsSold");
        custDatabase = FirebaseDatabase.getInstance().getReference().child("Customer").child(mAuth.getCurrentUser().getUid())
                .child(customer);
        saleDatabase = FirebaseDatabase.getInstance().getReference().child("Sales").child(mAuth.getCurrentUser().getUid())
            .child(date).child(customer);

        tvDate.setText("INVOICE DATE : "+date);
        tvCustomer.setText(customer);

        custDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvEmail.setText(dataSnapshot.child("EmailId").getValue().toString());
                tvPhone.setText(dataSnapshot.child("PhoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saleDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvSubtotal.setText("Subtotal : "+dataSnapshot.child("Subtotal").getValue().toString());
                tvDisc.setText("Discount : "+dataSnapshot.child("Discount").getValue().toString());
                tvTax.setText("Tax : "+dataSnapshot.child("Tax").getValue().toString());
                tvTotal.setText("Total : "+dataSnapshot.child("Total").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<CustomerProductSold,CustSalesHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CustomerProductSold, CustSalesHolder>
                (
                        CustomerProductSold.class,
                        R.layout.single_sales_customer_item,
                        CustSalesHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(CustSalesHolder viewHolder, CustomerProductSold model, int position)
            {
                Log.i("products",model.getProductName());

                viewHolder.name.setText(model.getProductName());
                viewHolder.qty.setText(model.getQuantity());
                viewHolder.price.setText(model.getPrice());
                viewHolder.total.setText(model.getSubtotal());
            }
        };
        itemList.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    public static class CustSalesHolder extends RecyclerView.ViewHolder
    {
        TextView name,qty,price,total;

        public CustSalesHolder(View itemView)
        {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.single_cust_sales_name);
            qty = (TextView)itemView.findViewById(R.id.single_cust_sales_qty);
            price = (TextView)itemView.findViewById(R.id.single_cust_sales_price);
            total = (TextView)itemView.findViewById(R.id.single_cust_sales_total);
        }
    }

}
