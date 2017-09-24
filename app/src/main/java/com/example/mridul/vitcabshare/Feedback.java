package com.example.mridul.vitcabshare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {

    private EditText editText,editText1;
    private Button button;

    public Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root= inflater.inflate(R.layout.fragment_feedback, container, false);

        editText=(EditText) root.findViewById(R.id.send_default_email);
        editText1=(EditText) root.findViewById(R.id.enter_message);
        button=(Button) root.findViewById(R.id.button_send_email);


        editText.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to_email=editText.getText().toString();
                String to_message=editText1.getText().toString();

                Intent email=new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,to_email);
                email.putExtra(Intent.EXTRA_TEXT,to_message);

                email.setType("message/rfc822");
                startActivity(email);

            }
        });

return root;
    }

}
