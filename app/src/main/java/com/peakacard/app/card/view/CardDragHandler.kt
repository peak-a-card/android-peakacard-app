package com.peakacard.app.card.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.peakacard.app.R

object CardDragHandler {

    private const val VELOCITY_LIMIT = 4000f
    private const val HEIGHT_FACTOR = 0.6

    private var animating = false

    @SuppressLint("ClickableViewAccessibility")
    fun makeDraggable(draggableView: View, height: Int) {
        val heightLimit = height * HEIGHT_FACTOR
        var dY = 0f
        var startY = 0f
        var velocityTracker: VelocityTracker? = null
        val springAnimation =
            SpringAnimation(draggableView, DynamicAnimation.Y).apply { spring = SpringForce() }
        draggableView.setOnTouchListener { view, motionEvent ->
            return@setOnTouchListener when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (velocityTracker == null) {
                        velocityTracker = VelocityTracker.obtain()
                    } else {
                        velocityTracker?.clear()
                    }
                    velocityTracker?.addMovement(motionEvent)

                    startY = view.y
                    dY = view.y - motionEvent.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (animating) return@setOnTouchListener false
                    velocityTracker?.addMovement(motionEvent)
                    velocityTracker?.computeCurrentVelocity(1000)
                    if (velocityTracker?.yVelocity ?: 0f <= -VELOCITY_LIMIT) {
                        view.translateYTo(-height.toFloat())
                    }

                    view.y = motionEvent.rawY + dY
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (animating) return@setOnTouchListener false
                    if (view.y <= -heightLimit) {
                        view.translateYTo(-height.toFloat())
                    } else {
                        springAnimation.animateToFinalPosition(startY)
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    velocityTracker?.recycle()
                    true
                }
                else -> false
            }
        }
    }

    private fun View.translateYTo(destination: Float) {
        animate().y(destination).apply {
            this.duration = context.resources.getInteger(R.integer.anim_duration_short).toLong()
            this.interpolator = AccelerateInterpolator()
        }.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                animating = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                animating = false
            }
        })
    }
}