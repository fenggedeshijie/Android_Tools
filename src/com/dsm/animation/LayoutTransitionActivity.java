package com.dsm.animation;

import com.dsm.mystudy.R;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;

public class LayoutTransitionActivity extends Activity implements OnCheckedChangeListener {

	private ViewGroup viewGroup;  
    private GridLayout mGridLayout;  
    private int mVal;  
    private LayoutTransition mTransition;  
  
    private CheckBox mAppear, mChangeAppear, mDisAppear, mChangeDisAppear;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.xml_layout_transition);  
        viewGroup = (ViewGroup) findViewById(R.id.id_container);  
  
        mAppear = (CheckBox) findViewById(R.id.id_appear);  
        mChangeAppear = (CheckBox) findViewById(R.id.id_change_appear);  
        mDisAppear = (CheckBox) findViewById(R.id.id_disappear);  
        mChangeDisAppear = (CheckBox) findViewById(R.id.id_change_disappear);  
  
        mAppear.setOnCheckedChangeListener(this);  
        mChangeAppear.setOnCheckedChangeListener(this);  
        mDisAppear.setOnCheckedChangeListener(this);  
        mChangeDisAppear.setOnCheckedChangeListener(this);  
  
        mGridLayout = new GridLayout(this);  
        mGridLayout.setColumnCount(5);  
        viewGroup.addView(mGridLayout);  
        mTransition = new LayoutTransition();  
        mGridLayout.setLayoutTransition(mTransition);  
  
    }  
  
    /** 
     * ��Ӱ�ť 
     *  
     * @param view 
     */  
    public void addBtn(View view) {  
        final Button button = new Button(this);  
        button.setText((++mVal) + "");  
        mGridLayout.addView(button, Math.min(1, mGridLayout.getChildCount()));  
        button.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                mGridLayout.removeView(button);  
            }  
        });  
    }  
  
    @Override  
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
        mTransition = new LayoutTransition();  
        
        //mTransition.setAnimator(LayoutTransition.APPEARING,  
        //        (mAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.APPEARING) : null)); 
        
        mTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,  
                (mChangeAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.CHANGE_APPEARING): null));
        
        mTransition.setAnimator(LayoutTransition.DISAPPEARING,  
                (mDisAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.DISAPPEARING) : null));  
        
        mTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,  
                (mChangeDisAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING): null));
        
        mTransition.setAnimator(LayoutTransition.APPEARING, 
        		(mAppear.isChecked() ? ObjectAnimator.ofFloat(this, "scaleX", 0, 1): null));
        
        mGridLayout.setLayoutTransition(mTransition);  
    }  

}
