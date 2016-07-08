package com.dsm.netty;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class FrameData implements Parcelable{
	// 网络类型(1-TCP 2-UDP)
	public int net_type = 0;
	
	// 对端网络地址
	public InetSocketAddress peer_address = null;
	
	// 数据帧属性
	public byte frame_type = 0x00;
	
	// 数据帧指令
	public byte frame_cmd = 0x00;
	
	// 操作类型
	public byte type = 0x00;
	
	// 设备url
	public String url = "";
	
	public String action_id = "";
	
	public String value = "";

	public int packet_len = 0;
	
	public String xml = "";
	
	public FrameData(){}
	
	public void clear(){
		net_type = 0;
		peer_address = null;
		frame_type = 0x00;
		frame_cmd = 0x00;
		type = 0x00;
		action_id = "";
		url = "";
		value = "";
		packet_len = 0;
		xml = "";
	}

	public byte[] toBytes() {
		byte[] buf = null;
		
		if (frame_cmd == FrameHandler._FRAME_CMD_DEVICE_FIND) {
			Log.v("MyFrameData", "FRAME_CMD_DEVICE_FIND");
			buf = findDeviceToBytes();
		}
		else if (frame_cmd == FrameHandler._FRAME_CMD_DEVICE_DESCRIBE) {
			Log.v("MyFrameData", "FRAME_CMD_DEVICE_DESCRIBE");
			buf = deviceDescribeToBytes();
		}
		else if(frame_cmd == FrameHandler._FRAME_CMD_DEVICE_ACTIION) {
			Log.v("MyFrameData", "FRAME_CMD_DEVICE_ACTIION");
			buf = deviceControlToBytes();
		} 
		else if(frame_cmd == FrameHandler._FRAME_CMD_DEVICE_EVENT) {
			Log.v("MyFrameData", "FRAME_CMD_DEVICE_EVENT");
			buf = eventSubscribeToBytes();
		}
		
		return buf;
	}
	
	public byte[] findDeviceToBytes() {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<root type=\"request\" cmd=\"cmd_shcp_search\"></root>";
		
		return generatePacket(content);
	}
	
	public byte[] deviceDescribeToBytes() {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<root type=\"request\" cmd=\"cmd_shcp_description\"></root>";
		
		return generatePacket(content);
	}
	
	public byte[] deviceControlToBytes() {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<root type=\"request\" cmd=\"cmd_shcp_control\">"
				+ "<action id=\"" + action_id + "\" url=\"" + url +"\" direction=\"in\" value=\"" + value + "\"/></root>";
		
		return generatePacket(content);
	}
	
	public byte[] eventSubscribeToBytes() {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<root type=\"request\" cmd=\"cmd_shcp_event_subscribe\"></root>";
		
		return generatePacket(content);
	}
	
	public byte[] generatePacket(String content) {
		int length = content.length();
		packet_len = length + 10;
		ByteBuffer buffer = ByteBuffer.allocate(length +10);
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.put((byte)0xFE);
		buffer.put((byte)0xA0);
		buffer.put((byte)0x86);
		buffer.put((byte)0xEF);
		buffer.put((byte)0x01);
		buffer.put((byte)0x00);
		buffer.putInt(length);
		buffer.put(content.getBytes());
		
		return buffer.array();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(net_type);
		out.writeValue(peer_address);
		out.writeByte(frame_type);
		out.writeByte(frame_cmd);
		out.writeByte(type);
		out.writeString(url);
		out.writeString(action_id);
		out.writeString(value);
		out.writeInt(packet_len);
		out.writeString(xml);
	}
	
	public static final Parcelable.Creator<FrameData> CREATOR = new Creator<FrameData>(){
	    @Override
	    public FrameData createFromParcel(Parcel in) {
	        return new FrameData(in);
	    }
	    
	    @Override
	    public FrameData[] newArray(int size) {
	        return new FrameData[size];
	    }
	};
	
	public FrameData(Parcel in){
		net_type = in.readByte();
		peer_address = (InetSocketAddress)in.readValue(InetSocketAddress.class.getClassLoader());
		frame_type = in.readByte();
		frame_cmd = in.readByte();
		type = in.readByte();
		url = in.readString();
		action_id = in.readString();
		value = in.readString();
		packet_len = in.readInt();
		xml = in.readString();
    }
 }
