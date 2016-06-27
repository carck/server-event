package org.lance.servetevent;

/**
 * Created by lance.zhou on 2016/6/27.
 */
public class HeartBeatThread extends Thread {
    private final EventPushService eventPushService;
    private boolean run = true;

    public HeartBeatThread(EventPushService eventPushService) {
        this.eventPushService = eventPushService;
    }

    @Override
    public void run() {
        super.run();

        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            eventPushService.heartBeat();
        }
    }

    void halt() {
        run = false;
    }
}
