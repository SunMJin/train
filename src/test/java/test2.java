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
        //BuyTicketHandle.start(new Param(null,"2019-05-02","WXH","CCT","ADULT","G",new int[]{20,32},null,"无锡","长春","dc"));
    }
}
