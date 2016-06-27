package org.lance.servetevent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lance.zhou on 2016/6/27.
 */
public class AsyncTaskService {
    EventPushService eventPushService;
    ExecutorService executorService;

    public AsyncTaskService(EventPushService eventPushService) {
        this.eventPushService = eventPushService;
        this.executorService = Executors.newFixedThreadPool(100); //do not flush threads
    }

    public void doLongTimeTask(final String requestId) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                eventPushService.publish(requestId, "message", requestId, true);
            }
        });
    }
}
