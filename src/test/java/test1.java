import com.sunrt.train.login.Captcha;
import com.sunrt.train.login.Login;

public class test1 {

    public static void main(String[] args) {
        Login login=new Login();
        Captcha captcha=new Captcha();
        captcha.createPassCode();
        String username="1036524012@qq.com";
        login.setUsername(username);
        char pw[]=new String("lol9403J").toCharArray();
        login.setPassword(pw);
        captcha.setVisible(true);
    }

}
