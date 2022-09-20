package com.bryant.videoplayerdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bryant.videoplayerdemo.DataRepository
import com.bryant.videoplayerdemo.databinding.FragmentRootBinding
import com.bryant.videoplayerdemo.extensions.TAG
import com.bryant.videoplayerdemo.viewmodel.DataViewModel
import timber.log.Timber

class RootFragment : Fragment() {

    private var _binding: FragmentRootBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val dataViewModel by activityViewModels<DataViewModel> {
        DataViewModel.Factory(DataRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(onPageChangeCallback)
        }
        dataViewModel.dataInfo.observe(viewLifecycleOwner) {
            viewPagerAdapter.dataList = it
        }
        dataViewModel.pageAction.observe(viewLifecycleOwner) {
            updatePage(binding.viewPager, it)
        }
    }

    private fun updatePage(view: ViewPager2, action: String) {
        if (action == "next") {
            if (view.currentItem < viewPagerAdapter.itemCount) {
                moveNext(view)
            }
        } else {
            if (view.currentItem > 0) {
                moveForward(view)
            }
        }
    }

    private fun moveNext(view: ViewPager2) {
        view.currentItem = view.currentItem + 1
    }

    private fun moveForward(view: ViewPager2) {
        view.currentItem = view.currentItem - 1
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Timber.e(TAG, "onPageSelected, position = $position")
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            Timber.e(TAG, "onPageScrollStateChanged, state = $state")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
}