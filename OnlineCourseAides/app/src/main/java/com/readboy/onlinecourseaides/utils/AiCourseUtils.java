package com.readboy.onlinecourseaides.utils;

import android.graphics.Point;

import com.readboy.onlinecourseaides.utils.aicourse.bean.AppKeyPoint;
import com.readboy.onlinecourseaides.utils.aicourse.bean.AppMapping;
import com.readboy.onlinecourseaides.utils.aicourse.bean.AppTestPoint;
import com.readboy.onlinecourseaides.utils.aicourse.bean.BaseUtil;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Link;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Mapping;
import com.readboy.wanimation.tagcloud.exp.PlanetAtlas.DataInfo.PlanetAtlasInfoData;
import com.readboy.wanimation.tagcloud.exp.PlanetAtlas.DataInfo.StarInfo;
import com.readboy.wanimation.tagcloud.exp.PlanetAtlas.DataInfo.TestSiteInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/22
 */
public class AiCourseUtils {

    private static boolean mSectionStudyStatus;

    //章节的知识点考点统计（图谱页显示）
    private static int keyPointSize = 0;
    private static int testPointSize = 0;

    /**
     * 转换图谱数据
     * @param mapping 图谱节点
     * @param keypoints   知识点信息
     * @param testPoints  考点信息
     * @param isGraph true：非精准学图谱
     * @return
     */
    public static List<StarInfo> getStarInfos(AppMapping mapping,
                                    List<AppKeyPoint> keypoints,
                                    List<AppTestPoint> testPoints,
                                    boolean isGraph) {
        List<StarInfo> mStarInfos = new ArrayList<>();
        mSectionStudyStatus = false;
        //获取知识点、考点对应的节点信息，
        ArrayList<Mapping.Node> keyPointNodes = new ArrayList<>();
        HashMap<String, Mapping.Node> testPointNodes = new HashMap<>();
        mapping.getSection_id();
        for (Mapping.Node node : mapping.getNodes()) {
            if (node.getType() == 1) {  //知识点
                keyPointNodes.add(node);
            } else {                    //考点
                testPointNodes.put(node.getId(), node);
            }
        }
        //刷新考点和知识点数量
        keyPointSize = keyPointNodes.size();
        testPointSize = testPointNodes.size();

        //获取知识点、考点的学习状态信息
        HashMap<String, AppKeyPoint> keyPointHashMap = new HashMap<>();
        HashMap<String, AppTestPoint> testpointHashMap = new HashMap<>();
        //把考点node归类到知识点node中
        HashMap<String, ArrayList<Mapping.Node>> allKeyPointHashMap = new HashMap<>();
        for (AppKeyPoint appKeyPoint : keypoints) {
            keyPointHashMap.put(appKeyPoint.getKeypointId(), appKeyPoint);
        }
        for (AppTestPoint testPoint : testPoints) {
            testpointHashMap.put(testPoint.getTestpointId(), testPoint);
            String keypoint_id = testPoint.getKeypointId();
            String testpoint_id = testPoint.getTestpointId();
            if (allKeyPointHashMap.containsKey(keypoint_id)) { //如果知识点map里已经有了这个知识点，则把考点加入这个知识点中
                if (testPointNodes.containsKey(testpoint_id)) {
                    allKeyPointHashMap.get(keypoint_id).add(testPointNodes.get(testpoint_id));
                }
            } else {    //否则添加信息的知识点信息，并把考点也加进去
                ArrayList<Mapping.Node> arrayList = new ArrayList<>();
                if (testPointNodes.containsKey(testpoint_id)) {
                    arrayList.add(testPointNodes.get(testpoint_id));
                }
                allKeyPointHashMap.put(keypoint_id, arrayList);
            }
        }

        //获取连线信息
        HashMap<String, List<Long>> sourceidMap = new HashMap<>();
        for (Link link : mapping.getLinks()) {
            if (sourceidMap.containsKey(link.getTargetid())) {
                sourceidMap.get(link.getTargetid()).add(BaseUtil.strToLong(link.getSourceid()));
            } else {
                List<Long> list = new ArrayList<>();
                list.add(BaseUtil.strToLong(link.getSourceid()));
                sourceidMap.put(link.getTargetid(), list);
            }
        }

        //转换为StarInfo
        for (int i = 0; i < keyPointNodes.size(); i++) {
            Mapping.Node keyPointNode = keyPointNodes.get(i); //知识点node
            //填充考点信息
            ArrayList<TestSiteInfo> testSiteInfos = new ArrayList<>();
            if (allKeyPointHashMap.containsKey(keyPointNode.getId())) {
                ArrayList<Mapping.Node> arrayList = allKeyPointHashMap.get(keyPointNode.getId());
                for (int j = 0; j < arrayList.size(); j++) {
                    Mapping.Node testPointNode = arrayList.get(j);//考点node
                    if (testPointNode != null) {
                        AppTestPoint appTestPoint = testpointHashMap.get(testPointNode.getId());//对应的考点信息
                        if (appTestPoint != null) {
                            long study_status = appTestPoint.getStudyStatus();//学习状态0-未学 1-学过
                            long score = appTestPoint.getScore();
                            float degree = -1;
                            if (study_status == 1) {
                                mSectionStudyStatus = true;
                            }
                            if (isGraph) {
                                degree = score > 0 ? score / 100f : 0;
                            } else {
                                if (study_status == 1) {
                                    degree = score > 0 ? score / 100f : 0;
                                }
                            }

                            // TODO : x y = double type
                            int tpNodeX = BaseUtil.doubleToInteger(testPointNode.getX());
                            int tpNodeY = BaseUtil.doubleToInteger(testPointNode.getY());
                            int kpNodeX = BaseUtil.doubleToInteger(keyPointNode.getX());
                            int kpNodeY = BaseUtil.doubleToInteger(keyPointNode.getY());
                            Point point = new Point(tpNodeX - kpNodeX, tpNodeY - kpNodeY);
                            long[] array = null;
                            if (sourceidMap.containsKey(testPointNode.getId())) {
                                array = new long[sourceidMap.get(testPointNode.getId()).size()];
                                for (int n = 0; n < sourceidMap.get(testPointNode.getId()).size(); n++) {
                                    array[n] = sourceidMap.get(testPointNode.getId()).get(n);
                                }
                            } else {
                                array = new long[]{(Long.valueOf(testPointNode.getId()))};
                            }
                            TestSiteInfo testSiteInfo = new TestSiteInfo(Long.valueOf(testPointNode.getId()), appTestPoint.getTestpointName(), PlanetAtlasInfoData.getStationImg(), degree, "中", 1, point, array);
                            testSiteInfos.add(testSiteInfo);
                        }
                    }
                }
            }
            //填充知识点的信息
            AppKeyPoint keyPoint = keyPointHashMap.get(keyPointNode.getId());
            if (keyPoint != null) {
                long study_status = keyPoint.getStudyStatus();//学习状态0-未学 1-学过
                long score = keyPoint.getScore();
                float degree = -1;
                if (study_status == 1) {
                    mSectionStudyStatus = true;
                }

                if (isGraph) {
                    degree = score > 0 ? score / 100f : 0;

                    if (testSiteInfos != null && testSiteInfos.size() > 0) {
                        float totalDegree = 0;
                        for (int n = 0; n < testSiteInfos.size(); n++) {
                            totalDegree += testSiteInfos.get(n).degree;
                        }
                        degree = totalDegree / testSiteInfos.size();
                    }
                } else {
                    if (study_status == 1) {
                        degree = score > 0 ? score / 100f : 0;
                    }
                }

                // TODO : x y = double type
                int kpNodeX = BaseUtil.doubleToInteger(keyPointNode.getX());
                int kpNodeY = BaseUtil.doubleToInteger(keyPointNode.getY());
                Point point = new Point(kpNodeX, kpNodeY);
                long[] array = null;
                if (sourceidMap.containsKey(keyPointNode.getId())) {
                    array = new long[sourceidMap.get(keyPointNode.getId()).size()];
                    for (int n = 0; n < sourceidMap.get(keyPointNode.getId()).size(); n++) {
                        array[n] = sourceidMap.get(keyPointNode.getId()).get(n);
                    }

                } else {
                    array = new long[]{(Long.valueOf(keyPointNode.getId()))};
                }

                StarInfo starInfo = new StarInfo(Long.valueOf(keyPointNode.getId()), keyPoint.getKeypointName(), PlanetAtlasInfoData.getStarImg(), degree, point, array, testSiteInfos);
                mStarInfos.add(starInfo);
            }
        }
        return mStarInfos;
    }
}
