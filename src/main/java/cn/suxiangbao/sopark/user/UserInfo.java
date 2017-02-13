package cn.suxiangbao.sopark.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = -5726893096399842865L;

    public static class Fields {
        // MongoDB--UserInfo
        public static final String FIELD_U_OBJ_ID = "_id";
        public static final String FIELD_U_YEAR = "year";// 用户的出生年份
        public static final String FIELD_U_USERNAME = "username";
        public static final String FIELD_U_NEWUSER = "newUser";// 是否是3.0版本的新用户
        public static final String FIELD_U_NICK = "nick";
        public static final String FIELD_U_UID = "uid";
        public static final String FIELD_U_SIGN = "signature";
        public static final String FIELD_U_USER_SOURCE = "userSource";
        public static final String FIELD_U_ROLE = "role";
        public static final String FIELD_U_VERIFIED = "verified";
        public static final String FIELD_U_LAST_SHOW_TIME = "lastShowTime";
        public static final String FIELD_U_VERIFIED_REASON = "verifiedReason";
        public static final String FIELD_U_LIKE_COUNT = "likeCount";
        public static final String FIELD_U_FEED_COUNT = "feedCount";
        public static final String FIELD_U_MY_LOCALE = "myLocale";
        public static final String FIELD_U_HEADER_URL = "headerUrl";
        public static final String FIELD_U_BANED = "baned";
        public static final String FIELD_U_BANED_TYPE = "banedType";
        public static final String FIELD_U_BANED_END_TIME = "banedEndTime";
        public static final String FIELD_U_BANED_ACTION_TIME = "banedActionTime";
        public static final String FIELD_U_ANCHORTYPE = "anchorType";
        public static final String FIELD_U_RECOMMENDTYPE = "recommandType";
        public static final String FIELD_U_SEX = "sex";
        public static final String FIELD_U_REG_PUSH_ID = "regPushId";
        public static final String FIELD_U_LONGITUDE="longitude";
        public static final String FIELD_U_LATITUDE="latitude";
        public static final String FIELD_U_CLIENT_TYPE = "clientType";
        public static final String FIELD_U_CLIENT_VER = "clientVer";
        public static final String FIELD_U_CHANNEL = "channel";// 渠道信息
        public static final String FIELD_U_LAST_WATCH_LS = "lastWatchLs";
        public static final String FIELD_U_LAST_WATCH_LS_TIME = "lastWatchLsTime";
        public static final String FIELD_U_THIRD_PARTY_ID = "thirdPartyId";
        public static final String FIELD_U_CHANGE_PSW_TIME = "changePswTime";
        public static final String FIELD_U_CHANGE_NAME_COUNT = "changeNameCount";
        public static final String FIELD_U_CHANGE_SEX_COUNT = "changeSexCount";
        public static final String FIELD_U_PIN_YIN_NICK = "pinyinNick";
        public static final String FIELD_U_SHORT_PY_NICK = "shortPyNick";
        public static final String FIELD_U_HISTORY_TOTAL_GUEST_COUNT = "historyTotalGuestCount";
        public static final String FIELD_U_LASTACTIVETIME = "lastActiveTime";// 上次活跃的时间
        public static final String FIELD_U_ONLINESTATUS = "onlineStatus";// 在线状态
        public static final String FIELD_U_BLOCKED = "blocked";// 是否屏蔽了别人
        public static final String FIELD_U_CREATETIME="createTime";
        public static final String FIELD_U_UPDATETIME="updateTime";

        public static final String FIELD_U_MEDALIDLIST="medalIdList";
        public static final String FIELD_U_MEDALLIST="medalList";
        public static final String FIELD_U_TOPMEDAL="topMedal";


        public static final String FIELD_U_LIVED = "haveLived";
        public static final String FIELD_U_DISTANCE = "distance";// 距离自己的距离
        public static final String FIELD_U_INTEGRITY = "integrity";// 资料的完整度
        public static final String FIELD_U_FRIENDS = "friends";// 好友人数
        public static final String FIELD_U_ISFRIEND = "isFriend";// 是否是好友
        public static final String FIELD_U_LID = "lid";//
        public static final String FIELD_U_STARTINGNOW = "startingNow";//
        public static final String FIELD_U_SHOWFANSENTRANCE = "fansEntrance";// 是否展示
        public static final String FIELD_U_FRIENDSREQUEST = "friendsRequest";// 新的好友申请数量
        public static final String FIELD_U_FANSCOUNT = "fansCount";// 粉丝总数
        public static final String FIELD_U_IDOLCOUNT = "idolCount";// 关注人的总数
        public static final String FIELD_U_SHOW_ATTEDSHEET = "showAttendSheet";//
        public static final String FIELD_U_SHOW_GONGHUI = "showGonghui";//
        public static final String FIELD_U_ATTEDSHEEETURL = "showAttedSheetUrl";
        public static final String FIELD_U_GONGHUI_URL = "gongHuiUrl";
        public static final String FIELD_U_SHOW_ARRANGEENTRANCE = "showArrangeEntrance";// 展示排班入口
        public static final String FIELD_U_ARRANGEENTRANCE_URL = "arrangeEntranceUrl";
    }

    /**
     * 用户id Database column userInfo.uid
     *
     * @mbggenerated
     */
    private Long uid;
    /**
     * 用户名
     */

    private String username;
    /**
     * 昵称 Database column userInfo.nick
     *
     * @mbggenerated
     */
    private String nick;

    /**
     * 签名 Database column userInfo.signature
     *
     * @mbggenerated
     */
    private String signature;

    /**
     * 用户头像URL Database column userInfo.headerUrl
     *
     * @mbggenerated
     */
    private String headerUrl;

    /**
     * 性别 Database column userInfo.sex
     *
     * @mbggenerated
     */
    private Byte sex;

    /**
     * 星座 Database column userInfo.constellation
     *
     * @mbggenerated
     */
    private Byte constellation;
    /**
     * 特殊用户的角色 例如：星探1
     */
    private Integer role;

    /**
     * 否是实名认证 Database column userInfo.verified
     *
     * @mbggenerated
     */
    private Boolean verified;

    /**
     * 实名认证成功原因
     */
    private String verifiedReason;

    /**
     * 用户来源：手机、email、微信、微博、fb、tw等 Database column userInfo.userSource
     *
     * @mbggenerated
     */
    private Integer userSource;

    /**
     * 历史总观看人数
     */
    private Long historyTotalGuestCount;

    /**
     * 用户更改username次数
     */
    private Integer changeNameCount;
    /**
     * 用户更改sex次数
     */
    private Integer changeSexCount;

    /**
     * 是否是3.0用户的标识
     */
    private Boolean newUser;

    /**
     * 建创时间 Database column userInfo.createTime
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 最新更新时间 Database column userInfo.updateTime
     *
     * @mbggenerated
     */
    private Date updateTime;
    /**
     * 需改密码的最新时间
     */
    private Date changePswTime;

    /**
     * 用户的开放平台的id(使用手机号登录时为手机号)
     */
    private String thirdPartyId;

    /**
     * 存储我的Locale信息
     */
    private String myLocale;

    /**
     * nick的拼音
     */
    private String pinyinNick;

    /**
     * nick的拼音缩写
     */
    private String shortPyNick;

    /**
     * 此用户是否已经被封禁
     */
    private Boolean baned;
    /**
     * 被封禁类型
     */
    private String banedType;
    /**
     * 被封时间
     */
    private Date banedActionTime;
    /**
     * 解封时间
     */
    private Date banedEndTime;
    /**
     * 被封禁，违规的条目
     */
    private String banedItem;
    /**
     * 被封禁，违规的子条目
     */
    private String banedSubItem;
    /**
     * 被封禁，违规的描述
     */
    private String banedDesc;

    /**
     * 在用户注册时就知道该用户所在区域，并以此作为建议的_ttserver值
     */
    private Integer _ttserver;

    private Integer anchorType;

    /**
     * 主播的出生年份
     */
    private Integer year;

    /**
     * 居住地，省份
     */
    private String placeProvince;

    /**
     * 居住地，市
     */
    private String placeCity;

    /**
     * 家乡，省份
     */
    private String homeTownProvince;

    /**
     * 家乡，市
     */
    private String homeTownCity;

    /**
     * 行业
     */
    private String business;

    /**
     * 公司
     */
    private String company;

    /**
     * 兴趣
     */
    private String interest;

    /**
     * 最近活跃时间
     */
    private Date lastActiveTime;

    /**
     * 在线状态，0离线，1在线
     */
    private Integer onlineStatus;

    /**
     * 客户端类型，Android，iOS
     */
    private String clientType;

    /**
     * 客户端版本
     */
    private String clientVer;

    /**
     * PushId
     */
    private String regPushId;

    /**
     * 用户所在经度
     */
    private Double longitude;
    /**
     * 用户所在纬度
     */
    private Double latitude;

    /**
     * 最近直播时间
     */
    private Long lastShowTime;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

    /**
     * 最近登录IP
     */
    private String lastLoginIp;

    /**
     * 用户勋章id列表
     */
    private List<Integer> medalIdList;

    /**
     * 用户最高优先级勋章
     */
    private String topMedal;

    public List<Integer> getMedalIdList() {
        return medalIdList;
    }

    public void setMedalIdList(List<Integer> medalIdList) {
        this.medalIdList = medalIdList;
    }

    public String getTopMedal() {
        return topMedal;
    }

    public void setTopMedal(String topMedal) {
        this.topMedal = topMedal;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Long getLastShowTime() {
        return lastShowTime;
    }

    public void setLastShowTime(Long lastShowTime) {
        this.lastShowTime = lastShowTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getRegPushId() {
        return regPushId;
    }

    public void setRegPushId(String regPushId) {
        this.regPushId = regPushId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientVer() {
        return clientVer;
    }

    public void setClientVer(String clientVer) {
        this.clientVer = clientVer;
    }

    public String getPlaceProvince() {
        return placeProvince;
    }

    public void setPlaceProvince(String placeProvince) {
        this.placeProvince = placeProvince;
    }

    public String getPlaceCity() {
        return placeCity;
    }

    public void setPlaceCity(String placeCity) {
        this.placeCity = placeCity;
    }

    public String getHomeTownProvince() {
        return homeTownProvince;
    }

    public void setHomeTownProvince(String homeTownProvince) {
        this.homeTownProvince = homeTownProvince;
    }

    public String getHomeTownCity() {
        return homeTownCity;
    }

    public void setHomeTownCity(String homeTownCity) {
        this.homeTownCity = homeTownCity;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    /**
     * 获取 用户id Database column userInfo.uid
     *
     * @return 用户id
     *
     * @mbggenerated
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置 用户id Database column userInfo.uid
     *
     * @param uid
     *            用户id
     *
     * @mbggenerated
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取 昵称 Database column userInfo.nick
     *
     * @return 昵称
     *
     * @mbggenerated
     */
    public String getNick() {
        return nick;
    }

    /**
     * 设置 昵称 Database column userInfo.nick
     *
     * @param nick
     *            昵称
     *
     * @mbggenerated
     */
    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    /**
     * 获取 签名 Database column userInfo.signature
     *
     * @return 签名
     *
     * @mbggenerated
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 设置 签名 Database column userInfo.signature
     *
     * @param signature
     *            签名
     *
     * @mbggenerated
     */
    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    /**
     * 获取 用户头像URL Database column userInfo.headerUrl
     *
     * @return 用户头像URL
     *
     * @mbggenerated
     */
    public String getHeaderUrl() {
        return headerUrl;
    }

    /**
     * 设置 用户头像URL Database column userInfo.headerUrl
     *
     * @param headerUrl
     *            用户头像URL
     *
     * @mbggenerated
     */
    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl == null ? null : headerUrl.trim();
    }

    /**
     * 获取 性别 Database column userInfo.sex
     *
     * @return 性别
     *
     * @mbggenerated
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * 设置 性别 Database column userInfo.sex
     *
     * @param sex
     *            性别
     *
     * @mbggenerated
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * 获取 星座 Database column userInfo.constellation
     *
     * @return 星座
     *
     * @mbggenerated
     */
    public Byte getConstellation() {
        return constellation;
    }

    /**
     * 设置 星座 Database column userInfo.constellation
     *
     * @param constellation
     *            星座
     *
     * @mbggenerated
     */
    public void setConstellation(Byte constellation) {
        this.constellation = constellation;
    }

    /**
     * 获取 否是实名认证 Database column userInfo.verified
     *
     * @return 否是实名认证
     *
     * @mbggenerated
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * 设置 否是实名认证 Database column userInfo.verified
     *
     * @param verified
     *            否是实名认证
     *
     * @mbggenerated
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * 获取 用户来源：手机、email、微信、微博、fb、tw等 Database column userInfo.userSource
     *
     * @return 用户来源：手机、email、微信、微博、fb、tw等
     *
     * @mbggenerated
     */
    public Integer getUserSource() {
        return userSource;
    }

    /**
     * 设置 用户来源：手机、email、微信、微博、fb、tw等 Database column userInfo.userSource
     *
     * @param userSource
     *            用户来源：手机、email、微信、微博、fb、tw等
     *
     * @mbggenerated
     */
    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    /**
     * 获取 建创时间 Database column userInfo.createTime
     *
     * @return 建创时间
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 建创时间 Database column userInfo.createTime
     *
     * @param createTime
     *            建创时间
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 最新更新时间 Database column userInfo.updateTime
     *
     * @return 最新更新时间
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 最新更新时间 Database column userInfo.updateTime
     *
     * @param updateTime
     *            最新更新时间
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getChangeNameCount() {
        return changeNameCount;
    }

    public void setChangeNameCount(Integer changeNameCount) {
        this.changeNameCount = changeNameCount;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public String getMyLocale() {
        return myLocale;
    }

    public void setMyLocale(String myLocale) {
        this.myLocale = myLocale;
    }

    public String getPinyinNick() {
        return pinyinNick;
    }

    public void setPinyinNick(String pinyinNick) {
        this.pinyinNick = pinyinNick;
    }

    public String getShortPyNick() {
        return shortPyNick;
    }

    public void setShortPyNick(String shortPyNick) {
        this.shortPyNick = shortPyNick;
    }

    public Boolean getBaned() {
        return baned;
    }

    public void setBaned(Boolean baned) {
        this.baned = baned;
    }

    public String getBanedType() {
        return banedType;
    }

    public void setBanedType(String banedType) {
        this.banedType = banedType;
    }

    public Date getBanedEndTime() {
        return banedEndTime;
    }

    public void setBanedEndTime(Date banedEndTime) {
        this.banedEndTime = banedEndTime;
    }

    public String getBanedItem() {
        return banedItem;
    }

    public void setBanedItem(String banedItem) {
        this.banedItem = banedItem;
    }

    public String getBanedSubItem() {
        return banedSubItem;
    }

    public void setBanedSubItem(String banedSubItem) {
        this.banedSubItem = banedSubItem;
    }

    public String getBanedDesc() {
        return banedDesc;
    }

    public void setBanedDesc(String banedDesc) {
        this.banedDesc = banedDesc;
    }

    public Integer getChangeSexCount() {
        return changeSexCount;
    }

    public void setChangeSexCount(Integer changeSexCount) {
        this.changeSexCount = changeSexCount;
    }

    public Integer get_ttserver() {
        return _ttserver;
    }

    public void set_ttserver(Integer _ttserver) {
        this._ttserver = _ttserver;
    }

    public String getVerifiedReason() {
        return verifiedReason;
    }

    public void setVerifiedReason(String verifiedReason) {
        this.verifiedReason = verifiedReason;
    }

    public Integer getAnchorType() {
        return anchorType;
    }

    public void setAnchorType(Integer anchorType) {
        this.anchorType = anchorType;
    }

    public Date getChangePswTime() {
        return changePswTime;
    }

    public void setChangePswTime(Date changePswTime) {
        this.changePswTime = changePswTime;
    }

    public Long getHistoryTotalGuestCount() {
        return historyTotalGuestCount;
    }

    public void setHistoryTotalGuestCount(Long historyTotalGuestCount) {
        this.historyTotalGuestCount = historyTotalGuestCount;
    }

    public Date getBanedActionTime() {
        return banedActionTime;
    }

    public void setBanedActionTime(Date banedActionTime) {
        this.banedActionTime = banedActionTime;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getNewUser() {
        return newUser;
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "uid=" + uid + ", username='" + username + '\'' + ", nick='" + nick + '\'' + ", signature='" + signature + '\''
                + ", headerUrl='" + headerUrl + '\'' + ", sex=" + sex + ", constellation=" + constellation + ", role=" + role + ", verified="
                + verified + ", verifiedReason='" + verifiedReason + '\'' + ", userSource=" + userSource + ", historyTotalGuestCount="
                + historyTotalGuestCount + ", changeNameCount=" + changeNameCount + ", changeSexCount=" + changeSexCount + ", newUser=" + newUser
                + ", createTime=" + createTime + ", updateTime=" + updateTime + ", changePswTime=" + changePswTime + ", thirdPartyId='"
                + thirdPartyId + '\'' + ", myLocale='" + myLocale + '\'' + ", pinyinNick='" + pinyinNick + '\'' + ", shortPyNick='" + shortPyNick
                + '\'' + ", baned=" + baned + ", banedType='" + banedType + '\'' + ", banedActionTime=" + banedActionTime + ", banedEndTime="
                + banedEndTime + ", banedItem='" + banedItem + '\'' + ", banedSubItem='" + banedSubItem + '\'' + ", banedDesc='" + banedDesc + '\''
                + ", _ttserver=" + _ttserver + ", anchorType=" + anchorType + ", year='" + year + '\'' + ", placeProvince='" + placeProvince + '\''
                + ", placeCity='" + placeCity + '\'' + ", homeTownProvince='" + homeTownProvince + '\'' + ", homeTownCity='" + homeTownCity + '\''
                + ", business='" + business + '\'' + ", company='" + company + '\'' + ", interest='" + interest + '\'' + ", lastActiveTime="
                + lastActiveTime + ", onlineStatus=" + onlineStatus + ", clientType='" + clientType + '\'' + ", clientVer='" + clientVer + '\''
                + ", regPushId='" + regPushId + '\'' + ", longitude=" + longitude + ", latitude=" + latitude + ", lastShowTime=" + lastShowTime
                + ", lastLoginTime=" + lastLoginTime + ", lastLoginIp='" + lastLoginIp + '\'' + '}';
    }
}