package cn.suxiangbao.sopark.http;

import cn.suxiangbao.sopark.host.HostInfoHelper;
import cn.suxiangbao.sopark.user.UserInfo;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 所有控制器基类，用于写一些共用方法
 * 
 * @author jiangchengyan@yy.com
 */
public abstract class BaseServletUtil {

    private static Logger logger = LoggerFactory.getLogger(BaseServletUtil.class);
    private static Logger performanceLogger = LoggerFactory.getLogger("PERFORMANCELOG");

    public final static String HEADER_X_AUTHORIZATION = "X-Authorization";
    public final static String HEADER_X_WEB_AUTHORIZATION = "X-Web-Authorization";
    public final static String HEADER_X_CLIENT = "X-Client";
    public final static String HEADER_X_CLIENT_VER = "X-Client-Ver";
    public final static String HEADER_X_CLIENT_NET = "X-Client-Net";
    public final static String HEADER_X_LOCALE = "X-Locale";
    public final static String HEADER_X_PUSH_ID = "X-PushId";
    public final static String HEADER_X_DEVICE_ID = "X-DeviceId";
    public final static String HEADER_X_CLIENT_UID = "X-Client-Uid";

    // Request Attribute
    public static final String REQ_ATTR_UID = "X-Uid";
    public static final String REQ_ATTR_USER_INFO = "X-UserInfo";
    public static final String REQ_ATTR_TOKEN_TIMESTAMP = "X-TokenTimeStamp";
    public static final String REQ_ATTR_TOKEN_VERIFIED = "X-TokenVerified";
    public static final String REQ_ATTR_CHANGE_PSWD = "X-ChangePswd";
    public static final String REQ_ATTR_UAAS_USERSOURCE = "X-UaasUserSource";
    public static final String REQ_ATTR_UAAS_ACCOUNT = "X-UaasAccount";
    public static final String REQ_ATTR_NANO_TIMESTAMP = "X-NanoTimeStamp";
    public static final String REQ_ATTR_RELEASE_THROTTING_FLAG = "X-ReleaseThrottingFlag";
    public static final String REQ_ATTR_TOKEN_VERIFIED_VALUE = "verified_bind";
    public static final String REQ_ATTR_HEADER = "X-ReqHeader";

    public static final String UID = "SessionUid";

    public static long DEFAUTL_LONG = -1;
    public static int DEFAUTL_INT = -1;
    public static byte DEFAUTL_BYTE = -1;
    public static boolean DEFAUTL_BOOLEAN = false;
    public static String DEFAUTL_EMPTY_STRING = "";

    public static int NOT_AUTH = 401;
    public static int VERSION_TOO_OLD = 505;// 代表这个app版本太旧了（重新打开app时的validate接口）
    public static int NOT_ALLOW = 402;
    public static int NOT_EXIST = 404;// 资源不存在（如Feed不存在）
    public static int SUCCESS = 1;
    public static int FAILED = 0;
    public static int USER_NOT_EXIST = -1;// /app/validate、/user/get接口
    public static int USER_BANED = -8;// 所有接口都会返回，特别是/app/loginReport、/app/validate接口，用户被封禁（返回的msg字段会有封禁的结果，如“您因连续违规被封号24小时”，弹出提示用户）

    public static int THEMEDAY_TOPIC_EXIST = -1;
    public static int COLUMN_TOPIC_EXIST = -2;
    public static int USERNAME_EXISTED = -2;// /user/addOrUpdate和/user/checkUsername接口
    public static int USER_SHOULD_LOGOUT = -3; // /app/validate接口
    public static int SENSITIVE_WORDS = -4;// /user/addOrUpdate接口，用户输入包含敏感词
    public static int USERNAME_NOT_LEGAL = -5;// /user/addOrUpdate接口，用户输入的ID包含非法字符
    public static int LS_STOP = -6;// /liveShow/*接口，直播已结束
    public static int LS_USER_NO_IN = -7;// /liveShow/link接口，用户不在直播间
    public static int EXISTS = -9; // 资源已存在。 /admin/popup/save
    public static int VERIFY_FAILED = -10; // /pay/getAccount 绑定提现账户验证token失败
    public static int PAY_ACCOUNT_NOT_BINDING = -11; // /pay/withdraw 未绑定提现账户
    public static int NOT_WITHDRAW_TIME = -12; // /pay/withdraw 提现日期限制
    public static int WITHDRAW_COUNT_LIMIT = -13; // /pay/withdraw 提现次数限制
    public static int WITHDRAW_LESS = -14; // /pay/withdraw 账户余额不足10元
    public static int BALANCE_NOT_ENOUGH = -15; // /pay/withdraw 账户余额小于提现金额
    public static int USER_SHOULD_KICK = -16; // guestEnter、guestShake接口，需要踢人
    public static int INVALID_ID_CARD = -17; // /pay/saveAccount 身份证号码不合法
    public static int INVALID_PAY_ACCOUNT = -18; // /pay/saveAccount 提现账户不合法
    public static int NOT_ALLOW_HANDS_UP = -19; // /liveShow/handsUp 没有举手资格
    public static int PASSWORD_HAS_CHANGED = -20;// 用户在其他地方已经更改了密码
    public static int LIVE_NOW = -21;// 用户正在直播中
    public static int NOT_ALLOW_LINK = -22; // 没有连麦资格
    public static int MUTE_NOT_ANCHOR = -23; //非主播去禁言
    public static int MIBI_BALANCE_NOT_ENOUGH = -25; // M币余额不足
    public static int DEVICE_BANED = -26;// /app/checkDevice接口返回，设备被封禁（返回的msg字段会有封禁的结果，如“您因连续违规被封设备24小时”，弹出提示用户）
    public static int LS_START_BANED = -27;// /liveShow/add, /liveShow/start接口返回，用户开播权限被封禁
    public static int LS_START_NOT_IN_24_HOUR_SCHEDULE = -28;// /liveShow/start接口返回，用户勾选24小时官方开播，但没在计划时间内
    public static int LS_START_IN_24_HOUR_SCHEDULE = -29;// /liveShow/start接口返回，用户没勾选24小时官方开播，但在计划时间内
    public static int LS_START_24_HOUR_CAN_NOT_START_LIVESHOW_IN_APP = -30;// 24小时官方号不能在app开播
    public static int LS_START_24_HOUR_ANCHOR_IN_GF_SCHEDULE_BUT_GF_NOT_START = -31;// 24小时官方号没有开启直播间，但主播在他的排班中
    public static int LS_MSG_ERROR_CODE = -32;// /liveShow/add ,/liveShow/start,/liveShow/guestEnter的错误码。客户端显示服务器返回的错误消息
    public static int ANCHOR_NOT_AUTH = -33;// 用户开播未实名认证
    public static int ANCHOR_AUTH_REVIEWING = -34;// 用户开播实名认证审核中
    public static int ANCHOR_AUTH_FAIL = -35;// 用户开播实名认证未通过
    public static int ANCHOR_AUTH_PHONE_BIND_EXCEED_LIMIT = -36;// 用户开播实名认证手机绑定账号已达上限
    public static int ANCHOR_AUTH_CAPTCHA_SEND_EXCEED_LIMIT = -37;// 用户开播实名认证发送验证码次数已达上限
    public static int ANCHOR_AUTH_IDCARD_BIND_EXCEED_LIMIT = -38;// 用户开播实名认证身份证绑定次数已达上限
    public static int ANCHOR_AUTH_CAPTCHA_VERIFY_FAIL = -39;// 用户开播实名认证验证码验证失败
    public static int ANCHOR_AUTH_ALREADY_BIND_PHONE = -40;// 用户开播实名认证已绑定手机
    public static int ANCHOR_AUTH_ALREADY_SUBMIT = -41;// 用户开播实名认证已提交
    public static int NOT_YOUR_OWNED_LS = -42;// 不属于主播的直播间
    public static int IN_SHAKE_LINK_STATE = -43;// 路人处于摇一摇状态
    public static int SHAKE_PAIR_NOT_EXIST_STATE = -44;// 没有可用的匹配
    public static int HANDS_UP_NOT_ZERO = -45;// 摇一摇请求下，举手人数不为0

    public static int EXCHANGE_EXCEEED = -23; // 兑换超过上限
    public static int GIFT_COMBO_UNSUPPORTED = -24; // 礼物不支持连送
    
    public static int IS_NOT_WEEKEND=-46;//抢班接口 时间不是周六周日
    public static int PARAM_ERROR=-47;//抢班接口 参数错误
    public static int PARAM_DAY_STR_FORMAT_ERROR=-48;//抢班日期格式错误
    public static int PARAM_BUCID_ERROR=-49;//抢班接口 buckid错误
    public static int GRABING_EXCEPTION=-50;//抢班接口 抢班异常
    public static int PARAM_NOW_BIGGER_DAYTIME=-51; //改班接口，日期天数小于今天
    public static int LS_FULL=-52; //直播间人数已满
    public static int IS_NOT_ARRANGED=-53; //不是排班主播
    public static int GRABING_HAS_FAILE_PEOPLE_FULL=-54;//抢班失败，人数满了
    public static final String serverIp = HostInfoHelper.getHostIp();

    private static ThreadLocal<ObjectMapper> objMapperLocal = new ThreadLocal<>();

    private static FastDateFormat dateFormatter4Log = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss,SSS");
    
    private static List<String> allowedDomain = Lists.newArrayList("yy.com", "mezhibo.com");

    public static ObjectMapper getLocalObjMapper() {
        ObjectMapper objMapperTmp = objMapperLocal.get();
        if (objMapperTmp == null) {
            objMapperTmp = new ObjectMapper();
            objMapperTmp.setSerializationInclusion(Include.NON_NULL);
            objMapperTmp.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            objMapperTmp.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objMapperLocal.set(objMapperTmp);
        }
        return objMapperTmp;
    }
    
    public static void main(String[] args) {
        ObjectMapper objMapperTmp = new ObjectMapper();
        objMapperTmp.setSerializationInclusion(Include.NON_NULL);
        objMapperTmp.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        try {
            System.out.println(objMapperTmp.writeValueAsString(objMapperTmp.valueToTree(Maps.newHashMap())));
            System.out.println(objMapperTmp.writeValueAsString(objMapperTmp.valueToTree(Lists.newArrayList())));
            System.out.println(objMapperTmp.writeValueAsString(objMapperTmp.valueToTree(BaseServletUtil.genMsgObj(1, null, Maps.newHashMap()))));
            System.out.println(objMapperTmp.writeValueAsString(objMapperTmp.valueToTree(BaseServletUtil.genMsgObj(1, null, Lists.newArrayList()))));

            Integer aaa = null;
            ObjectNode node = objMapperTmp.createObjectNode().put("aaa", aaa).put("ccc", "cat");
            System.out.println(objMapperTmp.writeValueAsString(node));
            
            // 某种怪怪的解决办法
            node.putObject("bbb").put("v1", aaa).putPOJO("v2", genMsgObj(1));
            MapType mapType = objMapperTmp.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
            Object mapValue = objMapperTmp.convertValue(node, mapType);
            System.out.println(objMapperTmp.writeValueAsString(mapValue));// TODO 为了解决{"aaa":null}这个问题，导致需要多一次转换（convertValue），蛋疼。。。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRequestPath(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (StringUtils.isBlank(path)) {
            path = StringUtils.EMPTY;
        }

        path = StringUtils.substringAfterLast(path, "/");

        return path;
    }

    public static final boolean isLogin(HttpServletRequest request) {
        return request.getAttribute(REQ_ATTR_UID) != null;
    }

    public static void login(HttpServletRequest request, long uid, Object userInfo) {
        request.setAttribute(REQ_ATTR_UID, uid);
        if (userInfo != null) {
            request.setAttribute(REQ_ATTR_USER_INFO, userInfo);
        }
    }

    public static Long getUid(ServletRequest request) {
        if (request == null) {
            return null;
        }
        Object ret = request.getAttribute(REQ_ATTR_UID);
        if (ret == null) {
            return null;
        }
        return (Long) ret;
    }

    public static UserInfo getUserInfo(ServletRequest request) {
        return (UserInfo) request.getAttribute(REQ_ATTR_USER_INFO);
    }
    
    public static String getUserSource(ServletRequest request) {
        String userSource = (String) ((HttpServletRequest) request).getAttribute(REQ_ATTR_UAAS_USERSOURCE);
        return userSource;
    }

    public static String getUserAccount(ServletRequest request) {
        String account = (String) ((HttpServletRequest) request).getAttribute(REQ_ATTR_UAAS_ACCOUNT);
        return account;
    }
    
    public static String legalize(String b4) {
        return legalize(b4, 40);
    }
    
    public static String legalize(String b4, int n) {
        if (b4 == null) {
            return null;
        }
        b4 = b4.trim();
        if (b4.length() > n) {
            b4 = b4.substring(0, n);
        }
        return b4;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getReqHeader(HttpServletRequest request) {
        if (request == null) {
            return new HashMap<>();
        }
        Map<String, Object> ret = (Map<String, Object>) request.getAttribute(REQ_ATTR_HEADER);
        if (ret == null) {
            ret= new HashMap<>();
            ret.put("transId", UUID.randomUUID().toString());
            ret.put("clientNet", legalize(request.getHeader(HEADER_X_CLIENT_NET)));
            ret.put("client", legalize(request.getHeader(HEADER_X_CLIENT)));
            ret.put("clientVer", legalize(request.getHeader(HEADER_X_CLIENT_VER)));
            ret.put("locale", legalize(request.getHeader(HEADER_X_LOCALE)));
            ret.put("reqUid", getUid(request));
            ret.put("pushId", legalize(request.getHeader(HEADER_X_PUSH_ID), 100));
            ret.put("routeMeta", Lists.newArrayList(legalize(HttpUtil.getRemoteIP(request)), serverIp == null ? "127.0.0.1" : serverIp));
            ret.put("deviceId", legalize(request.getHeader(HEADER_X_DEVICE_ID), 100));
            ret.put("webUri", legalize(request.getRequestURI()));
            request.setAttribute(REQ_ATTR_HEADER, ret);
        }
        return ret;
    }

    public static String formatClient(String client, String content) {
        return (client == null ? "null" : ("iOS".equalsIgnoreCase(client) ? "iOS" : "Android")) + "_" + content;
    }

    public static void setCookie(AsyncContext asyncContext, Long uid) {
        String token = ((HttpServletRequest) asyncContext.getRequest()).getHeader(HEADER_X_AUTHORIZATION);
        Cookie cookie = new Cookie(HEADER_X_AUTHORIZATION, token);
        cookie.setDomain(".mezhibo.com");
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setHttpOnly(true);
        ((HttpServletResponse) asyncContext.getResponse()).addCookie(cookie);
    }

    @SuppressWarnings("rawtypes")
    public static String queryMap2String(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuffer sb = new StringBuffer();
        for (Map.Entry entry : parameterMap.entrySet()) {
            sb.append((String) entry.getKey());
            for (String s : (String[]) entry.getValue()) {
                sb.append("=").append(s);
            }
            sb.append("&");
        }
        return sb.toString();
    }

    public static long getLong(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.equals("")) {
            return DEFAUTL_LONG;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return DEFAUTL_LONG;
    }

    public static byte getByte(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.equals("")) {
            return DEFAUTL_BYTE;
        }
        try {
            return Byte.parseByte(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return DEFAUTL_BYTE;
    }

    public static boolean getBoolean(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.equals("")) {
            return DEFAUTL_BOOLEAN;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return DEFAUTL_BOOLEAN;
    }

    public static int getInt(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.equals("")) {
            return DEFAUTL_INT;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return DEFAUTL_INT;
    }

    public static String getString(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.equals("")) {
            return DEFAUTL_EMPTY_STRING;
        }
        return value;
    }



    public static Locale getLocale(ServletRequest request) {
        String locale = ((HttpServletRequest) request).getHeader(HEADER_X_LOCALE);
        return LocaleUtil.genLocaleFromStr(locale);
    }

    public static Date getChangePswDate(ServletRequest request) {
        String timeMills = (String) (request).getAttribute(REQ_ATTR_CHANGE_PSWD);
        if (timeMills != null && !timeMills.equals("0")) {
            return new Date(Long.parseLong(timeMills));
        }
        return null;
    }

    public static String getLocaleStr(ServletRequest request) {
        String locale = ((HttpServletRequest) request).getHeader(HEADER_X_LOCALE);
        return LocaleUtil.genLocaleFromStr(locale).toString();
    }

    /**
     * 注意！result中的T不能为JsonElement子对象
     * 
     * @param request
     * @param response
     * @param result
     */
    public static void sendResponseAuto(ServletRequest request, ServletResponse response, RetMsgObj result) {
        if (request == null || response == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: {}", result);
            return;
        }
        
        double deltaTime = 0;
        logErrorResp(request, result);
        logDebugResp(request, result);
        String cbFunctionName = request.getParameter("callback");
        try {
            if (StringUtils.isNotBlank(cbFunctionName)) {
                sendResponseJsonpInner(request, response, cbFunctionName, result);
            } else {
                sendResponseInner(request, response, result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            String requestUri = ((HttpServletRequest) request).getRequestURI();
            requestUri = requestUri == null ? "null" : requestUri.replaceAll("/", "_");
            Object time = request.getAttribute(REQ_ATTR_NANO_TIMESTAMP);
            if (time instanceof Long) {
                deltaTime = (System.nanoTime() - (Long) time) / 1000000.0;
            }
            // 3秒超时日志
            if (deltaTime > 3000) {
                performanceLogger.warn(requestUri + "|" + deltaTime);
            }
        }
    }
    public static void sendResponse(ServletRequest request, ServletResponse response, RetMsgObj result) {
        if (request == null || response == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: {}", result);
            return;
        }
        
        double deltaTime = 0;
        logErrorResp(request, result);
        logDebugResp(request, result);
        try {
            sendResponseInner(request, response, result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            String requestUri = ((HttpServletRequest) request).getRequestURI();
            requestUri = requestUri == null ? "null" : requestUri.replaceAll("/", "_");
            Object time = request.getAttribute(REQ_ATTR_NANO_TIMESTAMP);
            if (time instanceof Long) {
                deltaTime = (System.nanoTime() - (Long) time) / 1000000.0;
            }
            // 3秒超时日志
            if (deltaTime > 3000) {
                performanceLogger.warn(requestUri + "|" + deltaTime);
            }
        }
    }
    
    public static void sendOuterResponse(ServletRequest request, ServletResponse response, Object result) {
        if (request == null || response == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: {}", result);
            return;
        }
        
        double deltaTime = 0;
        try {
            sendResponseInner(request, response, result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            String requestUri = ((HttpServletRequest) request).getRequestURI();
            requestUri = requestUri == null ? "null" : requestUri.replaceAll("/", "_");
            Object time = request.getAttribute(REQ_ATTR_NANO_TIMESTAMP);
            if (time instanceof Long) {
                deltaTime = (System.nanoTime() - (Long) time) / 1000000.0;
            }
            // 3秒超时日志
            if (deltaTime > 3000) {
                performanceLogger.warn(requestUri + "|" + deltaTime);
            }
        }
    }

    public static void send404Response(ServletResponse response) throws Exception {
        if (response == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: 404");
            return;
        }
        try {
            sendStatusCodeResponse(response, HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void send500Response(ServletResponse response) throws Exception {
        if (response == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: 500");
            return;
        }
        try {
            sendStatusCodeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void handle404AsyncOutput(AsyncContext context) {
        if (context == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: 404");
            return;
        }
        try {
            sendStatusCodeResponse(context.getResponse(), HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            context.complete();
        }
    }

    public static void handle500AsyncOutput(AsyncContext context) {
        if (context == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: 500");
            return;
        }
        try {
            sendStatusCodeResponse(context.getResponse(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            context.complete();
        }
    }

    public static void handleAsyncOutput(RetMsgObj ret, AsyncContext context) {
        try {
            if (context == null) {// 仅用于单元测试中调用service
                logger.debug("======Unit Test Ret: {}", ret);
                return;
            }
            sendResponseAuto(context.getRequest(), context.getResponse(), ret);
            context.complete();
        } catch (Exception e) {
            logError(context, e);
        }
    }
    
    public static RetMsgObj genMsgObj(int code) {
        return new ObjRetMsgObj(code, null, null);
    }
    
    public static RetMsgObj genMsgObj(int code, String msg) {
        return new ObjRetMsgObj(code, msg, null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static RetMsgObj genMsgObj(int code, String msg, Object data) {
        if (data instanceof Collection) {
            return new CollRetMsgObj<>(code, msg, (Collection) data);
        } else if (data instanceof Map) {
            return new MapRetMsgObj<>(code, msg, (Map) data);
        } else {
            return new ObjRetMsgObj(code, msg, data);
        }
    }

    public static class RetMsgObj {
        private int code;
        private String msg;
        private Integer size;

        public RetMsgObj(int code, String msg) {
            super();
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }
    }

    public static class ObjRetMsgObj extends RetMsgObj {
        private Object data;

        public ObjRetMsgObj(int code, String msg, Object data) {
            super(code, msg);
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }

    public static class CollRetMsgObj<T> extends RetMsgObj {
        private Collection<T> data;

        public CollRetMsgObj(int code, String msg, Collection<T> data) {
            super(code, msg);
            this.data = data;
        }

        public Collection<T> getData() {
            return data;
        }

        public void setData(Collection<T> data) {
            this.data = data;
        }

    }

    public static class MapRetMsgObj<K, V> extends RetMsgObj {
        private Map<K, V> data;

        public MapRetMsgObj(int code, String msg, Map<K, V> data) {
            super(code, msg);
            this.data = data;
        }

        public Map<K, V> getData() {
            return data;
        }

        public void setData(Map<K, V> data) {
            this.data = data;
        }

    }

    private static void logErrorResp(ServletRequest request, RetMsgObj result) {
        try {
            if (result.getCode() == FAILED) {
                logError(request, result.getMsg());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static String genTimeStrWithNano(Object timestampNano) {
        if (timestampNano instanceof Long && ((Long) timestampNano) > 0L) {
            return String.format("%.2fms", (System.nanoTime() - (Long) timestampNano) / 1000000.0);
        }
        return "";
    }

    private static void logError(AsyncContext context, Exception e) {
        logError(context.getRequest(), e.getMessage());
    }

    private static void logError(ServletRequest request, String msg) {
        try {
            msg = msg == null ? "" : msg;
            HttpServletRequest req = (HttpServletRequest) request;
            Object time = req.getAttribute(REQ_ATTR_NANO_TIMESTAMP);
            String costStr = genTimeStrWithNano(time);
            String pattern = "cost:{}ms, ip:{}, client:{}, ver:{}, locale:{}, host:{}, url:{}?{}, postData:{}, X-Authorization:{}, ret msg:{}";
            String postData = "POST".equalsIgnoreCase(req.getMethod()) ? getLocalObjMapper().writeValueAsString(req.getParameterMap()) : "";
            Object[] logData = new Object[] { costStr, HttpUtil.getRemoteIP(req), req.getHeader(HEADER_X_CLIENT), req.getHeader(HEADER_X_CLIENT_VER),
                    req.getHeader(HEADER_X_LOCALE), req.getServerName(), req.getRequestURI(), req.getQueryString(), postData,
                    req.getHeader(HEADER_X_AUTHORIZATION), msg };
            logger.warn("Error Response: \t" + pattern, logData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void logDebugResp(ServletRequest request, RetMsgObj result) {
        try {
            if (logger.isDebugEnabled()) {
                Object time = request.getAttribute(REQ_ATTR_NANO_TIMESTAMP);
                String str = genTimeStrWithNano(time);
                str += ",------" + dateFormatter4Log.format(new Date());
                Object uidObj = request.getAttribute(REQ_ATTR_UID);
                String uidStr = request.getParameter("uid");
                if (uidObj instanceof Long) {
                    long uid = (Long) uidObj;
                    str += "---" + uid;
                } else if (uidStr != null) {
                    str += "---" + uidStr + "[param]";
                }
                String logOut = getLocalObjMapper().writeValueAsString(result);
                if (logOut != null && logOut.length() > 1200) {
                    logOut = logOut.substring(0, 1200);
                }
                logger.debug("\n======" + ((HttpServletRequest) request).getRequestURI() + "(" + str + ")======"
                        + new String(logOut.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void sendStatusCodeResponse(ServletResponse response, int statusCode) throws Exception {
        if (response == null) {// 仅用于单元测试中调用service
            logger.debug("======Unit Test Ret: 404");
            return;
        }
        ((HttpServletResponse) response).setHeader("Keep-Alive", "180");
        try {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(statusCode);
            resp.getOutputStream().close();
        } catch (Exception e) {
            throw e;
        }
    }

    private static void sendResponseInner(ServletRequest request, ServletResponse response, Object result) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ((HttpServletResponse) response).setHeader("Keep-Alive", "180");
        
        // 处理cookie跨域
//        addCrossDomainHeader(request, response);
        
        try (ServletOutputStream stream = response.getOutputStream()) {
            getLocalObjMapper().writeValue(stream, result);// 减少内存copy
        } catch (Exception e) {
            throw e;
        }
    }

    private static void sendResponseJsonpInner(ServletRequest request, ServletResponse response, String callbackName, Object result)
            throws Exception {
        response.setContentType("application/javascript");
        response.setCharacterEncoding("UTF-8");
        ((HttpServletResponse) response).setHeader("Keep-Alive", "180");
        
        // 处理cookie跨域
        addCrossDomainHeader(request, response);
        
        try (ServletOutputStream stream = response.getOutputStream()) {
            stream.write(callbackName.getBytes("UTF-8"));
            stream.write("(".getBytes("UTF-8"));
            String str = getLocalObjMapper().writeValueAsString(result);
            stream.write(str.getBytes("UTF-8"));
            stream.write(")".getBytes("UTF-8"));
        } catch (Exception e) {
            throw e;
        }
    }
    
    private static void addCrossDomainHeader(ServletRequest request, ServletResponse response) {
        String origin = ((HttpServletRequest) request).getHeader("origin");
        String crosHeader = ((HttpServletRequest) request).getHeader("Access-Control-Request-Headers");

        if (StringUtils.isBlank(origin)) {
            return;
        }

        if (StringUtils.isBlank(crosHeader)) {
            crosHeader = "X-Custom-Header";
        }

        for (String domain : allowedDomain) {
            if (StringUtils.contains(origin, domain)) {
                ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", origin);
                ((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "true");
                ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                ((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", crosHeader);

                break;
            }
        }
    }
}
