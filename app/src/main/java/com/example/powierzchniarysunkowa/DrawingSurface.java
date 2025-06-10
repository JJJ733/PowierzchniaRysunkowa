package com.example.powierzchniarysunkowa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private int currentColor = Color.BLACK;

    private List<ColoredPath> paths;
    private List<ColoredCircle> circles;

    private Path tempPath;

    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setWillNotDraw(false);

        paths = new ArrayList<>();
        circles = new ArrayList<>();
        tempPath = new Path();
    }

    private static class ColoredPath {
        Path path;
        Paint paint;

        ColoredPath(Path path, int color) {
            this.path = path;
            this.paint = new Paint();
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(5);
            this.paint.setAntiAlias(true);
        }
    }

    private static class ColoredCircle {
        float x, y;
        int color;

        ColoredCircle(float x, float y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawARGB(255, 255, 255, 255);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tempPath = new Path();
                tempPath.moveTo(x, y);
                circles.add(new ColoredCircle(x, y, currentColor));
                break;

            case MotionEvent.ACTION_MOVE:
                tempPath.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                tempPath.lineTo(x, y);
                paths.add(new ColoredPath(tempPath, currentColor));
                circles.add(new ColoredCircle(x, y, currentColor));
                redrawCanvas();
                break;
        }

        invalidate();
        return true;
    }

    private void redrawCanvas() {
        // Czyść wszystko i rysuj od nowa
        mCanvas.drawARGB(255, 255, 255, 255); // białe tło

        for (ColoredPath cp : paths) {
            mCanvas.drawPath(cp.path, cp.paint);
        }

        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);

        for (ColoredCircle c : circles) {
            circlePaint.setColor(c.color);
            mCanvas.drawCircle(c.x, c.y, 10, circlePaint);
        }
    }

    public void setColor(int color) {
        currentColor = color;
    }

    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawARGB(255, 255, 255, 255);
        paths.clear();
        circles.clear();
        invalidate();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
