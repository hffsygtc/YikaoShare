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

    companion object {
        fun newInstance(fromUser: Boolean): PostIdUserInfoFragment {
            val args = Bundle()
            args.putBoolean("from_user", fromUser)
            val fragment = PostIdUserInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var fromUser = false


    override fun layoutId(): Int = R.layout.fragment_input_user_info

    override fun initView(savedInstanceState: Bundle?) {
        //初始化bundle数据
        arguments?.let {
            fromUser = it.getBoolean("from_user")
        }

        if (fromUser){
            mDatabind.nextBtn.text = "完成"
            mDatabind.titleBackBtn.setOnClickListener {
                activity?.finish()
            }
        }else{
            mDatabind.nextBtn.text = "下一步"
            mDatabind.titleBackBtn.setOnClickListener {
                nav().popBackStack()
            }
        }

    }


}