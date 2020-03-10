package com.peakacard.app.card.view

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
    fun makeDraggable(draggableView: View, height: Int, onViewDragged: () -> Unit) {
        val heightLimit = height * HEIGHT_FACTOR
        var dY = 0f
        var startY = 0f
        var previousY = 0f
        var velocityTracker: VelocityTracker? = null
        val springAnimation =
            SpringAnimation(draggableView, DynamicAnimation.Y).setSpring(SpringForce())
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
                    previousY = startY
                    dY = view.y - motionEvent.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (animating) return@setOnTouchListener false
                    val translationY = motionEvent.rawY + dY
                    velocityTracker?.addMovement(motionEvent)
                    velocityTracker?.computeCurrentVelocity(1000)
                    if (velocityTracker?.yVelocity ?: 0f <= -VELOCITY_LIMIT) {
                        if (previousY >= translationY) {
                            view.translateYTo(-height.toFloat(), onViewDragged)
                        }
                    }

                    view.y = translationY
                    previousY = translationY
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (animating) return@setOnTouchListener false
                    if (view.y <= -heightLimit) {
                        view.translateYTo(-height.toFloat(), onViewDragged)
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

    private fun View.translateYTo(destination: Float, endAction: () -> Unit) {
        animate().y(destination)
            .withStartAction { animating = true }
            .withEndAction {
                animating = false
                endAction()
            }
            .setDuration(context.resources.getInteger(R.integer.anim_duration_short).toLong())
            .interpolator = AccelerateInterpolator()
    }
}