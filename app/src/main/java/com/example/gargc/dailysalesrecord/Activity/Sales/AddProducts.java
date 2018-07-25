package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.Model.AddProduct;
import com.example.gargc.dailysalesrecord.Model.ProductContent;
import com.example.gargc.dailysalesrecord.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddProducts extends AppCompatActivity {

    Button buttonProduct;
    TextView productsellingprice,productactualprice,productprofit;
    int clickedPosition=-1;

    EditText quantityedittext;

    List<String> listOfCustomer;
    List<ProductContent> list;
    Button addProduct;
    String nameOfProduct=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        Log.i("click","true");

        findId();

        buttonProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click","ok");
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(AddProducts.this);

                alertDialogBuilder.setTitle("Select a Product");
                final View inflater=View.inflate(AddProducts.this,R.layout.alert_label_editor,null);
                ListView listView=(ListView) inflater.findViewById(R.id.customerList);
                alertDialogBuilder.setView(inflater);
                AlertDialog alertDialog=alertDialogBuilder.create();
                showList(listView,alertDialog);
                alertDialog.show();

            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("check","click");
                int numberOfItems;
                String text=quantityedittext.getText().toString();
                if(text.equals(""))
                {
                    numberOfItems=0;
                }
                else {
                   numberOfItems= Integer.parseInt(quantityedittext.getText().toString());
                }
                Log.i("check",numberOfItems+" ");
                if(nameOfProduct==null)
                {
                    Toast.makeText(AddProducts.this, "Select a Product", Toast.LENGTH_SHORT).show();
                    buttonProduct.requestFocus();
                }
                else if(numberOfItems==0)
                {
                    Toast.makeText(AddProducts.this, "Enter Number Of Quantity", Toast.LENGTH_SHORT).show();
                    quantityedittext.requestFocus();
                }
                else if(clickedPosition==-1 || Integer.parseInt(list.get(clickedPosition).getStock())<numberOfItems)
                {
                    if(clickedPosition==-1)
                    {
                        Toast.makeText(AddProducts.this, "Select a product", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddProducts.this, "Number Of Quantity is greater than Stock which is "+list.get(clickedPosition).getStock(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    AddProduct addProduct=new AddProduct();
                    addProduct.setProductName(list.get(clickedPosition).getName());
                    addProduct.setQuantity(numberOfItems);
                    addProduct.setActualPrice(Float.valueOf(productactualprice.getText().toString()));
                    addProduct.setSellingPrice( Float.valueOf(productsellingprice.getText().toString()));
                    addProduct.setProfit(Float.valueOf(productsellingprice.getText().toString()) - Float.valueOf(productactualprice.getText().toString()));
                    addProduct.setCgst(list.get(clickedPosition).getCgst());
                    addProduct.setSgst(list.get(clickedPosition).getSgst());
                    addProduct.setCess(list.get(clickedPosition).getCess());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",addProduct);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }

            }
        });

    }

    private void showList(final ListView listView, final AlertDialog alertDialog) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        listOfCustomer=new ArrayList<>();
        list=new ArrayList<>();
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("Products").child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("user",dataSnapshot.getChildrenCount()+" ");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductContent user = snapshot.getValue(ProductContent.class);
                    list.add(user);
                    Log.i("user",user.getName());
                    listOfCustomer.add(user.getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        AddProducts.this, android.R.layout.select_dialog_singlechoice, listOfCustomer );

                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        buttonProduct.setText(listOfCustomer.get(i));
                        nameOfProduct=listOfCustomer.get(i);
                        alertDialog.dismiss();
                        productsellingprice.setText(list.get(i).getSelling_price());
                        productactualprice.setText(list.get(i).getActual_price());
                        clickedPosition=i;
                        try {
                            Float a = Float.valueOf(productsellingprice.getText().toString()) - Float.valueOf(productactualprice.getText().toString());
                            productprofit.setText(a + " ");
                        }catch (Exception e)
                        {

                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void findId() {
        buttonProduct=(Button) findViewById(R.id.buttonProduct);
        productsellingprice=(TextView) findViewById(R.id.productsellingprice);
        productactualprice=(TextView) findViewById(R.id.productactualprice);
        productprofit=(TextView) findViewById(R.id.productprofit);
        addProduct=(Button) findViewById(R.id.addProduct);
        quantityedittext=(EditText) findViewById(R.id.quantityedittext);
    }
}
