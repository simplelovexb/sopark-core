package cn.suxiangbao.sopark.lbs;

import com.google.common.collect.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DistIpUtil {
    private static Logger logger = LoggerFactory.getLogger(DistIpUtil.class);

    public static double[] northPole = new double[] { 0.0, 90.0 };// 北极点
    public static double[] xiZangLaSa = new double[] { 91.13859, 29.65312 };// 西藏拉萨城关区
    public static double[] xinJiangWuLuMuQi = new double[] { 87.57406, 43.84348 };// 新建乌鲁木齐新市区
    public static double[] gzPanyuWanda = new double[] { 113.342367434044, 23.01069000968774 };// 广州番禺万达
    public static final String CITY_NAME_XIN_JIANG_WU_LU_MU_QI = "乌鲁木齐";

    /**
     * ip区域判断
     */
    private static volatile RangeSet<DistIpRange> rangeSet = TreeRangeSet.create();

    public static final String DEF_SPLITER = "\t";
    
    /**
     * 四个直辖市：北京简称京 上海简称沪 天津简称津 重庆简称渝
     * 5个自治区：
     *  广西壮族自治区简称桂 省会南宁
     *  宁夏回族自治区简称宁 省会银川
     *  新疆维吾尔族自治区简称新 省会乌鲁木齐
     *  内蒙古自治区简称内蒙古 省会呼和浩特
     *  西藏自治区简称藏 省会拉萨
     * 2个行政区：香港特别行政区、澳门特别行政区
     * @param name
     * @return
     */
    public static String locNameConverter(String name) {
        if (name == null) {
            return null;
        }
        switch (name) {
        case "北京":
        case "上海":
        case "天津":
        case "重庆":
            name += name + "市";
            break;
        case "广西":
            name += name + "壮族自治区";
            break;
        case "宁夏":
            name += name + "回族自治区";
            break;
        case "新疆":
            name += name + "维吾尔族自治区";
            break;
        case "内蒙古":
            name += name + "自治区";
            break;
        case "西藏":
            name += name + "自治区";
            break;
        case "香港":
            name += name + "特别行政区";
            break;
        case "澳门":
            name += name + "特别行政区";
            break;
        default:
            break;
        }
        return name;
    }
    
    public static boolean isSpecialLocName(String name) {
        String tmp = locNameConverter(name);
        if (tmp != name) {
            return true;
        }
        return false;
    }

    public static void initDistIps(InputStream inputStream, String spliter) {
        Scanner scanner = null;
        try {
            if (spliter == null) {
                spliter = DEF_SPLITER;
            }
            scanner = new Scanner(inputStream, "utf-8");
            int count = 0;
            RangeSet<DistIpRange> rangeSetRet = TreeRangeSet.create();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] words = line.split(spliter);
                if (words == null || words.length <= 6) {
                    System.out.println("Dist Ip not legal! " + line);
                    continue;
                }
                long ip1 = ip2Long(words[0]);
                long ip2 = ip2Long(words[1]);
                String country = StringUtils.isBlank(words[2]) ? "未知" : words[2].trim();
                String provice = StringUtils.isBlank(words[3]) ? "未知" : words[3].trim();
                if (isSpecialLocName(provice)) {
                    provice = locNameConverter(provice);
                } else {
                    provice += "省";                    
                }
                String city = StringUtils.isBlank(words[4]) ? "未知" : words[4].trim();
                if (isSpecialLocName(city)) {
                    city = locNameConverter(city);
                } else {
                    city += "市";                    
                }
                String dist = StringUtils.isBlank(words[5]) ? "未知" : words[5].trim();
                double longitude = Double.parseDouble(words[8].trim());
                double latitude = Double.parseDouble(words[9].trim());
                DistIpRange pir1 = DistIpRange.getInstance(ip1, country, provice, city, dist, longitude, latitude);
                DistIpRange pir2 = DistIpRange.getInstance(ip2, country, provice, city, dist, longitude, latitude);
                Range<DistIpRange> range = Range.closed(pir1, pir2);
                rangeSetRet.add(range);
                count++;
            }
            System.out.println("Total ip set is:" + count);
            if (count > 0) {
                rangeSet = rangeSetRet;
            }
        } catch (Exception e) {
            logger.error("init Dist Ips error:{}", e.getMessage(), e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static Range<DistIpRange> findIpRange(String ip) {
        DistIpRange pir = DistIpRange.getInstance(ip2Long(ip), null, null, null, null, 0d, 0d);
        if (rangeSet.contains(pir)) {
            Range<DistIpRange> tmp = rangeSet.rangeContaining(pir);
            logger.debug("{}===>{}----{}", ip, tmp.lowerEndpoint(), tmp.upperEndpoint());
            return tmp;
        }
        return null;
    }

    public static String findDist(String ip) {
        DistIpRange pir = DistIpRange.getInstance(ip2Long(ip), null, null, null, null, 0d, 0d);
        if (rangeSet.contains(pir)) {
            Range<DistIpRange> tmp = rangeSet.rangeContaining(pir);
            logger.debug("{}===>{}----{}", ip, tmp.lowerEndpoint(), tmp.upperEndpoint());
            return tmp.lowerEndpoint().dist;
        }
        return null;
    }

    /**
     * google maps的脚本里代码
     */
    private static final double EARTH_RADIUS = 6378.137;// 千米，地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为千米
     */
    public static double calDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * EARTH_RADIUS
                * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

        return s;
    }

    public static boolean judgeCoordinates(double[] coordinates) {
        if (coordinates == null || coordinates.length != 2 || coordinates[0] >= 180 || coordinates[0] < -180 || coordinates[1] > 90
                || coordinates[1] < -90) {
            return false;
        }
        if (coordinates[0] == 0.0d && coordinates[1] == 0.0d) {// 遗留的特殊不合法数据
            return false;
        }
        return true;
    }

    public static boolean judgeCoordinates(Double[] coordinates) {
        if (coordinates == null || coordinates.length != 2 || coordinates[0] == null || coordinates[1] == null || coordinates[0] >= 180
                || coordinates[0] < -180 || coordinates[1] > 90 || coordinates[1] < -90) {
            return false;
        }
        if (coordinates[0].equals(0.0d) && coordinates[1].equals(0.0d)) {// 遗留的特殊不合法数据
            return false;
        }
        return true;
    }

    /**
     * 
     * @param dis 公里数
     * @return
     */
    public static String getDisStr(double dis) {
        if (dis < 10d) {// 若小于10km展示时，保留两位小数
            return String.format("%.2fkm", dis);
        }
        return String.format(((int) dis) + "km");
    }

    public static void main(String[] args) {
        try {
            System.out.println(judgeCoordinates(new Double[] { 113.2799301147461, 23.1404914855957 }));
            System.out.println(DistIpUtil.calDistance(113.34235, 23.01070, 113.38397, 22.93599));// 9.3468公里
            System.out.println(DistIpUtil.calDistance(113.34236, 23.01069, 113.38397, 22.93599));// 9.3453公里
            System.out.println(DistIpUtil.calDistance(43.9667, 5.78, 40, 5));// 137.0公里
            // FileInputStream fi = new FileInputStream("e:/networkips.txt");
            // initDistIps(fi, DEF_SPLITER);
            // System.out.println(findIpRange("113.108.228.24"));
            // System.out.println(isCompanyIp("61.148.205.166", false, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 0是网络地址 15是广播地址 不可用
    private static long[] lanIp1 = new long[] { ip2Long("10.0.0.0"), ip2Long("10.255.255.255") };
    private static long[] lanIp2 = new long[] { ip2Long("172.16.0.0"), ip2Long("172.31.255.255") };
    private static long[] lanIp3 = new long[] { ip2Long("192.168.0.0"), ip2Long("192.168.255.255") };
    private static long[][] lanIps = new long[][] { lanIp1, lanIp2, lanIp3 };
    private static long[][] companyIps = null;

    static {
        List<long[]> ips = Lists.newArrayList();

        Set<String> singleIps = Sets.newLinkedHashSet();
        Set<String> cidrIps = Sets.newLinkedHashSet();
        List<long[]> pairIps = Lists.newArrayList();
        // 运维粗略ip库
        // http://ido.sysop.duowan.com/admin/faq/question/view.jsp?from=list&id=34001
        cidrIps.add("59.37.7.224/28");
        cidrIps.add("113.108.228.24/29");
        cidrIps.add("121.14.38.208/28");
        cidrIps.add("58.254.172.32/29");
        cidrIps.add("58.254.172.48/30");
        cidrIps.add("114.119.126.0/27");
        cidrIps.add("124.42.235.0/26");
        cidrIps.add("58.254.172.40/29");
        cidrIps.add("114.119.126.64/26");
        cidrIps.add("120.132.237.16/30");
        cidrIps.add("120.132.236.84/30");
        cidrIps.add("58.248.138.4/32");
        cidrIps.add("221.5.35.160/27");
        cidrIps.add("103.38.28.104/29");
        cidrIps.add("122.13.65.96/28");
        cidrIps.add("113.106.251.80/28");
        cidrIps.add("183.57.17.32/30");
        cidrIps.add("123.126.22.220/30");
        cidrIps.add("114.242.170.64/29");
        cidrIps.add("61.148.205.164/30");
        cidrIps.add("61.148.205.168/30");
        cidrIps.add("222.222.129.213/32");
        cidrIps.add("121.18.132.182/32");
        cidrIps.add("27.115.89.46/32");
        cidrIps.add("111.203.161.43/32");
        cidrIps.add("36.110.80.210/32");
        cidrIps.add("36.110.80.160/29");
        cidrIps.add("123.59.74.205/32");
        cidrIps.add("59.41.39.162/32");
        cidrIps.add("14.215.104.211/32");
        cidrIps.add("122.13.169.254/32");
        cidrIps.add("122.13.132.160/27");
        cidrIps.add("14.152.49.160/27");
        cidrIps.add("14.152.49.152/29");

        // IT部精准ip
        // http://webapi.sysop.duowan.com:62175/office/ip_desc.txt
        pairIps.add(new long[] { ip2Long("113.106.251.82"), ip2Long("113.106.251.91") });// 珠海友华大厦
        singleIps.add("183.57.17.34");// 珠海友华大厦
        pairIps.add(new long[] { ip2Long("121.14.38.216"), ip2Long("121.14.38.223") });// 广州番禺万达广场B1电信IP
        pairIps.add(new long[] { ip2Long("113.108.228.24"), ip2Long("113.108.228.31") });// 广州番禺万达广场B1电信IP
        pairIps.add(new long[] { ip2Long("58.254.172.37"), ip2Long("58.254.172.51") });// 广州番禺万达广场B1联通IP
        singleIps.add("27.115.89.46");// 上海办公室
        singleIps.add("123.126.22.222");// 致真大厦
        pairIps.add(new long[] { ip2Long("114.242.170.65"), ip2Long("114.242.170.70") });// 致真大厦
        singleIps.add("14.215.104.211");// 佛山VPN
        singleIps.add("122.13.169.254");// 佛山VPN
        pairIps.add(new long[] { ip2Long("61.148.205.165"), ip2Long("61.148.205.170") });// 威地大厦
        singleIps.add("222.222.129.213");// 保定分部
        singleIps.add("121.18.132.182");// 保定分部
        pairIps.add(new long[] { ip2Long("103.38.28.105"), ip2Long("103.38.28.108") });// 广州羊城创意产业园3-08国际IP
        singleIps.add("111.203.161.43");// 北京办公室
        singleIps.add("36.110.80.210");// 北京办公室
        pairIps.add(new long[] { ip2Long("36.110.80.162"), ip2Long("36.110.80.167") });// 北京办公室
        singleIps.add("59.41.39.162");// 环球网校-广州维多利广场分校办公区
        pairIps.add(new long[] { ip2Long("14.152.49.164"), ip2Long("14.152.49.190") });// 广州羊城创意产业园3-08电信IP 2016/8/25
        pairIps.add(new long[] { ip2Long("14.152.49.154"), ip2Long("14.152.49.158") });// 广州大观路1931生活中心电信IP 2016/8/25
        pairIps.add(new long[] { ip2Long("58.248.138.4"), ip2Long("58.248.138.5") });// 广州羊城创意产业园3-08联通IP
        pairIps.add(new long[] { ip2Long("221.5.35.164"), ip2Long("221.5.35.190") });// 广州羊城创意产业园3-08联通IP
        pairIps.add(new long[] { ip2Long("120.132.236.84"), ip2Long("120.132.236.87") });// 广州番禺万达广场B1国际IP
        pairIps.add(new long[] { ip2Long("120.132.237.16"), ip2Long("120.132.237.19") });// 广州番禺万达广场B1国际IP
        singleIps.add("123.59.74.205");// 北京环球网校
        pairIps.add(new long[] { ip2Long("59.37.7.226"), ip2Long("59.37.7.238") });// 广州番禺万达广场B1电信IP 2016/5/26
        pairIps.add(new long[] { ip2Long("122.13.132.164"), ip2Long("122.13.132.190") });// 广州番禺万达广场B1联通IP 2016/8/22
        pairIps.add(new long[] { ip2Long("114.119.126.4"), ip2Long("114.119.126.30") });// 广州番禺万达广场B1联通IP 2016/5/26
        pairIps.add(new long[] { ip2Long("124.42.235.4"), ip2Long("124.42.235.62") });// 广州羊城创意产业园3-08联通IP

        for (String s : cidrIps) {
            initCidrIpBlock(s, ips);
        }
        ips.addAll(pairIps);
        for (String s : singleIps) {
            ips.add(new long[] { ip2Long(s), ip2Long(s) });
        }
        companyIps = ips.toArray(new long[0][0]);
    }

    /*
     * 初始化CIDR IP范围
     * 
     * @param cidrIp 例如：x.x.x.x/n
     */
    private static void initCidrIpBlock(String cidrIp, List<long[]> ips) {
        if (cidrIp == null || "".equals(cidrIp.trim())) {
            logger.error("[" + cidrIp + "]参数格式不正确，CIDR地址部分为空");
        }
        String[] ipIds = cidrIp.split("\\/");
        if (ipIds == null || ipIds.length != 2) {
            logger.error("[" + cidrIp + "]参数格式不正确，CIDR地址格式不正确，正确格式为x.x.x.x/n");
        }
        int num = Integer.parseInt(ipIds[1]);
        if (num > 32 || num < 4) {
            logger.error("[" + cidrIp + "]参数格式不正确，CIDR地址格式不正确，网络ID值必须在（4,32）范围内");
        }
        // TODO
        String networkId = getNetworkId(cidrIp);
        String maxIpAddres = getMaxIpAddres(networkId, getMaskRevert(num));
        long[] tmp = new long[] { ip2Long(networkId), ip2Long(maxIpAddres) };
        ips.add(tmp);
        logger.info("[{}]IP 范围为[{},{}], 256进制值范围为[{},{}]", cidrIp, networkId, maxIpAddres, tmp[0], tmp[1]);
    }

    /*
     * 获取网络ID，即也是CIDR表示的最小IP
     * 
     * @param ipCidr CIDR法表示的IP，例如：172.16.0.0/12
     * 
     * @return 网络ID，即也是CIDR表示的最小IP
     */
    private static String getNetworkId(String ipCidr) {
        String[] ipMaskLen = ipCidr.split("\\/");
        String mask = getMask(Integer.parseInt(ipMaskLen[1]));
        String[] ips = ipMaskLen[0].split("\\.");
        String[] masks = mask.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(Integer.parseInt(ips[i]) & Integer.parseInt(masks[i]));
            if (i != 3) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /*
     * 获取IP最大值
     * 
     * @param netId 网络ID
     * 
     * @param maskReverse 掩码反码
     * 
     * @return
     */
    private static String getMaxIpAddres(String netId, String maskReverse) {
        String[] netIdArray = netId.split("\\.");
        String[] maskRevertArray = maskReverse.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = netIdArray.length; i < len; i++) {
            sb.append(Integer.parseInt(netIdArray[i]) + Integer.parseInt(maskRevertArray[i]));
            if (i != len - 1) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /*
     * 获取掩码
     * 
     * @param maskLength 网络ID位数
     * 
     * @return
     */
    private static String getMask(int maskLength) {
        int binaryMask = 0xFFFFFFFF << (32 - maskLength);
        StringBuffer sb = new StringBuffer();
        for (int shift = 24; shift > 0; shift -= 8) {
            sb.append(Integer.toString((binaryMask >>> shift) & 0xFF));
            sb.append(".");
        }
        sb.append(Integer.toString(binaryMask & 0xFF));
        return sb.toString();
    }

    /*
     * 获取掩码的反码
     * 
     * @param maskLength 网络ID位数
     * 
     * @return
     */
    private static String getMaskRevert(int maskLength) {
        int binaryMask = 0xFFFFFFFF << (32 - maskLength);
        binaryMask = binaryMask ^ 0xFFFFFFFF;
        StringBuffer sb = new StringBuffer(15);
        for (int shift = 24; shift > 0; shift -= 8) {
            sb.append(Integer.toString((binaryMask >>> shift) & 0xFF));
            sb.append(".");
        }
        sb.append(Integer.toString(binaryMask & 0xFF));
        return sb.toString();
    }

    public static boolean isCompanyIp(String accessIp, boolean canLocalAccess, boolean canLanAccess) {
        if (accessIp == null || "".equals(accessIp)) {
            return false;
        }
        if (accessIp.contains(":")) {// 所有IPv6的地址都不管
            return true;
        }
        if (canLocalAccess && "127.0.0.1".equals(accessIp)) {
            return true;
        }
        if (accessIp.contains(",")) {
            logger.debug("++++++ AccessIp contain not legal Char:{}", accessIp);
            accessIp = accessIp.split(",")[0].trim();
        }
        long accessIpL = ip2Long(accessIp);
        for (long[] companyIp : companyIps) {
            if (accessIpL >= companyIp[0] && accessIpL <= companyIp[1]) {
                return true;
            }
        }
        if (canLanAccess) {
            for (long[] lanIp : lanIps) {
                if (accessIpL >= lanIp[0] && accessIpL <= lanIp[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLanOrLoIp(String accessIp) {
        if (accessIp == null || "".equals(accessIp)) {
            return false;
        }
        if ("127.0.0.1".equals(accessIp)) {
            return true;
        }
        long accessIpL = ip2Long(accessIp);
        for (long[] lanIp : lanIps) {
            if (accessIpL >= lanIp[0] && accessIpL <= lanIp[1]) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLanOrLoIpOrCompanyIp(String accessIp) {
        if (accessIp == null || "".equals(accessIp)) {
            return false;
        }
        if ("127.0.0.1".equals(accessIp)) {
            return true;
        }
        long accessIpL = ip2Long(accessIp);
        for (long[] lanIp : lanIps) {
            if (accessIpL >= lanIp[0] && accessIpL <= lanIp[1]) {
                return true;
            }
        }
        for (long[] companyIp : companyIps) {
            if (accessIpL >= companyIp[0] && accessIpL <= companyIp[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转化ip字符串为long值（本来应该转化成无符号的32位整形，但是java不支持，所以用long）
     * 
     * @param ipStr ip字符
     * @return
     */
    public static long ip2Long(String ipStr) {
        String[] tmp = ipStr.split("[.]");
        long ip = 0L | Long.valueOf(tmp[0]) << 24;
        ip = ip | (Long.valueOf(tmp[1]) << 16);
        ip = ip | (Long.valueOf(tmp[2]) << 8);
        ip = ip | Long.valueOf(tmp[3]);
        if (ip < 0) {
            System.out.println("Ip2Long Error!!!! ip=" + ip);
        }
        return ip;
    }

    /**
     * 转化long值为ip字符串
     * 
     * @param ipLong
     * @return
     */
    public static String long2Ip(long ipLong) {
        return ((ipLong >> 24) & 0xFF) + "." + ((ipLong >> 16) & 0xFF) + "." + ((ipLong >> 8) & 0xFF) + "." + (ipLong & 0xFF);
    }

    /**
     * 在Range区间数据中保持省份、城市、县区属性
     * 
     * @author JCY
     * 
     */
    public static class DistIpRange implements Comparable<DistIpRange> {
        private String country;
        private String provice;
        private String city;
        private String dist;
        private long value;
        private double longitude;
        private double latitude;

        public static DistIpRange getInstance(long value, String country, String provice, String city, String dist, double longitude, double latitude) {
            DistIpRange ret = new DistIpRange();
            ret.country = country;
            ret.provice = provice;
            ret.city = city;
            ret.dist = dist;
            ret.value = value;
            ret.longitude = longitude;
            ret.latitude = latitude;
            return ret;
        }

        @Override
        public int compareTo(DistIpRange distIpRange) {
            return value > distIpRange.value ? 1 : (value == distIpRange.value ? 0 : -1);
        }

        public String getDist() {
            return dist;
        }

        public long getValue() {
            return value;
        }

        public String getCountry() {
            return country;
        }

        public String getProvice() {
            return provice;
        }

        public String getCity() {
            return city;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        @Override
        public String toString() {
            return "DistIpRange [" + (country != null ? "country=" + country + ", " : "") + (provice != null ? "provice=" + provice + ", " : "")
                    + (city != null ? "city=" + city + ", " : "") + (dist != null ? "dist=" + dist + ", " : "") + "value=" + value + ", longitude="
                    + longitude + ", latitude=" + latitude + "]";
        }

    }
}
