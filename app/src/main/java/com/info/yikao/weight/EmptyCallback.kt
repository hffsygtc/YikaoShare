package com.info.yikao.weight

import com.info.yikao.R
import com.kingja.loadsir.callback.Callback

class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }
}