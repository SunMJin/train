import com.sunrt.train.TrainHttp;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.ticket.Constant;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

public class test4 {
    private static HttpUtils httpUtils = TrainHttp.getHttp();

    public static void main(String[] args) throws IOException, HttpException {
        List<NameValuePair> list= Form.form()
                .add("cancel_flag","2")
                .add("bed_level_order_num","000000000000000000000000000000")
                .add("passengerTicketStr","1,0,1,孙梦金,1,320322199403184415,13861732734,N")
                .add("oldPassengerStr","孙梦金,1,320322199403184415,1_")
                .add("tour_flag","dc")
                .add("randCode","")
                .add("whatsSelect","1")
                .add("_json_att","")
                .add("REPEAT_SUBMIT_TOKEN","aa92c943c963a96481369ad69b4ac2ae")
                .build();

        HttpPost httpPost=new HttpPost(Constant.CHECKORDERINFOURL);
        httpPost.setEntity(new UrlEncodedFormEntity(list,"utf-8"));
        httpPost.setHeader("Cookie", "JSESSIONID=C2B7DE73D36C07BCF2D49F57D7D065A0; tk=mTnEVF4y3YdAn6vxi-ZjrejsKKoHc0CqMYijlkQLDtcYs7xe511110; route=6f50b51faa11b987e576cdb301e545c4; BIGipServerpool_passport=166527498.50215.0000; BIGipServerotn=149946890.50210.0000; _jc_save_fromStation=%u65E0%u9521%2CWXH; _jc_save_toStation=%u4E0A%u6D77%2CSHH; _jc_save_wfdc_flag=dc; RAIL_EXPIRATION=1556729706762; RAIL_DEVICEID=DEy8bOCvL69atDhuB2la4eg_AcENzEfhCi4LC0hws8AL_SN936vMUWmIfXPz_ZJXlN6gEmK4ugpiEdQUjdq1HVLkmuCRkFKyddAVw5PT1VyOUUp-dQhWa3lQdE88H_nW2QO7T9HYE614KXXnPfOd6zdXQrjuK_H4; _jc_save_fromDate=2019-04-30; _jc_save_toDate=2019-04-29");
        JSONObject json=httpUtils.PostCus(httpPost);
        System.out.println(json);
    }
}
