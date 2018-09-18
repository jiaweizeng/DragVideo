package com.example.admin.dragvideo

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.greenrobot.eventbus.EventBus

/**
 * Created by zjw on 2018/9/10.
 */
class VideoDetailActivity: AppCompatActivity(), Player.EventListener {


    private var mVideoPlayer: SimpleExoPlayer?=null

    private val mDragLayout by lazy {
        findViewById<VideoDragRelativeLayout>(R.id.rl_drag)
    }
    private val mIvBg by lazy {
        findViewById<ImageView>(R.id.iv_bg)
    }
    private val mVideoView by lazy {
        findViewById<SimpleExoPlayerView>(R.id.video_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        Eyes.translucentStatusBar(this, true)
        mIvBg.setBackgroundResource(R.mipmap.ic_video_drag_bg)
        intent.apply {
            val arrayExtra = getIntArrayExtra("global_rect")
            mDragLayout.apply {
                setIsLastRow(getBooleanExtra("is_last_row",false))
                setOriginView(arrayExtra[0],arrayExtra[1],arrayExtra[2]-arrayExtra[0],
                        arrayExtra[3]- arrayExtra[1],arrayExtra[4])
                startAnimation()
            }
        }
        mDragLayout.setOnVideoDragListener(object :VideoDragRelativeLayout.OnVideoDragListener{
            override fun onStartDrag() {

            }

            override fun onReleaseDrag(isRestoration: Boolean) {
                if (!isRestoration){
                    mIvBg.visibility= View.VISIBLE
                }
            }

            override fun onEnterAnimationEnd(isOutOfBound: Boolean) {
                EventBus.getDefault().post(ScrollTopEvent(isOutOfBound))
            }

            override fun onExitAnimationEnd() {
                finish()
            }

            override fun onRestorationAnimationEnd() {
            }

        })
        initPlayer()
        initVideo()
    }

    override fun onResume() {
        super.onResume()
        mVideoPlayer?.playWhenReady = true
        mIvBg.postDelayed({ mIvBg.visibility = View.GONE }, 400)
    }

    override fun onPause() {
        super.onPause()
        mVideoPlayer?.playWhenReady = false
    }

    /**
     * 初始化player
     */
    private fun initPlayer() {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTackSelectionFactory)
        val loadControl = DefaultLoadControl(DefaultAllocator(true, 65536), 3000, 5000, 2500L, 5000L)
        //2.创建ExoPlayer
        mVideoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
        mVideoView.player = mVideoPlayer
        mVideoPlayer?.addListener(this)
    }

    private fun initVideo() {
        val dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "useExoPlayer"), null)
        val extractorsFactory = DefaultExtractorsFactory()
        //http://sv.dingdangyixia.cn/sv/2fced8a5922843c19c038330cb66f505
        //由于头条的视频地址 在一定的时间会变化  过期的地址无法访问 后期可以通过 python 抓起实时地址
        val videoSource = ExtractorMediaSource(Uri.parse("https://vd3.bdstatic.com/" +
                "mda-idip3ibsutbfkae1/sc/mda-idip3ibsutbfkae1.mp4?auth_key=1528440598-0-0-f8eb95c6ad0f5c6b066feef02630cde0&amp;bcevod_channel=searchbox_feed"),
                dataSourceFactory, extractorsFactory, null, null)
        mVideoPlayer?.prepare(videoSource)
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity() {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    fun onBackPressedSupport(): Boolean {
        mDragLayout.onBackPressed()
        return true
    }
}