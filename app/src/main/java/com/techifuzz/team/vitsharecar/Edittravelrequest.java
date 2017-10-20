package com.techifuzz.team.vitsharecar;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Edittravelrequest extends Fragment {

    private DatabaseReference mdatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference ndatabse;
    private Button button, button1, button2;
    private DatabaseReference getMdatabase;

    public Edittravelrequest() {
        // Required empty public constructor
    }

    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edittravelreq, container, false);
        final String[] values =
                {"VIT-VELLORE", "VIT-CHENNAI", "CHENNAI-AIRPORT", "BANGLORE-AIRPORT", "CHENNAI-RAILWAY-STATION", "VELLORE-RAILWAY-STATION",};
        String[] values1 =
                {"VIT-VELLORE", "VIT-CHENNAI", "CHENNAI-AIRPORT", "BANGLORE-AIRPORT", "CHENNAI-RAILWAY-STATION", "VELLORE-RAILWAY-STATION"};
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_1);
        final Spinner spinner1 = (Spinner) rootView.findViewById(R.id.spinner_2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, values);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, values1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter1);
        final TextView editText = (TextView) rootView.findViewById(R.id.edit_travel_date);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cal, 0, 0, 0);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(MONTHS[monthOfYear])
                                + "-" + String.valueOf(year);
                        editText.setText(date);
                    }

                }, yy, mm, dd);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });
        final TextView editText1 = (TextView) rootView.findViewById(R.id.edit_travel_time);
        editText1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clcok, 0, 0, 0);
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editText1.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user != null) {
            final String uid = current_user.getUid();
        }
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String to = spinner.getSelectedItem().toString();
                final String from = spinner1.getSelectedItem().toString();
                final String date = editText.getText().toString();
                final String time = editText1.getText().toString();

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                if (to.isEmpty()) {
                    Toast.makeText(getContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                } else if (from.isEmpty() || from.matches(to)) {
                    Toast.makeText(getContext(), "Cannot be same", Toast.LENGTH_LONG).show();
                } else if (date.isEmpty()) {
                    Toast.makeText(getContext(), "Date cannot be empty", Toast.LENGTH_LONG).show();
                } else if (time.isEmpty()) {
                    Toast.makeText(getContext(), "Time cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ndatabse = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                    ndatabse.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String name = dataSnapshot.child("name").getValue().toString();
                            final String image = dataSnapshot.child("image").getValue().toString();
                            final String email = dataSnapshot.child("email").getValue().toString();
                            final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                            final String token = dataSnapshot.child("token").getValue().toString();
                            final String number = dataSnapshot.child("number").getValue().toString();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("travel").child(uid);
                            HashMap<String, String> datamap = new HashMap<>();
                            datamap.put("to", to);
                            datamap.put("from", from);
                            datamap.put("date", date);
                            datamap.put("time", time);
                            datamap.put("image", image);
                            datamap.put("name", name);
                            datamap.put("email", email);
                            datamap.put("token", token);
                            datamap.put("thumb_image", thumb_image);
                            datamap.put("number", number);
                            databaseReference.setValue(datamap);
                            Toast.makeText(getContext(), "Your Request has been sent", Toast.LENGTH_LONG).show();
                            Fragment fragment = new Showall();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

        });
        button1 = (Button) rootView.findViewById(R.id.viewrequest);
        button1.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Viewrequest.class);
                startActivity(intent);
            }
        });
        button2 = (Button) rootView.findViewById(R.id.delereq);
        button2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();
                getMdatabase = FirebaseDatabase.getInstance().getReference().child("travel").child(uid);
                getMdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            getMdatabase.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Request successfully deleted", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "No request to delete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return rootView;
    }
}