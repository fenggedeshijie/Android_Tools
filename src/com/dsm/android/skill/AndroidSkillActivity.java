package com.dsm.android.skill;

import java.util.ArrayList;

import com.dsm.mystudy.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class AndroidSkillActivity extends Activity {

	private EditText mEtUserName;
	private AutoCompleteTextView mActvAccount;
	private ArrayAdapter<String> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_skill);
		
		initView();
	}
	
	private void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_username);
		Button btn_commit = (Button) findViewById(R.id.btn_commit);
		
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(mEtUserName.getText().toString().trim())) {
					Drawable icon = getResources().getDrawable(R.drawable.ball_6);
					icon.setBounds(0, 0, 50, 50);
					mEtUserName.setError("Please input user's name!", icon);
				}
			}
		});
		
		mActvAccount = (AutoCompleteTextView) findViewById(R.id.actv_account);
		String[] strList = {"aa", "ab", "aac", "bbb", "bac", "ccc"};
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strList);
		mActvAccount.setAdapter(arrayAdapter);
	}
}
