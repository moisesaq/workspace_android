package com.moises.ormlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable{

	private boolean isChecked;
	private List<Checkable> checkableViews;
	
	public CheckableLinearLayout(Context context) {
		super(context);
		setup(null);
	}

	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup(attrs);
	}

	public CheckableLinearLayout(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		setup(attrs);
	}
	
	@Override
	public void onFinishInflate(){
		super.onFinishInflate();
		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			searchComponentCheckables(this.getChildAt(i));
		}
	}
	
	public void setup(AttributeSet attrs){
		this.isChecked = false;
		this.checkableViews = new ArrayList<Checkable>();
	}
	
	private void searchComponentCheckables(View view) {
		if(view instanceof Checkable) this.checkableViews.add((Checkable)view);
		if(view instanceof ViewGroup){
			final ViewGroup vg = (ViewGroup)view;
			final int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; i++) {
				searchComponentCheckables(vg.getChildAt(i));
			}
		}
	}

	// Methods CHECKABLE
	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		for(Checkable c: checkableViews) c.setChecked(isChecked);
	}

	@Override
	public void toggle() {
		this.isChecked = !this.isChecked;
		for(Checkable c: checkableViews) c.toggle();
	}

	
}
