package com.lt.jerrymous;

import com.lt.jerrymous.connector.HttpConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Start {
    static Logger logger = LoggerFactory.getLogger(Start.class);
    public static void main(String[] args) {
        try(HttpConnector connector = new HttpConnector()){
            for(;;){
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("jerrymouse http server was shutdown.");
    }
}
