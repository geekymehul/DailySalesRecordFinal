package com.example.gargc.dailysalesrecord.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private FirebaseAuth mAuth;
    private DatabaseReference productDatabase;
    private Spinner edt_qty;
    private EditText edt_name,edt_currency,edt_stock,edt_sell,edt_actual,edt_notes,edt_sku,edt_subcat;
    private ImageView btn_img;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mAuth = FirebaseAuth.getInstance();
        productDatabase = FirebaseDatabase.getInstance().getReference().child("Products").child(mAuth.getCurrentUser().getUid());
        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(AddProductActivity.this);

        edt_actual = (EditText)findViewById(R.id.add_pro_edt_actual_price);
        edt_currency = (EditText)findViewById(R.id.add_pro_edt_curr);
        edt_name = (EditText)findViewById(R.id.add_pro_edt_name);
        edt_qty = (Spinner) findViewById(R.id.add_pro_edt_qty);
        edt_stock = (EditText)findViewById(R.id.add_pro_edt_stock);
        edt_sell = (EditText)findViewById(R.id.add_pro_edt_selling_price);
        edt_notes = (EditText)findViewById(R.id.add_pro_edt_notes);
        edt_sku = (EditText)findViewById(R.id.add_pro_edt_sku);
        btn_img = (ImageView)findViewById(R.id.add_pro_btn_img);
        btn_add = (Button)findViewById(R.id.add_pro_btn_add);
        edt_subcat = (EditText)findViewById(R.id.add_pro_edt_subcat);

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 1);

            }
        });

        edt_qty.setOnItemSelectedListener(this);
        // set up the list for spinner
        List<String> categories = new ArrayList<String>();
        categories.add("Box");
        categories.add("Case");
        categories.add("Centimeter");
        categories.add("Feet");
        categories.add("Gallon");
        categories.add("Gram");
        categories.add("Inch");
        categories.add("Kilogram");
        categories.add("Kilometer");
        categories.add("Meter");
        categories.add("Litre");
        categories.add("Miligram");
        categories.add("Mililitre");
        categories.add("Pack");
        categories.add("Piece");
        categories.add("Set");
        categories.add("Ton");
        categories.add("Unit");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        edt_qty.setAdapter(dataAdapter2);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String actual = edt_actual.getText().toString();
                final String sell = edt_sell.getText().toString();
                final String name = edt_name.getText().toString();
                final String currency = edt_currency.getText().toString();
                final String sku = edt_sku.getText().toString();
                final String stock = edt_stock.getText().toString();
                final String qty = edt_qty.getSelectedItem().toString();;
                final String subcat = edt_subcat.getText().toString();
                final String notes = edt_notes.getText().toString();

                if(TextUtils.isEmpty(actual) || TextUtils.isEmpty(sell) || TextUtils.isEmpty(name) || TextUtils.isEmpty(currency) ||
                        TextUtils.isEmpty(stock) ||mImageUri == null)
                {
                    Toast.makeText(AddProductActivity.this, "Enter these details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgress.setTitle("Posting to Blog");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                        StorageReference filepath = mStorage.child("Products").child(mAuth.getCurrentUser().getUid())
                                .child(mImageUri.getLastPathSegment());
                        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                @SuppressWarnings("VisibleForTests") final Uri imgUrl = taskSnapshot.getDownloadUrl();

                                DatabaseReference info = productDatabase.child(name);
                                info.child("name").setValue(name);
                                info.child("selling_price").setValue(sell);
                                info.child("actual_price").setValue(actual);
                                info.child("quantity").setValue(qty);
                                info.child("image").setValue(imgUrl.toString());
                                info.child("currency").setValue(currency);
                                info.child("stock").setValue(stock);
                                info.child("sku").setValue(sku);
                                info.child("notes").setValue(notes);
                                info.child("subcategory").setValue(subcat);

                                Toast.makeText(AddProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        });

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            mImageUri = data.getData();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
