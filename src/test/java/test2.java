import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class test2 {
    static final int MAXTHREADCOUNT=10;
    public static void main(String[] args) {
        ExecutorService executorService= Executors.newFixedThreadPool(MAXTHREADCOUNT);
        for(int i=0;i<11;i++){
            int count=((ThreadPoolExecutor)executorService).getActiveCount();
            System.out.println("---"+count);
            if(count>=MAXTHREADCOUNT){
                System.out.println("服务器处于负载状态，请稍后再试！");
                return;
            }
            executorService.submit(() -> {
                try {
                    Thread.sleep(10000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }
    }

}
