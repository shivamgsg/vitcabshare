package com.techifuzz.team.vitsharecar;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends Activity{

    private Animation animation;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView imageView=(ImageView) findViewById(R.id.start_splah_image);


        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();

                    sleep(400);


                } catch (Exception e) {

                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
//                            final int soundid=soundPool.load(getBaseContext(),R.raw,1);
                            animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.move);
                            imageView.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
//                                    soundPool.play(soundid,1,1,0,0,1);

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Intent i = new Intent(Splash.this,MainActivity.class);
                                    startActivity(i);
                                    finish();

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                        }
                    });



                }
            }
        };
        welcomeThread.start();
    }
}
