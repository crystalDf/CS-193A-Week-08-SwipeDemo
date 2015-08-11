package com.star.swipedemo;


import android.content.Context;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class OnSwipeListener implements View.OnTouchListener {

    private boolean mDragHorizontal = false;
    private boolean mDragVertical = false;
    private boolean mDragSnapBack = false;
    private boolean mAnimated = true;
    private boolean mExitScreenOnSwipe = false;
    private long mAnimationDelay = 500;
    private float mDragSnapThreshold = 50;
    private float mSwipeDistanceThreshold = 100;
    private float mSwipeVelocityThreshold = 100;
    private float mDragPrevX;
    private float mDragPrevY;
    private GestureDetector mGestureDetector = null;
    private Impl mSwiper = null;
    private View mDraggedView = null;

    public OnSwipeListener(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureListener());
        if (context instanceof Impl) {
            mSwiper = (Impl) context;
        }
    }

    public void onSwipeLeft(float distance) {
        if (mSwiper != null) {
            mSwiper.onSwipeLeft(distance);
        }
    }

    public void onSwipeRight(float distance) {
        if (mSwiper != null) {
            mSwiper.onSwipeRight(distance);
        }
    }

    public void onSwipeUp(float distance) {
        if (mSwiper != null) {
            mSwiper.onSwipeUp(distance);
        }
    }

    public void onSwipeDown(float distance) {
        if (mSwiper != null) {
            mSwiper.onSwipeDown(distance);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != null) {
            mDraggedView = v;
        }

        boolean gesture = mGestureDetector.onTouchEvent(event);

        int action = event.getAction();

        if (mDragHorizontal || mDragVertical) {
            if (action == event.ACTION_DOWN || action == event.ACTION_POINTER_DOWN) {

            } else if (action == event.ACTION_MOVE) {
                float dragCurrX = event.getRawX();
                float dragCurrY = event.getRawY();

                if (mDragHorizontal) {
                    v.setTranslationX(v.getTranslationX() + dragCurrX - mDragPrevX);
                }

                if (mDragVertical) {
                    v.setTranslationY(v.getTranslationY() + dragCurrY - mDragPrevY);
                }

            } else if (action == event.ACTION_UP || action == event.ACTION_POINTER_UP ||
                    action == event.ACTION_CANCEL) {
                if (mDragSnapBack) {
                    float dx = event.getRawX() - mDragPrevX;
                    float dy = event.getRawY() - mDragPrevY;

                    boolean shouldDoX = (Math.abs(dx) <= mDragSnapThreshold) ||
                            (mDragSnapThreshold <= 0);
                    boolean shouldDoY = (Math.abs(dy) <= mDragSnapThreshold) ||
                            (mDragSnapThreshold <= 0);

                    if (mAnimated) {
                        ViewPropertyAnimator viewPropertyAnimator = v.animate();

                        if (shouldDoX) {
                            viewPropertyAnimator.translationX(0);
                        }

                        if (shouldDoY) {
                            viewPropertyAnimator.translationY(0);
                        }

                        viewPropertyAnimator.setDuration(mAnimationDelay);

                        viewPropertyAnimator.start();
                    } else {
                        if (shouldDoX) {
                            v.setTranslationX(0);
                        }

                        if (shouldDoY) {
                            v.setTranslationY(0);
                        }
                    }
                }
            }

            mDragPrevX = event.getRawX();
            mDragPrevY = event.getRawY();
        }

        return gesture;
    }

    public OnSwipeListener setDragHorizontal(boolean dragHorizontal) {
        mDragHorizontal = dragHorizontal;
        return this;
    }

    public OnSwipeListener setDragVertical(boolean dragVertical) {
        mDragVertical = dragVertical;
        return this;
    }

    public OnSwipeListener setDragSnapBack(boolean dragSnapBack) {
        mDragSnapBack = dragSnapBack;
        return this;
    }

    public OnSwipeListener setAnimated(boolean animated) {
        mAnimated = animated;
        return this;
    }

    public OnSwipeListener setExitScreenOnSwipe(boolean exitScreenOnSwipe) {
        mExitScreenOnSwipe = exitScreenOnSwipe;
        return this;
    }

    public OnSwipeListener setAnimationDelay(long animationDelay) {
        mAnimationDelay = animationDelay;

        setAnimated(animationDelay > 0);

        return this;
    }

    public OnSwipeListener setDragSnapThreshold(float dragSnapThreshold) {
        mDragSnapThreshold = dragSnapThreshold;

        if (dragSnapThreshold > 0) {
            setDragSnapBack(true);
        }

        return this;
    }

    public OnSwipeListener setSwipeDistanceThreshold(float swipeDistanceThreshold) {
        mSwipeDistanceThreshold = swipeDistanceThreshold;
        return this;
    }

    public OnSwipeListener setSwipeVelocityThreshold(float swipeVelocityThreshold) {
        mSwipeVelocityThreshold = swipeVelocityThreshold;
        return this;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float dx = e2.getRawX() - e1.getRawX();
            float dy = e2.getRawY() - e1.getRawY();

            Configuration configuration = mDraggedView.getContext().getApplicationContext()
                    .getResources().getConfiguration();

            int screenWidth = configuration.screenWidthDp;
            int screenHeight = configuration.screenHeightDp;

            if ((Math.abs(dx) > Math.abs(dy)) &&
                    (Math.abs(dx) > mSwipeDistanceThreshold) &&
                    (Math.abs(velocityX) > mSwipeVelocityThreshold)) {
                if (dx > 0) {
                    onSwipeRight(dx);
                    dragEdgeHelper(screenWidth * 2, true, 0, false);
                } else {
                    onSwipeLeft(-dx);
                    dragEdgeHelper(-screenWidth, true, 0, false);
                }

                return true;
            } else if ((Math.abs(dy) > Math.abs(dx)) &&
                    (Math.abs(dy) > mSwipeDistanceThreshold) &&
                    (Math.abs(velocityY) > mSwipeVelocityThreshold)) {
                if (dy > 0) {
                    onSwipeDown(dy);
                    dragEdgeHelper(0, false, screenHeight * 2, true);
                } else {
                    onSwipeUp(-dy);
                    dragEdgeHelper(0, false, -screenHeight, true);
                }

                return true;
            }

            return false;
        }

        private void dragEdgeHelper(float tx, boolean useTx, float ty, boolean useTy) {
            if (mExitScreenOnSwipe && (mDraggedView != null)) {
                if (mAnimated) {
                    ViewPropertyAnimator viewPropertyAnimator = mDraggedView.animate()
                            .setDuration(mAnimationDelay);

                    if (useTx) {
                        viewPropertyAnimator.translationX(tx);
                    }

                    if (useTy) {
                        viewPropertyAnimator.translationY(ty);
                    }

                    viewPropertyAnimator.start();
                } else {
                    mDraggedView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private interface Impl {
        void onSwipeLeft(float distance);
        void onSwipeRight(float distance);
        void onSwipeUp(float distance);
        void onSwipeDown(float distance);
    }
}
