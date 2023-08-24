package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Edition;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Page;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Region;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Section;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书本集合
 * (教材：tiku.book、
 * 教辅：tiku.tutor、
 * 自适应、智能评测：evaluation.book、
 * 电子书包：eschoolbag.book)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class BookBean {

    @SerializedName("id")
    private String id; // 书本编号(id)[Integer]  (不沿用猿题库书本id)
    @SerializedName("oid")
    private String oid; // 原始编号(oid)[Integer] (原始数据id，如猿题库id)
    @SerializedName("type")
    private Long type; // 类型(type)[Integer]
    @SerializedName("category")
    private Long category; // 分类(category)[Integer]
    @SerializedName("module")
    private Long module; // 模块(module)[Integer]
    @SerializedName("series")
    private Long series; // 系列(series)[Integer]
    @SerializedName("grade")
    private Long grade; // 年级(grade)[Integer]
    @SerializedName("subject")
    private Long subject; // 科目(subject)[Integer]
    @SerializedName("semester")
    private Long semester; // 学期(semester)[Integer]
    @SerializedName("courseId")
    private String courseId; // 课程(courseId)[Integer]
    @SerializedName("edition")
    private Edition edition; // 版本信息(edition)[Object]
    @SerializedName("pressId")
    private String pressId; // 出版社id(pressId)[String]
    @SerializedName("pressName")
    private String pressName; // 出版社(pressName)[String]
    @SerializedName("barCode")
    private String barCode;  // 条形码(barCode)[String]
    @SerializedName("editionTimes")
    private String editionTimes;  // 出版版次(editionTimes)[String]
    @SerializedName("printingTimes")
    private String printingTimes;  // 印刷版次(printingTimes)[String]
    @SerializedName("extCode")
    private String extCode;  // 附加编码(extCode)[String]
    @SerializedName("cover")
    private String cover;  // 封面(cover)[String] 图片名
    @SerializedName("coverThumb")
    private String coverThumb;  // 封面缩略图(coverThumb)[String] 图片名 (废弃)
    @SerializedName("speech")
    private String speech;  // 声音(speech)[String] 声音文件名
    @SerializedName("children")
    private List<Section> children;  // 目录(children)[ObjectArray]
    @SerializedName("relation")
    private List<Relation> relation;  // 关联的书本(relation)[ObjectArray]
    @SerializedName("region")
    private List<Region> region;  // 地区(region)[ObjectArray]
    @SerializedName("page")
    private Page page;  // 点读页面(page)[Object]
    @SerializedName("pageRes")
    private PageRes pageRes;  // 学习眼页面资源(pageRes)[Object]
    @SerializedName("pageRobot")
    private PageRobot pageRobot;  // 机器人页面资源(pageRobot)[Object]
    @SerializedName("feature")
    private Feature feature;  // 学习眼识别特征库(feature)[Object]
    @SerializedName("featureAR")
    private FeatureAR featureAR;  // AR识别特征库(featureAR)[Object]
    @SerializedName("featureRobot")
    private FeatureRobot featureRobot;  // 机器人识别特征库(featureRobot)[Object]
    @SerializedName("packageUri")
    private FeatureRobot packageUri;  // 离线数据包地址(packageUri)[String] (小学《教材全解》或中学《曲一线教辅》离线数据用到)
    @SerializedName("packageSize")
    private Long packageSize;  // 离线数据包大小(packageSize)[Integer] (小学《教材全解》或中学《曲一线教辅》离线数据用到)
    @SerializedName("packTime")
    private Long packTime;  // 离线打包时间(packTime)[Timestamp] (小学《教材全解》或中学《曲一线教辅》离线数据用到)
    @SerializedName("count")
    private Long count;  // 题目数(count)[Integer] (弃用)
    @SerializedName("status")
    private Long status;  // 书本状态(status)[Integer]
    @SerializedName("copyright")
    private Long copyright;  // 是否有版权(copyright)[Integer]
    @SerializedName("createTime")
    private Long createTime;  // 创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private Long updateTime;  // 更新时间(updateTime)[Timestamp]
    @SerializedName("version")
    private Long version;  // 数据版本号(version)[Integer]

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Relation {
        @SerializedName("id")
        private String id; // 书本编号(id)[Integer]
        @SerializedName("type")
        private Long type; // 书本类型(type)[Integer]
        @SerializedName("category")
        private Long category; // 书本分类(category)[Integer]
        @SerializedName("module")
        private Long module; // 书本模块(module)[[Integer]

        @Override
        public String toString() {
            return "Relation{" +
                    "id='" + id + '\'' +
                    ", type=" + type +
                    ", category=" + category +
                    ", module=" + module +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public Long getCategory() {
            return category;
        }

        public void setCategory(Long category) {
            this.category = category;
        }

        public Long getModule() {
            return module;
        }

        public void setModule(Long module) {
            this.module = module;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class PageRes {
        @SerializedName("status")
        private Long status; // 状态(status)[Integer]
        @SerializedName("expectTime")
        private Long expectTime; // 预计完成时间(expectTime)[Timestamp]
        @SerializedName("createTime")
        private Long createTime; // 创建时间(createTime)[Timestamp]
        @SerializedName("updateTime")
        private Long updateTime; // 更新时间(updateTime)[Timestamp]
        @SerializedName("qstFlag")
        private Long qstFlag; // 题干显示标记(qstFlag)[Integer]

        @Override
        public String toString() {
            return "PageRes{" +
                    "status=" + status +
                    ", expectTime=" + expectTime +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    ", qstFlag=" + qstFlag +
                    '}';
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Long getExpectTime() {
            return expectTime;
        }

        public void setExpectTime(Long expectTime) {
            this.expectTime = expectTime;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public Long getQstFlag() {
            return qstFlag;
        }

        public void setQstFlag(Long qstFlag) {
            this.qstFlag = qstFlag;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class PageRobot {
        @SerializedName("status")
        private Long status; // 状态(status)[Integer]
        @SerializedName("packageUri")
        private String packageUri; // 离线数据包地址(packageUri)[String]
        @SerializedName("packageSize")
        private Long packageSize; // 离线数据包大小(packageSize)[Integer] 单位：字节
        @SerializedName("packTime")
        private Long packTime; // 离线打包时间(packTime)[Timestamp]

        @Override
        public String toString() {
            return "PageRobot{" +
                    "status=" + status +
                    ", packageUri='" + packageUri + '\'' +
                    ", packageSize=" + packageSize +
                    ", packTime=" + packTime +
                    '}';
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public String getPackageUri() {
            return packageUri;
        }

        public void setPackageUri(String packageUri) {
            this.packageUri = packageUri;
        }

        public Long getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(Long packageSize) {
            this.packageSize = packageSize;
        }

        public Long getPackTime() {
            return packTime;
        }

        public void setPackTime(Long packTime) {
            this.packTime = packTime;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Feature {
        @SerializedName("id")
        private String id; // 编号(id)[Integer]
        @SerializedName("status")
        private Long status; // 状态(status)[Integer]
        @SerializedName("orientation")
        private Long orientation; // 方向(orientation)[Integer]
        @SerializedName("offline")
        private Offline offline; // 离线(offline)[Object]
        @SerializedName("online")
        private Offline online; // 在线内页(online)[Object]
        @SerializedName("onlineFm")
        private OnlineFm onlineFm; // 在线封面(onlineFm)[Object]

        @Override
        public String toString() {
            return "Feature{" +
                    "id='" + id + '\'' +
                    ", status=" + status +
                    ", orientation=" + orientation +
                    ", offline=" + offline +
                    ", online=" + online +
                    ", onlineFm=" + onlineFm +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Long getOrientation() {
            return orientation;
        }

        public void setOrientation(Long orientation) {
            this.orientation = orientation;
        }

        public Offline getOffline() {
            return offline;
        }

        public void setOffline(Offline offline) {
            this.offline = offline;
        }

        public Offline getOnline() {
            return online;
        }

        public void setOnline(Offline online) {
            this.online = online;
        }

        public OnlineFm getOnlineFm() {
            return onlineFm;
        }

        public void setOnlineFm(OnlineFm onlineFm) {
            this.onlineFm = onlineFm;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class FeatureAR {
        @SerializedName("id")
        private String id; // 编号(id)[Integer]
        @SerializedName("status")
        private Long status; // 状态(status)[Integer]
        @SerializedName("orientation")
        private Long orientation; // 方向(orientation)[Integer]
        @SerializedName("onlineFm")
        private OnlineFm onlineFm; // AR在线封面(onlineFm)[Object]
        @SerializedName("online")
        private Online online; // AR在线内页(online)[Object]

        @Override
        public String toString() {
            return "FeatureAR{" +
                    "id='" + id + '\'' +
                    ", status=" + status +
                    ", orientation=" + orientation +
                    ", onlineFm=" + onlineFm +
                    ", online=" + online +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Long getOrientation() {
            return orientation;
        }

        public void setOrientation(Long orientation) {
            this.orientation = orientation;
        }

        public OnlineFm getOnlineFm() {
            return onlineFm;
        }

        public void setOnlineFm(OnlineFm onlineFm) {
            this.onlineFm = onlineFm;
        }

        public Online getOnline() {
            return online;
        }

        public void setOnline(Online online) {
            this.online = online;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class FeatureRobot {
        @SerializedName("id")
        private Long id; // 编号(id)[Integer]
        @SerializedName("status")
        private Long status; // 状态(status)[Integer]
        @SerializedName("orientation")
        private Long orientation; // 方向(orientation)[Integer]
        @SerializedName("offline")
        private Offline offline; // 离线识别(offline)[Object]
        @SerializedName("ai")
        private AI ai; // AI识别(ai)[Object]

        @Override
        public String toString() {
            return "FeatureRobot{" +
                    "id=" + id +
                    ", status=" + status +
                    ", orientation=" + orientation +
                    ", offline=" + offline +
                    ", ai=" + ai +
                    '}';
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Long getOrientation() {
            return orientation;
        }

        public void setOrientation(Long orientation) {
            this.orientation = orientation;
        }

        public Offline getOffline() {
            return offline;
        }

        public void setOffline(Offline offline) {
            this.offline = offline;
        }

        public AI getAi() {
            return ai;
        }

        public void setAi(AI ai) {
            this.ai = ai;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Offline {
        @SerializedName("dbPath")
        private String dbPath; // db路径(dbPath)[String]
        @SerializedName("dbSize")
        private Long dbSize; // db大小(dbSize)[Integer] 单位：字节
        @SerializedName("indexPath")
        private String indexPath; // index路径(indexPath)[String]
        @SerializedName("indexSize")
        private Long indexSize; // index大小(indexSize)[Integer] 单位：字节
        @SerializedName("createTime")
        private Long createTime; // 创建时间(createTime)[Integer]
        @SerializedName("updateTime")
        private Long updateTime; // 更新时间(updateTime)[Integer]

        @Override
        public String toString() {
            return "Offline{" +
                    "dbPath='" + dbPath + '\'' +
                    ", dbSize=" + dbSize +
                    ", indexPath='" + indexPath + '\'' +
                    ", indexSize=" + indexSize +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public String getDbPath() {
            return dbPath;
        }

        public void setDbPath(String dbPath) {
            this.dbPath = dbPath;
        }

        public Long getDbSize() {
            return dbSize;
        }

        public void setDbSize(Long dbSize) {
            this.dbSize = dbSize;
        }

        public String getIndexPath() {
            return indexPath;
        }

        public void setIndexPath(String indexPath) {
            this.indexPath = indexPath;
        }

        public Long getIndexSize() {
            return indexSize;
        }

        public void setIndexSize(Long indexSize) {
            this.indexSize = indexSize;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Online {
        @SerializedName("dbPath")
        private String dbPath; // db路径(dbPath)[String]
        @SerializedName("dbSize")
        private Long dbSize; // db大小(dbSize)[Integer] 单位：字节
        @SerializedName("createTime")
        private Long createTime; // 创建时间(createTime)[Integer]
        @SerializedName("updateTime")
        private Long updateTime; // 更新时间(updateTime)[Integer]

        @Override
        public String toString() {
            return "Online{" +
                    "dbPath='" + dbPath + '\'' +
                    ", dbSize=" + dbSize +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public String getDbPath() {
            return dbPath;
        }

        public void setDbPath(String dbPath) {
            this.dbPath = dbPath;
        }

        public Long getDbSize() {
            return dbSize;
        }

        public void setDbSize(Long dbSize) {
            this.dbSize = dbSize;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class OnlineFm {
        @SerializedName("dbPath")
        private String dbPath; // db路径(dbPath)[String]
        @SerializedName("dbSize")
        private Long dbSize; // db大小(dbSize)[Integer] 单位：字节
        @SerializedName("createTime")
        private Long createTime; // 创建时间(createTime)[Integer]
        @SerializedName("updateTime")
        private Long updateTime; // 更新时间(updateTime)[Integer]

        @Override
        public String toString() {
            return "OnlineFm{" +
                    "dbPath='" + dbPath + '\'' +
                    ", dbSize=" + dbSize +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public String getDbPath() {
            return dbPath;
        }

        public void setDbPath(String dbPath) {
            this.dbPath = dbPath;
        }

        public Long getDbSize() {
            return dbSize;
        }

        public void setDbSize(Long dbSize) {
            this.dbSize = dbSize;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class AI {
        @SerializedName("dbPath")
        private String dbPath; // db路径(dbPath)[String]
        @SerializedName("dbSize")
        private Long dbSize; // db大小(dbSize)[Integer] 单位：字节
        @SerializedName("indexPath")
        private String indexPath; // index路径(indexPath)[String]
        @SerializedName("indexSize")
        private Long indexSize; // index大小(indexSize)[Integer] 单位：字节
        @SerializedName("createTime")
        private Long createTime; // 创建时间(createTime)[Integer]
        @SerializedName("updateTime")
        private Long updateTime; // 更新时间(updateTime)[Integer]

        @Override
        public String toString() {
            return "AI{" +
                    "dbPath='" + dbPath + '\'' +
                    ", dbSize=" + dbSize +
                    ", indexPath='" + indexPath + '\'' +
                    ", indexSize=" + indexSize +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public String getDbPath() {
            return dbPath;
        }

        public void setDbPath(String dbPath) {
            this.dbPath = dbPath;
        }

        public Long getDbSize() {
            return dbSize;
        }

        public void setDbSize(Long dbSize) {
            this.dbSize = dbSize;
        }

        public String getIndexPath() {
            return indexPath;
        }

        public void setIndexPath(String indexPath) {
            this.indexPath = indexPath;
        }

        public Long getIndexSize() {
            return indexSize;
        }

        public void setIndexSize(Long indexSize) {
            this.indexSize = indexSize;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
