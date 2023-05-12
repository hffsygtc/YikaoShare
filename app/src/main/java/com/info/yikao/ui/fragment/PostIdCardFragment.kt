package com.info.yikao.ui.fragment

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentIdCardPhotoBinding
import com.info.yikao.viewmodel.PostIdCardViewModel
import me.hgj.jetpackmvvm.ext.nav

class PostIdCardFragment : BaseFragment<PostIdCardViewModel, FragmentIdCardPhotoBinding>() {

    override fun layoutId(): Int = R.layout.fragment_id_card_photo

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.nextBtn.setOnClickListener {
            nav().navigate(R.id.action_postIdCardFragment_to_postIdUserInfoFragment)
        }


    }


}