package com.dsm.netty;


import android.util.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/*
 *  netty中事件的传播
 *  write:
 *  1. channel.write();
 *  2. channel.write() --> pipe.write()
 *  3. pipe.write() --> tail.write(), tail是一个特殊的Context, 所以其实是调用了context.write()
 *     context.write方法的作用是：寻找下一个包含ChannelOutboundHandler的Context
 *  4. context.invokewrite()
 *  5. Context.invokewrite() --> handler.write()
 *  6. handler.write() -- > context.write()
 *  
 *  channel.write() --> pipe.write() --> context.write() --> context.invokewrite() --> handler.write()
 *  --> context.write();
 */
public class NetTcpReceiver {

	private static final int PORT = Integer.parseInt(System.getProperty("port", "5523"));
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private ServerBootstrap bootstrap = null;
	private Channel mListenChannel = null;
	
	public NetTcpReceiver() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	}
	
	public boolean startServer(int inetPort) {
		stopServer();
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipe = ch.pipeline();
				//pipe.addLast(new TcpFrameDecoder());
				//pipe.addLast(new ReceiverHandler());
			}
		}).childOption(ChannelOption.SO_KEEPALIVE, true);
		
		try {  
			// Bind and start to accept incoming connections.
			mListenChannel = bootstrap.bind(inetPort).sync().channel();  
        } catch (Exception e) {  
            Log.d(String.format("TCP����(PORT[%s])ʧ��", PORT), e.getMessage());  
            return false;  
        }  
		return true;
	}
	
	public void stopServer() {
		if (mListenChannel != null && mListenChannel.isOpen()) {
			mListenChannel.close();
		}
		mListenChannel = null;
	}
	
	public boolean isRunning(){
		if(mListenChannel == null)
			return false;
		return mListenChannel.isOpen();
	}
	
	public void release(){
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
	// 发送数据
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
