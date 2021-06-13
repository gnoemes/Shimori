package com.gnoemes.common.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import com.infideap.drawerbehavior.AdvanceDrawerLayout
import dev.chrisbanes.insetter.doOnApplyWindowInsets

class FixedTopInsetAdvanceDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AdvanceDrawerLayout(context, attrs, defStyle) {

    private var topInset = 0
    private var cardView: CardView? = null

    init {
        doOnApplyWindowInsets { _, insets, _ ->
            topInset = insets.systemWindowInsetTop
        }
        addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                cardView?.updateLayoutParams<FrameLayout.LayoutParams> {
                    topMargin = (topInset * slideOffset).toInt()
                }
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }
        })
    }

    override fun addView(child: View?) {
        super.addView(child)

        children
            .forEach { container ->
                if (container is FrameLayout) {
                    cardView = container
                        .children
                        .find { it is CardView } as? CardView
                    return
                }
            }
    }
}