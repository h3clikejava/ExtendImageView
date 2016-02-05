package h3c.extendimageview.demo;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import h3c.extendimageview.ExtendImageView;

/**
 * Created by H3c on 2/4/16.
 */
public class PhotoActivity extends AppCompatActivity {
    public static View clickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), getIntent().getIntExtra("photoRes", 0));

        // 尼玛拿个ActionBar高度都这么麻烦
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        // 尼玛拿个状态栏高度都这么麻烦
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        final ExtendImageView iv = (ExtendImageView)findViewById(R.id.iv);
        iv.setOriginalView(clickView);
        // 尼玛坑爹的安卓必须在这里设定View的位置，因为view里面会累加ActionBar和StateBar高度
        int[] location = new int[2];
        clickView.getLocationOnScreen(location);
        iv.setPosition(location[0],
                location[1] - mActionBarSize - statusBarHeight);
        iv.setImageBitmap(bmp);
        iv.setDuration(5000);
//        iv.start(new Rect(150, 250, 750, 850));// 指定位置
        iv.start(null);// 全屏
    }

    // 去掉Activity 动画
    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }
}
