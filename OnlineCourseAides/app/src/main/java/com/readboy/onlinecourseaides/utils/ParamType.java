package com.readboy.onlinecourseaides.utils;

/**
 * 项目常见类型参数
 * 状态 0 ~ 99
 * 类型 1000 ~  9999
 * 按钮操作 10000 ~ 99999
 * 返回码 100  200 404
 */
public enum ParamType {
    // 关闭服务和回到主页
    SERVICE_TOOLS_TO_MASTER(10001),
    SERVICE_TOOLS_CLOSE(10002),

    // 截屏
    SERVICE_TOOLS_START_Screenshot(10003),
    // 跳转练习
    SERVICE_TOOLS_START_Exercises(10004),
    // 录制状态
    SERVICE_TOOLS_START_RECORD(2),
    SERVICE_TOOLS_STOP_RECORD(3),
    SERVICE_TOOLS_NO_RECORD(4),
    SERVICE_TOOLS_YES_RECORD(5),

    // 录制类型
    SERVICE_RECORD_VIDEO(1005),
    SERVICE_RECORD_SOUND(1006),

    //截屏类型
    SERVICE_TOOLS_SCREENSHOT_NORMAL(1008),
    SERVICE_TOOLS_SCREENSHOT_SCREEN(1009),
    SERVICE_TOOLS_SCREENSHOT_SOUND(1010),

    //窗口
    SERVICE_TOOLS_WIN(1013),
    SERVICE_LOAD_WIN(1014),

    //服务窗口状态
    SERVICE_WIN_OPEN(0),
    SERVICE_WIN_CLOSE(1),

    // 动画状态
//    SERVICE_WIN_ANIM_START(2),
//    SERVICE_WIN_ANIM_STOP(3),

    // 悬浮窗状态 缩小（悬浮球）还是工具栏（小窗口）
    FLOAT_WIN_TOOLS(6),
    FLOAT_WIN_SMALL(8),

    // 处理结果
    RESULT_DO_SUCCESS(200),
    RESULT_DO_ERROR(100),

    // 首页页面类型，网课应用还是历史记录
    SELECT_APP_TITLE_TYPE(1011),
    SELECT_HISTORY_TITLE_TYPE(1012),

    // dialogType
    TYPE_CLASS_DIALOG(1009),
    TYPE_APP_DIALOG(1008),

    // 删除APP
    APP_MORE_DEL_APP(1010);

    int param;

    ParamType(int param) {
        this.param = param;
    }

    public int getParam() {
        return param;
    }
}
