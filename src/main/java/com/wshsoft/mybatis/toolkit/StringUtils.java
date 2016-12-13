package com.wshsoft.mybatis.toolkit;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wshsoft.mybatis.enums.SQLlikeType;

/**
 * <p>
 * String 工具类
 * </p>
 *
 * @author Carry xie
 * @Date 2016-08-18
 */
public class StringUtils {

	/**
	 * 空字符
	 */
	public static final String EMPTY = "";

	/**
	 * 下划线字符
	 */
	public static final char UNDERLINE = '_';

	/**
	 * 占位符
	 */
	public static final String PLACE_HOLDER = "{%s}";

	/**
	 * <p>
	 * 判断字符串是否为空
	 * </p>
	 *
	 * @param cs
	 *            需要判断字符串
	 * @return 判断结果
	 */
	public static boolean isEmpty(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 判断字符串是否不为空
	 * </p>
	 *
	 * @param cs
	 *            需要判断字符串
	 * @return 判断结果
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * <p>
	 * 字符串驼峰转下划线格式
	 * </p>
	 *
	 * @param param
	 *            需要转换的字符串
	 * @return 转换好的字符串
	 */
	public static String camelToUnderline(String param) {
		if (isEmpty(param)) {
			return EMPTY;
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append(UNDERLINE);
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * 字符串下划线转驼峰格式
	 * </p>
	 *
	 * @param param
	 *            需要转换的字符串
	 * @return 转换好的字符串
	 */
	public static String underlineToCamel(String param) {
		if (isEmpty(param)) {
			return EMPTY;
		}
		String temp = param.toLowerCase();
		int len = temp.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = temp.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(temp.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * 判断字符串是否为纯大写字母
	 * </p>
	 *
	 * @param str
	 *            要匹配的字符串
	 * @return
	 */
	public static boolean isUpperCase(String str) {
		return match("^[A-Z]+$", str);
	}

	/**
	 * <p>
	 * 正则表达式匹配
	 * </p>
	 *
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * <p>
	 * SQL 参数填充
	 * </p>
	 *
	 * @param content
	 *            填充内容
	 * @param args
	 *            填充参数
	 * @return
	 */
	public static String sqlArgsFill(String content, Object... args) {
		if (null == content) {
			return null;
		}
		if (args != null) {
			int length = args.length;
			if (length >= 1) {
				for (int i = 0; i < length; i++) {
					// 改造 String.replace() 用法
					content = Pattern.compile(String.format(PLACE_HOLDER, i), Pattern.LITERAL).matcher(content)
							.replaceAll(sqlParam(args[i]));
				}
			}
		}
		return content;
	}

	/**
	 * 获取SQL PARAMS字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String sqlParam(Object obj) {
		String repStr;
		if (obj instanceof Collection) {
			repStr = StringUtils.quotaMarkList((Collection<?>) obj);
		} else {
			repStr = StringUtils.quotaMark(obj);
		}
		return repStr;
	}

	/**
	 * <p>
	 * 使用单引号包含字符串
	 * </p>
	 *
	 * @param obj
	 *            原字符串
	 * @return 单引号包含的原字符串
	 */
	public static String quotaMark(Object obj) {
		String srcStr = String.valueOf(obj);
		if (obj instanceof String) {
			// fix #79
			return StringEscape.escapeString(srcStr);
		}
		return srcStr;
	}

	/**
	 * <p>
	 * 用%连接like
	 * </p>
	 *
	 * @param str
	 *            原字符串
	 * @return
	 */
	public static String concatLike(String str, SQLlikeType type) {
		switch (type) {
		case LEFT:
			str = "%" + str;
			break;
		case RIGHT:
			str += "%";
			break;
		default:
			str = "%" + str + "%";
		}
		return StringEscape.escapeString(str);
	}

	/**
	 * <p>
	 * 使用单引号包含字符串
	 * </p>
	 *
	 * @param coll
	 *            集合
	 * @return 单引号包含的原字符串的集合形式
	 */
	public static String quotaMarkList(Collection<?> coll) {
		StringBuilder sqlBuild = new StringBuilder();
		sqlBuild.append("(");
		int _size = coll.size();
		int i = 0;
		Iterator<?> iterator = coll.iterator();
		while (iterator.hasNext()) {
			String tempVal = StringUtils.quotaMark(iterator.next());
			if (i + 1 == _size) {
				sqlBuild.append(tempVal);
			} else {
				sqlBuild.append(tempVal);
				sqlBuild.append(",");
			}
			i++;
		}
		sqlBuild.append(")");
		return sqlBuild.toString();
	}

	/**
	 * <p>
	 * 拼接字符串第二个字符串第一个字母大写
	 * </p>
	 *
	 * @param concatStr
	 * @param str
	 * @return
	 */
	public static String concatCapitalize(String concatStr, final String str) {
		if (isEmpty(concatStr)) {
			concatStr = EMPTY;
		}
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		final char firstChar = str.charAt(0);
		if (Character.isTitleCase(firstChar)) {
			// already capitalized
			return str;
		}

		StringBuilder sb = new StringBuilder(strLen);
		sb.append(concatStr);
		sb.append(Character.toTitleCase(firstChar));
		sb.append(str.substring(1));
		return sb.toString();
	}

	/**
	 * <p>
	 * 字符串第一个字母大写
	 * </p>
	 *
	 * @param str
	 * @return
	 */
	public static String capitalize(final String str) {
		return concatCapitalize(null, str);
	}

	/**
	 * <p>
	 * 判断对象是否为空
	 * </p>
	 *
	 * @param object
	 * @return
	 */
	public static boolean checkValNotNull(Object object) {
		if (object instanceof CharSequence) {
			return isNotEmpty((CharSequence) object);
		}
		return object == null ? false : true;
	}

	/**
	 * <p>
	 * 判断对象是否为空
	 * </p>
	 *
	 * @param object
	 * @return
	 */
	public static boolean checkValNull(Object object) {
		return !checkValNotNull(object);
	}

	/**
	 * 判断对象是否为空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		if (str instanceof CharSequence) {
			return isEmpty((CharSequence) str);
		}
		return (str == null || "".equals(str));
	}

	/**
	 * 判断对象是否不为空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}

	// endsWith
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a String ends with a specified suffix.
	 * </p>
	 *
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.endsWith(null, null)      = true
	 * StringUtils.endsWith(null, "abcdef")  = false
	 * StringUtils.endsWith("def", null)     = false
	 * StringUtils.endsWith("def", "abcdef") = true
	 * StringUtils.endsWith("def", "ABCDEF") = false
	 * </pre>
	 *
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case
	 *         sensitive, or both <code>null</code>
	 * @since 2.4
	 */
	public static boolean endsWith(String str, String suffix) {
		return endsWith(str, suffix, false);
	}

	/**
	 * <p>
	 * Case insensitive check if a String ends with a specified suffix.
	 * </p>
	 *
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case
	 * insensitive.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.endsWithIgnoreCase(null, null)      = true
	 * StringUtils.endsWithIgnoreCase(null, "abcdef")  = false
	 * StringUtils.endsWithIgnoreCase("def", null)     = false
	 * StringUtils.endsWithIgnoreCase("def", "abcdef") = true
	 * StringUtils.endsWithIgnoreCase("def", "ABCDEF") = false
	 * </pre>
	 *
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case
	 *         insensitive, or both <code>null</code>
	 * @since 2.4
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return endsWith(str, suffix, true);
	}

	/**
	 * <p>
	 * Check if a String ends with a specified suffix (optionally case
	 * insensitive).
	 * </p>
	 *
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @param ignoreCase
	 *            inidicates whether the compare should ignore case (case
	 *            insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or both
	 *         <code>null</code>
	 */
	private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
		if (str == null || suffix == null) {
			return (str == null && suffix == null);
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
	}

}