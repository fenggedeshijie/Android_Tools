package com.dsm.new_v7;

import java.util.ArrayList;
import java.util.List;

import com.dsm.mystudy.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class RecyclerViewActivity extends Activity {

	private RecyclerView mRecyclerView;
	private List<String> mLists;
	private MyAdapter mAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recyclerview);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new MyAdapter(this, mLists));
    }
	
	private void initData() {
		mLists = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
        	mLists.add("Hello world " + (char) i);
        }
	}
	
	private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
		
		private Context mContext;
		private List<String> mDatas;
		
		public MyAdapter(Context context, List<String> data) {
			mContext = context;
			mDatas = data;
		}
		
		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			holder.tv.setText(mDatas.get(position));
		}

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext)
					.inflate(R.layout.activity_recyclerview_item, parent, false));
            return holder;
		}
		
		class MyViewHolder extends ViewHolder {
            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
	}
}
