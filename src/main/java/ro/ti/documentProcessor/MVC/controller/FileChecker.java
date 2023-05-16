package ro.ti.documentProcessor.MVC.controller;

import java.util.HashMap;

public class FileChecker {
   private static HashMap<String,Thread> threads;


    public static void checkForNewerVersion(String path)  {
         Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        //check if newer version
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
         thread.start();
         threads.put(path+"run",thread);
    }

    public boolean stopFunc(String path){
        threads.get(path+"run").interrupt();
        return true;

    }
}
