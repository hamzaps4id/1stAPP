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

    private int themeIndex = 0;

    private static final int[] OUTER_COLORS = {
            0xFF000000,
            0xFF4A3928,
            0xFF163A54,
            0xFF050505,
            0xFF173B25
    };

    private static final int[] FACE_COLORS = {
            0xFFFFFFFF,
            0xFFFFF4D8,
            0xFFEAF7FF,
            0xFF202124,
            0xFFF1FFF4
    };

    private static final int[] TEXT_COLORS = {
            0xFF111111,
            0xFF2B2118,
            0xFF10293B,
            0xFFFFFFFF,
            0xFF153020
    };

    public AnalogClockView(Context context) {
        super(context);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        setTheme(0);
    }

    public void setTheme(int index) {
        themeIndex = Math.max(0, Math.min(index, FACE_COLORS.length - 1));
        setBackgroundColor(OUTER_COLORS[themeIndex]);
        invalidate();
    }

    public int getThemeIndex() {
        return themeIndex;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) * 0.40f;
        int textColor = TEXT_COLORS[themeIndex];

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(FACE_COLORS[themeIndex]);
        canvas.drawCircle(cx, cy, radius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(4f, radius * 0.018f));
        paint.setColor(textColor);
        canvas.drawCircle(cx, cy, radius, paint);

        for (int i = 0; i < 12; i++) {
            double a = Math.PI * 2 * i / 12.0 - Math.PI / 2;
            float outer = radius * 0.94f;
            float inner = (i % 3 == 0) ? radius * 0.84f : radius * 0.885f;
            paint.setStrokeWidth((i % 3 == 0) ? radius * 0.040f : radius * 0.020f);
            canvas.drawLine(
                    cx + (float) Math.cos(a) * inner,
                    cy + (float) Math.sin(a) * inner,
                    cx + (float) Math.cos(a) * outer,
                    cy + (float) Math.sin(a) * outer,
                    paint);
        }

        String[] numbers = {
                "12", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10", "11"
        };

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        paint.setTextSize(radius * 0.155f);

        float numberRadius = radius * 0.67f;
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float baselineOffset = -(metrics.ascent + metrics.descent) / 2f;

        for (int i = 0; i < 12; i++) {
            double a = Math.PI * 2 * i / 12.0 - Math.PI / 2;
            float x = cx + (float) Math.cos(a) * numberRadius;
            float y = cy + (float) Math.sin(a) * numberRadius + baselineOffset;
            canvas.drawText(numbers[i], x, y, paint);
        }

        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        float hourAngle = (hour + minute / 60f) * 30f;
        float minuteAngle = (minute + second / 60f) * 6f;
        float secondAngle = second * 6f;

        drawHand(canvas, cx, cy, radius * 0.50f, hourAngle,
                radius * 0.060f, textColor);
        drawHand(canvas, cx, cy, radius * 0.70f, minuteAngle,
                radius * 0.035f, textColor);
        drawHand(canvas, cx, cy, radius * 0.77f, secondAngle,
                radius * 0.010f, 0xFFE53935);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        canvas.drawCircle(cx, cy, radius * 0.060f, paint);

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
                cx + (float) Math.cos(a) * length,
                cy + (float) Math.sin(a) * length,
                paint);
        paint.setStrokeCap(Paint.Cap.BUTT);
    }
}
