package com.edu.eduorganizer.bubble;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.edu.eduorganizer.R;

public class Bubble extends Service implements View.OnTouchListener, View.OnClickListener {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 123;
    private WindowManager mWindowManager;
    private View mFloatingBubbleView;
    private boolean isMinimized = false;

    private WindowManager.LayoutParams params;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private boolean isMoving = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mFloatingBubbleView = LayoutInflater.from(this).inflate(R.layout.activity_bubble, null);

        ImageView bubbleIcon = mFloatingBubbleView.findViewById(R.id.bubbleIcon);
        bubbleIcon.setOnClickListener(this);
        bubbleIcon.setOnTouchListener(this);


        bubbleIcon.setOnClickListener(v -> toggleWidget());

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        mWindowManager.addView(mFloatingBubbleView, params);
    }

    private void toggleWidget() {
        if (!isMinimized) {

            Intent intent;
            intent = new Intent(Bubble.this, MaxBubble.class);
            stopService(new Intent(this,Bubble.class));
            startService(intent);

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingBubbleView != null) {
            mWindowManager.removeView(mFloatingBubbleView);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = false;
                initialX = params.x;
                initialY = params.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                // Calculate the new position of the bubble
                int newX = initialX + (int) (event.getRawX() - initialTouchX);
                int newY = initialY + (int) (event.getRawY() - initialTouchY);
                // Update the position of the bubble view
                if (Math.abs(newX - initialX) > 10 || Math.abs(newY - initialY) > 10) {
                    isMoving = true;
                }
                updateBubblePosition(newX, newY);
                return true;
            case MotionEvent.ACTION_UP:
                if (!isMoving) {
                    v.performClick();
                }
                return true;

        }
        return false;
    }

    private void updateBubblePosition(int x, int y) {
        // Update the layout parameters with the new position
        params.x = x;
        params.y = y;
        // Update the bubble view's position
        mWindowManager.updateViewLayout(mFloatingBubbleView, params);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bubbleIcon){
            toggleWidget();
        }
    }
}
