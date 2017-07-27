package com.karrel.rellsignview;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.karrel.rellsignview.databinding.ActivityMainBinding;
import com.karrel.signviewlib.SignViewUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.makeBitmap.setOnClickListener(onMakeBitmapListener);
    }

    private View.OnClickListener onMakeBitmapListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap bitmap1 = mBinding.signView.getBitmap();
            Bitmap bitmap2 = SignViewUtil.loadBitmapFromView(mBinding.constraintLayout);

            Bitmap overlay = SignViewUtil.overlay(bitmap1, bitmap2);
            mBinding.imageView.setImageBitmap(overlay);
        }
    };
}
