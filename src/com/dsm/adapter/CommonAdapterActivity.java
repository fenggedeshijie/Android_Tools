package com.dsm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dsm.mystudy.R;


import com.dsm.utils.adapter.CommonAdapter;
import com.dsm.utils.adapter.ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CommonAdapterActivity extends Activity {

	private List<String> mTitleList = new ArrayList<String>();
	private List<Message> mMessageList = new ArrayList<Message>();
	private CommonAdapter<Message> mAdapter;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_adapter);
        
        initData();
        ListView mListView = (ListView)findViewById(R.id.lv_list);
        mAdapter = new CommonAdapter<Message>(this,	mMessageList, R.layout.common_adapter_item) {
			@Override
			public void convertView(ViewHolder holder, Message item) {
				holder.setText(R.id.tv_title, item.title);  
				holder.setText(R.id.tv_describe, item.desc);  
				holder.setText(R.id.tv_phone, item.phone);  
				holder.setText(R.id.tv_time, item.time);  
			}
        };
        mListView.setAdapter(mAdapter);
    }
	
	private void initData() {
		mTitleList.add("Common adapter title 1");
		mTitleList.add("Common adapter title 2");
		mTitleList.add("Common adapter title 3");
		mTitleList.add("Common adapter title 4");
		mTitleList.add("Common adapter title 5");
		mTitleList.add("Common adapter title 6");
		
		Message message = new Message();
		message.title = "美女一只";
		message.time = "2015-12-12 16:31";
		message.desc = "周四早上捡到妹子一只，在食堂二楼";
		message.phone = "10086";
		mMessageList.add(message);
		
		message = new Message();
		message.title = "帅哥一打";
		message.time = "2015-12-12 19:31";
		message.desc = "晚上的时候遇到一群帅锅，在主教楼";
		message.phone = "10086";
		mMessageList.add(message);
		
		message = new Message();
		message.title = "恐龙一只";
		message.time = "2015-12-12 1:31";
		message.desc = "在图上看书时，习惯性回头，妈的，吓死哥了！";
		message.phone = "10086";
		mMessageList.add(message);
		
	}
	
}
