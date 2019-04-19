import com.sunrt.train.login.Captcha;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class test1 {

    public static void main(String[] args) {
        Captcha captcha=new Captcha();
        captcha.createPassCode();
    }
}
