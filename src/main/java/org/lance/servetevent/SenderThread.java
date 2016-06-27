package org.lance.servetevent;

/**
 * Created by lance.zhou on 2016/6/27.
 */
public class SenderThread extends Thread {
    private final EventPushService eventPushService;
    private boolean run = true;

    public SenderThread(EventPushService eventPushService) {
        this.eventPushService = eventPushService;
    }

    @Override
    public void run() {
        super.run();
        while (run) {
            eventPushService.doSend();
        }
    }

    void halt() {
        run = false;
    }
}
