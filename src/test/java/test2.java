import com.sunrt.train.data.Stations;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.login.Login;
import com.sunrt.train.ticket.BuyTicketHandle;
import com.sunrt.train.ticket.Param;
import com.sunrt.train.ticket.Reservation;
import com.sunrt.train.ticket.Stum;
import org.json.JSONObject;

public class test2 {
    public static void main(String[] args) throws HttpException {
        //初始化站点
        if(!Stations.init()){
            System.out.println("站点初始化失败");
            return;
        }
        BuyTicketHandle.start(new Param(new Stum[]{Stum.yz,Stum.ze},"2019-05-03","WXH","SHH","ADULT",null,null,null,"无锡","上海","dc"));
        //登陆
      //  Login.testmain();
    }
}
