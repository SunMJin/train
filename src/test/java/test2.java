import com.sunrt.train.data.Stations;
import com.sunrt.train.login.Login;

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
        //BuyTicketHandle.start(new Param(null,"2019-04-27","WXH","SHH","ADULT",null,null,null,"无锡","上海","dc"));
    }
}
