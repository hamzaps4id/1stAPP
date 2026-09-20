package com.hamzaps4id.grandmaclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
    private static final String PREFS = "grandma_clock";
    private static final String THEME = "theme";

    private AnalogClockView clockView;
    private SharedPreferences preferences;

    private final String[] themeNames = {
            "أبيض كلاسيكي",
            "بيج دافئ",
            "أزرق فاتح",
            "أسود ليلي",
            "أخضر هادئ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        preferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        FrameLayout root = new FrameLayout(this);

        clockView = new AnalogClockView(this);
        clockView.setTheme(preferences.getInt(THEME, 0));
        root.addView(clockView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        Button backgroundButton = new Button(this);
        backgroundButton.setText("الخلفية");
        backgroundButton.setTextSize(16f);
        backgroundButton.setTextColor(Color.WHITE);
        backgroundButton.setAllCaps(false);
        backgroundButton.setPadding(24, 8, 24, 8);

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(0xCC222222);
        buttonBackground.setCornerRadius(60f);
        backgroundButton.setBackground(buttonBackground);

        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        buttonParams.bottomMargin = 28;
        root.addView(backgroundButton, buttonParams);

        backgroundButton.setOnClickListener(v -> showBackgroundDialog());

        setContentView(root);
    }

    private void showBackgroundDialog() {
        int current = clockView.getThemeIndex();

        new AlertDialog.Builder(this)
                .setTitle("اختر الخلفية")
                .setSingleChoiceItems(themeNames, current, (dialog, which) -> {
                    clockView.setTheme(which);
                    preferences.edit().putInt(THEME, which).apply();
                    dialog.dismiss();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }
}
