package h3c.extendimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by H3c on 2/4/16.
 */
public class ExtendImageView extends ImageView {
    public ExtendImageView(Context context) {
        super(context);
        init();
    }

    public ExtendImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExtendImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);
    }

    /**
     * 必须在外面设定该视图位置，因为必须手动指定具体位置
     * 否则容易包含ActionBar和StatusBar高度!!!
     * 变态安卓，写个动画真麻烦
     *
     * @param x
     * @param y
     */
    float viewX;
    float viewY;
    public void setPosition(float x, float y) {
        viewX = x;
        viewY = y;
    }

    // 原始View的高宽
    int viewWidth;
    int viewHeight;
    public void setOriginalView(View view) {
        if(view == null) return;
        viewWidth = view.getWidth();
        viewHeight = view.getHeight();
    }

    private float animValue = 0;// 0 ~ 1
    @Override
    protected void onDraw(Canvas canvas) {
        isPhotoLoaded = true;
        Drawable drawable = getDrawable();
        if(drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            drawBmp(canvas, bitmap, animValue);
        }
    }

    private void drawBmp(Canvas canvas, Bitmap bmp, float value) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        Rect drawRect = new Rect();// BMP裁切区域
        if(bmpHeight > bmpWidth) {
            drawRect.left = 0;
            drawRect.right = bmpWidth;
            drawRect.top = (int)((bmpHeight - bmpWidth) / 2.0f * (1 - value));
            drawRect.bottom = bmpHeight - drawRect.top;
        } else {
            drawRect.top = 0;
            drawRect.bottom = bmpHeight;
            drawRect.left = (int)((bmpWidth - bmpHeight) / 2.0f * (1 - value));
            drawRect.right = bmpWidth - drawRect.left;
        }

        Rect toRect;// 最终绘制大小
        if(mToRect == null) {
            toRect = new Rect(0, 0, getWidth(), getHeight());
        } else {
            toRect = new Rect(mToRect);
        }

        float toHWRatio = toRect.height() / (float) toRect.width();
        float orgHWRatio = bmpHeight / (float) bmpWidth;

        if(toHWRatio > orgHWRatio) {// 相当于横图
            int space = (int) ((toRect.height() - bmpHeight / (float) bmpWidth * toRect.width()) / 2.0f);
            toRect.top = toRect.top + space;
            toRect.bottom = toRect.bottom - space;
        } else {// 相当于竖图
            int space = (int) ((toRect.width() - bmpWidth / (float) bmpHeight * toRect.height()) / 2.0f);
            toRect.left = toRect.left + space;
            toRect.right = toRect.right - space;
        }

        Rect viewRect = new Rect();// 当前绘制大小
        viewRect.left = (int) (viewX + animValue * (toRect.left - viewX));
        viewRect.top = (int) (viewY + animValue * (toRect.top - viewY));
        viewRect.right = (int) (viewRect.left + viewWidth + animValue * (toRect.width() - viewWidth));
        viewRect.bottom = (int) (viewRect.top + viewHeight + animValue * (toRect.height() - viewHeight));

        canvas.drawBitmap(bmp, drawRect, viewRect, null);
    }

    private long mDuration = 5000;
    public void setDuration(long duration) {
        this.mDuration = duration;
        if(duration < 100) {
            ANIM_FRAME = 10;
        }
    }

    private static int ANIM_FRAME = 100;// 帧数
    private boolean isPhotoLoaded = false;// 等View绘制了之后才能开始动画
    private Rect mToRect;// 最终的大小
    public void start(Rect toRect) {
        mToRect = toRect;
        if(!isPhotoLoaded) {
            handler.sendEmptyMessageDelayed(0, 50);
            return;
        }

        reset();

        handler.sendEmptyMessage(1);
        long singleFrameTime = mDuration / ANIM_FRAME;
        for (int n = 1; n < ANIM_FRAME; n++) {
            handler.sendEmptyMessageDelayed(1, singleFrameTime * n);
        }
    }

    public void reset() {
        animValue = 0;
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    start(mToRect);
                    break;
                case 1:
                    animValue += 1.0f / ANIM_FRAME;
                    if(animValue <= 1) {
                        invalidate();
                    }
                    break;
            }
        }
    };
}
