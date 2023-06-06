package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
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

    override fun layoutId(): Int = R.layout.activity_exam_result

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
            mDatabind.videoView.visibility = View.VISIBLE
            mDatabind.offlineClassTv.visibility = View.GONE
            mDatabind.offlineClassTvContent.visibility = View.GONE
            initPlayer()
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
//            if (certNum.canShow()) {
//                //有证书ID才跳转
//            }
            val intent = Intent(this@ExamResultActivity, CertiDetailActivity::class.java)
            intent.putExtra("id", examNum)
            intent.putExtra("name", examName)
            startActivity(intent)
        }

        loadsir.showLoading()
        mViewModel.getExamDetail(examNum)

    }

    /**
     * 初始化播放器
     */
    private fun initPlayer() {
//        mDatabind.videoView.playerControlHandler.apply {
//            init(this@ExamResultActivity)
//            setDecodeType(QPlayerSetting.QPlayerDecoder.QPLAYER_DECODER_SETTING_AUTO)
//            setSeekMode(QPlayerSetting.QPlayerSeek.QPLAYER_SEEK_SETTING_NORMAL)
//            setStartAction(QPlayerSetting.QPlayerStart.QPLAYER_START_SETTING_PLAYING)
//            setSpeed(1.0f)
//        }
//
//        playVideo("http://rvin5iszh.hn-bkt.clouddn.com/Video/1686036324700")
//        mDatabind.videoView.setVideoURI(Uri.parse("http://rvin5iszh.hn-bkt.clouddn.com/Video/13412341234-2023051516043002-1686039627069.mp4"))
//        mDatabind.videoView.start()

        player = ExoPlayer.Builder(this) // <- context
            .build()

// create a media item.
        val mediaItem = MediaItem.Builder()
            .setUri("http://rvin5iszh.hn-bkt.clouddn.com/Video/13412341234-2023051516043002-1686039627069.mp4")
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()

        // Create a media source and pass the media item
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSource.Factory(this) // <- context
        )
            .createMediaSource(mediaItem)

        // Finally assign this media source to the player
        player!!.apply {
            setMediaSource(mediaSource)
            playWhenReady = true // start playing when the exoplayer has setup
            seekTo(0, 0L) // Start from the beginning
            prepare() // Change the state from idle.
        }.also {
            // Do not forget to attach the player to the view
            mDatabind.videoView.player = it
            mDatabind.customPlayerController.player = it
        }

        mDatabind.videoView.setOnClickListener {
            if (mDatabind.customPlayerController.isVisible){
                mDatabind.customPlayerController.hide()
            }else{
                mDatabind.customPlayerController.show()
            }
        }

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