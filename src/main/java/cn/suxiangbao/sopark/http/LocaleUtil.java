package cn.suxiangbao.sopark.http;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Created by wangke on 2016/11/9.
 * 不要把servlet相关代码放进来
 */
public class LocaleUtil {

    public static Locale genLocaleFromStr(String localStr) {
        if (StringUtils.isBlank(localStr)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        localStr = localStr.toLowerCase();
        if (localStr.startsWith("zh") && localStr.endsWith("cn")) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if (localStr.startsWith("zh") || localStr.endsWith("hk") || localStr.endsWith("tw") || localStr.endsWith("sg")) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return Locale.US;
    }

    public static Locale genLocale(Locale locale) {
        if (locale == null) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if (Locale.SIMPLIFIED_CHINESE.equals(locale) || Locale.TRADITIONAL_CHINESE.equals(locale) || Locale.US.equals(locale)) {
            return locale;
        }
        return Locale.US;
    }
}
