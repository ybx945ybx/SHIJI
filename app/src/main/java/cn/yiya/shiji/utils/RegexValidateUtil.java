package cn.yiya.shiji.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则表达式进行表单验证
 * 
 */
public class RegexValidateUtil {
	static boolean flag = false;
	static String regex = "";

	public static boolean check(String str, String regex) {
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证非空
	 * 
//	 * @param email
	 * @return
	 */
	public static boolean checkNotEmputy(String notEmputy) {
		regex = "^\\s*$";
		return check(notEmputy, regex) ? false : true;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String regex = "^\\w+[-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$ ";
		return check(email, regex);
	}

	/**
	 * 验证手机号码
	 * 
	 * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
	 * 联通号码段:130、131、132、136、185、186、145 
	 * 电信号码段:133、153、180、189、170
//	 * @param cellphone
	 * @return
	 */
//	public static boolean checkCellphone(String cellphone) {
//		String regex = "^((170)|(13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8}$";
//		return check(cellphone, regex);
//	}
	public static boolean isMobileNO(String mobiles) {
//		Pattern pMobile = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$");
//		Pattern pCM = Pattern.compile("(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)");
//		Pattern pCU = Pattern.compile("(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)");
//		Pattern pCT = Pattern.compile("(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)");
		Pattern pMobile = Pattern.compile("^[1][34578]\\d{9}$");
		if(pMobile.matcher(mobiles).matches()){
			return true;
		}
		return false;
	}

	/**
	 * 验证固话号码
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean checkTelephone(String telephone) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		return check(telephone, regex);
	}

	/**
	 * 验证传真号码
	 * 
	 * @param fax
	 * @return
	 */
	public static boolean checkFax(String fax) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		return check(fax, regex);
	}

	/**
	 * 验证QQ号码
	 * 
	 * @param QQ
	 * @return
	 */
	public static boolean checkQQ(String QQ) {
		String regex = "^[1-9][0-9]{4,} $";
		return check(QQ, regex);
	}

	/**
	 * 验证26字母
	 *
	 * @param letter
	 * @return
	 */
	public static boolean checkLetter(String letter) {
		String regex = "^[A-Za-z]+$";
		return check(letter, regex);
	}
}