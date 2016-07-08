package com.dsm.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;


public class UdpFrameEncoder extends MessageToMessageEncoder<FrameData>{

	@Override
	protected void encode(ChannelHandlerContext ctx, FrameData msg, List<Object> out) throws Exception {
		if(msg == null) return;

		byte data[] = msg.toBytes();
        ByteBuf frame = ctx.alloc().buffer(data.length);
        frame.writeBytes(data);
		out.add(new DatagramPacket(frame, msg.peer_address));
	}
}
