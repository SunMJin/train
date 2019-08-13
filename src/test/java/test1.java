import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.*;

public class test1 {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        CookieStore cs=new BasicCookieStore();
        String file="cookies";
//        ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(file));
//        os.writeObject(cs);
//        os.close();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));){
            CookieStore ck=(CookieStore)inputStream.readObject();
            System.out.println(ck);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }
}