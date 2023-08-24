package com.readboy.onlinecourseaides.network;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求采用
 * Retrofit + RxJava ＋ OkHttp
 *
 * [me.jessyan:retrofit-url-manager:1.4.0] =>> RetrofitUrlManager
 * => 实现 Retrofit 动态切换URL
 */
public class NetWorkManager {
    private static final String TAG = "NetWorkManager";

    //https://ai-course.readboy.com/api/v1/book/list?sn=11536545645a05e0af685cb9386f03d55c00acf72b9&book_id=545154
    //https://ai-course.readboy.com/api/v1/book/list
    //https://admin-parent.readboy.com//api/web_school
    public static final String BASE_URL = "https://ai-course.readboy.com/";
    // 需要共用Manager 所以区分URL
    public static final String URL_NAME_PREFIX = "Domain-Name:";
    public static final String URL_NAME_AI_COURSE = "AI_COURSE";
    public static final String URL_NAME_GREEN = "GREEN";
    public static final String URL_NAME_ONLINE_COURSE = "ONLINE_COURSE";
    public static final String BOOK_BASE_URL = "https://ai-course.readboy.com/";
    public static final String GREEN_BROWSER_BASE_URL = "https://api-super.readboy.com/";
    public static final String ONLINE_COURSE_BROWSER_BASE_URL = "https://admin-parent.readboy.com/";
    public static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;

//    Retrofit retrofit;

    private static NetWorkManager manager;

    public static NetWorkManager getManager() {
        if (manager == null) {
            synchronized (NetWorkManager.class) {
                if (manager == null)
                    manager = new NetWorkManager();
            }
        }
        return manager;
    }

    private static class NetWorkManagerHolder {
        private static final NetWorkManager INSTANCE = new NetWorkManager();
    }

    public static final NetWorkManager getInstance() {
        return NetWorkManagerHolder.INSTANCE;
    }


    /**
     *  封装  retrofit
     *  初始化 Okhttp 、retrofit,  添加Okhttp拦截器
     * 说明：配置了接口的baseUrl和一个converter,GsonConverterFactory
     * 是默认提供的Gson 转换器，Retrofit 也支持其他的一些转换器，详情请看官网Retrofit官网
     */
    public NetWorkManager() {

        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS);//读操作超时时间

        this.mOkHttpClient = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()) //RetrofitUrlManager 初始化
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        /*
           创建Retrofit  特别注意:【这里的BASE_URL 必须是服务器URL 格式:https://ai-course.readboy.com/】
           此类情况默认回去掉后缀: https://ai-course.readboy.com/api/v1/  会导致请求失败URL拼接异常

           使用
           @GET("/api/v1/common/edition")
           Observable<BookBean> getVersionList();
         */
        this.mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create())//使用Gson
                .client(mOkHttpClient)
                .baseUrl(BASE_URL)
                .build();

        // 添加公共参数拦截器
//        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
//                .addHeaderParam("userName","")//添加公共参数
//                .addHeaderParam("device","")
//                .build();
//        builder.addInterceptor(basicParamsInterceptor);

        // 添加URL  根据  @header(name,url) 切换url 由RetrofitUrlManager 提供
        this.putUrl(URL_NAME_AI_COURSE, BOOK_BASE_URL);
        this.putUrl(URL_NAME_GREEN, GREEN_BROWSER_BASE_URL);
        this.putUrl(URL_NAME_ONLINE_COURSE, ONLINE_COURSE_BROWSER_BASE_URL);
    }

    /**
     * 封装
     * 获取对应的Service实例 对应【MyApi movieService = retrofit.create(MyApi.class);】
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

    public OkHttpClient getMOkHttpClient() {
        return mOkHttpClient;
    }

    public Retrofit getMRetrofit() {
        return mRetrofit;
    }

    public void putUrl(String name,String url) {
        RetrofitUrlManager.getInstance().putDomain(name, url);
    }
}
