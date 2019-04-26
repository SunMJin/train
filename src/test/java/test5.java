import com.sunrt.train.utils.HttpUtils;
import org.apache.http.client.fluent.Form;

import java.io.*;

public class test5 {
    public static void main(String[] args) throws Exception {
        String x=HttpUtils.GetStr("http://www.baidu.com/s", Form.form().add("wd", "1").build());
        System.out.println(x);
    }
}
