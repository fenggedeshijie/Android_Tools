package com.dsm.mystudy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.dsm.inject.ViewInjectUtils;
import com.dsm.inject.annotation.ContentView;
import com.dsm.inject.annotation.OnClick;
import com.dsm.inject.annotation.ViewInject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(value = R.layout.activity_dynamic_proxy)
public class DynamicProxyActivity extends Activity {

	@ViewInject(value = R.id.btn_proxy)
	private Button mButton;
	
	@ViewInject(value = R.id.tv_logcat)
	private TextView mLogcat;
	
	@ViewInject(value = R.id.im_headback)
	private ImageView mHeadback;
	
	@ViewInject(value = R.id.im_headtitle)
	private TextView mHeadtitle;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewInjectUtils.inject(this);

        initHeadViews();
        
        testDynamicProxy();
        myProxyTest();
    }
    
    @TargetApi(Build.VERSION_CODES.KITKAT) 
	private void initHeadViews() {
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     // 透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); // 透明导航栏
        mLogcat.setMovementMethod(ScrollingMovementMethod.getInstance());
		mHeadtitle.setText("Java的动态代理");
	}
    
    @OnClick({R.id.im_headback})
    private void headClick(View view) {
    	switch (view.getId()) {
    	case R.id.im_headback:
    		finish();
    		break;
    	}
    }
    
    private void showLastestLog(String str) {
		Log.d("DynamicProxyActivity", str);
		mLogcat.append(str + "\n");
		int offset = mLogcat.getLineCount() * mLogcat.getLineHeight();
	    if(offset > mLogcat.getHeight()){
	    	mLogcat.scrollTo(0, offset - mLogcat.getHeight() + mLogcat.getLineHeight());
	    }
	}
    
    public interface Subject {
        public void rent();
        public void hello(String str);
    }
    
    public class RealSubject implements Subject {
        @Override
        public void rent() {
            System.out.println("I want to rent my house");
        }
        
        @Override
        public void hello(String str) {
            System.out.println("hello: " + str);
        }
    }
    
    public class DynamicProxy implements InvocationHandler {
        //　这个就是我们要代理的真实对象
        private Object subject;
        
        // 构造方法，给我们要代理的真实对象赋初值
        public DynamicProxy(Object subject) {
            this.subject = subject;
        }
        
        @Override
        public Object invoke(Object object, Method method, Object[] args)
                throws Throwable {
            //　在代理真实对象前我们可以添加一些自己的操作
            System.out.println("before rent house");
            System.out.println("Method:" + method);
            // 当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
            method.invoke(subject, args);
            // 在代理真实对象后我们也可以添加一些自己的操作
            System.out.println("after rent house");
            return null;
        }

    }
    
    private void testDynamicProxy() {
    	// 我们要代理的真实对象
        Subject realSubject = new RealSubject();

        // 我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new DynamicProxy(realSubject);

        /*
         * 通过Proxy的newProxyInstance方法来创建我们的代理对象，我们来看看其三个参数
         * 第一个参数 handler.getClass().getClassLoader() ，我们这里使用handler这个类的ClassLoader对象来加载我们的代理对象
         * 第二个参数realSubject.getClass().getInterfaces()，我们这里为代理对象提供的接口是真实对象所实行的接口，
         * 表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
         * 第三个参数handler， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
         */
        Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);
        
        System.out.println(subject.getClass().getName());
        subject.rent();
        subject.hello("world");
    }
    
    
    /*
     * 以下是我本人写的关于java动态代理的测试代码
     */
    public void click(View view) {
    	showLastestLog("This is my proxy code!");
    	Toast.makeText(this, "Dyname Proxy", Toast.LENGTH_SHORT).show();
    }
    
    public class DynamicProxyHandler implements InvocationHandler {
        private Object subject;
        private Map<String, Method> methods = new HashMap<String, Method>();
        
        public DynamicProxyHandler(Object subject) {
            this.subject = subject;
        }
        
        public void addMethod(String methodName, Method method) {  
            methods.put(methodName, method);  
        }  
      
        @Override  
        public Object invoke(Object proxy, Method method, Object[] args)  
                throws Throwable {
        	showLastestLog("Method: " + method);
            String methodName = method.getName();  
            Method realMethod = methods.get(methodName);  
            return realMethod.invoke(subject, args);
        }  
    }
    
    private void myProxyTest() {
    	DynamicProxyHandler handler = new DynamicProxyHandler(this);
    	Object proxy = Proxy.newProxyInstance(OnClickListener.class.getClassLoader(), 
    				new Class<?>[] { OnClickListener.class }, handler);
    
		try {
			Method method = getClass().getMethod("click", View.class);
			handler.addMethod("onClick", method);
			Method clickMethod = mButton.getClass().getMethod("setOnClickListener", OnClickListener.class);
			clickMethod.invoke(mButton, proxy);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
    }
    
}
