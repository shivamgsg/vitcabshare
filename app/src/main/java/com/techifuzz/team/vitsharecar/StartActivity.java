package com.techifuzz.team.vitsharecar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wenchao.cardstack.CardStack;

public class StartActivity extends AppCompatActivity {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mCardStack = (CardStack) findViewById(R.id.container);
//        TextView textView=(TextView) findViewById(R.id.start);
//        textView.setVisibility(View.INVISIBLE);

        mCardStack.setContentResource(R.layout.card_view);
//        mCardStack.setStackMargin(20);
        mCardAdapter = new CardsDataAdapter(getApplicationContext());
        mCardAdapter.add("Welcome to VIT ShareCar");
        mCardAdapter.add("Find your travelling mates");
        mCardAdapter.add("Easy to use");
        mCardStack.setAdapter(mCardAdapter);
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.button_reg);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent=new Intent(StartActivity.this,Login.class);
                startActivity(reg_intent);
            }
        });
    }
}
