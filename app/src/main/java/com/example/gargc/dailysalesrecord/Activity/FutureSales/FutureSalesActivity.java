package com.example.gargc.dailysalesrecord.Activity.FutureSales;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Activity.AddProductActivity;
import com.example.gargc.dailysalesrecord.Model.ProductContent;
import com.example.gargc.dailysalesrecord.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

public class FutureSalesActivity extends AppCompatActivity {

    private RecyclerView productList;
    private DatabaseReference productDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productList = (RecyclerView)findViewById(R.id.product_list);
        productList.setLayoutManager(new LinearLayoutManager(this));
        productList.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.product_btn_add);
        fab.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        productDatabase = FirebaseDatabase.getInstance().getReference().child("Products").child(mAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<ProductContent,ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductContent, FutureSalesActivity.ProductViewHolder>
                (
                        ProductContent.class,
                        R.layout.single_product_future_item_sales,
                        FutureSalesActivity.ProductViewHolder.class,
                        productDatabase
                ) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, final ProductContent model, int position)
            {
                final String product_key = getRef(position).getKey();

                viewHolder.nameOfProduct.setText(model.getName());

                if(!model.getImage().equals("none"))
                    Picasso.with(getApplicationContext()).load(model.getImage()).into(viewHolder.img);

                final DatabaseReference itemDatabase = productDatabase.child(model.getName());

                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String item = model.getName();
                        Intent predictIntent = new Intent(getApplicationContext(),PredictActivity.class);
                        predictIntent.putExtra("name",item);
                        startActivity(predictIntent);
                    }
                });

            }
        };

        productList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameOfProduct;
        ImageView img;
        RelativeLayout container;

        public ProductViewHolder(View itemView)
        {
            super(itemView);

            container = (RelativeLayout) itemView.findViewById(R.id.futuresales_item);
            img = (ImageView)itemView.findViewById(R.id.imageOfProduct);
            nameOfProduct = (TextView)itemView.findViewById(R.id.textview);



        }

    }



}