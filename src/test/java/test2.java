import com.sunrt.train.data.Stations;
import com.sunrt.train.login.Login;
import com.sunrt.train.ticket.BuyTicket;
import com.sunrt.train.ticket.Params;
import com.sunrt.train.ticket.Seats;

public class test2 {
    public static void main(String[] args) {
        //初始化站点
        if(!Stations.init()){
            System.out.println("站点初始化失败");
            return;
        }
        //登陆
        Login.testmain();
        //Params.getParams();
    }
}
