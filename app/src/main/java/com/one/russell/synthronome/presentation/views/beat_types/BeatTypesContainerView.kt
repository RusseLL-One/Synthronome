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

@SuppressLint("ClickableViewAccessibility")
class BeatTypesContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBeatTypesContainerBinding.inflate(LayoutInflater.from(context), this)
    private var onBeatTypeClick: ((index: Int) -> Unit)? = null
    private val adapter = BeatTypesAdapter { onBeatTypeClick?.invoke(it) }

    private var beatTypesColorPrimary = Color.TRANSPARENT
    private var beatTypesColorSecondary = Color.TRANSPARENT

    private val itemsSpacing = context.resources.getDimension(R.dimen.beat_types_spacing_half) * 2
    private val horizontalMargin = context.resources.getDimension(R.dimen.beat_types_horizontal_margin) * 2

    private var beatTypesList: List<BeatType> = emptyList()

    init {
        layoutTransition = LayoutTransition() // animate layout changes
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.run {
            rvBeatTypesList.adapter = adapter
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
        this.beatTypesList = beatTypes
        binding.rvBeatTypesList.executeAfterAllAnimationsAreFinished {
            adapter.items = beatTypesList.mapToListItems()
        }
    }

    fun setColors(@ColorInt colorPrimary: Int, @ColorInt colorSecondary: Int) {
        this.beatTypesColorPrimary = colorPrimary
        this.beatTypesColorSecondary = colorSecondary
        binding.rvBeatTypesList.executeAfterAllAnimationsAreFinished {
            adapter.items = beatTypesList.mapToListItems()
        }
    }

    private fun List<BeatType>.mapToListItems(): List<BeatTypeItem> = mapIndexed { index, beatType ->
        beatType.toListItem(index, beatTypesColorPrimary, beatTypesColorSecondary)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val height = ((widthSize - horizontalMargin - itemsSpacing * 5) / 4).toInt()
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    enum class Direction(val i: Int) {
        LEFT(-1), RIGHT(1)
    }
}