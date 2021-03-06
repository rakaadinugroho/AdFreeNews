package com.cogitator.adfreenews.view.news

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cogitator.adfreenews.R
import com.cogitator.adfreenews.utils.CATEGORY_TO_TAB_POS_MAP
import com.cogitator.adfreenews.utils.TAB_TO_COLOR_MAP
import com.cogitator.adfreenews.utils.inflate
import com.cogitator.adfreenews.view.article.ArticleFragment
import kotlinx.android.synthetic.main.fragment_news_section.*

/**
 * @author Ankit Kumar on 14/09/2018
 */

class NewsSectionFragment : Fragment() {

    var mListener: OnNewsSectionFragmentInteractionListener? = null

    private val newsCategoryList = listOf("General", "Science", "Entertainment", "Technology", "Health", "Sports", "Business")

    private val adapter: NewsPagerAdapter by lazy {
        NewsPagerAdapter(childFragmentManager, newsCategoryList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.fragment_news_section)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager.adapter = adapter
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        pager.offscreenPageLimit = newsCategoryList.size
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setupWithViewPager(pager)
        pager.currentItem = 0
    }

    private var onTabSelectedListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            val colorFrom = activity?.window?.statusBarColor
            val colorTo = getColorForTab(tab?.position ?: 0)

            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 1000
            colorAnimation.addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                tabLayout?.setBackgroundColor(color)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity?.window?.statusBarColor = color
                }
            }
            colorAnimation.start()
            mListener?.changeBottomTabColor(colorTo)
        }
    }

    fun refreshBookmarkNewsByCategory(newsId: String, category: String) {
        val articleFragment = adapter.getRegisteredFragment(CATEGORY_TO_TAB_POS_MAP[category]
                ?: 0) as ArticleFragment
        articleFragment.refreshBookmarkStatus(newsId)
    }


    fun getColorForTab(position: Int): Int {
        val colorId = TAB_TO_COLOR_MAP[position] ?: R.color.colorPrimary
        return context?.let { ContextCompat.getColor(it, colorId) } ?: R.color.colorPrimary
    }

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        if (context is OnNewsSectionFragmentInteractionListener) {
            mListener = context
            mListener?.onNewsSectionFragmentAttach(this)
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    interface OnNewsSectionFragmentInteractionListener {
        fun onNewsSectionFragmentAttach(fragment: NewsSectionFragment)
        fun changeBottomTabColor(color: Int)
    }

    companion object {

        fun newInstance(): NewsSectionFragment {
            return NewsSectionFragment()
        }

        @JvmField
        val TAG = NewsSectionFragment::class.java.simpleName
    }
}