package com.dsm.adapter;

import java.util.ArrayList;

import com.dsm.mystudy.R;
import com.dsm.utils.adapter.MultiItemTestAdapter;

import android.app.ListActivity;
import android.os.Bundle;

public class MultiItemAdapterActivity extends ListActivity {

	private ArrayList<ChatMessage> mDatas = new ArrayList<ChatMessage>();
	
	@Override
	public void onCreate(Bundle bundle)   {
	    super.onCreate(bundle);
	    initDatas();
	    getListView().setDivider(null);
	    setListAdapter(new MultiItemTestAdapter(this, mDatas)); 
	}
	
	private void initDatas() {
        ChatMessage msg = null;
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ", null, false);
        mDatas.add(msg);
        
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ", null, true);
        mDatas.add(msg);
        
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ", null, false);
        mDatas.add(msg);
        
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ", null, true);
        mDatas.add(msg);
        
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ", null, false);
        mDatas.add(msg);

        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.renma, "renma", "where are you ",
                null, true);
        mDatas.add(msg);
        msg = new ChatMessage(R.drawable.xiaohei, "xiaohei", "where are you ",
                null, false);
        mDatas.add(msg);
    }
}
