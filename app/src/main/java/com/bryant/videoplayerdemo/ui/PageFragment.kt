package com.bryant.videoplayerdemo.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.bryant.videoplayerdemo.databinding.FragmentPageBinding
import com.bryant.videoplayerdemo.extensions.AppContext
import com.bryant.videoplayerdemo.extensions.TAG
import com.bryant.videoplayerdemo.extensions.getIcon
import com.bryant.videoplayerdemo.extensions.getVideo
import com.bryant.videoplayerdemo.viewmodel.DataViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import timber.log.Timber

internal const val ARG_INDEX = "index"

class PageFragment : Fragment() {
    private var currentIndex = 0
    private var videoUrl: String? = null
    private var iconUrl: String? = null
    private var exoPlayer: ExoPlayer? = null
    private val dataViewModel: DataViewModel by activityViewModels()

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentIndex = it.getInt(ARG_INDEX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageBinding.inflate(inflater, container, false)
        dataViewModel.dataInfo.value?.get(currentIndex)?.let { data ->
            videoUrl = data.id?.getVideo
            iconUrl = (data.source?.get(2) as? List<*>)?.get(0)?.toString()?.getIcon
            iconUrl?.let {
                binding.ivCover.load(it) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    scale(Scale.FILL)
                }
            }
        }
        binding.left.setOnClickListener {
            dataViewModel.updatePageIndex("forward")
        }
        binding.right.setOnClickListener {
            dataViewModel.updatePageIndex("next")
        }
        return binding.root
    }

    private fun initPlayer() {
        Timber.d(TAG, "initPlayer, uri = $videoUrl")
        val uri = Uri.parse(videoUrl)
        val mediaDataSourceFactory: DataSource.Factory =
            DefaultDataSource.Factory(AppContext)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        exoPlayer = ExoPlayer.Builder(AppContext)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        binding.playerView.apply {
            player = exoPlayer
            controllerHideOnTouch = false
            showController()
            controllerShowTimeoutMs = 150000000
        }
        exoPlayer?.run {
            addMediaSource(mediaSource)
            prepare()
            playWhenReady = true
        }
    }

    private fun releasePlayer() {
        Timber.d(TAG, "releasePlayer, uri = $videoUrl")
        exoPlayer?.run {
            stop()
            release()
        }
    }

    override fun onStart() {
        Timber.d(TAG, "onStart, uri = $videoUrl")
        super.onStart()
//        if (Util.SDK_INT > 23) initPlayer()
    }

    override fun onResume() {
        Timber.d(TAG, "onResume, uri = $videoUrl")
        super.onResume()
        initPlayer()
    }

    override fun onPause() {
        Timber.d(TAG, "onPause, uri = $videoUrl")
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        Timber.d(TAG, "onStop, uri = $videoUrl")
        super.onStop()
//        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(index: Int) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INDEX, index)
                }
            }
    }
}