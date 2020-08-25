package com.camelot.openplatform.common.sms;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * 发送短信 ，返回的状态码以及对应的状态信息
 * 
 * @author 王东晓
 *
 */
public class SendSmsResult {
	public static Map<String,String> resultMap = new HashMap<String,String>();
	static{
		resultMap.put("-1", "参数为空。信息、电话号码等有空指针，登录失败");
		resultMap.put("-12", "有异常电话号码");
		resultMap.put("-14", "实际号码个数超过100");
		resultMap.put("-999", "服务器内部错误");
		resultMap.put("-10001", "用户登录不成功(帐号不存在/停用/密码错误)");
		resultMap.put("-10003", "用户余额不足");
		resultMap.put("-10011", "信息内容超长");
		resultMap.put("-10029", "此用户没有权限从此通道发送信息(用户没有绑定该性质的通道，比如：用户发了小灵通的号码)");
		resultMap.put("-10030", "不能发送移动号码");
		resultMap.put("-10031", "手机号码(段)非法");
		resultMap.put("-10057", "IP受限");
		resultMap.put("-10056", "连接数超限");
	}
	
	public static Map<String,String> resultMapGreen = new HashMap<String,String>();
	static{
		resultMapGreen.put("0", "成功");
		resultMapGreen.put("-2", "帐号/密码不正确");
		resultMapGreen.put("-4", "余额不足支持本次发送");
		resultMapGreen.put("-5", "数据格式错误");
		resultMapGreen.put("-6", "参数有误");
		resultMapGreen.put("-7", "权限受限");
		resultMapGreen.put("-8", "流量控制错误");
		resultMapGreen.put("-9", "扩展码权限错误");
		resultMapGreen.put("-10", "内容长度长");
		resultMapGreen.put("-11", "内部数据库错误");
		resultMapGreen.put("-12", "序列号状态错误");
		resultMapGreen.put("-14", "服务器写文件失败");
		resultMapGreen.put("-17", "没有权限");
		resultMapGreen.put("-22/-21", "Ip鉴权失败");
		resultMapGreen.put("-23", "缓存无此序列号信息");
		resultMapGreen.put("-601", "序列号为空，参数错误");
		resultMapGreen.put("-602", "序列号格式错误，参数错误");
		resultMapGreen.put("-603", "密码为空，参数错误");
		resultMapGreen.put("-604", "手机号码为空，参数错误");
		resultMapGreen.put("-605", "内容为空，参数错误");
		resultMapGreen.put("-606", "ext长度大于9，参数错误");
		resultMapGreen.put("-607", "参数错误 扩展码非数字");
		resultMapGreen.put("-608", "参数错误 定时时间非日期格式");
		resultMapGreen.put("-609", "rrid长度大于18,参数错误");
		resultMapGreen.put("-610", "参数错误 rrid非数字");
		resultMapGreen.put("-611", "参数错误 内容编码不符合规范");
		resultMapGreen.put("-623", "手机个数与内容个数不匹配");
		resultMapGreen.put("-624", "扩展个数与手机个数不匹配");
		resultMapGreen.put("-625", "定时时间个数与手机个数不匹配");
		resultMapGreen.put("-626", "Rrid个数与手机号个数不一致");
	}
	
	public static Map<String,String> resultMapShushi100 = new HashMap<String,String>();
	static{
		resultMapShushi100.put("0", "成功");
		resultMapShushi100.put("-1", "系统异常");
		resultMapShushi100.put("-2", "客户端异常");
		resultMapShushi100.put("-101", "命令不被支持");
		resultMapShushi100.put("-102", "RegistryTransInfo删除信息失败");
		resultMapShushi100.put("-103", "RegistryInfo更新信息失败");
		resultMapShushi100.put("-104", "请求超过限制");
		resultMapShushi100.put("-110", "号码注册激活失败");
		resultMapShushi100.put("-111", "企业注册失败");
		resultMapShushi100.put("-113", "充值失败");
		resultMapShushi100.put("-117", "发送短信失败");
		resultMapShushi100.put("-118", "接收MO失败");
		resultMapShushi100.put("-119", "接收Report失败");
		resultMapShushi100.put("-120", "修改密码失败");
		resultMapShushi100.put("-122", "号码注销激活失败");
		resultMapShushi100.put("-123", "查询单价失败");
		resultMapShushi100.put("-124", "查询余额失败");
		resultMapShushi100.put("-125", "设置MO转发失败");
		resultMapShushi100.put("-126", "路由信息失败");
		resultMapShushi100.put("-127", "计费失败0余额");
		resultMapShushi100.put("-128", "计费失败余额不足");
		resultMapShushi100.put("-190", "数据操作失败");
		resultMapShushi100.put("-1100", "序列号错误,序列号不存在内存中,或尝试攻击的用户");
		resultMapShushi100.put("-1102", "序列号密码错误");
		resultMapShushi100.put("-1103", "序列号Key错误");
		resultMapShushi100.put("-1104", "路由失败，请联系系统管理员");
		resultMapShushi100.put("-1105", "注册号状态异常, 未用");
		resultMapShushi100.put("-1107", "注册号状态异常, 停用");
		resultMapShushi100.put("-1108", "注册号状态异常, 停止");
		resultMapShushi100.put("-1131", "充值卡无效");
		resultMapShushi100.put("-1132", "充值密码无效");
		resultMapShushi100.put("-1133", "充值卡绑定异常");
		resultMapShushi100.put("-1134", "充值状态无效");
		resultMapShushi100.put("-1135", "充值金额无效");
		resultMapShushi100.put("-1901", "数据库插入操作失败");
		resultMapShushi100.put("-1902", "数据库更新操作失败");
		resultMapShushi100.put("-1903", "数据库删除操作失败");
		resultMapShushi100.put("-9000", "数据格式错误,数据超出数据库允许范围");
		resultMapShushi100.put("-9001", "序列号格式错误");
		resultMapShushi100.put("-9002", "密码格式错误");
		resultMapShushi100.put("-9003", "客户端Key格式错误");
		resultMapShushi100.put("-9004", "设置转发格式错误");
		resultMapShushi100.put("-9005", "公司地址格式错误");
		resultMapShushi100.put("-9006", "企业中文名格式错误");
		resultMapShushi100.put("-9007", "企业中文名简称格式错误");
		resultMapShushi100.put("-9008", "邮件地址格式错误");
		resultMapShushi100.put("-9009", "企业英文名格式错误");
		resultMapShushi100.put("-9010", "企业英文名简称格式错误");
		resultMapShushi100.put("-9011", "传真格式错误");
		resultMapShushi100.put("-9012", "联系人格式错误");
		resultMapShushi100.put("-9013", "联系电话");
		resultMapShushi100.put("-9014", "邮编格式错误");
		resultMapShushi100.put("-9015", "新密码格式错误");
		resultMapShushi100.put("-9016", "发送短信包大小超出范围");
		resultMapShushi100.put("-9017", "发送短信内容格式错误");
		resultMapShushi100.put("-9018", "发送短信扩展号格式错误");
		resultMapShushi100.put("-9019", "发送短信优先级格式错误");
		resultMapShushi100.put("-9020", "发送短信手机号格式错误");
		resultMapShushi100.put("-9021", "发送短信定时时间格式错误");
		resultMapShushi100.put("-9022", "发送短信唯一序列值错误");
		resultMapShushi100.put("-9023", "充值卡号格式错误");
		resultMapShushi100.put("-9024", "充值密码格式错误");
		resultMapShushi100.put("-9025", "客户端请求sdk5超时");
	}
}
