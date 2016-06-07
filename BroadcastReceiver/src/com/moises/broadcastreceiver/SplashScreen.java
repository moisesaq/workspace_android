package com.moises.broadcastreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity{

	public static final int TIME_SPLASH = 5000;
	private TextView count;
	private ImageView logo;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		count = (TextView)findViewById(R.id.count);
		logo = (ImageView)findViewById(R.id.logo);
		
		new CountDownTimer(TIME_SPLASH, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				long n = millisUntilFinished/1000;
				if(n == 1) animateLogo();
				count.setText(String.valueOf(n));
			}
			
			@Override
			public void onFinish() {
				count.setVisibility(View.GONE);
				//animateLogo();
				Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(intent);
			}
		}.start();
	}
	
	public void animateLogo(){
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_gone);
		animation.setFillAfter(true);
		logo.startAnimation(animation);
		//logo.setVisibility(View.GONE);
	}
}
