package cn.suxiangbao.sopark.http;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Chris on 16/6/7.
 */
public class HttpUtil {

    public static final String HTTP_HEADER_XRP = "X-Real-IP";
    public static final String HTTP_HEADER_XFF = "X-Forwarded-For";
    public static final String HTTP_HEADER_CSI = "cdn-src-ip";

    private static String getRemoteIpInfo(HttpServletRequest request) {
        // 优先级1：从cdn-src-ip中取，用于请求经过云防NG的情况
        String csi = request.getHeader(HTTP_HEADER_CSI);
        if (StringUtils.isNotEmpty(csi)) {
            return csi;
        }

        // 优先级2：从X-Forwarded-For中取，有被伪造的风险
        // 适用于多级nginx代理的形式
        String xff = request.getHeader(HTTP_HEADER_XFF);
        if (StringUtils.isNotEmpty(xff)) {
            return xff;
        }

        // 优先级3：从X-Real-IP中取，有多重代理时，这个地址可能不准确，但一般不会被伪造
        String ip = request.getHeader(HTTP_HEADER_XRP);
        if (StringUtils.isNotEmpty(ip)) {
            return ip;
        }

        // 优先级4：直接获取RemoteAddr，有反向代理时这个地址会不准确
        return request.getRemoteAddr();
    }

    public static String getRemoteIP(HttpServletRequest request) {
        if (request == null) {
            return StringUtils.EMPTY;
        }
        String ipInfo = getRemoteIpInfo(request);

        if (StringUtils.isEmpty(ipInfo)) {
            return StringUtils.EMPTY;
        }

        String[] ipArray = StringUtils.split(ipInfo, ",");

        String ip = StringUtils.trimToEmpty(ipArray[0]);

        return ip;
    }
    
    private final static String USER_AGENT = "me-core-http/1.0";
    
    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    
    public static final String CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";
    /**
     * 
     * @param url
     * @param postBody 若
     * @param contentType
     * @return
     * @throws Exception
     */
    public static String sendPost(String url, String postBody, String contentType) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        if (contentType == null) {
            contentType = CONTENT_TYPE_URL_ENCODED;
        }
        con.setRequestProperty("Content-Type", contentType);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        if (postBody != null && !postBody.equals("")) {
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            if (CONTENT_TYPE_JSON.equals(contentType)) {
                wr.write(postBody.getBytes("UTF-8"));
            } else {
                wr.writeBytes(postBody);
            }
            wr.flush();
            wr.close();
        }

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + postBody);
//        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
//            System.out.println(sendPost("http://test.me.yy.com", null));
            System.out.println(sendPost("http://test.me.yy.com/liveShow/chat", "lid=57baa5410000650b3ad36c83&auth=no2&uid=100001417&msg=" + URLEncoder.encode("你好", "UTF-8") + "&txt=88", CONTENT_TYPE_URL_ENCODED));
            System.out.println(sendPost("http://127.0.0.1:10039/metrics_api", "{\"app_name\":\"app1\",\"app_ver\":\"1.0\",\"service_name\":\"keyService\",\"step\":60,\"ver\":\"0.1\",\"skip_1st_period\":\"false\",\"defmodel\":[{\"topic\":\"\",\"uri\":\"http/ping\",\"uri_tag\":\"s\",\"duration\":10,\"code\":\"1\",\"isSuccess\":\"y\",\"scale\":[5000,10000,20000,50000,100000,200000,300000,500000,800000,1000000,1500000,2000000,3000000,5000000,8000000,10000000,15000000,20000000,30000000]}]}", CONTENT_TYPE_JSON));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
