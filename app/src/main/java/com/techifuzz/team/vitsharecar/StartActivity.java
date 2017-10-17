package com.techifuzz.team.vitsharecar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wenchao.cardstack.CardStack;

import me.relex.circleindicator.CircleIndicator;

public class StartActivity extends AppCompatActivity {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    private ViewPager viewPager;
    private int Dotcount;
   Customswipe customswipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);
//        mCardStack = (CardStack) findViewById(R.id.container);
        setContentView(R.layout.startactivity);
//        mCardStack.setContentResource(R.layout.card_view);
//        mCardAdapter = new CardsDataAdapter(getApplicationContext());
//        mCardAdapter.add("Welcome to VIT ShareCar");
//        mCardAdapter.add("Find your travelling mates");
//        mCardAdapter.add("Easy to use");
//        mCardStack.setAdapter(mCardAdapter);
        viewPager = (ViewPager) findViewById(R.id.pageview);
        customswipe = new Customswipe(this);
        viewPager.setAdapter(customswipe);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        TextView textView=(TextView) findViewById(R.id.skip);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(StartActivity.this,Main3Activity.class);
               startActivity(reg_intent);
            }
        });

        final TextView textView1=(TextView) findViewById(R.id.next);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dotcount=viewPager.getChildCount();
                viewPager.setCurrentItem((viewPager.getCurrentItem()<Dotcount)?viewPager.getCurrentItem()+1:0);
            }
        });
 }
}
