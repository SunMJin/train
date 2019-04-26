import com.sunrt.train.utils.DateUtils;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.HttpPost;
import sun.management.Agent;
import sun.plugin2.os.windows.Windows;
import sun.security.util.Length;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test4 {

    public static void main(String[] args) throws UnsupportedEncodingException {



        HttpPost post=new HttpPost("https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest");
        post.setHeader("Host", "kyfw.12306.cn");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Pragma", "no-cache");
        post.setHeader("Cache-Control","no-cache");
        post.setHeader("Accept", "*/*");
        post.setHeader("Origin", "https://kyfw.12306.cn");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setHeader("Referer", "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc&fs=%E6%97%A0%E9%94%A1,WXH&ts=%E4%B8%8A%E6%B5%B7,SHH&date=2019-04-27&flag=N,N,Y");
        post.setHeader("Accept-Encoding","gzip, deflate, br");
        post.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        post.setHeader("Cookie", "JSESSIONID=F1DAB0B03E273062CBCA69F7E0516D1A; tk=9sjRsqtNWjNg7lFDTXPJG3g_UfwcHTvT-3qgl6ka2IJoQu1bub1110; route=6f50b51faa11b987e576cdb301e545c4; BIGipServerpool_passport=166527498.50215.0000; RAIL_EXPIRATION=1556445055487; RAIL_DEVICEID=UU4_7PafjyzKW0DEP37utPDYXAtzbmkz4CElNvTQL8HKydvwNQQKImDUuR_bl7ZOfhHwoKKoxi0nM1db8BbUpp2_fbI-hbzQ7397Z0WUpZ0EBCubqrKSBln7-pMs1eLIIyuqYgOZCkoRbSBL4paBHLY63L0VhDrP; BIGipServerotn=149946890.50210.0000; _jc_save_fromStation=%u65E0%u9521%2CWXH; _jc_save_toStation=%u4E0A%u6D77%2CSHH; _jc_save_fromDate=2019-04-27; _jc_save_toDate=2019-04-26; _jc_save_wfdc_flag=dc");
        List<NameValuePair> listParmas= Form.form()
                .add("secretStr", "i7GsOisvjwbKtnJ8yuWPoh7zDnO5fNztqGlLa10JAiFbT//bUnyVBwVUCRrJn65dC3UJVSZ1iuq8\n" +
                        "n0tGsYW8fM9h49EeFVlvJMjndL6ndaidWl8SRsw/2XBfejBjHtN48KsGK46fbxhng4RfcNG8dWNF\n" +
                        "V/gKvcAzWV9a/LHYnMIAkEZR/5UEfdOrcHCmo1n1kO/Ebi6LePSMSwe4wYktlNklRQH4YMuSU5v3\n" +
                        "5IAxbJrtBIUtDNw4Tf2l6bhcacKZ1A/+ekz0CQUysN23OhC8Wzz43V3Np1rxb3xAv1uj1MyAOd/c\n" +
                        "glJJSMjuANo=")//票id
                .add("train_date", "2019-04-27")//出发日期
                .add("back_train_date", "2019-04-26")//返程日期(单程为查票日期)
                .add("tour_flag","dc")//表单单程车票
                .add("purpose_codes", "ADULT")//成人票
                .add("query_from_station_name", URLEncoder.encode("", "utf-8"))//出发站
                .add("query_to_station_name", URLEncoder.encode("上海", "utf-8"))//到达站
                .add("undefined", null)
                .build();


        post.setEntity(new UrlEncodedFormEntity(listParmas));

        HttpUtils.PostCus(post);;
    }
}
