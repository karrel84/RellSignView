package com.karrel.signviewlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class SignView extends View {
    Paint mPaint;
    ArrayList<Vertex> arVertex;
    private Bitmap mBitmap;
    private boolean isSetBitmap = false;
    private Canvas mCanvas;

    public SignView(Context context) {
        super(context);
        this.init(context);
    }

    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public SignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.parseColor("#000000"));
        this.mPaint.setStrokeWidth(6f);
        this.mPaint.setAntiAlias(true);
        this.arVertex = new ArrayList();
    }

    private void initBitmap() {
        if (this.mBitmap == null) {
            this.isSetBitmap = false;
            int width = this.getWidth();
            int height = this.getHeight();
            if (width != 0 && height != 0) {
                this.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                this.mBitmap.eraseColor(0);
            }
        }
    }

    public void clearCanvas() {
        this.arVertex = new ArrayList();
        this.mBitmap = null;
        this.invalidate();
    }

    public Uri getImageUri() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        this.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContext().getContentResolver(), this.getBitmap(), "title", (String) null);
        return Uri.parse(path);
    }

    public Uri getImageUri(Bitmap.CompressFormat format) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        this.getBitmap().compress(format, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContext().getContentResolver(), this.getBitmap(), "title", (String) null);
        return Uri.parse(path);
    }

    public Bitmap loadBitmap(Uri uri) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), uri);
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return bitmap;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.arVertex.add(new SignView.Vertex(event.getX(), event.getY(), false));
            case 1:
            default:
                break;
            case 2:
                this.arVertex.add(new SignView.Vertex(event.getX(), event.getY(), true));
        }

        this.invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.initBitmap();
        if (this.mBitmap != null) {
            if (!this.isSetBitmap) {
                this.mCanvas = new Canvas(this.mBitmap);
                this.mCanvas.setBitmap(this.mBitmap);
                this.isSetBitmap = true;
            }

            for (int i = 0; i < this.arVertex.size(); ++i) {
                if (this.arVertex.get(i).draw) {
                    this.mCanvas.drawLine(this.arVertex.get(i - 1).x, this.arVertex.get(i - 1).y, this.arVertex.get(i).x, this.arVertex.get(i).y, this.mPaint);
                } else {
                    this.mCanvas.drawPoint(this.arVertex.get(i).x, this.arVertex.get(i).y, this.mPaint);
                }
            }

            canvas.drawBitmap(this.mBitmap, 0.0F, 0.0F, this.mPaint);
        }
    }

    @SuppressLint({"NewApi"})
    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public class Vertex {
        float x;
        float y;
        boolean draw;

        public Vertex(float x, float y, boolean draw) {
            this.x = x;
            this.y = y;
            this.draw = draw;
        }
    }
}
