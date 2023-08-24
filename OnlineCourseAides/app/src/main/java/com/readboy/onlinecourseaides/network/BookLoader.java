package com.readboy.onlinecourseaides.network;

import static com.readboy.onlinecourseaides.network.NetWorkManager.URL_NAME_AI_COURSE;
import static com.readboy.onlinecourseaides.network.NetWorkManager.URL_NAME_PREFIX;

import com.readboy.onlinecourseaides.base.ObjectLoader;
import com.readboy.onlinecourseaides.bean.response.BookOneResponse;
import com.readboy.onlinecourseaides.bean.response.BookResponse;
import com.readboy.onlinecourseaides.bean.response.GradeResponse;
import com.readboy.onlinecourseaides.bean.response.SectionOneResponse;
import com.readboy.onlinecourseaides.bean.response.SubjectResponse;
import com.readboy.onlinecourseaides.bean.response.VersionResponse;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 加载网络数据类
 *
 * 管理APIService
 * => 获取Rx观察者对象发起请求，需要自己获取Observable
 * => 用Rx添加回调 getXXX.subscribe(new Subscriber<SubjectResponse>(){...})
 * RetrofitUrlManager.getInstance().putDomain(name, url) =>
 * @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE}) =>
 * => 用来配合 [me.jessyan:retrofit-url-manager:1.4.0] =>> RetrofitUrlManager
 * => 实现 Retrofit 动态切换URL
 */
public class BookLoader extends ObjectLoader {

    private BookService mBookService;

    public BookLoader(){
//        mBookService = NetWorkManager.getManager().create(BookService.class);
        mBookService = NetWorkManager.getInstance().create(BookService.class);
    }

    public Observable<BookResponse> getBookList(String sn,String grade,String subject,String semester, String editionId){
        return observe(mBookService.getBookList(sn,0, grade,subject,semester,editionId));
    }

    public Observable<VersionResponse> getVersionList(String sn, int subject, String grade){
        return observe(mBookService.getVersionList(sn, subject, grade));
    }

    public Observable<SubjectResponse> getSubjectList(String sn){
        return observe(mBookService.getSubjectList(sn));
    }

    public Observable<BookOneResponse> getSectionList(String sn, String bookId){
        return observe(mBookService.getSectionList(sn, bookId));
    }

    public Observable<GradeResponse> getGradeList(String sn, int subject){
        return observe(mBookService.getGradeList(sn, subject));
    }

    public Observable<SectionOneResponse> getSectionOne(String sn, String subject){
        return observe(mBookService.getSectionOne(sn, subject));
    }

    public interface BookService{

        //获取BookList
        @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE}) // 用于切换URL
        @GET("/api/v1/book/list")
        Observable<BookResponse> getBookList(@Query("sn") String sn,@Query("page") int page, @Query("grade") String grade, @Query("subject")String subject, @Query("semester") String semester, @Query("edition_id") String editionId);

        @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE})
        // 获取版本信息 : 人教版  根据学科加载
        @GET("/api/v1/common/edition")
        Observable<VersionResponse> getVersionList(@Query("sn") String sn,@Query("subject") int subject,@Query("grade") String grade);

        @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE})
        // 获取学科信息 : 语文
        @GET("/api/v1/common/subject")
        Observable<SubjectResponse> getSubjectList(@Query("sn") String sn);

        @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE})
        // 获取年级信息 : 一年级上
        @GET("/api/v1/common/grade")
        Observable<GradeResponse> getGradeList(@Query("sn") String sn, @Query("subject") int subject);

        @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE})
        // 获取课程详情信息 : 章节小节
        @GET("/api/v1/book/one")
        Observable<BookOneResponse> getSectionList(@Query("sn") String sn, @Query("book_id") String bookId);

        @Headers({URL_NAME_PREFIX+URL_NAME_AI_COURSE})
        // 获取章节详情信息 : 章节   对应appMapping
        @GET("/api/v1/section/one")
        Observable<SectionOneResponse> getSectionOne(@Query("sn") String sn, @Query("section_id") String sectionId);
    }
}
