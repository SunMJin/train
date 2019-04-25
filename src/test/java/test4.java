import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test4 {

    public static void main(String[] args) {
        String str="[{\"start_time\":null,\"start_station_name\":null,\"end_station_name\":null,\"end_time\":null,\"id\":\"O\",\"value\":\"二等座\"}]";
        String reg="(?<=\"id\":\")\\w*(?=\",\"value\":\"二等座\")";
        Matcher matcher=Pattern.compile(reg).matcher(str);
        if(matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
