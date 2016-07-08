package com.dsm.rxjava;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

import com.dsm.mystudy.R;
import com.jakewharton.rxbinding.view.RxView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class RxjavaOperatorFragment extends Fragment {

	private static final String TAG = RxjavaOperatorFragment.class.getSimpleName();
	private TextView mLogcat;
	private Button mDefer;
	
	private Observable<Long> mDeferObservable;
	private Observable<Long> mJustObservable;
	
	private Observable<Long> mInteObservable;
	private Subscriber<Long> mInteSubscriber;
	
	private Subscription mWindowTime;
	private boolean isStartOfWindowTime = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rxjava_operator, container, false);
		
		initViews(view);
		
		return view;
	}
	
	private void initViews(View view) {
		mLogcat = (TextView) view.findViewById(R.id.tv_logcat);
		mLogcat.setMovementMethod(ScrollingMovementMethod.getInstance());
		
	    mDefer = (Button) view.findViewById(R.id.btn_defer);
        RxView.clicks(mDefer)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				rxjavaDeferOperator1();
			}
		});
        
        final Button interval_l = (Button) view.findViewById(R.id.btn_interval_l);
        final Button interval_r = (Button) view.findViewById(R.id.btn_interval_r);
        RxView.clicks(interval_l)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				rxjavaIntervalOperator();
				showLastestLog("Starting Interval operator");
				mInteObservable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(mInteSubscriber);
				
				interval_l.setEnabled(false);
				interval_r.setEnabled(true);
			}
		});
        
        RxView.clicks(interval_r)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				showLastestLog("Stoping Interval operator");
				if (mInteSubscriber != null && !mInteSubscriber.isUnsubscribed()) {
					mInteSubscriber.unsubscribe();
				}
				interval_l.setEnabled(true);
				interval_r.setEnabled(false);
			}
		});
        
        rxjavaOperatorTest();
        rxjavaDeferOperator();
        rxjavaGroupByOperator();
        rxjavaGroupByOperator_2();
        
        Button timer = (Button)view.findViewById(R.id.btn_timer);
        RxView.clicks(timer)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				rxjavaTimerOperator();
			}
		});
        
        Button repeat = (Button)view.findViewById(R.id.btn_repeat);
        RxView.clicks(repeat)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				rxjavaRepeatOperator();
			}
		});
        
        Button buffer = (Button)view.findViewById(R.id.btn_buffer);
        RxView.clicks(buffer)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				getBufferObservable()
				.subscribe(new Action1<List<Integer>>() {
					@Override
					public void call(List<Integer> lists) {
						String str = "Buffer Operator: ";
						for (Integer i : lists) {
							str = str + i + " ";
						}
						showLastestLog(str);
					}
				});
				
				getBufferObservable()
				.doOnRequest(new Action1<Long>() {
					@Override
					public void call(Long t) {
						showLastestLog("Buffer Operator 1: ");
					}
				})
				.flatMap(new Func1<List<Integer>, Observable<Integer>>() {
					@Override
					public Observable<Integer> call(List<Integer> lists) {
						return Observable.from(lists);
					}
				})
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer t) {
						showLastestLog(t + "");
					}
				});
			}
		});
        
        Button window_count = (Button)view.findViewById(R.id.btn_window_count);
        RxView.clicks(window_count)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				getWindowObservable()
				.subscribe(new Action1<Observable<Integer>>() {
					@Override
					public void call(Observable<Integer> obs) {
						showLastestLog("Window By Count: ");
						obs.subscribe(new Action1<Integer>() {
							@Override
							public void call(Integer t) {
								showLastestLog("window count: " + t);
							}
						});
					}
				});
			}
		});
        
        Button window_time = (Button)view.findViewById(R.id.btn_window_time);
        RxView.clicks(window_time)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Void>() {
			@Override
			public void call(Void t) {
				if (!isStartOfWindowTime) {
					mWindowTime = getWindowTimeObservable()
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Action1<Observable<Long>>() {
						@Override
						public void call(Observable<Long> obs) {
							showLastestLog("Window By Time: ");
							obs.subscribe(new Action1<Long>() {
								@Override
								public void call(Long t) {
									showLastestLog("window time: " + t);
								}
							});
						}
					});
					isStartOfWindowTime = true;
				} else {
					if (mWindowTime != null && !mWindowTime.isUnsubscribed()) {
						mWindowTime.unsubscribe();
						isStartOfWindowTime = false;
					}
				}
				
			}
		});
	}
	
	private void showLastestLog(String str) {
		Log.d(TAG, str);
		mLogcat.append(str + "\n");
		int offset = mLogcat.getLineCount() * mLogcat.getLineHeight();
	    if(offset > mLogcat.getHeight()){
	    	mLogcat.scrollTo(0, offset - mLogcat.getHeight() + mLogcat.getLineHeight());
	    }
	}
	
	private void rxjavaOperatorTest() {
		rxjavaRangeOperator();
	}
	
	/*
	 *  Range操作符根据输入的初始值n和数目m发射一系列大于等于n的m个值
	 */
	private void rxjavaRangeOperator() {
		Observable.range(10, 5)
		.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer t) {
				showLastestLog("The current value is " + t);
			}
		});
	}
	
	/*
	 * Defer操作符只有当有Subscriber来订阅的时候才会创建一个新的Observable对象,也就是说每次订阅都会得到一个刚创建的最新的Observable对象，
	 * 这可以确保Observable对象里的数据是最新的，其特点我们将在下面和Just进行对比理解。
	 */
	private void rxjavaDeferOperator() {
		mDeferObservable = Observable.defer(new Func0<Observable<Long>>() {
			@Override
			public Observable<Long> call() {
				return Observable.just(System.currentTimeMillis());
			}
		});
			
		mJustObservable = Observable.just(System.currentTimeMillis());
	}
	private void rxjavaDeferOperator1() {
		mDeferObservable.subscribe(new Action1<Long>() {
			@Override
			public void call(Long t) {
				showLastestLog("Defer Operator is " + t);
			}
		});
		
		mJustObservable.subscribe(new Action1<Long>() {
			@Override
			public void call(Long t) {
				showLastestLog("Just Operator is " + t);
			}
		});
	}
	
	/*
	 *  Interval所创建的Observable对象会从0开始，每隔固定的时间发射一个数字。需要注意的是这个对象是运行在computation Scheduler,
	 *  所以如果需要在view中显示结果，要在主线程中订阅。
	 */
	private void rxjavaIntervalOperator() {
		mInteObservable = Observable.interval(1, TimeUnit.SECONDS);
		
		mInteSubscriber = new Subscriber<Long>() {
			@Override
			public void onCompleted() {}

			@Override
			public void onError(Throwable e) {}

			@Override
			public void onNext(Long t) {
				showLastestLog("Interval Operator is " + t);
			}
		};
	}
	
	/*
	 * Timer会在指定时间后发射一个数字0，注意其也是运行在computation Scheduler
	 */
	private void rxjavaTimerOperator() {
		showLastestLog("You will get a event after 3 seconds!");
		Observable.timer(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
		.subscribe(new Action1<Long>() {
			@Override
			public void call(Long t) {
				showLastestLog("This is a test of Timer operator!");
			}
		});
	}
	
	private void rxjavaRepeatOperator() {
		Observable.just("Repeat operator test!").repeat(3)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<String>() {
			@Override
			public void call(String t) {
				showLastestLog("This is a test of Repeat operator!");
			}
		});
	}
	
	/* 
	 * Rxjava buffer操作符
	 */
	private Observable<List<Integer>> getBufferObservable() {
		 return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).buffer(3);
	}
	
	private Observable<List<Long>> getTimeObservable() {
		return Observable.interval(1, TimeUnit.SECONDS)
				.buffer(3, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread());
	}
	
	private Observable<Observable<Integer>> getWindowObservable() {
		return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).window(3);
	}
	
	private Observable<Observable<Long>> getWindowTimeObservable() {
		return Observable.interval(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
				.window(3000, TimeUnit.MILLISECONDS);
	}
	
	/*
	 * GroupBy操作符将原始Observable发射的数据按照key来拆分成一些小的Observable，然后这些小的Observable分别发射其所包含的的数据，类似于sql里面的groupBy。
	 * 在使用中，我们需要提供一个生成key的规则，所有key相同的数据会包含在同一个小的Observable种。另外我们还可以提供一个函数来对这些数据进行转化，有点类似于集成了flatMap。
	 */
	private void rxjavaGroupByOperator() {
		
		Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
		.groupBy(new Func1<Integer, Integer>() {
			@Override
			public Integer call(Integer integer) {
				return integer % 2;
			}
		})
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
			@Override
			public void call(final GroupedObservable<Integer, Integer> t) {
				// 在GroupBy中中只能有一个Subscriber
				/*t.subscribe(new Subscriber<Integer>() {
					@Override
					public void onNext(Integer value) {
						showLastestLog("key:" + t.getKey() +", value:" + value);
					}
					
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
					}
				});*/
				
				t.count().subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						showLastestLog("key" + t.getKey() + " contains:" + integer + " numbers");
					}
				});
			}

		});

	}
	
	private void rxjavaGroupByOperator_2() {
		/*Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
		.groupBy(new Func1<Integer, String>() {
			@Override
			public String call(Integer integer) {
				String key = null;
				if(integer < 5) {
					key = "小于5";
				} else {
					key = "不小于5";
				}
				return key;
			}
		}).subscribe(new Action1<GroupedObservable<String, Integer>>() {
			@Override
			public void call(final GroupedObservable<String, Integer> group) {
				group.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer value) {
						if (group.getKey().equals("不小于5")) {
							showLastestLog("key = " + group.getKey() +", value:" + value);
						}
					}
				});
			}
		});*/
		
		Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
		.groupBy(new Func1<Integer, String>() {
			@Override
			public String call(Integer integer) {
				String key = null;
				if(integer < 5) {
					key = "小于5";
				} else {
					key = "不小于5";
				}
				return key;
			}
		}, new Func1<Integer, String>() {
			@Override
			public String call(Integer value) {
				return "GroupByKeyValue: " + value;
			}
		})
		.subscribe(new Action1<GroupedObservable<String, String>>() {
			@Override
			public void call(final GroupedObservable<String, String> group) {
				group.subscribe(new Action1<String>() {
					@Override
					public void call(String value) {
						if (group.getKey().equals("不小于5")) {
							showLastestLog("key = " + group.getKey() +", value:" + value);
						}
					}
				});
			}
		});
	}
}
