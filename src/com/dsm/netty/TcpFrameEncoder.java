package com.dsm.netty;

import android.util.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TcpFrameEncoder extends MessageToByteEncoder<FrameData>{
	
    @Override
    protected void encode(ChannelHandlerContext ctx, FrameData msg, ByteBuf out) throws Exception {
    	if(msg == null){
    		return;
    	}
    	
    	byte[] buf = msg.toBytes();
    	
    	// print information
    	for (int i = 0; i < buf.length; i ++) {
    		System.out.printf("%02x ", buf[i]);
    	}
    	System.out.flush();
    	byte[] tmp = new byte[buf.length -10];
    	System.arraycopy(buf, 10, tmp, 0, buf.length -10);
    	String str = new String(tmp);
    	Log.d("TcpFrameEncoder", str);
    	
        out.writeBytes(buf);
    }
}
