package cn.suxiangbao.sopark.host;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostInfoHelper {

    private static final Logger log = LoggerFactory.getLogger(HostInfoHelper.class);
    private static final String DEFAULT_FILE_PATH = "/home/dspeak/yyms/hostinfo.ini";

    private static Properties properties;
    private static OriginHostInfo origInfo;

    static {
        readFileData();
    }

    private static void readFileData() {
        File file = new File(DEFAULT_FILE_PATH);
        if (file.exists() && file.isFile() && file.canRead()) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(file));
                properties = new Properties();
                properties.load(in);
                if (log.isDebugEnabled()) {
                    for (Object s : properties.keySet()) {
                        log.debug("property " + s + ":" + properties.getProperty((String) s));
                    }
                }
                parseProperties();
            } catch (Exception e) {
                log.error("read data info error:", e);
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    private static void parseProperties() {
        origInfo = new OriginHostInfo();
        origInfo.setArea_id(properties.getProperty("area_id"));
        origInfo.setDnips(properties.getProperty("dnips"));
        origInfo.setVid(properties.getProperty("vid"));
        origInfo.setCity_id(properties.getProperty("city_id"));
        origInfo.setIp_isp_list(properties.getProperty("ip_isp_list"));
        origInfo.setGuid(properties.getProperty("guid"));
        origInfo.setFlock_id(properties.getProperty("flock_id"));
        origInfo.setServer_id(properties.getProperty("server_id"));
        origInfo.setVm_type(properties.getProperty("vm_type"));
        origInfo.setServer_level(properties.getProperty("server_level"));
        origInfo.setIdc_id(properties.getProperty("idc_id"));
        origInfo.setServiceName(properties.getProperty("serviceName"));
        origInfo.setRoom_id(properties.getProperty("room_id"));
        origInfo.setSysopResponsibleAdmin_dw(properties.getProperty("sysopResponsibleAdmin_dw"));
        origInfo.setResponsibleAdmin(properties.getProperty("responsibleAdmin"));
        origInfo.setStatus(properties.getProperty("status"));
        origInfo.setServer_type(properties.getProperty("server_type"));
        origInfo.setBuss_name(properties.getProperty("buss_name"));
        origInfo.setSysopResponsibleAdmin(properties.getProperty("sysopResponsibleAdmin"));
        origInfo.setResponsibleAdmin_dw(properties.getProperty("responsibleAdmin_dw"));
        origInfo.setRoom(properties.getProperty("room"));
        origInfo.setPri_group_id(properties.getProperty("pri_group_id"));
        origInfo.setDept(properties.getProperty("dept"));
        origInfo.setGroup_id(properties.getProperty("group_id"));
    }

    public static HostInfo getHostInfo() {
        return HostInfo.parse(origInfo);
    }

    public static String getHostIp() {
        HostInfo hostInfo = HostInfoHelper.getHostInfo();
        if (hostInfo != null) {
            Map<NetType, IpInfo> ipMap = hostInfo.getIpList();
            IpInfo ipInfo = null;
            if (ipMap.containsKey(NetType.CTL)) {// 优先使用电信IP
                ipInfo = ipMap.get(NetType.CTL);
            } else if (ipMap.containsKey(NetType.CNC)) {// 其次使用联通/网通IP
                ipInfo = ipMap.get(NetType.CNC);
            } else if (ipMap.containsKey(NetType.MOB)) {// 然后是移动IP
                ipInfo = ipMap.get(NetType.MOB);
            } else if (ipMap.containsKey(NetType.CNII)) {// 然后是铁通IP
                ipInfo = ipMap.get(NetType.CNII);
            } else if (ipMap.containsKey(NetType.EDU)) {// 然后是教育IP
                ipInfo = ipMap.get(NetType.EDU);
            } else if (ipMap.containsKey(NetType.WBN)) {// 然后是长城IP
                ipInfo = ipMap.get(NetType.WBN);
            } else if (ipMap.containsKey(NetType.BMC)) {// 最后是管理网IP
                ipInfo = ipMap.get(NetType.BMC);
            }
            if (null != ipInfo) {
                return ipInfo.getIp();
            }
        }
        return null;
    }

    private static void readTestData() {
        String testProperties = "dnips=59.38.102.67:61.130.29.167\narea_id=6\nvid=0\ncity_id=1078\nip_isp_list=14.17.106.117:CTL,10.21.43.116:INTRANET,10.21.51.244:BMC\nguid=70855f3a-18ab-4676-a6bf-faed924c50af\nflock_id=103948732\nserver_id=73313\nvm_type=2\nserver_level=1\nidc_id=15384\nserviceName=\nroom_id=15384\nsysopResponsibleAdmin_dw=dw_zhangyong1\nresponsibleAdmin=江成彦\nstatus=7\nserver_type=123\nbuss_name=ME->WEB服务->WEB服务\nsec_group_id=58\nsysopResponsibleAdmin=张勇\nresponsibleAdmin_dw=dw_jiangchengyan\nroom=佛山智慧城多线-01\npri_group_id=58\ndept=dw-yy\ngroup_id=103948732";
        try {
            properties = new Properties();
            properties.load(new StringReader(testProperties));
            if (log.isDebugEnabled()) {
                for (Object s : properties.keySet()) {
                    log.debug("property " + s + ":" + properties.getProperty((String) s));
                }
            }
            parseProperties();
        } catch (Exception e) {
            log.error("read test data info error:", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(HostInfoHelper.getHostInfo());
        readTestData();
        System.out.println(HostInfoHelper.getHostInfo());
        System.out.println(HostInfoHelper.getHostIp());
    }

    public static class LocalIpHelper {
        /**
         * 获取本机的IP
         *
         * @return Ip地址
         */
        public static String getLocalHostIP() {
            String ip;
            try {
                /** 返回本地主机。 */
                InetAddress addr = InetAddress.getLocalHost();
                /** 返回 IP 地址字符串（以文本表现形式） */
                ip = addr.getHostAddress();
            } catch (Exception ex) {
                ip = "";
            }

            return ip;
        }

        /**
         * 或者主机名：
         *
         * @return
         */
        public static String getLocalHostName() {
            String hostName;
            try {
                /** 返回本地主机。 */
                InetAddress addr = InetAddress.getLocalHost();
                /** 获取此 IP 地址的主机名。 */
                hostName = addr.getHostName();
            } catch (Exception ex) {
                hostName = "";
            }

            return hostName;
        }

        /**
         * 获得本地所有的IP地址
         *
         * @return
         */
        public static String[] getAllLocalHostIP() {

            String[] ret = null;
            try {
                /** 获得主机名 */
                String hostName = getLocalHostName();
                if (hostName.length() > 0) {
                    /** 在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。 */
                    InetAddress[] addrs = InetAddress.getAllByName(hostName);
                    if (addrs.length > 0) {
                        ret = new String[addrs.length];
                        for (int i = 0; i < addrs.length; i++) {
                            /** .getHostAddress() 返回 IP 地址字符串（以文本表现形式）。 */
                            ret[i] = addrs[i].getHostAddress();
                        }
                    }
                }

            } catch (Exception ex) {
                ret = null;
            }

            return ret;
        }
    }

    public static class HostInfo {
        private String areaId;
        private String cityId;
        private Map<NetType, IpInfo> ipList;

        public String getAreaId() {
            return areaId;
        }

        public String getCityId() {
            return cityId;
        }

        public Map<NetType, IpInfo> getIpList() {
            return ipList;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public void setIpList(Map<NetType, IpInfo> ipList) {
            this.ipList = ipList;
        }

        public static HostInfo parse(OriginHostInfo orig) {
            if (null == orig) {
                return null;
            }
            HostInfo hi = new HostInfo();
            hi.setAreaId(orig.getArea_id());
            hi.setCityId(orig.getCity_id());
            String ipInfos = orig.getIp_isp_list();
            try {
                hi.setIpList(new HashMap<NetType, IpInfo>());
                String[] ips = ipInfos.split(",");
                for (String ip : ips) {
                    String[] info = ip.split(":");
                    NetType nt = null;
                    try {
                        nt = NetType.valueOf(info[1]);
                    } catch (Exception e) {
                    }
                    if (null != nt) {
                        hi.getIpList().put(nt, new IpInfo(info[0], nt));
                    }
                }
            } catch (Exception e) {
                return null;
            }
            return hi;
        }

        @Override
        public String toString() {
            return "HostInfo [areaId=" + areaId + ", cityId=" + cityId + ", ipList=" + ipList + "]";
        }

    }

    public static class IpInfo {

        private String ip;
        private NetType type;

        public IpInfo(String ip, NetType type) {
            this.ip = ip;
            this.type = type;
        }

        public String getIp() {
            return ip;
        }

        public NetType getType() {
            return type;
        }

        @Override
        public String toString() {
            return "IpInfo [ip=" + ip + ", type=" + type + "]";
        }

    }

    public enum NetType {
        CTL(1, "CTL", "电信"), //
        CNC(2, "CNC", "联通/网通"), //
        EDU(3, "EDU", "教育"), //
        MOB(4, "MOB", "移动"), //
        CNII(5, "CNII", "铁通"), //
        WBN(6, "WBN", "长城"), //
        INTRANET(7, "INTRANET", "内网"), //
        BMC(8, "BMC", "管理网"), ;
        private int value;
        private String shortName;
        private String name;

        private NetType(int value, String shortName, String name) {
            this.name = name;
            this.value = value;
            this.shortName = shortName;
        }

        public int getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        public String getName() {
            return name;
        }

        public static void main(String[] args) {
            System.out.println(NetType.valueOf("CTL"));
        }
    }

    public static class OriginHostInfo {

        private String dnips;
        private String area_id;
        private String vid;
        private String city_id;
        private String ip_isp_list;
        private String guid;
        private String flock_id;
        private String server_id;
        private String vm_type;
        private String server_level;
        private String idc_id;
        private String serviceName;
        private String room_id;
        private String sysopResponsibleAdmin_dw;
        private String responsibleAdmin;
        private String status;
        private String server_type;
        private String buss_name;
        private String sec_group_id;
        private String sysopResponsibleAdmin;
        private String responsibleAdmin_dw;
        private String room;
        private String pri_group_id;
        private String dept;
        private String group_id;

        public String getDnips() {
            return dnips;
        }

        public String getArea_id() {
            return area_id;
        }

        public String getVid() {
            return vid;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getIp_isp_list() {
            return ip_isp_list;
        }

        public String getGuid() {
            return guid;
        }

        public String getFlock_id() {
            return flock_id;
        }

        public String getServer_id() {
            return server_id;
        }

        public String getVm_type() {
            return vm_type;
        }

        public String getServer_level() {
            return server_level;
        }

        public String getIdc_id() {
            return idc_id;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getRoom_id() {
            return room_id;
        }

        public String getSysopResponsibleAdmin_dw() {
            return sysopResponsibleAdmin_dw;
        }

        public String getResponsibleAdmin() {
            return responsibleAdmin;
        }

        public String getStatus() {
            return status;
        }

        public String getServer_type() {
            return server_type;
        }

        public String getBuss_name() {
            return buss_name;
        }

        public String getSec_group_id() {
            return sec_group_id;
        }

        public String getSysopResponsibleAdmin() {
            return sysopResponsibleAdmin;
        }

        public String getResponsibleAdmin_dw() {
            return responsibleAdmin_dw;
        }

        public String getRoom() {
            return room;
        }

        public String getPri_group_id() {
            return pri_group_id;
        }

        public String getDept() {
            return dept;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setDnips(String dnips) {
            this.dnips = dnips;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public void setIp_isp_list(String ip_isp_list) {
            this.ip_isp_list = ip_isp_list;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public void setFlock_id(String flock_id) {
            this.flock_id = flock_id;
        }

        public void setServer_id(String server_id) {
            this.server_id = server_id;
        }

        public void setVm_type(String vm_type) {
            this.vm_type = vm_type;
        }

        public void setServer_level(String server_level) {
            this.server_level = server_level;
        }

        public void setIdc_id(String idc_id) {
            this.idc_id = idc_id;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public void setSysopResponsibleAdmin_dw(String sysopResponsibleAdmin_dw) {
            this.sysopResponsibleAdmin_dw = sysopResponsibleAdmin_dw;
        }

        public void setResponsibleAdmin(String responsibleAdmin) {
            this.responsibleAdmin = responsibleAdmin;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setServer_type(String server_type) {
            this.server_type = server_type;
        }

        public void setBuss_name(String buss_name) {
            this.buss_name = buss_name;
        }

        public void setSec_group_id(String sec_group_id) {
            this.sec_group_id = sec_group_id;
        }

        public void setSysopResponsibleAdmin(String sysopResponsibleAdmin) {
            this.sysopResponsibleAdmin = sysopResponsibleAdmin;
        }

        public void setResponsibleAdmin_dw(String responsibleAdmin_dw) {
            this.responsibleAdmin_dw = responsibleAdmin_dw;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public void setPri_group_id(String pri_group_id) {
            this.pri_group_id = pri_group_id;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

    }
}
