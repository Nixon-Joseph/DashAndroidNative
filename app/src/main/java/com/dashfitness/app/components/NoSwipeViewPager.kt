package com.dashfitness.app.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent

class NoSwipeViewPager(context: Context, attrs: AttributeSet): androidx.viewpager.widget.ViewPager(context, attrs) {
    private var swipeEnabled = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (swipeEnabled) super.onTouchEvent(ev) else false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (swipeEnabled) super.onInterceptTouchEvent(ev) else false
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return if (swipeEnabled) super.executeKeyEvent(event) else false
    }
}