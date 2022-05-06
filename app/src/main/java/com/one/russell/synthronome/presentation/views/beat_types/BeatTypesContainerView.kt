package com.one.russell.synthronome.presentation.views.beat_types

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.ViewBeatTypesContainerBinding
import com.one.russell.synthronome.domain.BeatType
import com.one.russell.synthronome.presentation.views.utils.createPaddingsDecoration
import com.one.russell.synthronome.presentation.views.utils.executeAfterAllAnimationsAreFinished
import com.one.russell.synthronome.toPx

@SuppressLint("ClickableViewAccessibility")
class BeatTypesContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBeatTypesContainerBinding.inflate(LayoutInflater.from(context), this)
    private var onBeatTypeClick: ((index: Int) -> Unit)? = null

    private var beatTypesColorPrimary = Color.BLACK
    private var beatTypesColorSecondary = Color.BLACK

    private val itemsSpacing = context.resources.getDimension(R.dimen.beat_types_spacing_half) * 2

    init {
        layoutTransition = LayoutTransition() // animate layout changes
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.run {
            rvBeatTypesList.adapter = BeatTypesAdapter { onBeatTypeClick?.invoke(it) }
            rvBeatTypesList.addItemDecoration(
                createPaddingsDecoration(horizontalPadding = itemsSpacing)
            )

            rvBeatTypesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    ivScrollLeft.isVisible = recyclerView.canScrollHorizontally(Direction.LEFT.i)
                    ivScrollRight.isVisible = recyclerView.canScrollHorizontally(Direction.RIGHT.i)
                }
            })

            ivScrollLeft.setOnTouchListener(OnPressTouchListener(
                onTouchDown = { rvBeatTypesList.linearScrollToTheEnd(Direction.LEFT) },
                onTouchUp = { rvBeatTypesList.stopScroll() }
            ))

            ivScrollRight.setOnTouchListener(OnPressTouchListener(
                onTouchDown = { rvBeatTypesList.linearScrollToTheEnd(Direction.RIGHT) },
                onTouchUp = { rvBeatTypesList.stopScroll() }
            ))
        }
    }

    @Suppress("UnnecessaryVariable")
    private fun RecyclerView.linearScrollToTheEnd(direction: Direction) {
        if (canScrollHorizontally(direction.i)) {
            val scrollBy = when (direction) {
                Direction.LEFT -> computeHorizontalScrollOffset()
                Direction.RIGHT -> computeHorizontalScrollRange() - (computeHorizontalScrollOffset() + computeHorizontalScrollExtent())
            }
            val duration = scrollBy // duration must always depend on scroll distance
            smoothScrollBy(direction.i * scrollBy, 0, LinearInterpolator(), duration)
        }
    }

    fun setOnBeatTypeClickListener(onBeatTypeClick: (index: Int) -> Unit) {
        this.onBeatTypeClick = onBeatTypeClick
    }

    fun setBeatTypes(beatTypes: List<BeatType>) {
        binding.rvBeatTypesList.executeAfterAllAnimationsAreFinished {
            val listItems = beatTypes.mapIndexed { index, beatType ->
                beatType.toListItem(index, beatTypesColorPrimary, beatTypesColorSecondary)
            }
            (binding.rvBeatTypesList.adapter as BeatTypesAdapter).items = listItems
        }
    }

    fun setColors(@ColorInt colorPrimary: Int, @ColorInt colorSecondary: Int) {
        this.beatTypesColorPrimary = colorPrimary
        this.beatTypesColorSecondary = colorSecondary
        binding.rvBeatTypesList.executeAfterAllAnimationsAreFinished {
            val currentItems = (binding.rvBeatTypesList.adapter as BeatTypesAdapter).items
            val newItems = currentItems.map {
                it.copy(colorPrimary = colorPrimary, colorSecondary = colorSecondary)
            }
            (binding.rvBeatTypesList.adapter as BeatTypesAdapter).items = newItems
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val height =
            ((widthSize - 66.toPx() /*recyclerview margins*/ - itemsSpacing * 5) / 4).toInt()
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    enum class Direction(val i: Int) {
        LEFT(-1), RIGHT(1)
    }
}