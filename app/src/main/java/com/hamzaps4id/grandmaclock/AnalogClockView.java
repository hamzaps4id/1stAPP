package com.hamzaps4id.grandmaclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import java.util.Calendar;

public class AnalogClockView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Calendar calendar = Calendar.getInstance();

    public AnalogClockView(Context context) {
        super(context);
        setBackgroundColor(0xFF000000);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) * 0.40f;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFFFFFFF);
        canvas.drawCircle(cx, cy, radius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(4f, radius * 0.018f));
        paint.setColor(0xFF111111);
        canvas.drawCircle(cx, cy, radius, paint);

        // Hour markers: thick, highly visible for an older user.
        for (int i = 0; i < 12; i++) {
            double a = Math.PI * 2 * i / 12.0 - Math.PI / 2;
            float outer = radius * 0.91f;
            float inner = (i % 3 == 0) ? radius * 0.80f : radius * 0.85f;
            paint.setStrokeWidth((i % 3 == 0) ? radius * 0.045f : radius * 0.025f);
            canvas.drawLine(
                    cx + (float)Math.cos(a) * inner,
                    cy + (float)Math.sin(a) * inner,
                    cx + (float)Math.cos(a) * outer,
                    cy + (float)Math.sin(a) * outer,
                    paint);
        }

        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        float hourAngle = (hour + minute / 60f) * 30f;
        float minuteAngle = (minute + second / 60f) * 6f;
        float secondAngle = second * 6f;

        drawHand(canvas, cx, cy, radius * 0.52f, hourAngle, radius * 0.065f, 0xFF111111);
        drawHand(canvas, cx, cy, radius * 0.72f, minuteAngle, radius * 0.038f, 0xFF111111);
        drawHand(canvas, cx, cy, radius * 0.78f, secondAngle, radius * 0.012f, 0xFFE53935);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF111111);
        canvas.drawCircle(cx, cy, radius * 0.065f, paint);

        postInvalidateDelayed(250);
    }

    private void drawHand(Canvas canvas, float cx, float cy, float length,
                          float degrees, float width, int color) {
        double a = Math.toRadians(degrees - 90);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(width);
        paint.setColor(color);
        canvas.drawLine(
                cx, cy,
                cx + (float)Math.cos(a) * length,
                cy + (float)Math.sin(a) * length,
                paint);
        paint.setStrokeCap(Paint.Cap.BUTT);
    }
}
