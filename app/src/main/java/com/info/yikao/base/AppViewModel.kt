package  com.info.yikao.base

import com.info.yikao.ext.Constant
import com.info.yikao.model.UserDetailInfo
import com.info.yikao.model.UserInfo
import com.info.yikao.util.CacheUtil
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * app全局的viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会受到回调，也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息，APP的基本配置等
 */
class AppViewModel : BaseViewModel() {

    //app的用户信息
    var userInfo: UnPeekLiveData<UserInfo> =
        UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    var memberInfo: UnPeekLiveData<UserDetailInfo> =
        UnPeekLiveData.Builder<UserDetailInfo>().setAllowNullValue(true).create()

    var memberData = UserDetailInfo()

    init {
        //默认值保存的用户信息，没有登录则为null
        val user = CacheUtil.getUser()
        userInfo.postValue(user)
        "global token init $user".loge()
        Constant.userToken = user?.Token ?: ""

    }

}