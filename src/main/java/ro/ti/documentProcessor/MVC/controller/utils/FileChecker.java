package ro.ti.documentProcessor.MVC.controller.utils;

import ro.ti.documentProcessor.MVC.Interfaces.Controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class FileChecker {
   private static HashMap<String,Thread> threads = new HashMap<>();
    private static String pattern = "MM/dd/yyyy E kk:mm:ss";
    private static SimpleDateFormat format = new SimpleDateFormat(pattern);

    public static synchronized void checkForNewerVersion(Controller controller, String path)  {
         Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                long timeStamp=  file.lastModified();
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        if (timeStamp!= file.lastModified()){
                            //actions if file was modified here
                            controller.reloadFile(path, new Timestamp(file.lastModified()) );
                            //("File modified at: " + format.format(new Date(file.lastModified())));
                            timeStamp=file.lastModified();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
         thread.start();
         threads.put(path+"run",thread);
    }

    public static synchronized boolean stopDocumentChecker(String path){
        threads.get(path+"run").interrupt();
        return true;
    }
}
