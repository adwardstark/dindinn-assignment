package com.adwardstark.dd_mini_assignment.ui.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.core.view.doOnPreDraw
import com.adwardstark.dd_mini_assignment.R

/**
 * Created by Aditya Awasthi on 10/09/21.
 * @author github.com/adwardstark
 */
class ProgressStepBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val defaultHeight =
        resources.getDimensionPixelSize(R.dimen.step_progressbar_default_height)

    private var needInitial = true

    private var max: Int = DEFAULT_MAX
        set(value) {
            field = value
            makeStepView()
        }

    private var step: Int = DEFAULT_STEP
        set(value) {
            field = value
            makeStepView()
        }

    private var stepDoneColor = Color.BLUE
        set(value) {
            field = value
            makeStepView()
        }

    private var stepUndoneColor = Color.LTGRAY
        set(value) {
            field = value
            makeStepView()
        }

    private var stepMargin = resources.getDimensionPixelSize(R.dimen.step_progressbar_default_margin)
        set(value) {
            field = value
            makeStepView()
        }

    private var preserveStepMargin = false
        set(value) {
            field = value
            makeStepView()
        }


    init {
        orientation = HORIZONTAL
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.ProgressStepBar, defStyleAttr, 0
                )

            max = typedArray.getInt(R.styleable.ProgressStepBar_max, max)
            step = typedArray.getInt(R.styleable.ProgressStepBar_step, step)
            stepDoneColor =
                typedArray.getColor(R.styleable.ProgressStepBar_stepDoneColor, stepDoneColor)
            stepUndoneColor =
                typedArray.getColor(R.styleable.ProgressStepBar_stepUndoneColor, stepUndoneColor)
            stepMargin =
                typedArray.getDimensionPixelSize(R.styleable.ProgressStepBar_stepMargin, stepMargin)
            preserveStepMargin =
                typedArray.getBoolean(R.styleable.ProgressStepBar_stepKeepMargin, false)

            typedArray.recycle()
        }
    }

    @CallSuper
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultHeight(defaultHeight, heightMeasureSpec)
        super.onMeasure(width, height)
        if (needInitial) {
            needInitial = false
            doOnPreDraw { makeStepView(width, height) }
        }
    }

    private fun getDefaultHeight(size: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> size
            else -> size
        }
    }

    private fun makeStepView(width: Int = getWidth(), height: Int = getHeight()) {
        if (needInitial) {
            return
        }

        removeAllViewsInLayout()

        val widthWithoutMargin = width - stepMargin
        val totalViewWidth = widthWithoutMargin - stepMargin * (max - 1)
        val undoneViewWidth = totalViewWidth / max
        val undoneStepCount = max - step

        if (preserveStepMargin) {
            val doneViewWidth = totalViewWidth / max
            repeat(step) { addDoneViewMargin(doneViewWidth, height) }

        } else {
            val doneViewWidth = width - undoneStepCount * (undoneViewWidth + stepMargin)
            addDoneView(doneViewWidth, height)
        }

        repeat(undoneStepCount) { addUndoneView(undoneViewWidth, height) }
    }

    private fun addDoneView(doneViewWidth: Int, height: Int) {
        addView(View(context).apply {
            layoutParams = LayoutParams(doneViewWidth, height)
            setBackgroundColor(stepDoneColor)
        })
    }

    private fun addUndoneView(stepItemWidth: Int, height: Int) {
        addView(View(context).apply {
            layoutParams = LayoutParams(stepItemWidth, height)
                .apply { leftMargin = stepMargin }
            setBackgroundColor(stepUndoneColor)
        })
    }

    private fun addDoneViewMargin(doneViewWidth: Int, height: Int) {
        addView(View(context).apply {
            layoutParams = LayoutParams(doneViewWidth, height)
                .apply { leftMargin = stepMargin }
            setBackgroundColor(stepDoneColor)
        })
    }

    companion object {
        private const val DEFAULT_MAX = 10
        private const val DEFAULT_STEP = 0
    }

}