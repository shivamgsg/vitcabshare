package com.example.mridul.vitcabshare;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wenchao.cardstack.CardStack;

public class StartActivity extends AppCompatActivity {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mCardStack = (CardStack) findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.card_view);
//        mCardStack.setStackMargin(20);



        mCardAdapter = new CardsDataAdapter(getApplicationContext());
        mCardAdapter.add("Welcome to VIT Cab Share");
        mCardAdapter.add("maa chudao");
        mCardAdapter.add("gaand marao");
        mCardStack.setAdapter(mCardAdapter);
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.button_reg);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent=new Intent(StartActivity.this,Register.class);
                startActivity(reg_intent);
            }
        });
    }
}
