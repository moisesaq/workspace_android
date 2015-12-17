package com.moises.animations;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AnimationsSimple extends Fragment implements OnClickListener{

	View v;
	
	private Button btnTraslate, btnRotate, btnScale, btnAlpha;
	public LinearLayout layout1, layout2;
	private ProgressBar pb;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			pb.incrementProgressBy(5);
		}
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.animations_simple, container, false);
		startComponents(v);
		return v;
	}
	
	private void startComponents(View v) {
		btnTraslate = (Button)v.findViewById(R.id.btnTraslate);
		btnTraslate.setOnClickListener(this);
		btnRotate = (Button)v.findViewById(R.id.btnRotate);
		btnRotate.setOnClickListener(this);
		btnScale = (Button)v.findViewById(R.id.btnScale);
		btnScale.setOnClickListener(this);
		btnAlpha = (Button)v.findViewById(R.id.btnAlpha);
		btnAlpha.setOnClickListener(this);
		layout1 = (LinearLayout)v.findViewById(R.id.layout1);
		layout2 = (LinearLayout)v.findViewById(R.id.layout2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnTraslate:
				if(layout1.getVisibility()==View.VISIBLE){
					startAnimationTraslateInvisible(layout1);
				}else{
					startAnimationTraslateVisible(layout1);
				}
				break;
			case R.id.btnRotate:
				if(layout1.getVisibility()==View.VISIBLE){
					startAnimationRotate(layout1);
				}else{
					Toast.makeText(getActivity(), "Layout no esta visible", Toast.LENGTH_SHORT).show();
				}
				
				break;
			case R.id.btnScale:
				if(layout1.getVisibility()==View.VISIBLE){
					startAnimationScaleGone(layout1);
				}else{
					startAnimationScaleVisible(layout1);
				}
				break;
			case R.id.btnAlpha:
				if(layout1.getVisibility()==View.GONE){
					startAnimationCustom(layout1);
				}
//				if(layout1.getVisibility()==View.VISIBLE){
//					startAnimationAlphaGone(layout1);
//				}else{
//					startAnimationAlphaVisible(layout1);
//				}
				break;
		}
	}
	
	public void startAnimationTraslateVisible(View view){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.traslate_right);
		animation.setFillAfter(true);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(animation);
	}
	
	public void startAnimationTraslateInvisible(View view){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.traslate_left);
		animation.setFillAfter(true);
		view.startAnimation(animation);
		view.setVisibility(View.GONE);
	}
	
	public void startAnimationRotate(View view){
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_custom);
		anim.setFillAfter(false);
		view.startAnimation(anim);
	}

	public void startAnimationScaleVisible(View view){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_visible);
		animation.setFillAfter(false);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(animation);
	}
	
	public void startAnimationScaleGone(View view){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_gone);
		animation.setFillAfter(false);
		view.startAnimation(animation);
		view.setVisibility(View.GONE);
	}
	
	public void startAnimationAlphaVisible(View view){
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_visible);
		anim.setFillAfter(false);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(anim);
	}
	
	public void startAnimationAlphaGone(View view){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_gone);
		animation.setFillAfter(false);
		view.startAnimation(animation);
		view.setVisibility(View.GONE);
	}
	
	public void startAnimationCustom(View view){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animate_custom);
		animation.setFillAfter(false);
		view.startAnimation(animation);
		view.setVisibility(View.VISIBLE);
	}
	
}
