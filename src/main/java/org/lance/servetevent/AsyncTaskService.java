package org.lance.servetevent;

/**
 * Created by lance.zhou on 2016/6/27.
 */
public class AsyncTaskService {
    EventPushService eventPushService;

    public AsyncTaskService(EventPushService eventPushService) {
        this.eventPushService = eventPushService;
    }

    public void doLongTimeTask(final String requestId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                eventPushService.publish(requestId, "message", requestId, true);
            }
        }).start();
    }
}
