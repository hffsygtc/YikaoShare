package com.info.yikao.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentIdCardPhotoBinding
import com.info.yikao.databinding.FragmentInputUserInfoBinding
import com.info.yikao.databinding.FragmentMainLoginBinding
import com.info.yikao.ui.activity.UserOrderActivity
import com.info.yikao.viewmodel.*
import me.hgj.jetpackmvvm.ext.nav

class PostIdUserInfoFragment : BaseFragment<PostIdInfoViewModel, FragmentInputUserInfoBinding>() {

    override fun layoutId(): Int = R.layout.fragment_input_user_info

    override fun initView(savedInstanceState: Bundle?) {


    }


}