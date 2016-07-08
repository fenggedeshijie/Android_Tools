package com.dsm.netty;

import android.util.Log;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public abstract class FrameHandler extends SimpleChannelInboundHandler<FrameData> {

	// 网络数据类型
	public static final byte _FRAME_TYPE_REQ     = (byte)0x01;   // 数据请求（需应答)
	public static final byte _FRAME_TYPE_MSG     = (byte)0x02;   // 数据消息
	public static final byte _FRAME_TYPE_ACK_OK  = (byte)0x09;   // 数据应答(成功)
	public static final byte _FRAME_TYPE_ACK_ERR = (byte)0x0A;   // 数据应答(失败)
	
	// 网络数据指令
	public static final byte _FRAME_CMD_DEVICE_FIND     = (byte)0xA1;   // 设备发现
	public static final byte _FRAME_CMD_DEVICE_DESCRIBE = (byte)0xA2;   // 设备描述
	public static final byte _FRAME_CMD_DEVICE_ACTIION  = (byte)0xA3;   // 设备操作
	public static final byte _FRAME_CMD_DEVICE_EVENT    = (byte)0xA4;   // 设备事件
	public static final byte _FRAME_CMD_DEVICE_MESSAGE  = (byte)0xA5;   // 消息请求
	
	// 操作标识
	public static final byte _ACTION_SIGN_ADD    = (byte)0x01;   // 添加
	public static final byte _ACTION_SIGN_DELETE = (byte)0x02;   // 删除
	public static final byte _ACTION_SIGN_MODIFY = (byte)0x03;   // 修改
	public static final byte _ACTION_SIGN_QUERY  = (byte)0x04;   // 查询
	
	private Object mOwner;
	
	public void setOwner(Object owner){
		mOwner = owner;
	}
	
	public boolean equalsOwner(Object owner){
		if(owner == null || mOwner == null)
			return false;
		return owner.equals(mOwner);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FrameData msg) throws Exception {
		//Log.d("client接收到服务器返回的消息:", msg.data_content);  
		Log.d("ddf", "aaaa");
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
    	System.out.println("exception");
    	super.exceptionCaught(ctx, cause);
    }
	    
}
