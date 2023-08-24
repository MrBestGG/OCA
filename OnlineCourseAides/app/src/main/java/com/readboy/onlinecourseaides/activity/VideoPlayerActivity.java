package com.readboy.onlinecourseaides.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.databinding.ActivityVideoPlayerBinding;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    public static final String VIDEO_PATH = "MY_VIDEO_PATH";
    public static final String VIDEO_TITLE = "MY_VIDEO_TITLE";

    private ActivityVideoPlayerBinding binding;

    private View mVideoView;
    private VideoView mVideoViewPlayer;
    private MediaController mediaController;

    private List<String> paths;
    private List<String> titles;
    private boolean isShowTitle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(LayoutInflater.from(this));

        Intent intent = getIntent();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initData(intent);
        initView();
    }

    private void startPlayer() {
        //让VideoView获取焦点
        mVideoViewPlayer.requestFocus();
        mVideoViewPlayer.start();
    }

    private void initData(Intent intent) {
        paths = intent.getStringArrayListExtra(VIDEO_PATH);
        titles = intent.getStringArrayListExtra(VIDEO_TITLE);
        mediaController = new MediaController(this);
    }

    private void initView() {
        mVideoView  = binding.getRoot();
        setContentView(mVideoView);
        mVideoViewPlayer = binding.videoPlayerView;
        mVideoViewPlayer.setMediaController(mediaController);
        binding.videoPlayerViewTitle.setVisibility(View.VISIBLE);
        binding.myVideoControl.setOnClickListener(v->{
            isShowTitle = !isShowTitle;
            if(isShowTitle) {
                mediaController.show();
                binding.videoPlayerViewTitle.setVisibility(View.VISIBLE);
            }else {
                mediaController.hide();
                binding.videoPlayerViewTitle.setVisibility(View.INVISIBLE);
            }
        });
        binding.videoPlayerReturn.setOnClickListener(v->{
            finish();
        });
        loadVideo();
    }

    private void loadVideo() {
        if(paths.size() != 0 && titles.size() != 0) {
            if(paths.get(0) != null) {
                mVideoViewPlayer.setVideoURI(Uri.parse(paths.get(0)));
            }else {
                MyApplication.getInstances().showToast("错误！找不到相关资源");
            }
            if(titles.get(0) != null) {
                binding.videoPlayerViewTitleTv.setText(titles.get(0));
            }
        }else {
            MyApplication.getInstances().showToast("错误！找不到相关资源");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlayer();
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}