package com.camelot.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * userDto的一个工具类
 * 
 * @author fans
 *
 */
public class UserDtoUtils {

	/**
	 * 隐藏手机号中间四位
	 * @param cellPhone 手机号
	 * @return
	 */
	public static String hideUserCellPhone(String cellPhone)
	{
		if(StringUtils.isNotBlank(cellPhone) && cellPhone.trim().length() > 7)
		{
			//隐藏手机号码中间四位
			StringBuilder temp = new StringBuilder().append(cellPhone.substring(0, 3)).append("****").append(cellPhone.substring(7));
			cellPhone = temp.toString();
		}
		return cellPhone;
	}
	
	
	/**
	 * 隐藏邮箱中间四位
	 * @param email
	 * @return
	 */
	public static String hideUserEmail(String email)
	{
		if(StringUtils.isNotBlank(email))
		{
			//Email去掉@后，剩下的字符串
			String emailTemp = email.substring(0, email.indexOf("@"));
			if(emailTemp.length() > 6)
			{
				//拼接隐藏用户中间四位的邮箱，格式：邮箱前（一半 -2）个字符 + "****"(4个*) + 邮箱（一半 + 2）后的字符。
				//邮箱前（一半 -2）个字符
				StringBuilder temp = new StringBuilder(emailTemp.substring(0, (emailTemp.length()-4) / 2));
				//"****"(4个*)
				temp.append("****");
				//邮箱（一半 + 2）后的字符
				temp.append(email.substring((emailTemp.length()-4) / 2 + 4));
				email = temp.toString();
			}else
			{
				StringBuilder temp = new StringBuilder(email.charAt(0) + "");
				for(int i =0; i < emailTemp.length() -2; i ++)
				{
					temp.append("*");
				}
				temp.append(email.substring(emailTemp.length() - 1)).toString();
				email = temp.toString();
			}
		}
		return email;
	}
	
}
