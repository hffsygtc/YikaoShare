package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bumptech.glide.Glide
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
import com.info.yikao.databinding.ActivityOfflineExamPointBinding
import com.info.yikao.databinding.ActivityOfflineManagerQrBinding
import com.info.yikao.databinding.ActivityOnlineExamPointBinding
import com.info.yikao.ext.*
import com.info.yikao.ui.adapter.OfflineStudentListAdapter
import com.info.yikao.ui.adapter.OfflineStudentPointListAdapter
import com.info.yikao.ui.adapter.PopPickerAdapter
import com.info.yikao.viewmodel.OfflineJudgePointViewModel
import com.info.yikao.viewmodel.OfflineManagerQrViewModel
import com.info.yikao.viewmodel.OnlineJudgePointViewModel
import com.kingja.loadsir.core.LoadService
//import com.qiniu.qmedia.component.player.QMediaModelBuilder
//import com.qiniu.qmedia.component.player.QPlayerSetting
//import com.qiniu.qmedia.component.player.QURLType
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw
import me.hgj.jetpackmvvm.util.get

/**
 * 线上考试的评分
 */
class OnlineJudgePointActivity :
    BaseActivity<OnlineJudgePointViewModel, ActivityOnlineExamPointBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { OfflineStudentPointListAdapter(arrayListOf()) }
    private val classAdapter by lazy { PopPickerAdapter(1, arrayListOf()) }
    private val sortAdapter by lazy { PopPickerAdapter(2, arrayListOf()) }

    private var underClassPicker = false
    private var underSortPicker = false

    private var selectPos = 2

    var stuId = ""

    val juryResultList = arrayListOf("优秀", "良好", "合格", "不合格")

    var stuJuryResult = "优秀"
    var stuJuryRemark = ""


    private var player: ExoPlayer? = null
    private val isPlaying get() = player?.isPlaying ?: false


    private var fullBtn: ImageView? = null
    private var playBtn: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var fullScreen = false

    private var oriParams: ConstraintLayout.LayoutParams? = null


    private val resultPicker: OptionsPickerBuilder by lazy {
        OptionsPickerBuilder(
            this
        ) { options1, _, _, _ ->
            "picker check $options1".logw()
            stuJuryResult = juryResultList[options1]
            mDatabind.stuScoreTvContent.text = stuJuryResult
        }
    }

    private val quickPicker: OptionsPickerBuilder by lazy {
        OptionsPickerBuilder(
            this
        ) { options1, _, _, _ ->
            "picker check $options1".logw()
            stuJuryRemark = mViewModel.templateContents[options1]
            mDatabind.stuFastScoreTvContent.text = stuJuryRemark
            mDatabind.stuScorePonitTvContent.setText(stuJuryRemark)
        }
    }


    override fun layoutId(): Int = R.layout.activity_online_exam_point

    override fun initView(savedInstanceState: Bundle?) {

//        mDatabind.titleTv.text = "考试"
        //状态页配置

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        intent?.let {
            stuId = it.getStringExtra("id") ?: ""
        }


        mDatabind.videoView.visibility = View.GONE

        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getStudentInfo(stuId)
        }

        mDatabind.stuScoreTvContent.setOnClickListener {
            //点击了评分部分
            resultPicker.apply {
                setCancelColor(Color.parseColor("#666666"))
                setSubmitColor(Color.parseColor("#999999"))
            }
            val bulider = resultPicker.build<String>()
            //dealPickList
            bulider.setPicker(juryResultList)
            bulider.show()
        }

        mDatabind.stuFastScoreTvContent.setOnClickListener {
            //点击了快捷评语
            if (mViewModel.templateContents.isNotEmpty()) {
                //如果存在快捷评语
                quickPicker.apply {
                    setCancelColor(Color.parseColor("#666666"))
                    setSubmitColor(Color.parseColor("#999999"))
                }
                val bulider = quickPicker.build<String>()
                //dealPickList
                bulider.setPicker(mViewModel.templateContents)
                bulider.show()
            }
        }

        mDatabind.submitScoreBtn.setOnClickListener {
            //提交评分
            mViewModel.submitUserGrade(stuJuryResult, stuJuryRemark, stuId)
        }


        loadsir.showLoading()
        mViewModel.getStudentInfo(stuId)
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
                Snackbar.make(mDatabind.submitScoreBtn,error.message.toString(),Snackbar.LENGTH_LONG).show()
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

    override fun createObserver() {
        mViewModel.stuInfoBean.observe(this) { result ->
            //获取到了考生的信息
            parseState(result, {
                //显示用户的信息
                loadsir.showSuccess()
                mDatabind.stuNameTvContent.text = it.RealName
                mDatabind.stuSexTvContent.text = it.Sex
                mDatabind.stuAgeTvContent.text = it.Age.toString()
                mDatabind.stuExamTypeTvContent.text = it.SubjectsStr

                val cover = Constant.imgUrlHead + it.StuImg1
                Glide.with(this).load(cover)
                    .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
                    .into(mDatabind.stuHeadImg)

                if (it.OnLineVideoUrl.canShow()){
                    //有线上视频
                    mDatabind.videoView.visibility = View.VISIBLE
                    initPlayer(Constant.imgUrlHead+it.OnLineVideoUrl)
                }else{
                    mDatabind.videoView.visibility = View.GONE
                }


            }, {
                loadsir.showError()
                Snackbar.make(mDatabind.submitScoreBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()

            })
        }

        mViewModel.stuGradeBean.observe(this) {
            //拿到当前的用户评分
            if (it.JuryResultStr.canShow()) {
                //如果有用户的评分
                stuJuryResult = it.JuryResultStr
                mDatabind.stuScoreTvContent.text = stuJuryResult
                mDatabind.stuScorePonitTvContent.setText(it.Remark)
            }
        }

        mViewModel.confirmOk.observe(this) {
            if (it != "ok") {
                Snackbar.make(mDatabind.submitScoreBtn, it, Snackbar.LENGTH_SHORT).show()
            } else {
                //提交成功，关闭页面
                Snackbar.make(mDatabind.submitScoreBtn, "提交成功", Snackbar.LENGTH_SHORT).show()
                finish()
            }
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