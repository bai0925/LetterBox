package com.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by 29924 on 2018/8/15.
 */

public class SurFaceV extends SurfaceView {
    public SurFaceV(Context context) {
        super(context);
    }
    public SurFaceV(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public SurFaceV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        Path path = new Path();
        //用矩形表示SurfaceView宽高
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        //15.0f即是圆角半径
        path.addRoundRect(rect, 300.0f, 300.0f, Path.Direction.CCW);
        //裁剪画布，并设置其填充方式
        canvas.clipPath(path, Region.Op.REPLACE);
        super.draw(canvas);
    }
}
