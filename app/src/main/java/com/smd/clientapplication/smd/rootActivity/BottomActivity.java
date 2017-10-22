package com.smd.clientapplication.smd.rootActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.maps.MapView;
import com.smd.clientapplication.smd.R;
import com.smd.clientapplication.smd.rootActivity.fragments.BlankFragment;
import com.smd.clientapplication.smd.rootActivity.fragments.ConsumptionFragment;
import com.smd.clientapplication.smd.rootActivity.fragments.PlotFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BottomActivity extends AppCompatActivity {

    MapView mapView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeToTripFragment();
                    return true;
                case R.id.navigation_dashboard:
                    changeToConsumptionFragment();
                    return true;
                case R.id.navigation_notifications:
                    changeToPlotFragment();
                    return true;
            }
            return false;
        }

    };
    private void changeToConsumptionFragment(){
        ConsumptionFragment newFragment = new ConsumptionFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_id, newFragment);

// Commit the transaction
        transaction.commit();
//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.fragment_id, new ConsumptionFragment());
//        tx.commit();
    }

    private void changeToPlotFragment(){
        PlotFragment newFragment = new PlotFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_id, newFragment);

        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void changeToTripFragment(){
        BlankFragment newFragment = new BlankFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_id, newFragment);

        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
        setOnDateChangedListener();

    }

    private void setOnDateChangedListener(){
        final Button changeDateButton = (Button) findViewById(R.id.todayButton);
        if (changeDateButton!=null) {
            changeDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, dayOfMonth);

                            String dateString = sdf.format(calendar.getTime());
                            changeDateButton.setText(dateString);
                        }
                    };
                    DatePickerDialog dpd = new DatePickerDialog(v.getContext(), onDateSetListener, 2017, 1, 1);
                    dpd.show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        if (findViewById(R.id.fragment_id) != null) {
            if (savedInstanceState != null) {
                return;
            }
            BlankFragment firstFragment = new BlankFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_id, firstFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
            setOnDateChangedListener();
        }



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }

    public void onChangeDate(final View button) {

    }
}
