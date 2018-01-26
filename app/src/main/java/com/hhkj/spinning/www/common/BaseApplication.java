package com.hhkj.spinning.www.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alivc.player.AliVcMediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.service.SpinningService;
import com.hhkj.spinning.www.ui.PlayerActivity;
import com.hhkj.spinning.www.utils.ClientManager;
import com.inuker.bluetooth.library.BluetoothContext;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;


import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;

public class BaseApplication extends Application {
    private static final boolean DEVELOPER_MODE = true;
    public static BaseApplication application;
    protected boolean isNeedCaughtExeption = true;// 是否捕获未知异常
    private MyUncaughtExceptionHandler uncaughtExceptionHandler;
    private static String packgeName;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        application = this;
        Config.isJumptoAppStore = true;
        Config.DEBUG = true;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxb3c22f4afd141744", "b14688e0458766d438e88014baa9ba67");
        PlatformConfig.setQQZone("1106546585", "n8KeWMdPkW13dWdW");
        PlatformConfig.setSinaWeibo("", "", "");
        AliVcMediaPlayer.init(this);
        BluetoothContext.set(this);
        packgeName = getPackageName();
        if (isNeedCaughtExeption) {
            cauchException();
        }



        initImageLoader(application);
        P.c("启动");
//		mLocationClient = new LocationClient(application); // 声明LocationClient类
//		mLocationClient.registerLocationListener(myListener); // 注册监听函数
//		initLocation();
//		mLocationClient.start();
       startService();
    }
    private void startService(){
        Intent startServiceIntent = new Intent(this, SpinningService.class);
        startServiceIntent.putExtra("watch","");
        startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(startServiceIntent);
    }
    public static String getName() {
        return packgeName;
    }

    public void initImageLoader(Context context) {

        DisplayImageOptions dio = new DisplayImageOptions.Builder().build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                /**
                 * 传入另一个DisplayImageOptions会覆盖上面的设置
                 */
                .cloneFrom(dio)
                /**
                 * 加载时的图片
                 */
//                .showImageOnLoading(bd)
//                .showImageOnLoading(R.drawable.ic_stub)
//                .showStubImage(R.drawable.ic_stub)
                /**
                 * 加载图片失败后显示的图片
                 */
                //   .showImageOnFail(bd)
                //    .showImageOnFail(R.drawable.ic_error)
                /**
                 * 加载图片为空的时候
                 */
                //     .showImageForEmptyUri(bd)
                //    .showImageForEmptyUri(R.drawable.ic_empty)
                /**
                 * 在内存中缓存
                 * 默认：true
                 */
                .cacheInMemory(true)
                .cacheInMemory()
                /**
                 * 在磁盘中缓存
                 * 默认：true
                 */
                .cacheOnDisk(true)
                .cacheOnDisc(true)
                .cacheOnDisc()
                /**
                 * 设置Bitmap.Config
                 * 默认：Bitmap.Config.ARGB_8888
                 */
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                /**
                 * 是否考虑EXIF的信息
                 * 默认：false
                 */
                .considerExifParams(false)
                /**
                 * 设置解析图片参数
                 */
                .decodingOptions(new BitmapFactory.Options())
                /**
                 * 设置加载图片的时间
                 * 默认：0
                 */
                .delayBeforeLoading(0)
                /**
                 * 设置自定义BitmapDisplayer
                 * 默认：SimpleBitmapDisplayer
                 */
                .displayer(DefaultConfigurationFactory.createBitmapDisplayer())
                /**
                 * 设置辅助的传递对象
                 * 默认：null
                 */
                .extraForDownloader(null)
                /**
                 * 设置自定义Hanlder
                 */
                .handler(null)
                /**
                 * 设置解码时图片的缩放
                 * 默认：ImageScaleType.IN_SAMPLE_POWER_OF_2
                 */
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                /**
                 * 在图片加载到内存中前进行处理
                 * 默认：null
                 */
                .preProcessor(null)
                /**
                 * 在显示图片时对图片处理
                 * 默认：null
                 */
                .postProcessor(null)
                /**
                 * 设置是否在加载图片前重置
                 * 默认：true
                 */
                .resetViewBeforeLoading(true)
                .resetViewBeforeLoading()

                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileNameGenerator(newMd5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(500) //缓存的文件数量
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建

        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().clearDiskCache();
    }


   /* public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.img_load) // resource or
            // drawable
            .showImageForEmptyUri(R.mipmap.no_img) // resource or
            // drawable
            .showImageOnFail(R.mipmap.no_img) // resource or
            // drawable
            .resetViewBeforeLoading(false) // default
            .delayBeforeLoading(1000).cacheInMemory(false) // default
            .cacheOnDisk(false) // default
            .considerExifParams(false) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .handler(new Handler()) // default
            .build();*/

    /**
     * 获得应用版本
     *
     * @return
     */
    public String tripLittle(String temp) {
        return temp.replaceAll("\\.", "");
    }

    public String getVersion() {

        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
    private PendingIntent restartIntent;

    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }

    private void cauchException() {
        System.out
                .println("-----------------------------------------------------");
        // 程序崩溃时触发线程
        uncaughtExceptionHandler = new MyUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    private class MyUncaughtExceptionHandler implements
            UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // 保存错误日志
            saveCatchInfo2File(ex);
            // 如果报错就不进行重启
//			android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(0);
        }

    }

    ;


    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称
     */
    private String saveCatchInfo2File(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String sb = writer.toString();
        P.c("日志" + sb);
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String time = formatter.format(new Date());
            String fileName = time + ".txt";
            System.out.println("fileName:" + fileName);
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String filePath = Common.BASE_DIR + Common.DIR
                        + packgeName + "/crash/";
                File dir = new File(filePath);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        // 创建目录失败: 一般是因为SD卡被拔出了
                        return "";
                    }
                }
                P.c("filePath + fileName:" + filePath + fileName);
                FileOutputStream fos = new FileOutputStream(filePath + fileName);
                fos.write(sb.getBytes());
                fos.close();
                // 文件保存完了之后,在应用下次启动的时候去检查错误日志,发现新的错误日志,就发送给开发者
            }
            return fileName;
        } catch (Exception e) {
            P.c("an error occured while writing file..." + e.getMessage());
        }
        return null;
    }

    /**
     * 百度服務
     */
    /*public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000*6*5;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
		P.c("~~~~~~~~~~");
	}*/

	/*public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			P.c("---->&&");
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			P.c("BaiduLocationApiDem---" + sb.toString());
			//
			// load(location);
			try {
				val(location);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
	private final String SEND_ = "http://api.map.baidu.com/geodata/v3/poi/create";
	private final String VAL_ = "http://api.map.baidu.com/geodata/v3/poi/list";
	private final String UPDATA_ = "http://api.map.baidu.com/geodata/v3/poi/update";
	private final String AK = "jQsqcYNYn1vFm6Dcs954fis3gFDoILZd";
	private final String geotable_id = "161478";
	private final String sn = "EDWU1RuimaUfRuGfZdt1xNQTrQLmvioQ";
	private final String TAG="基本版";
	private void load(BDLocation location) {
		requestCall = OkHttpUtils
				.post()
				.url(SEND_)
				.addParams("latitude", String.valueOf(location.getLatitude()))
				.addParams("longitude", String.valueOf(location.getLongitude()))
				.addParams("coord_type", "2")
				.addParams("geotable_id", geotable_id).addParams("ak", AK)
				.addParams("address", location.getAddrStr()!=null?location.getAddrStr():"定位异常")
				// .addParams("sn", sn)
				.addParams("tags", TAG)
				.addParams("version", BaseApplication.application.getVersion())
				.addParams("mac",FileUtils.getDeviceId() ).build();
		requestCall.execute(loginCallBack);
	}

	private synchronized void val(final BDLocation location) {
		P.c("时间+"+System.currentTimeMillis());

		valCall = OkHttpUtils.get().url(VAL_).addParams("ak", AK)
				.addParams("geotable_id", geotable_id)
				.addParams("mac", FileUtils.getDeviceId()).build();
		valCall.execute(new StringCallback() {

			@Override
			public void onResponse(String response, int id) {
				// TODO Auto-generated method stub
				P.c(response);
				try {
					JSONObject json = new JSONObject(response);
					if (json.getInt("status") == 0) {
						if (json.getInt("total") == 0) {
							// 增加
							if(location!=null){
								 load(location);
							 }
						} else  {
							// 修改
							JSONArray ob=  json.getJSONArray("pois");
							if(ob!=null){
								 for(int i=0;i<ob.length();i++){
									 JSONObject o = ob.getJSONObject(i);
									 P.c(ob.length()+"机器:"+o.getString("mac")+"----"+FileUtils.getDeviceId());
									 if( o.getString("mac").equals(FileUtils.getDeviceId())){
										 //表示有数据
										 P.c("修改---->");
										 P.c("数据"+o.getString("id"));
										 if(location!=null&&o!=null){
											 P.c("禁止执行");
											 updata(location,o.getString("id"));
										 }

										 return;
									 }
								 }
								 P.c("增加---->");
								 if(location!=null){
									 load(location);
								 }
							}


						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(valCall!=null){
					valCall.cancel();
					valCall = null;
				}
			}

			@Override
			public void onError(Call call, Exception e, int id) {
				// TODO Auto-generated method stub

			}
		});
	}

	*//**
     * 更新数据
     *//*
	private void updata(BDLocation location,String id) {
		P.c(String.valueOf(location.getLatitude())+"---->"+String.valueOf(location.getLongitude())+"-->"+location.getAddrStr());
		updataCall = OkHttpUtils
				.post()
				.url(UPDATA_)
				//.addParams("mac", FileUtils.getDeviceId())
				.addParams("id", id)
				.addParams("latitude", String.valueOf(location.getLatitude()))
				.addParams("longitude", String.valueOf(location.getLongitude()))
				.addParams("geotable_id", geotable_id)
				.addParams("address", location.getAddrStr()!=null?location.getAddrStr():"定位异常")
				//
				.addParams("ak", AK)
				.addParams("tags", TAG)
				.addParams("version", BaseApplication.application.getVersion())
				.addParams("coord_type","2").build();
				 if(upCallback!=null){
					 updataCall.execute(upCallback);
				 }


	}

	private StringCallback upCallback = new StringCallback() {
		@Override
		public void onError(Call call, Exception e, int id) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onResponse(String response, int id) {
			// TODO Auto-generated method stub
			P.c("修改" + response);
			if(updataCall!=null){
				updataCall.cancel();
				updataCall = null;
			}

		}

	};
	private StringCallback loginCallBack = new StringCallback() {
		@Override
		public void onError(Call call, Exception e, int id) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onResponse(String response, int id) {
			// TODO Auto-generated method stub
			P.c("增加" + response);
			if(requestCall!=null){
				requestCall.cancel();
				requestCall = null;
			}
		}

	};

	private RequestCall requestCall, valCall, updataCall;*/
}
