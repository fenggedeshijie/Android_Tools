package com.dsm.netty;


import android.util.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class NetUdpReceiver {

	private EventLoopGroup workerGroup = null;
	private Bootstrap bootstrap = null;
	private Channel mChannel = null;
	
	public NetUdpReceiver() {
		workerGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(workerGroup);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.option(ChannelOption.SO_BROADCAST, true);  
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
	}
	
	public boolean startServer(int inetPort, final FrameHandler handler) {
		stopServer();
		bootstrap.handler(new ChannelInitializer<DatagramChannel>() {
			@Override
			public void initChannel(DatagramChannel ch) throws Exception {
				ChannelPipeline pipe = ch.pipeline();
				pipe.addLast("udpFrameEncoder", new UdpFrameEncoder());
				pipe.addLast("udpFrameDecoder", new UdpFrameDecoder());
				pipe.addLast("udpMyHandler", handler);
			}
		});
		
		try {  
			mChannel = bootstrap.bind(inetPort).sync().channel();  
        } catch (Exception e) {  
            Log.d(String.format("Udp listen (PORT[%s]) failed", inetPort), e.getMessage());  
            return false;  
        }  
		return true;
	}
	
	public void stopServer() {
		if (mChannel != null && mChannel.isOpen()) {
			mChannel.close();
		}
		mChannel = null;
	}
	
	public boolean isRunning(){
		if(mChannel == null)
			return false;
		return mChannel.isOpen();
	}
	
	public void release(){
		workerGroup.shutdownGracefully();
	}
	
	// 发送数据
	public boolean send(FrameData data){     
    	try { 
    		mChannel.writeAndFlush(data).sync();
    		Log.d("NetUdpReceiver", "send MyFrameData");  
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
		}
        return true;
	}
}
