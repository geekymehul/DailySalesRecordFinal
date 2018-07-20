package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.Activity.CustomerActivity;
import com.example.gargc.dailysalesrecord.Model.AddProduct;
import com.example.gargc.dailysalesrecord.Model.CustomerContent;
import com.example.gargc.dailysalesrecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;

public class AddSalesActivity extends AppCompatActivity {

    Button addSalesCustomer,addSalesProduct,add;
    Spinner spinnerDateIn,statusSpinner;

    TextView total,AmountToBePaid,Due,profit;

    TableLayout tableLayout;

    EditText discount,noteOfSales,taxToBeCharged,shippingCharge,amountPaidAlready;
    Float taxPercentage=0f,discountPercentage=0f;

    Float totalAmount= 0f,productAmount=0f;

    int selectDiscountType=-1,taxDiscountType=-1;
    Float lastDiscount=0f,lastTax=0f,lastShippingCharge=0f,amountAlreadyPaid=0f;

    Float profitLeft=0f;

    ArrayList<AddProduct> addProductArrayList=new ArrayList<>();



    Float totalProfit;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String nameOfCustomer="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);

        findId();
        findFirebase();

        addSalesCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(AddSalesActivity.this);

                alertDialogBuilder.setTitle("Select a Customer");
                final View inflater=View.inflate(AddSalesActivity.this,R.layout.alert_label_editor,null);
                ListView listView=(ListView) inflater.findViewById(R.id.customerList);
                alertDialogBuilder.setView(inflater);
                AlertDialog alertDialog=alertDialogBuilder.create();
                showList(listView,alertDialog);
                alertDialog.show();
            }
        });

        CalendarSpinnerAdapter mSpinnerDateInAdapter = new CalendarSpinnerAdapter(AddSalesActivity.this, Calendar.getInstance(), 30);
        spinnerDateIn.setAdapter(mSpinnerDateInAdapter);

        addSalesProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("check1","true1");
                Intent intent=new Intent(AddSalesActivity.this,AddProducts.class);
                startActivityForResult(intent,1);
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectDiscountType==-1) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddSalesActivity.this);
                    alertDialogBuilder.setTitle("Choose Discount Type");
                    final View inflater = View.inflate(AddSalesActivity.this, R.layout.alert_label_editor, null);
                    ListView listView = (ListView) inflater.findViewById(R.id.customerList);
                    alertDialogBuilder.setView(inflater);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    showDiscountType(listView, alertDialog);
                    alertDialog.show();
                }


            }
        });

        taxToBeCharged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectDiscountType==-1)
                {
                    Toast.makeText(AddSalesActivity.this, "Please select discount first", Toast.LENGTH_SHORT).show();
                }
                else if(taxDiscountType==-1)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddSalesActivity.this);
                    alertDialogBuilder.setTitle("Tax Type");
                    final View inflater = View.inflate(AddSalesActivity.this, R.layout.alert_label_editor, null);
                    ListView listView = (ListView) inflater.findViewById(R.id.customerList);
                    alertDialogBuilder.setView(inflater);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    showTaxType(listView, alertDialog);
                    alertDialog.show();

                }
            }
        });

        discount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    Log.i("finish","yo");
                    if(selectDiscountType==-1)
                    {
                        Toast.makeText(AddSalesActivity.this, "Please Select Discount Type", Toast.LENGTH_SHORT).show();
                        discount.requestFocus();
                    }
                    else
                    {
                        // WORK UPON THIS

                        Float dis=Float.valueOf(discount.getText().toString());
                        Float remove=(totalAmount*dis)/100;
                        if(selectDiscountType==0)
                        {

                            totalAmount=totalAmount-dis+lastDiscount;
                            AmountToBePaid.setText(totalAmount+" ");
                            lastDiscount=dis;

                        }
                        else
                        {
                            totalAmount=totalAmount-remove+lastDiscount;
                            AmountToBePaid.setText(totalAmount+" ");
                            lastDiscount=remove;
                            discount.setText(dis+"%");
                            discountPercentage=dis;
                        }
                    }
                }
                else {
                    Log.i("finish","no");
                }
            }
        });

        taxToBeCharged.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b)
                {
                    Log.i("finish","yo");
                    if(taxDiscountType==-1)
                    {
                        Toast.makeText(AddSalesActivity.this, "Please Select Tax Type", Toast.LENGTH_SHORT).show();
                        taxToBeCharged.requestFocus();
                    }
                    else
                    {
                        // WORK UPON THIS

                        Float tax=Float.valueOf(taxToBeCharged.getText().toString());
                        Float remove=(totalAmount*tax)/100;

                         if(taxDiscountType==0)
                        {

                            totalAmount=totalAmount+tax-lastTax;
                            AmountToBePaid.setText(totalAmount+" ");
                            lastTax=tax;

                        }
                        else
                        {
                            totalAmount=totalAmount+remove-lastTax;
                            AmountToBePaid.setText(totalAmount+" ");
                            lastTax=remove;
                            taxToBeCharged.setText(tax+"%");
                            taxPercentage=tax;
                        }
                    }
                }
                else {
                    Log.i("finish","no");
                }
            }

        });

        shippingCharge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    Float amount=Float.valueOf(shippingCharge.getText().toString());
                    totalAmount=totalAmount+amount-lastShippingCharge;
                    lastShippingCharge=amount;
                    AmountToBePaid.setText(totalAmount+" ");
                }

            }
        });

        amountPaidAlready.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    Float paidAmount=Float.valueOf(amountPaidAlready.getText().toString());
                    amountAlreadyPaid=paidAmount;
                    Float d=totalAmount-paidAmount;
                    Due.setText(d+" ");
                    profitLeft=totalAmount-totalProfit;
                    profit.setText(profitLeft+" ");
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameOfCustomer.equals(""))
                {
                    Toast.makeText(AddSalesActivity.this, "Enter Customer Name", Toast.LENGTH_SHORT).show();
                }
                else if(totalAmount==0f || lastDiscount==0f || lastTax==0f || lastShippingCharge==0f || amountAlreadyPaid==0f)
                {
                    Toast.makeText(AddSalesActivity.this, "You haven't filled all details", Toast.LENGTH_SHORT).show();
                }
                else if(selectDiscountType==-1||taxDiscountType==-1)
                {
                    Toast.makeText(AddSalesActivity.this, "Select Discount And Tax Type", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.i("success","checking");
                    Log.i("success",spinnerDateIn.getSelectedItem()+" ");
                    Calendar cal = (Calendar) spinnerDateIn.getSelectedItem();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM y");
                    System.out.println(dateFormat.format(cal.getTime()));
                    Log.i("success",dateFormat.format(cal.getTime()));

                    final DatabaseReference database = mDatabase.child(dateFormat.format(cal.getTime())).child(nameOfCustomer);

                    HashMap<String,String> hashMap=new HashMap<String, String>();
                    hashMap.put("Subtotal",productAmount+"");
                    hashMap.put("Discount",lastDiscount+"");
                    hashMap.put("DiscountType",selectDiscountType+"");
                    hashMap.put("DiscountPercentage",discountPercentage+"");
                    hashMap.put("Tax",lastTax+"");
                    hashMap.put("TaxType",taxDiscountType+"");
                    hashMap.put("TaxPercentage",taxPercentage+"");
                    hashMap.put("Shipping",lastShippingCharge+"");
                    hashMap.put("Total",totalAmount+" ");
                    hashMap.put("Paid",amountAlreadyPaid+"");
                    hashMap.put("Profit",profitLeft+"");
                    hashMap.put("Note",noteOfSales.getText().toString()+"");
                    hashMap.put("Status",statusSpinner.getSelectedItem().toString());
                    database.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference productList=database.child("ProductsSold");
                                Log.i("success",addProductArrayList.size()+" ");
                                for(int i=0;i<addProductArrayList.size();i++)
                                {

                                    AddProduct addProduct=addProductArrayList.get(i);
                                    DatabaseReference databaseReference=productList.child(addProduct.getProductName()+i);

                                    HashMap<String,String> hashMap=new HashMap<String, String>();
                                    hashMap.put("ProductName",addProduct.getProductName());
                                    hashMap.put("Quantity",addProduct.getQuantity()+"");
                                    hashMap.put("Price",addProduct.getSellingPrice()+"");
                                    float total=addProduct.getQuantity()*addProduct.getSellingPrice();
                                    hashMap.put("Subtotal",total+"");

                                    Log.i("success","entering");


                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Intent intent=new Intent(AddSalesActivity.this,SalesActivity.class);
                                                startActivity(intent);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Log.i("success","Failure");
                                                Toast.makeText(AddSalesActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            }

                        }
                    });


                }

            }
        });
        addStatusSpinner();


    }

    private void findFirebase() {

        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Sales").child(mAuth.getCurrentUser().getUid());
    }

    private void showTaxType(ListView listView, final AlertDialog alertDialog) {

        final List<String> listOfCustomer=new ArrayList<>();
        listOfCustomer.add("Flat");
        listOfCustomer.add("Percent(%)");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                AddSalesActivity.this, android.R.layout.select_dialog_singlechoice, listOfCustomer );

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taxDiscountType=i;
                alertDialog.dismiss();
            }
        });


    }

    private void addStatusSpinner() {

        List<String> categories = new ArrayList<String>();
        categories.add("Pending");
        categories.add("Awaiting Payment");
        categories.add("Awaiting Fulfillment");
        categories.add("Awaiting Pickup");
        categories.add("Partially Shipped");
        categories.add("Completed");
        categories.add("Shipped");
        categories.add("Cancelled");
        categories.add("Declined");
        categories.add("Refunded");
        categories.add("Disputed");
        categories.add("Verification Required");
        categories.add("Partially Refunded");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        statusSpinner.setAdapter(dataAdapter2);

    }

    private void showDiscountType(ListView listView, final AlertDialog alertDialog) {
        final List<String> listOfCustomer=new ArrayList<>();
        listOfCustomer.add("Flat Price");
        listOfCustomer.add("Percent(%)");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                AddSalesActivity.this, android.R.layout.select_dialog_singlechoice, listOfCustomer );

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectDiscountType=i;
                alertDialog.dismiss();
            }
        });

    }

    private void showList(final ListView listView, final AlertDialog alertDialog) {
        FirebaseAuth  mAuth = FirebaseAuth.getInstance();
        final List<String> listOfCustomer=new ArrayList<>();
        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference().child("Customer").child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("user",dataSnapshot.getChildrenCount()+" ");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerContent user = snapshot.getValue(CustomerContent.class);
                    Log.i("user",user.getCustomerName());
                    listOfCustomer.add(user.getCustomerName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        AddSalesActivity.this, android.R.layout.select_dialog_singlechoice, listOfCustomer );

                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        addSalesCustomer.setText(listOfCustomer.get(i));
                        nameOfCustomer=listOfCustomer.get(i);
                        alertDialog.dismiss();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void findId() {

        addSalesCustomer=(Button) findViewById(R.id.addSalesCustomer);
        spinnerDateIn = (Spinner) findViewById(R.id.currentDateOfSales);
        addSalesProduct=(Button) findViewById(R.id.addSalesProduct);
        add=(Button) findViewById(R.id.add);

        tableLayout=(TableLayout) findViewById(R.id.tableLayout);
        total=(TextView) findViewById(R.id.totalAmount);
        AmountToBePaid=(TextView) findViewById(R.id.AmountToBePaid);
        noteOfSales=(EditText) findViewById(R.id.noteOfSales);
        taxToBeCharged=(EditText) findViewById(R.id.taxToBeCharged);
        shippingCharge=(EditText) findViewById(R.id.shippingCharge);
        Due=(TextView) findViewById(R.id.due);

        discount=(EditText) findViewById(R.id.discount);
        statusSpinner=(Spinner) findViewById(R.id.statusSpinner);
        amountPaidAlready=(EditText) findViewById(R.id.amountPaidAlready);
        profit=(TextView) findViewById(R.id.profit);
    }

    public class CalendarSpinnerAdapter extends BaseAdapter {

        private SimpleDateFormat mDateFormat = new SimpleDateFormat("d MMM yyyy");

        private LayoutInflater mInflater;
        private Calendar mCalendar;
        private int mDayCount;
        private int mLastRequestedDay = 0;

        public CalendarSpinnerAdapter(Context context, Calendar startDate, int dayCount) {
            mInflater = LayoutInflater.from(context);
            mDayCount = dayCount;
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(startDate.getTimeInMillis());
        }

        @Override
        public int getCount() {
            return mDayCount;
        }

        @Override
        public Calendar getItem(int position) {
            mCalendar.add(Calendar.DAY_OF_YEAR, position - mLastRequestedDay);
            mLastRequestedDay = position;
            return mCalendar;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

            Calendar item = getItem(position);
            ((TextView) convertView).setText(mDateFormat.format(item.getTimeInMillis()));

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                AddProduct result= (AddProduct) data.getSerializableExtra("result");
                addProductArrayList.add(result);
                Log.i("result",result.getProductName()+" "+result.getQuantity()+" "+result.getActualPrice()+" "+result.getSellingPrice()+" "+result.getProfit()+" ");

                int leftMargin=0;
                int topMargin=4;
                int rightMargin=0;
                int bottomMargin=0;


                TableRow tr_head=new TableRow(AddSalesActivity.this);
                tr_head.setBackgroundColor(Color.WHITE);        // part1
                tr_head.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView Detail = new TextView(this);
                Detail.setText(result.getProductName());
                Detail.setTextColor(Color.BLACK);
                Detail.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f));
                tr_head.addView(Detail);

                TextView Quantity = new TextView(this);
                Quantity.setText(result.getQuantity()+" ");
                Quantity.setTextColor(Color.BLACK);
                Quantity.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f));
                tr_head.addView(Quantity);

                TextView Price = new TextView(this);
                Price.setText(result.getSellingPrice()+" ");
                Price.setTextColor(Color.BLACK);
                Price.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f));
                tr_head.addView(Price);

                TextView Total = new TextView(this);

                Float multiply=result.getQuantity()*result.getSellingPrice();
                Total.setText(multiply+" ");
                Total.setTextColor(Color.BLACK);
                Total.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f));
                tr_head.addView(Total);

                tableLayout.addView(tr_head, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));

                totalAmount=totalAmount+multiply;
                productAmount=productAmount+multiply;
                total.setText(productAmount+" ");
                AmountToBePaid.setText(totalAmount+" ");

                totalProfit=result.getActualPrice()*result.getQuantity();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
