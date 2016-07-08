package com.dsm.utils.adapter;

import java.util.List;

import com.dsm.adapter.ChatMessage;
import com.dsm.mystudy.R;

import android.content.Context;

public class MultiItemTestAdapter extends MultiItemCommonAdapter2<ChatMessage>{

	public MultiItemTestAdapter(Context context, List<ChatMessage> data) {
		super(context, data);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return mTypes.get(position);
	}

	@Override
	public int getLayoutId(int position, ChatMessage msg) {
		int layoutId = -1;
		
		if (msg.isComMeg()) {
			mTypes.put(position, ChatMessage.RECIEVE_MSG);
			layoutId = R.layout.main_chat_from_msg;
        } else {
        	mTypes.put(position, ChatMessage.SEND_MSG);
        	layoutId = R.layout.main_chat_send_msg;
        }
        return layoutId;
	}

	@Override
	public void convertView(ViewHolder holder, ChatMessage chatMessage) {
		switch (holder.getLayoutId()) {
        case R.layout.main_chat_from_msg:
        	 holder.setText(R.id.chat_from_content, chatMessage.getContent());
             holder.setText(R.id.chat_from_name, chatMessage.getName());
             holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());
            break;
        case R.layout.main_chat_send_msg:
        	 holder.setText(R.id.chat_send_content, chatMessage.getContent());
             holder.setText(R.id.chat_send_name, chatMessage.getName());
             holder.setImageResource(R.id.chat_send_icon, chatMessage.getIcon());
            break;
		}
	}
}
