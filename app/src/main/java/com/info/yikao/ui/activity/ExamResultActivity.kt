package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityExamResultBinding
import com.info.yikao.ext.*
import com.info.yikao.model.TeacherResultBean
import com.info.yikao.ui.adapter.ExamTeacherResultAdapter
import com.info.yikao.viewmodel.ExamResultViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState


class ExamResultActivity : BaseActivity<ExamResultViewModel, ActivityExamResultBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { ExamTeacherResultAdapter(arrayListOf()) }

    private var examNum = ""
    private var online = false
    private var examName = ""

    private var certNum = ""

    private var player: ExoPlayer? = null
    private val isPlaying get() = player?.isPlaying ?: false


    private var fullBtn: ImageView? = null
    private var playBtn: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var fullScreen = false

    private var oriParams: ConstraintLayout.LayoutParams? = null


    override fun layoutId(): Int = R.layout.activity_exam_result

    override fun showMajorStatusBar() {
        showTransStatusBar()
    }

    override fun initView(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        intent?.let {
            online = it.getBooleanExtra("online", false)
            examNum = it.getStringExtra("id") ?: ""
            examName = it.getStringExtra("name") ?: ""
        }

        if (online) {
            //线上考试，需要显示考生的视频
            //默认先不显示视频
            mDatabind.videoView.visibility = View.GONE
            mDatabind.offlineClassTv.visibility = View.GONE
            mDatabind.offlineClassTvContent.visibility = View.GONE
//            initPlayer()
        } else {
            //不显示视频
            mDatabind.videoView.visibility = View.GONE
            mDatabind.offlineClassTv.visibility = View.VISIBLE
            mDatabind.offlineClassTvContent.visibility = View.VISIBLE
        }

        mDatabind.titleTv.text = examName
        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getExamDetail(examNum)
        }

        //初始化recycleview
        mDatabind.teacherResultRv.init(LinearLayoutManager(this@ExamResultActivity), mAdapter)

        mDatabind.saveCertBook.setOnClickListener {
            if (certNum.canShow()) {
                //有证书ID才跳转
                val intent = Intent(this@ExamResultActivity, CertiDetailActivity::class.java)
                intent.putExtra("id", examNum)
                intent.putExtra("name", examName)
                startActivity(intent)
            }
        }

        loadsir.showLoading()
        mViewModel.getExamDetail(examNum)

    }

    /**
     * 初始化播放器
     */
    private fun initPlayer(url: String) {
        player = ExoPlayer.Builder(this) // <- context
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()

        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSource.Factory(this) // <- context
        )
            .createMediaSource(mediaItem)

        progressBar = mDatabind.videoView.findViewById(R.id.load_buffering)
        fullBtn = mDatabind.videoView.findViewById(R.id.full_btn)
        playBtn = mDatabind.videoView.findViewById(R.id.play_btn)

        playBtn?.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause();
                playBtn?.setBackgroundResource(R.mipmap.icon_play_white)
            } else {
                player!!.play();
                playBtn?.setBackgroundResource(R.mipmap.icon_pause_white)
            }
        }

        player!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_BUFFERING) {
                    progressBar?.visibility = View.VISIBLE
                } else {
                    progressBar?.visibility = View.GONE
                }

                if (state == Player.STATE_READY && !player!!.isPlaying) {
                    playBtn?.setBackgroundResource(R.mipmap.icon_play_white);
                } else if (state == Player.STATE_READY && player!!.isPlaying) {
                    playBtn?.setBackgroundResource(R.mipmap.icon_pause_white);
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                Snackbar.make(mDatabind.saveCertBook,error.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        })

        player!!.apply {
            setMediaSource(mediaSource)
            playWhenReady = true
            seekTo(0, 0L)
            prepare()
        }.also {
            mDatabind.videoView.player = it
        }

        fullBtn?.setOnClickListener {
            if (fullScreen) {
                exitFullscreen()
            } else {
                enterFullscreen()
            }
        }

    }


    private fun enterFullscreen() {
        // 隐藏系统 UI
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // 设置 PlayerView 的布局参数        // 设置 PlayerView 的布局参数
        oriParams = mDatabind.videoView.layoutParams as ConstraintLayout.LayoutParams?

        val params = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mDatabind.videoView.setLayoutParams(params)
        // 更新全屏状态
        fullScreen = true
    }

    private fun exitFullscreen() {
        // 显示系统 UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        // 还原 PlayerView 的布局参数
//        val params = ConstraintLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
        if (oriParams != null) {
            mDatabind.videoView.setLayoutParams(oriParams)
        }

        // 更新全屏状态
        fullScreen = false
    }

    /**
     * 播放视频
     */
    private fun playVideo(url: String) {
//        val builder = QMediaModelBuilder()
//        builder.addElement("", QURLType.QAUDIO_AND_VIDEO, 0, url, true)
//        var mediaModel = builder.build(false)
//        mDatabind.videoView.playerControlHandler.playMediaModel(mediaModel, 0)
    }

    override fun createObserver() {
        mViewModel.examDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()
                //显示界面
                mDatabind.nameTvContent.text = it.Detail.RealName
                mDatabind.idCardTvContent.text = it.Detail.IDNumber
                mDatabind.majorTvContent.text = it.Detail.SubjectsName
                mDatabind.timeTvContent.text = it.Detail.TestStart
                mDatabind.offlineClassTvContent.text = it.Detail.TestClassAddr

                if (online) {
                    if (it.Detail.OnLineVideoUrl.canShow()) {
                        //如果有录制的视频，则显示视频并且播放
                        mDatabind.videoView.visibility = View.VISIBLE
                        initPlayer(Constant.imgUrlHead+it.Detail.OnLineVideoUrl)
                    } else {
                        mDatabind.videoView.visibility = View.GONE
                    }
                }

                if (it.Detail.CertificateNo.canShow()) {
                    //有证书领取
                    certNum = it.Detail.CertificateNo
                    mDatabind.saveCertBook.setBackgroundResource(R.drawable.blue_corner_15)
                } else {
                    mDatabind.saveCertBook.setBackgroundResource(R.drawable.grey_cc_corner_15)
                }

                //处理评级
                if (it.GradeInfoList != null && it.GradeInfoList.isNotEmpty()) {
                    //如果有数据
                    val headGrade = TeacherResultBean(
                        1,
                        -1,
                        "",
                        it.Detail.TestResult,
                        it.Detail.JuryTotalResultStr,
                        "", "",
                        -1,
                        "",
                        ""
                    )

                    it.GradeInfoList.add(0, headGrade)
                    mAdapter.setList(it.GradeInfoList)

                } else {
                    //没有数据
                    val emptyGrade =
                        arrayListOf(TeacherResultBean(3, -1, "", -1, "", "", "", -1, "", ""))
                    mAdapter.setList(emptyGrade)
                }
            }, {
                loadsir.showError()
            })

        }
    }

    override fun onStart() {
        super.onStart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}