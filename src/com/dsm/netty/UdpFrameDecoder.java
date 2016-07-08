package com.dsm.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

public class UdpFrameDecoder extends MessageToMessageDecoder<DatagramPacket> {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
		ByteBuf data  =  msg.content(); 
		if (data.readableBytes() < 10) return;
		int leng = 0;
		while (data.isReadable(10)) {
			if(data.getByte(0) != (byte)0xFE || data.getByte(1) != (byte)0xA0 || data.getByte(2) != (byte)0x86 
					|| data.getByte(3) != (byte)0xEF || data.getByte(4) != (byte)0x01 || data.getByte(5) != (byte)0x00) 
			{
				data.skipBytes(1);
				continue;
			}
			leng = data.getInt(6);
			break;
		}
		
		if (data.isReadable(leng + 10)) {
			byte[] content = new byte[leng];
			data.getBytes(10, content);
			FrameData frameData = new FrameData();
			frameData.net_type = 2;
			frameData.peer_address = msg.sender();
			frameData.xml = content.toString();
			out.add(frameData);
		}
	}
}
