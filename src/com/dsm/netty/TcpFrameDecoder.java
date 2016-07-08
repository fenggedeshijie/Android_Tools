package com.dsm.netty;

import java.nio.ByteOrder;
import java.util.List;

import android.util.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TcpFrameDecoder extends ByteToMessageDecoder {
	
    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }
    
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    	FrameData frameData = null;
		if (in.readableBytes() < 10) return null;
		int leng = 0;
		while (in.isReadable(10)) {
			if(in.getByte(0) != (byte)0xFE || in.getByte(1) != (byte)0xA0 || in.getByte(2) != (byte)0x86 
					|| in.getByte(3) != (byte)0xEF || in.getByte(4) != (byte)0x01 || in.getByte(5) != (byte)0x00) 
			{
				in.skipBytes(1);
				continue;
			}
			
			for (int i = 0; i < 10; i ++) {
				System.out.printf("%02x ", in.getByte(i));
			}
			
			in.order(ByteOrder.BIG_ENDIAN);
			leng = in.getInt(6);
			
			System.out.println("leng = " + leng);
			System.out.flush();
			break;
		}
		
		if (in.isReadable(leng + 10)) {
			byte[] content = new byte[leng];
			in.getBytes(10, content);
			
			for (int i : content) {
				System.out.printf("%02x ", content[i]);
			}
			System.out.flush();
			Log.d("TcpFrameDecoder", new String(content));
			
			frameData = new FrameData();
			frameData.net_type = 2;
			frameData.xml = new String(content);
			in.skipBytes(leng + 10);
		}
		
        return frameData;
    }
}
