package com.techifuzz.team.vitsharecar;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {

    private EditText editText1;
    private Button button;
    private DatabaseReference databaseReference;

    public Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root= inflater.inflate(R.layout.fragment_feedback, container, false);

        editText1=(EditText) root.findViewById(R.id.enter_message);
        button=(Button) root.findViewById(R.id.button_send_email);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Feedback");
        final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to_message=editText1.getText().toString();

                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("message",to_message);
                databaseReference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
                        Toast.makeText(getContext(),"Thank you for your feedback",Toast.LENGTH_SHORT).show();
                        editText1.setText("");
                    }
                });

            }
        });

return root;
    }

}
