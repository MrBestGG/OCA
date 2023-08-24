package com.readboy.onlinecourseaides.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.readboy.onlinecourseaides.ui.DefaultTipsDialogView;

public class DialogUtils {

    public static AlertDialog getAlertDialog(Context context,String title,String content,DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.makeSure();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", (dialog, which) -> {
            listener.cancel();
            dialog.dismiss();
        });

//        //设置中立按钮
//        builder.setNeutralButton("保密", (dialog, which) -> {
//            dialog.dismiss();
//        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                listener.show();
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.gone();
            }
        });

        return dialog;
    }

    public interface DialogListener {
        void cancel();
        void makeSure();
        void show();
        void gone();
    };

    /**
     *  获取基本的XPopUp对象
     * @param context
     * @return
     */
    public static XPopup.Builder getNoStatusBarXPopUp(Context context) {
         return new XPopup.Builder(context)
                .hasStatusBar(false);
    }

    /**
     *  获取默认的提示弹框
     *  信息提示删除弹框
     * @param context
     * @return
     */
    public static BasePopupView getDefaultTipsXPopUp(Context context, DefaultTipsDialogView.DefaultTipsDialogListener listener,String name) {
        return new XPopup.Builder(context)
                .asCustom(new DefaultTipsDialogView(context, listener, name));
    }
}
