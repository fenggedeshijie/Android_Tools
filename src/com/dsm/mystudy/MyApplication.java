package com.dsm.mystudy;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import android.app.Application;

public class MyApplication extends Application {

	{//友盟各个平台的配置
		//微信    
		PlatformConfig.setWeixin("wx4f6c136fa79d7397", "f7d1f60ae75e8491201a289671cae459");
        Config.OpenEditor = false;
	}
}
