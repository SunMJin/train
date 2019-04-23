import com.sunrt.train.buy.ArriveDate;
import com.sunrt.train.buy.STUM;
import com.sunrt.train.buy.SeatType;
import com.sunrt.train.login.Captcha;
import com.sunrt.train.login.Login;
import com.sunrt.train.screening.SearchTickets;
import com.sunrt.train.screening.Stations;
import com.sunrt.train.screening.cP;
import com.sunrt.train.screening.cR;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Scanner;

public class test1 {

    public static void testLogin(){
        Login login=new Login();
        Captcha captcha=new Captcha();
        captcha.createPassCode();
        String username="1036524012@qq.com";
        login.setUsername(username);
        char pw[]=new String("lol9403J").toCharArray();
        login.setPassword(pw);
        captcha.setVisible(true);
    }




    public static void main(String[] args) {



       /* System.out.println("请选择座位类型：");
        SeatType sts[]=SeatType.getSeatTypes();
        for(int i=0;i<sts.length;i++){
            System.out.println(i+":"+sts[i].desc);
        }
        System.out.println("请输入出行日期：（格式如：2019-01-01）");
        String train_date=sc.nextLine();*/


        //System.out.println("请输入到达站名称：");
        //String to_station=sc.nextLine();

        /*Integer index=null;
        while(true){
            try{
                index=Integer.parseInt(sc.nextLine());
                if(index<0||index>sts.length-1){
                    System.out.println("请指定一个有效的数字");
                }else{
                    break;
                }
            }catch (NumberFormatException e){
                System.out.println("无效的输入");
            }
        }

        List<cR> list= SearchTickets.search(train_date,from_station,to_station,"ADULT");
        for(cR cr:list){
            cP cp=cr.queryLeftNewDTO;
            if("Y".equals(cp.canWebBuy)){
                String countStr=getSeatCountStr(sts[index].stum,cp);
                if(countStr!=null){
                    System.out.println(cr.queryLeftNewDTO.station_train_code+":  "+countStr);
                }
            }
        }*/

    }
}