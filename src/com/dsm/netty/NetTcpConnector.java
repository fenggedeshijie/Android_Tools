package com.dsm.netty;

import java.net.InetSocketAddress;

import android.util.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NetTcpConnector {
	
	private EventLoopGroup mWorkgroup = null;
	private Bootstrap mBootstrap = null;
	private Channel mChannel = null; 
	
	public NetTcpConnector() {
		mWorkgroup = new NioEventLoopGroup();
		mBootstrap = new Bootstrap();
		mBootstrap.group(mWorkgroup);
		mBootstrap.channel(NioSocketChannel.class);
		mBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	}
	
	public synchronized boolean connect(String inetHost, int inetPort, final FrameHandler handler) {
		mBootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipe = ch.pipeline();
				mChannel = pipe.channel();
				pipe.addLast("tcpFrameEncoder", new TcpFrameEncoder());
				pipe.addLast("tcpFrameDecoder", new TcpFrameDecoder());
				pipe.addLast("myHandler", handler);
			}
		});
		
		try {
			mBootstrap.connect(inetHost, inetPort).sync();
		} catch (InterruptedException e) {  
            Log.d(String.format("Connect to Server(IP[%s],PORT[%s]) faied", inetHost, inetPort), e.getMessage()); 
            return false;
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return true;
	}
	
	public synchronized boolean isConnected(){
        if(mChannel == null || !mChannel.isActive()){    
        	return false;
        }
        return true;
	}
	
	public void disconnect() {
		if (isConnected()) {
			try {
				mChannel.disconnect().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void release(){
		mWorkgroup.shutdownGracefully();
	}
	
	public String remoteHostAddress(){
		if(!isConnected()){
			return "";
		}
		return ((InetSocketAddress)mChannel.remoteAddress()).getAddress().getHostAddress();
	}
	
	//
	// 发送数据
	//
	public synchronized boolean send(FrameData data){
        if(!isConnected()){  
        	Log.d("send", "消息发送失败, 连接尚未建立!");  
        	return false;
        } 
        return send(mChannel, data);
	}	
	
	public static boolean send(Channel channel, FrameData data){
    	try {
    		channel.writeAndFlush(data).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch(Exception e){
        	e.printStackTrace();
        }
        return true;
	}
}
