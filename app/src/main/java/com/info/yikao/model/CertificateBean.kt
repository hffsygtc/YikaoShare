package com.info.yikao.model

/**
 *
MyCertificateDetailDto {
SubjectsStr (string, optional): 科目信息 ,
CertificateNo (string, optional): 通过证书的编号 ,
CertificateImgUrl (string, optional): 电子证书图片地址 ,
PostProvinceId (integer, optional): 邮寄地址省份ID ,
PostProvince (string, optional): 邮寄地址省份 ,
PostCityId (integer, optional): 邮寄地址城市ID ,
PostCity (string, optional): 邮寄地址城市 ,
PostAreaId (integer, optional): 邮寄地址区县ID ,
PostArea (string, optional): 邮寄地址区县 ,
PostDetail (string, optional): 邮寄-详细地址 ,
PostName (string, optional): 邮寄姓名 ,
PostTel (string, optional): 邮寄电话
}
 */
data class CertificateBean(
    var SubjectsStr:Int,
    val CertificateNo: Int,
    val CertificateImgUrl: String,
    val PostProvinceId: Int,
    val PostProvince: String,
    val PostCityId: Int,
    val PostCity: String,
    val PostAreaId: String,
    val PostArea: String,
    val PostDetail: String,
    val PostName: String,
    val PostTel: String,
)