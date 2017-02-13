package cn.suxiangbao.sopark.lbs;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class Position {
    private String country;
    private String provice;
    private String city;
    
    /**
     * 区
     */
    private String dist;
    
    /**
     * 详细地址，如：中国广东省佛山市禅城区季华西路38号
     */
    private String locationName;
    
    private Double longitude;
    
    private Double latitude;
    
    private Date updateTime;

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = DistIpUtil.locNameConverter(provice);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = DistIpUtil.locNameConverter(city);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String genLocationName() {
        if (StringUtils.equals(provice, city)) {
            locationName = (country == null ? "" : country) + (provice == null ? "" : provice) + (dist == null ? "" : dist);// 直辖市
        } else {
            locationName = (country == null ? "" : country) + (provice == null ? "" : provice) + (city == null ? "" : city) + (dist == null ? "" : dist);
        }
        return locationName;
    }

    @Override
    public String toString() {
        return "Position [" + (country != null ? "country=" + country + ", " : "") + (provice != null ? "provice=" + provice + ", " : "")
                + (city != null ? "city=" + city + ", " : "") + (dist != null ? "dist=" + dist + ", " : "")
                + (locationName != null ? "locationName=" + locationName + ", " : "") + (longitude != null ? "longitude=" + longitude + ", " : "")
                + (latitude != null ? "latitude=" + latitude + ", " : "") + (updateTime != null ? "updateTime=" + updateTime : "") + "]";
    }

}
