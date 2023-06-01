package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 * 报考介绍
 */
class MajorIntroViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<MajorIntroWrapper>> = MutableLiveData()

    var price = ""
    var subject = ""
    var time = ""


    fun getListData(isRefresh: Boolean, id: Int) {

        request({ apiService.getMajorDetail(id) }, {
            price = it.Price.toString() + "元"
            subject = it.TestContent
            time = "${it.TestStart}~${it.TestEnd}"
            val datas = arrayListOf(
                MajorIntroWrapper(
                    1,
                    MajorTimeInfo(
                        "${it.ApplyStart}~${it.ApplyEnd}",
                        "${it.TestStart}~${it.TestEnd}",
                        it.SubjectsName,
                        it.TestType
                    ),
                    null,
                    null
                ),
                MajorIntroWrapper(
                    2,
                    null,
                    ExamContent("科目1", it.TestContent, true, "练习曲一首，自备钢琴，无伴奏（演奏前请说明曲目内..."),
                    null,
                    isHead = true
                ),
//                MajorIntroWrapper(
//                    2,
//                    null,
//                    ExamContent("科目2", "钢琴演奏1", true, "练习曲一首，自备钢琴，无伴奏（演奏前请说明曲目内..."),
//                    null,
//                    isHead = false
//                ),
                MajorIntroWrapper(
                    3,
                    null,
                    null,
                    SectionInfo(
                        "报名须知:", "1、报名费为${it.Price}元，考试费${it.Price}元(考试费由学校收取)\n" +
                                "2、报名成功后，报名考试费不予以退还;\n" +
                                "3、报名后，招生办老师会微信或电话联系考生，发送材料和作品提交要求，请保持电话畅通，注意接收电话或信息。"
                    )
                ),
                MajorIntroWrapper(
                    3,
                    null,
                    null,
                    SectionInfo(
                        "报考要求:", "1、考生及父母均为中国国籍;\n" +
                                "2、应、往届高中毕业生及同等学历者;\n" +
                                "3、身心健康，无传染性疾病者。"
                    )
                ),
                MajorIntroWrapper(
                    3,
                    null,
                    null,
                    SectionInfo(
                        "考生须知:", "1、合作院校为圣彼得堡国立大学，俄罗斯排名第二，普京总统母校。\n" +
                                "2、开设艺术类专业:新闻学、艺术史、戏剧和电影演员管风琴音乐演奏、大提琴音乐演奏、钟琴音乐演奏、小提琴演奏、学院派歌唱、平面设计、设计环境、美术和装饰艺术品修复、绘画、影视服装设计等。\n" +
                                "3、学费:约25万卢布/年(人民币25万元左右)"
                    )
                ),
                MajorIntroWrapper(
                    3,
                    null,
                    null,
                    SectionInfo("录取规则:", "以各省、市、自治区教育主管部门公布及我校2023年招生简章为准")
                ),
            )

            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = datas.isEmpty(),
                    listData = datas,
                    hasMore = false
                )

            listData.value = listDataUiState


        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    isEmpty = true,
                    listData = arrayListOf<MajorIntroWrapper>(),
                    hasMore = false
                )

            listData.value = listDataUiState
        })

    }

}