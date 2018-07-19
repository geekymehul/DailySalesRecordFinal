package com.example.gargc.dailysalesrecord.Activity.Sales;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Activity.CustomerActivity;
import com.example.gargc.dailysalesrecord.Model.CustomerContent;
import com.example.gargc.dailysalesrecord.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddSalesActivity extends AppCompatActivity {

    Button addSalesCustomer,addSalesProduct,add;
    Spinner spinnerDateIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);

        findId();

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
                startActivity(intent);
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
}
