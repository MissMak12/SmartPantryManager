package za.co.smartpantry;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BannerNotification {

    public enum Type {
        SUCCESS, ERROR, INFO, WARNING
    }

    public static void show(Activity activity, String message, Type type) {
        if (activity == null || activity.isFinishing()) return;

        ViewGroup root = activity.findViewById(android.R.id.content);
        if (root == null) return;

        // Container card for floating popup
        LinearLayout banner = new LinearLayout(activity);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setGravity(Gravity.CENTER_VERTICAL);
        int paddingPx = dpToPx(activity, 16);
        banner.setPadding(paddingPx, dpToPx(activity, 14), paddingPx, dpToPx(activity, 14));

        // Background resource and icon based on Type
        ImageView iconView = new ImageView(activity);
        int iconSize = dpToPx(activity, 24);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        iconParams.setMarginEnd(dpToPx(activity, 12));
        iconView.setLayoutParams(iconParams);

        if (type == Type.SUCCESS) {
            banner.setBackgroundResource(R.drawable.bg_popup_success);
            iconView.setImageResource(R.drawable.ic_check_circle);
        } else if (type == Type.ERROR) {
            banner.setBackgroundResource(R.drawable.bg_popup_error);
            iconView.setImageResource(R.drawable.ic_error);
        } else if (type == Type.INFO) {
            banner.setBackgroundResource(R.drawable.bg_popup_success);
            iconView.setImageResource(R.drawable.ic_check_circle);
        } else {
            banner.setBackgroundResource(R.drawable.bg_popup_error);
            iconView.setImageResource(R.drawable.ic_error);
        }

        TextView textView = new TextView(activity);
        textView.setText(message);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        banner.addView(iconView);
        banner.addView(textView);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int marginPx = dpToPx(activity, 16);
        layoutParams.setMargins(marginPx, dpToPx(activity, 28), marginPx, 0);
        layoutParams.gravity = Gravity.TOP;
        banner.setLayoutParams(layoutParams);

        // Initial offscreen translation above top
        banner.setTranslationY(-dpToPx(activity, 120));
        banner.setAlpha(0f);
        root.addView(banner);

        // Slide Down Animation
        banner.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(450)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();

        // Dismiss handler after 3.2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (banner.getParent() != null) {
                banner.animate()
                        .translationY(-dpToPx(activity, 120))
                        .alpha(0f)
                        .setDuration(350)
                        .setInterpolator(new DecelerateInterpolator())
                        .withEndAction(() -> root.removeView(banner))
                        .start();
            }
        }, 3200);

        // Tap to dismiss immediately
        banner.setOnClickListener(v -> {
            banner.animate()
                    .translationY(-dpToPx(activity, 120))
                    .alpha(0f)
                    .setDuration(250)
                    .withEndAction(() -> root.removeView(banner))
                    .start();
        });
    }

    private static int dpToPx(Activity activity, int dp) {
        return Math.round(dp * activity.getResources().getDisplayMetrics().density);
    }
}
