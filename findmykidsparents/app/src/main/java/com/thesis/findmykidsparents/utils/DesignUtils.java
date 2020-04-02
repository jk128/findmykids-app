package com.thesis.findmykidsparents.utils;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DesignUtils {
    public static <T extends View> List<T> findViewsWithType(View root, Class<T> type) {
        List<T> views = new ArrayList<>();
        findViewsWithType(root, type, views);
        return views;
    }

    private static <T extends View> void findViewsWithType(View view, Class<T> type, List<T> views) {
        if (type.isInstance(view)) {
            views.add(type.cast(view));
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                findViewsWithType(viewGroup.getChildAt(i), type, views);
            }
        }
    }

    public static boolean showSnackbar(Activity activity, MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            return false;
        }

        Snackbar.make(
                activity.findViewById(android.R.id.content), menuItem.getTitle(), Snackbar.LENGTH_SHORT)
                .show();
        return true;
    }

//    public static void addBottomSpaceInsetsIfNeeded(
//            ViewGroup scrollableViewAncestor, ViewGroup demoContainer) {
//        List<? extends ViewGroup> scrollViews =
//                DesignUtils.findViewsWithType(scrollableViewAncestor, ScrollView.class);
//
//        List<? extends ViewGroup> nestedScrollViews = DesignUtils
//                .findViewsWithType(scrollableViewAncestor, NestedScrollView.class);
//
//        ArrayList<ViewGroup> scrollingViews = new ArrayList<>();
//        scrollingViews.addAll(scrollViews);
//        scrollingViews.addAll(nestedScrollViews);
//        ViewCompat.setOnApplyWindowInsetsListener(
//                demoContainer,
//                (view, insets) -> {
//                    for (ViewGroup scrollView : scrollingViews) {
//                        scrollView.addOnLayoutChangeListener(
//                                new OnLayoutChangeListener() {
//                                    @Override
//                                    public void onLayoutChange(
//                                            View v,
//                                            int left,
//                                            int top,
//                                            int right,
//                                            int bottom,
//                                            int oldLeft,
//                                            int oldTop,
//                                            int oldRight,
//                                            int oldBottom) {
//                                        scrollView.removeOnLayoutChangeListener(this);
//                                        int systemWindowInsetBottom = insets.getSystemWindowInsetBottom();
//                                        if (!shouldApplyBottomInset(scrollView, systemWindowInsetBottom)) {
//                                            return;
//                                        }
//
//                                        int insetBottom = calculateBottomInset(scrollView, systemWindowInsetBottom);
//                                        View scrollableContent = scrollView.getChildAt(0);
//                                        scrollableContent.setPadding(
//                                                scrollableContent.getPaddingLeft(),
//                                                scrollableContent.getPaddingTop(),
//                                                scrollableContent.getPaddingRight(),
//                                                insetBottom);
//                                    }
//                                });
//                    }
//                    return insets;
//                });
//    }

//    private static int calculateBottomInset(ViewGroup scrollView, int systemWindowInsetBottom) {
//        View scrollableContent = scrollView.getChildAt(0);
//        int calculatedInset = Math.min(
//                systemWindowInsetBottom,
//                scrollableContent.getHeight() + systemWindowInsetBottom - scrollView.getHeight());
//        return Math.max(calculatedInset, 0);
//    }

//    @SuppressWarnings("RestrictTo")
//    private static boolean shouldApplyBottomInset(ViewGroup scrollView, int systemWindowInsetBottom) {
//        View scrollableContent = scrollView.getChildAt(0);
//        int scrollableContentHeight = scrollableContent.getHeight();
//        int scrollViewHeight = scrollView.getHeight();
//        int[] scrollViewLocation = new int[2];
//        scrollView.getLocationOnScreen(scrollViewLocation);
//        Context context = scrollView.getContext();
//
//        Activity activity = ContextUtils.getActivity(context);
//        return scrollViewHeight + scrollViewLocation[1] >= getContentViewHeight(activity)
//                && scrollableContentHeight + systemWindowInsetBottom >= scrollViewHeight;
//    }
//
//    private static int getContentViewHeight(Activity context) {
//        return context.findViewById(android.R.id.content).getHeight();
//    }
}
