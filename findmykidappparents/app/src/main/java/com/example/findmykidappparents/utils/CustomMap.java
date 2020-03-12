package com.example.findmykidappparents.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class CustomMap extends com.google.android.gms.maps.MapView {

    RectF rectF = new RectF();
    private int cornerRadiusX = 50;

    public CustomMap(Context context) {
        super(context);
    }

    public CustomMap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        rectF.set(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        int count = canvas.save();

        final float[] radii = new float[8];

        radii[0] = cornerRadiusX;
        radii[1] = cornerRadiusX;
        radii[2] = cornerRadiusX;
        radii[3] = cornerRadiusX;

        path.addRoundRect(rectF, radii, Path.Direction.CW);

        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(count);
    }

}