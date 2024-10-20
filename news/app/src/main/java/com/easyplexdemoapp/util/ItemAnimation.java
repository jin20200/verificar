package com.easyplexdemoapp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ItemAnimation {



    private ItemAnimation(){



    }

    /* animation type */
    public static final int BOTTOM_UP = 1;
    public static final int FADE_IN = 2;
    public static final int LEFT_RIGHT = 3;
    public static final int RIGHT_LEFT = 4;
    public static final int NONE = 0;

    /* animation duration */
    private static final long DURATION_IN_BOTTOM_UP = 150;
    private static final long DURATION_IN_FADE_ID = 500;
    private static final long DURATION_IN_LEFT_RIGHT = 150;
    private static final long DURATION_IN_RIGHT_LEFT = 150;

    public static void animate(View view, int position, int type) {
        switch (type) {
            case BOTTOM_UP:
                animateBottomUp(view, position);
                break;

            case FADE_IN:
                animateFadeIn(view, position);
                break;

            case LEFT_RIGHT:
                animateLeftRight(view, position);
                break;

            case RIGHT_LEFT:
                animateRightLeft(view, position);
                break;
        }
    }

    private static void animateBottomUp(View view, int position) {
        boolean notFirstItem = position == -1;
        position = position + 1;
        view.setTranslationY(notFirstItem ? 800 : 500);
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", notFirstItem ? 800 : 500, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1.f);
        animatorTranslateY.setStartDelay(notFirstItem ? 0 : (position * DURATION_IN_BOTTOM_UP));
        animatorTranslateY.setDuration((notFirstItem ? 3 : 1) * DURATION_IN_BOTTOM_UP);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }

    private static void animateFadeIn(View view, int position) {
        boolean notFirstItem = position == -1;
        position = position + 1;
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 0.5f, 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorAlpha.setStartDelay(notFirstItem ? DURATION_IN_FADE_ID / 2 : (position * DURATION_IN_FADE_ID / 3));
        animatorAlpha.setDuration(DURATION_IN_FADE_ID);
        animatorSet.play(animatorAlpha);
        animatorSet.start();
    }

    private static void animateLeftRight(View view, int position) {
        boolean notFirstItem = position == -1;
        position = position + 1;
        view.setTranslationX(-400f);
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationX", -400f, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorTranslateY.setStartDelay(notFirstItem ? DURATION_IN_LEFT_RIGHT : (position * DURATION_IN_LEFT_RIGHT));
        animatorTranslateY.setDuration((notFirstItem ? 2 : 1) * DURATION_IN_LEFT_RIGHT);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }

    private static void animateRightLeft(View view, int position) {
        boolean notFirstItem = position == -1;
        position = position + 1;
        view.setTranslationX(view.getX() + 400);
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationX", view.getX() + 400, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorTranslateY.setStartDelay(notFirstItem ? DURATION_IN_RIGHT_LEFT : (position * DURATION_IN_RIGHT_LEFT));
        animatorTranslateY.setDuration((notFirstItem ? 2 : 1) * DURATION_IN_RIGHT_LEFT);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }


    public static void expand(final View v, final AnimListener animListener) {
        Animation a = expandAction(v);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animListener.onFinish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(a);
    }

    public static void expand(final View v) {
        Animation a = expandAction(v);
        v.startAnimation(a);
    }

    private static Animation expandAction(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        return a;
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void flyInDown(final View v, final AnimListener animListener) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0.0f);
        v.setTranslationY(0);
        v.setTranslationY(-v.getHeight());
        // Prepare the View for the animation
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animListener != null) animListener.onFinish();
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1.0f)
                .start();
    }

    public static void flyOutDown(final View v, final AnimListener animListener) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1.0f);
        v.setTranslationY(0);
        // Prepare the View for the animation
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animListener != null) animListener.onFinish();
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(0.0f)
                .start();
    }

    public static void fadeIn(final View v) {
        ItemAnimation.fadeIn(v, null);
    }

    public static void fadeIn(final View v, final AnimListener animListener) {
        v.setVisibility(View.GONE);
        v.setAlpha(0.0f);
        // Prepare the View for the animation
        v.animate()
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.VISIBLE);
                        if (animListener != null) animListener.onFinish();
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1.0f);
    }

    public static void fadeOut(final View v) {
        ItemAnimation.fadeOut(v, null);
    }

    public static void fadeOut(final View v, final AnimListener animListener) {
        v.setAlpha(1.0f);
        // Prepare the View for the animation
        v.animate()
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animListener != null) animListener.onFinish();
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(0.0f);
    }

    public static void showIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void initShowOut(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }

    public static void showOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }


    public interface AnimListener {
        void onFinish();
    }

    public static void fadeOutIn(View view) {
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 0.5f, 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorAlpha.setDuration(500);
        animatorSet.play(animatorAlpha);
        animatorSet.start();
    }


    public static void showScale(final View v) {
        ItemAnimation.showScale(v, null);
    }

    public static void showScale(final View v, final AnimListener animListener) {
        v.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animListener != null) animListener.onFinish();
                        super.onAnimationEnd(animation);
                    }
                })
                .start();
    }

    public static void hideScale(final View v) {
        ItemAnimation.fadeOut(v, null);
    }

    public static void hideScale(final View v, final AnimListener animListener) {
        v.animate()
                .scaleY(0)
                .scaleX(0)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animListener != null) animListener.onFinish();
                        super.onAnimationEnd(animation);
                    }
                })
                .start();
    }

    public static void hideFab(View fab) {
        int moveY = 2 * fab.getHeight();
        fab.animate()
                .translationY(moveY)
                .setDuration(300)
                .start();
    }

    public static void showFab(View fab) {
        fab.animate()
                .translationY(0)
                .setDuration(300)
                .start();
    }

}
