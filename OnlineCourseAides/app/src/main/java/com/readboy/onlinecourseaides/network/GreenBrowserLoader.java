package com.readboy.onlinecourseaides.network;

import static com.readboy.onlinecourseaides.network.NetWorkManager.URL_NAME_GREEN;
import static com.readboy.onlinecourseaides.network.NetWorkManager.URL_NAME_ONLINE_COURSE;
import static com.readboy.onlinecourseaides.network.NetWorkManager.URL_NAME_PREFIX;

import com.readboy.onlinecourseaides.base.ObjectLoader;
import com.readboy.onlinecourseaides.bean.response.DeafultResponse;
import com.readboy.onlinecourseaides.bean.response.DefaultAppResponse;
import com.readboy.onlinecourseaides.bean.response.OnlineClassResponse;
import com.readboy.onlinecourseaides.bean.response.UserWhiteSiteResponse;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * @Author jll
 * @Date 2022/11/26
 *
 *  加载绿色上网学科导航内容，主要是网课云信息
 */
public class GreenBrowserLoader extends ObjectLoader {
    private static final String TAG = "GreenBrowserLoader";

    private GreenService mGreenService;

    public GreenBrowserLoader(){
        mGreenService = NetWorkManager.getInstance().create(GreenService.class);
    }

    public Observable<OnlineClassResponse> getOnlineCourseUrlList(String sn){
        return observe(mGreenService.getOnlineCourseUrlList());
    }

    public Observable<UserWhiteSiteResponse> getUserWhiteList(String sn, String uId){
        return observe(mGreenService.getUserWhiteList(sn, uId));
    }

    public Observable<DefaultAppResponse> getDefaultAppList(String sn){
        return observe(mGreenService.getDefaultAppList());
    }

    public Observable<DeafultResponse> addUserWithe(String sn, String uId, String name, String url, String source){
        return observe(mGreenService.addUserWhite(sn,uId,name,url,source));
    }

    public Observable<DeafultResponse> updateUserWithe(String sn, String uId, String name, String url, String collect){
        return observe(mGreenService.updateUserWhite(sn,uId,name,url,collect));
    }

    public Observable<DeafultResponse> delUserWithe(String sn, String id, String uId){
        return observe(mGreenService.delUserWhite(sn,id,uId));
    }

    public interface GreenService{

        @Headers({URL_NAME_PREFIX+URL_NAME_ONLINE_COURSE})
        //获取对应省份网课云
        @GET("/api/web_school")
        Observable<OnlineClassResponse> getOnlineCourseUrlList();

        @Headers({URL_NAME_PREFIX+URL_NAME_ONLINE_COURSE})
        //获取默认的网课应用
        @GET("/api/web_class_app")
        Observable<DefaultAppResponse> getDefaultAppList();

        @Headers({URL_NAME_PREFIX+URL_NAME_GREEN})
        @GET("/api/green_browser/user_whitelist")
        Observable<UserWhiteSiteResponse> getUserWhiteList(@Query("sn") String sn, @Query("uid") String uId);

        /**
         *
         * @param sn
         * @param uId
         * @param name  白名单标题
         * @param url   白名单网址
         * @param source  白名单来源  绿色上网0  网课助手1
         * @return
         */
        @Headers({URL_NAME_PREFIX+URL_NAME_GREEN})
        @POST("/api/green_browser/add_whitelist")
        Observable<DeafultResponse> addUserWhite(
                @Query("sn") String sn,
                @Query("uid") String uId,
                @Query("name") String name,
                @Query("url") String url,
                @Query("source") String source
        );

        /**
         *
         * @param sn
         * @param uId
         * @param name  白名单标题
         * @param url   白名单网址
         * @param source  白名单来源  绿色上网0  网课助手1
         * @return
         */
        @Headers({URL_NAME_PREFIX+URL_NAME_GREEN})
        @POST("/api/green_browser/edit_whitelist")
        Observable<DeafultResponse> updateUserWhite(
                @Query("sn") String sn,
                @Query("uid") String uId,
                @Query("name") String name,
                @Query("url") String url,
                @Query("collect") String collect
        );

        /**
         *
         * @param sn
         * @param id  白名单ID
         * @param uId
         * @return
         */
        @Headers({URL_NAME_PREFIX+URL_NAME_GREEN})
        @POST("/api/green_browser/del_whitelist")
        Observable<DeafultResponse> delUserWhite(
                @Query("sn") String sn,
                @Query("id") String id,
                @Query("uid") String uId
        );
    }
}
