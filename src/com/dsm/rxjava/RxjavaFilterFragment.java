package com.dsm.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import com.dsm.mystudy.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RxjavaFilterFragment extends Fragment implements OnClickListener{

	private static final String TAG = RxjavaFilterFragment.class.getCanonicalName();
	private TextView mLogcat;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rxjava_filtering, container, false);
		mLogcat = (TextView)view.findViewById(R.id.tv_logcat);
		mLogcat.setMovementMethod(ScrollingMovementMethod.getInstance());
		initViews(view);
		return view;
	}
	
	private void initViews(View view) {
		Button debounce = (Button) view.findViewById(R.id.btn_debounce);
		Button distinct = (Button) view.findViewById(R.id.btn_distinct);
		Button distinct_unit = (Button) view.findViewById(R.id.btn_distinct_unit);
		Button elementAt = (Button) view.findViewById(R.id.btn_elementat);
		Button filter = (Button) view.findViewById(R.id.btn_filter);
		Button first = (Button) view.findViewById(R.id.btn_first);
		Button last = (Button) view.findViewById(R.id.btn_last);
		Button block = (Button) view.findViewById(R.id.btn_block);
		Button take = (Button) view.findViewById(R.id.btn_take);
		Button skip = (Button) view.findViewById(R.id.btn_skip);
		Button sample = (Button) view.findViewById(R.id.btn_sample);
		Button throttleFirst = (Button) view.findViewById(R.id.btn_throttleFirst);
		
		debounce.setOnClickListener(this);
		distinct.setOnClickListener(this);
		distinct_unit.setOnClickListener(this);
		elementAt.setOnClickListener(this);
		filter.setOnClickListener(this);
		first.setOnClickListener(this);
		last.setOnClickListener(this);
		block.setOnClickListener(this);
		skip.setOnClickListener(this);
		take.setOnClickListener(this);
		sample.setOnClickListener(this);
		throttleFirst.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_debounce:
			debounceObservable()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Action1<Integer>() {
				@Override
				public void call(Integer t) {
					showLastestLog("Debounce func: " + t);
				}
			});
			break;
		case R.id.btn_distinct:
			rxjavaDistinctOperator();
			break;
		case R.id.btn_distinct_unit:
			rxjavaDistinctUntilChangedOperator();
			break;
		case R.id.btn_elementat:
			rxjavaElementAtOperator();
			break;
		case R.id.btn_filter:
			rxjavaFilterOperator();
			break;
		case R.id.btn_first:
			rxjavaFirstOperator();
			break;
		case R.id.btn_last:
			rxjavaLastOperator();
			break;
		case R.id.btn_block:
			rxjavaBlockingObservableOperator();
			break;
		case R.id.btn_skip:
			rxjavaSkipOperator();
			break;
		case R.id.btn_take:
			rxjavaTakeOperator();
			break;
		case R.id.btn_sample:
			rxjavaSampleOperator();
			break;
		case R.id.btn_throttleFirst:
			rxjavaThrottleFirstOperator();
			break;
		}
	}
	
	private void showLastestLog(String str) {
		Log.d(TAG, str);
		mLogcat.append(str + "\n");
		int offset = mLogcat.getLineCount() * mLogcat.getLineHeight();
	    if(offset > mLogcat.getHeight()){
	    	mLogcat.scrollTo(0, offset - mLogcat.getHeight() + mLogcat.getLineHeight());
	    }
	}
	
	private Observable<Integer> deBounceObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(i);
                    }
                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation());
    }
	
	
	private Observable<Integer> debounceObservable() {
		return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
				.debounce(new Func1<Integer, Observable<Integer>>() {
					@Override
					public Observable<Integer> call(final Integer integer) {
						return Observable.create(new OnSubscribe<Integer>() {
							@Override
							public void call(Subscriber<? super Integer> sub) {
								if (integer % 2 == 0) {
									sub.onNext(integer);
									sub.onCompleted();
								}
							}
						});
					}
				});
	}

	
	/*
	 * Distinct操作符的用处就是用来去重，非常好理解。如下图所示，所有重复的数据都会被过滤掉。
	 * 还有一个操作符distinctUntilChanged,是用来过滤掉连续的重复数据。
	 */
	private void rxjavaDistinctOperator() {
        Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1)
        .distinct()
        .subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer t) {
				showLastestLog("Distinct: " + t);
			}
		});
    }

    private void rxjavaDistinctUntilChangedOperator() {
        Observable.just(1, 2, 3, 3, 3, 1, 2, 3, 3)
        .distinctUntilChanged()
        .subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer t) {
				showLastestLog("DistinctUnitChanged: " + t);
			}
		});
    }
	
    /*
     * 这两个操作符都很好理解，ElementAt只会返回指定位置的数据，而Filter只会返回满足过滤条件的数据.
     */
    private void rxjavaElementAtOperator() {
        Observable.just(0, 1, 2, 3, 4, 5)
        .elementAt(2)
        .subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer t) {
				showLastestLog("Elementat: " + t);
			}
		});
    }

    private void rxjavaFilterOperator() {
        Observable.just(0, 1, 2, 3, 4, 5)
        .filter(new Func1<Integer, Boolean>() {
			@Override
			public Boolean call(Integer t) {
				return t < 3;
			}
		})
		.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer t) {
				showLastestLog("Filter: " + t);
			}
		});
    }
  
    /*
     * First操作符只会返回第一条数据，并且还可以返回满足条件的第一条数据。如果你看过我以前的博客，就会发现在我们使用Rxjava实现三级缓存的例子里，
     * 就是使用first操作符来选择所要使用的缓存。与First相反，Last操作符只返回最后一条满足条件的数据。
     */
    private void rxjavaFirstOperator() {
    	Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    	.first(new Func1<Integer, Boolean>() {
			@Override
			public Boolean call(Integer t) {
				return t > 3;
			}
		})
		.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer value) {
				showLastestLog("First Operator: " + value);
			}
		});
    }
    
    private void rxjavaLastOperator() {
    	Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    	.last(new Func1<Integer, Boolean>() {
			@Override
			public Boolean call(Integer t) {
				return t < 7;
			}
		})
		.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer value) {
				showLastestLog("Last Operator: " + value);
			}
		});
    }

    private void rxjavaBlockingObservableOperator() {
    	int value = Observable.create(new OnSubscribe<Integer>() {
			@Override
			public void call(Subscriber<? super Integer> sub) {
				for (int i = 0; i < 9; i ++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("onNext" + i);
					sub.onNext(i);
				}
				sub.onCompleted();
			}
		})
		.toBlocking()
		.first(new Func1<Integer, Boolean>() {
			@Override
			public Boolean call(Integer value) {
				return value > 3;
			}
		});
    	
    	showLastestLog("the value is " + value);
    }
    
    /*
     * Skip操作符将源Observable发射的数据过滤掉前n项，而Take操作符则只取前n项，理解和使用起来都很容易，但是用处很大。
     * 另外还有SkipLast和TakeLast操作符，分别是从后面进行过滤操作。
     */
    private void rxjavaSkipOperator() {
    	Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    	.skip(3)
    	.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer value) {
				showLastestLog("Skip Operator: " + value);
			}
		});
    }
    
    private void rxjavaTakeOperator() {
    	Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    	.take(3)
    	.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer value) {
				showLastestLog("Take Operator: " + value);
			}
		});
    }
    
    /*
     * Sample操作符会定时地发射源Observable最近发射的数据，其他的都会被过滤掉，等效于ThrottleLast操作符，
     * 而ThrottleFirst操作符则会定期发射这个时间段里源Observable发射的第一个数据
     * 我们创建一个Observable每隔200毫秒发射一个数据，然后分别使用sample和throttleFirst操作符对其进行过滤
     */
    private void rxjavaSampleOperator() {
    	Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        })
        .sample(1000, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer value) {
				showLastestLog("Sample Operator: " + value);
			}
		});
    }
    
    private void rxjavaThrottleFirstOperator() {
    	Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        })
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer value) {
				showLastestLog("ThrottleFirst Operator: " + value);
			}
		});
    	
    }
}
