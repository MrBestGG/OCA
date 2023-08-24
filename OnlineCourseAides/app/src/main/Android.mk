LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
#LOCAL_SDK_VERSION := system_current
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true
LOCAL_USE_AAPT2 := true

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res \
#    frameworks/support/design/res \
    frameworks/support/v7/appcompat/res \
    frameworks/support/cardview/res

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_AAPT_FLAGS := \
    --auto-add-overlay \
    --extra-packages android.support.constraint \
    --extra-packages android.support.design \
    --extra-packages android.support.v7.appcompat \
    --extra-packages android.support.v7.cardview \
    --extra-packages android.support.v7.recyclerview \
    --extra-packages com.chad.library \
    --extra-packages com.bumptech.glide \
    --extra-packages com.suke.widget

LOCAL_STATIC_ANDROID_LIBRARIES += \
    android-support-annotations \
    android-support-constraint-layout \
    $(ANDROID_SUPPORT_DESIGN_TARGETS) \
    android-support-transition \
    android-support-v4 \
    android-support-v7-appcompat \
    android-support-v7-cardview \
    android-support-v7-recyclerview

LOCAL_STATIC_JAVA_LIBRARIES += \
    android-support-constraint-layout-solver \
    base-recyclerview-adapter-helper \
    glide3.7.0 \
    switch-button0.0.3
	
#LOCAL_STATIC_JAVA_AAR_LIBRARIES += \
#    android-support-constraint-layout \

LOCAL_PACKAGE_NAME := ScreenRecord
LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += \
	glide3.7.0:/libs/glide-3.7.0.jar \
	base-recyclerview-adapter-helper:/libs/BaseRecyclerViewAdapterHelper-2.9.46.aar \
	switch-button0.0.3:/libs/switch-button-0.0.3.aar
#    android-support-constraint-layout:/libs/constraint-layout-1.1.0-beta2.aar \
#    android-support-constraint-layout-solver:/libs/constraint-layout-solver-1.1.0-beta2.jar

include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
