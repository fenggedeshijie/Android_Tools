package com.dsm.rxjava;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import com.dsm.mystudy.R;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RxjavaBasicFragment extends Fragment {

	private static final String TAG = RxjavaBasicFragment.class.getSimpleName();
	private TextView mLogcat;
	
	private TextView mTextView;
	private Button mButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rxjava_basic, container, false);
		
		initViews(view);
		startObservable();
		
		return view;
	}
	
	private void initViews(View view) {
		mLogcat = (TextView) view.findViewById(R.id.tv_logcat);
		mLogcat.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		mTextView = (TextView) view.findViewById(R.id.tv_title);
		mButton = (Button) view.findViewById(R.id.btn_button);
	}
	
	private void startObservable() {
		firstFuncOfRxJava();
        secondFuncOfRxJava();
        thirdFuncOfRxJava();
        fourthFuncOfRxJava(); 
        testRxJavaOperators();
        
        testRxAndroid();
	}
	
	private void showLastestLog(String str) {
		Log.d(TAG, str);
		mLogcat.append(str + "\n");
		int offset = mLogcat.getLineCount() * mLogcat.getLineHeight();
	    if(offset > mLogcat.getHeight()){
	    	mLogcat.scrollTo(0, offset - mLogcat.getHeight() + mLogcat.getLineHeight());
	    }
	}
	
	private void firstFuncOfRxJava() {
		Observable<String> observable = Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> sub) {
				sub.onNext("Hello, This is first function!");
				sub.onCompleted();
			}
		});
		
		Subscriber<String> subscriber = new Subscriber<String>() {
			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(String str) {
				showLastestLog(str);
			}
		};
		
		observable.subscribe(subscriber);
	}
	
	private void secondFuncOfRxJava() {
		Observable<String> observable = Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> sub) {
				sub.onNext("Hello, This is second function!");
				sub.onNext("This is next one sentence!");
				sub.onNext("This is next two sentence!");
				sub.onCompleted();
			}
		});
		
		Action1<String> onNextAction = new Action1<String>() {
			@Override
			public void call(String str) {
				showLastestLog(str);
			}
		};
		
		observable.subscribe(onNextAction);
	}
	
	private void thirdFuncOfRxJava() {
		Observable.just("Hello, This is third function!")
			.subscribe(new Action1<String>() {
				@Override
				public void call(String str) {
					showLastestLog(str);
				}
			});
		
		Observable.just("Hello, This is third function! --Tong Cheng")
		.subscribe(new Action1<String>() {
			@Override
			public void call(String str) {
				showLastestLog(str);
			}
		});
		
		Observable.just("Hello, This is third function!")
			.subscribe(new Action1<String>() {
				@Override
				public void call(String str) {
					showLastestLog(str + " --Tong Cheng");
				}
			});
		
		Observable.just("Hello, This is third function!")
			.map(new Func1<String, String>() {
				@Override
				public String call(String str) {
					return str + " --Tony Cheng";
				}
			})
			.subscribe(new Action1<String>() {
				@Override
				public void call(String str) {
					showLastestLog(str);
				}
			});
	}

	private void fourthFuncOfRxJava() {
		Observable.just("Hello, This is fourth function!")
			.map(new Func1<String, Integer>() {
				@Override
				public Integer call(String str) {
					return str.hashCode();
				}
			})
			.map(new Func1<Integer, String>() {
				@Override
				public String call(Integer arg) {
					return arg.toString();
				}
			})
			.subscribe(new Action1<String>() {
				@Override
				public void call(String arg) {
					showLastestLog(arg);
				}
			});
	}

	
	private List<String> getInfoList(String key) {
		List<String> urls = new ArrayList<String>();
		urls.add("http://hao.360.cn/kuanping.html");
	    urls.add("http://www.cnblogs.com/halzhang/p/4458095.html");
	    urls.add("http://www.tuicool.com/articles/BBNRRf");
	    urls.add("http://blog.csdn.net/lzyzsd/article/details/41833541");
	    urls.add("");
		return urls;
	}
	
	private Observable<String> getUrlTitle(final String url) {
		return Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> sub) {
				String title = null;
				if (!TextUtils.isEmpty(url)) {
					title = url + "/hehe";
				}
				sub.onNext(title);
			}
		});
	}
	
	private Observable<List<String>> mySearch(final String key) {
		return Observable.create(new OnSubscribe<List<String>>() {
			@Override
			public void call(Subscriber<? super List<String>> sub) {
				sub.onNext(getInfoList(key));
	            sub.onCompleted();
			}
		});
	}

	private void testRxJavaOperators() {
		
		/*mySearch("大自然")
		.subscribe(new Action1<List<String>>() {
			@Override
			public void call(List<String> urls) {
				for (String url : urls) {
					System.out.println(url);
				}
			}
		});
		
		mySearch("大自然")
		.subscribe(new Action1<List<String>>() {
			@Override
			public void call(List<String> urls) {
				Observable.from(urls)
				.subscribe(new Action1<String>() {
					@Override
					public void call(String url) {
						System.out.println(url);
					}
				});
			}
		});*/
		
		mySearch("大自然")
		.flatMap(new Func1<List<String>, Observable<String>>() {
			@Override
			public Observable<String> call(List<String> urls) {
				return Observable.from(urls);
			}
		})
		.flatMap(new Func1<String, Observable<String>>() {
			@Override
			public Observable<String> call(String url) {
				return getUrlTitle(url);
			}
		})
		.filter(new Func1<String, Boolean>() {
			@Override
			public Boolean call(String title) {
				return title != null;
			}
		})
		.take(3)
		.doOnNext(new Action1<String>() {
			@Override
			public void call(String title) {
				showLastestLog("Sava those titles to disk in here!");
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<String>() {
			@Override
			public void call(String title) {
				showLastestLog(title);
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void testRxAndroid() {
		RxView.clicks(mButton)
		.throttleFirst(500, TimeUnit.MILLISECONDS)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void arg0) {
				showLastestLog("Click Once Button!");
				long time = System.currentTimeMillis();
			    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    Date date = new Date(time);  
				mTextView.setText(format.format(date));
			}
		});
		
		RxView.longClicks(mButton)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				Toast.makeText(getActivity(), "This is a long click!", Toast.LENGTH_SHORT).show();
			}
		});
		
		RxTextView.textChanges(mTextView)
		.subscribe(new Action1<CharSequence>() {
			@Override
			public void call(CharSequence text) {
				showLastestLog("TextView's text changes to " + text);
			}
		});
		
		RxTextView.textChangeEvents(mTextView)
		.subscribe(new Action1<TextViewTextChangeEvent>() {
			@Override
			public void call(TextViewTextChangeEvent event) {
				showLastestLog("TextView text had been changed!");
			}
		});
	}

	private void rxjavaOfParabola() {	// 来自“抛物线”博客
		
		final File[] folders = new File[5];
		new Thread() {
		    @Override
		    public void run() {
		        super.run();
		        for (File folder : folders) {
		            File[] files = folder.listFiles();
		            for (File file : files) {
		                if (file.getName().endsWith(".png")) {
		                    //final Bitmap bitmap = getBitmapFromFile(file);
		                    getActivity().runOnUiThread(new Runnable() {
		                        @Override
		                        public void run() {
		                            //imageCollectorView.addImage(bitmap);
		                        }
		                    });
		                }
		            }
		        }
		    }
		}.start();
		
		Observable.from(folders)
		.flatMap(new Func1<File, Observable<File>>() {
			@Override
			public Observable<File> call(File folder) {
				return Observable.from(folder.listFiles());
			}
		})
		.filter(new Func1<File, Boolean>() {
			@Override
			public Boolean call(File file) {
				String name = file.getName();
				return name.endsWith(".png") || name.endsWith(".PNG");
			}
		})
		.map(new Func1<File, Bitmap>() {
			@Override
			public Bitmap call(File file) {
				Bitmap bitmap = null;
				return bitmap;
			}
		})
		.toList()
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<List<Bitmap>>() {
			@Override
			public void call(List<Bitmap> arg0) {
				
			}
		});
	}

	private static Bitmap getBitmapFromFile(File file) {
		return null;
	}
	
	public static void loadPicture(File[] floder, final Context context) {
		Observable.from(floder)
		.flatMap(new Func1<File, Observable<File>>() {
			@Override
			public Observable<File> call(File file) {
				return Observable.from(file.listFiles());
			}
		})
		.filter(new Func1<File, Boolean>() {
			@Override
			public Boolean call(File file) {
				String fileName = file.getName();
				return fileName.endsWith(".png") || fileName.endsWith(".PNG");
			}
		})
		.map(new Func1<File, Bitmap>() {
			@Override
			public Bitmap call(File file) {
				return getBitmapFromFile(file);
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<Bitmap>() {
			@Override
			public void call(Bitmap bitmap) {
				//TODO
			}
		});
	}
	
	public static void printString(String[] names) {
		Observable.from(names)
		.subscribe(new Action1<String>() {
			@Override
			public void call(String t) {
				System.out.println(t);
			}
		});
	}
	
}
