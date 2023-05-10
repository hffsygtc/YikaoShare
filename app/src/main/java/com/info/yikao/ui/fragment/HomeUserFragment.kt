package com.info.yikao.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainUserBinding
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.ui.activity.*
import com.info.yikao.viewmodel.HomeUserViewModel
import me.hgj.jetpackmvvm.ext.util.loge

class HomeUserFragment : BaseFragment<HomeUserViewModel, FragmentMainUserBinding>() {

    private var isLogin = false

    override fun layoutId(): Int = R.layout.fragment_main_user

    override fun initView(savedInstanceState: Bundle?) {

//        if (userViewModel)
        mViewModel.initUser()

        showUserState()

        mDatabind.userHeadLayout.setOnClickListener {
            if (!isLogin) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }

        mDatabind.userEditLayout.setOnClickListener {
            if (isLogin){
                //编辑资料
            }
        }

        mDatabind.userFuncInfo.setOnClickListener {
            //考生信息
            val intent = Intent(requireActivity(),FragmentContainerActivity::class.java)
            intent.putExtra("type",1)
            startActivity(intent)
        }

        mDatabind.userFuncExam.setOnClickListener {
            //我的考试
            val intent = Intent(requireActivity(),UserExamActivity::class.java)
            startActivity(intent)
        }

        mDatabind.userFuncOrder.setOnClickListener {
            //报考订单
            startActivity(Intent(requireActivity(), UserOrderActivity::class.java))
        }

        mDatabind.userFuncSetting.setOnClickListener {
            //设置
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        mDatabind.teacherOnlineFunc.setOnClickListener {
            //线上监考
        }

        mDatabind.teacherOfflineFunc.setOnClickListener {
            //线下监考
        }

        mDatabind.teacherSettingFunc.setOnClickListener {
            //设置
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }




    }


    /**
     * 显示用户的登录信息
     */
    private fun showUserState() {
        val user = mViewModel.userInfo
        if (user != null && user.Token != "") {
            //有登录信息
            isLogin = true
            val userHead = Constant.imgUrlHead + user.HeadImg
            Glide.with(requireContext()).load(userHead)
                .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_USER_HEAD))
                .into(mDatabind.userHead)
            mDatabind.userName.text = user.NickName
            mDatabind.userDesc.text = user.Tel
            mDatabind.userEditBtn.setBackgroundResource(R.drawable.user_edit)

            //判断是学生还是教师
            when (user.MemberType) {
                1, 2, 3 -> {
                    //评委
                    mDatabind.studentFuncLayout.visibility = View.GONE
                    mDatabind.teacherFuncLayout.visibility = View.VISIBLE
                }
                else -> {
                    //统一当学生处理
                    mDatabind.studentFuncLayout.visibility = View.VISIBLE
                    mDatabind.teacherFuncLayout.visibility = View.GONE
                }
            }
        } else {
            isLogin = false
            //没有登录信息
            Glide.with(requireContext()).load("")
                .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_USER_HEAD))
                .into(mDatabind.userHead)
            mDatabind.userName.text = "未登录"
            mDatabind.userDesc.text = "请点击头像登录"
            mDatabind.userEditBtn.setBackgroundResource(R.drawable.user_un_edit)

            //todo 老师端的打开
            mDatabind.teacherFuncLayout.visibility = View.VISIBLE
        }
    }


    override fun createObserver() {
        appViewModel.userInfo.observeInFragment(this) {
            //监听用户登录信息变化
            mViewModel.userInfo = it
            showUserState()
        }
    }
}