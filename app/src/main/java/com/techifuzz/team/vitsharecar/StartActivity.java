package com.techifuzz.team.vitsharecar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import me.relex.circleindicator.CircleIndicator;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int Dotcount;
    private int currentpos;
   Customswipe customswipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
        viewPager = (ViewPager) findViewById(R.id.pageview);
        customswipe = new Customswipe(this);
        viewPager.setAdapter(customswipe);
        viewPager.setOffscreenPageLimit(4);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        final TextView textView=(TextView) findViewById(R.id.next);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(StartActivity.this,Main3Activity.class);
               startActivity(reg_intent);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentpos = position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final TextView textView1=(TextView) findViewById(R.id.skip);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dotcount=viewPager.getChildCount();
                viewPager.setCurrentItem(currentpos + 1);
                if(currentpos==4){

                    textView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent reg_intent=new Intent(StartActivity.this,Main3Activity.class);
                            startActivity(reg_intent);

                        }
                    });
                }
            }
        });
 }
}
