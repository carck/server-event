package org.lance.servetevent;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by lance.zhou on 2016/6/27.
 */
public class EventPushService implements ServletContextListener {
    private ConcurrentMap<String, AsyncContext> watchers = new ConcurrentHashMap<String, AsyncContext>();
    private BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<Message>();
    private HeartBeatThread heartBeatThread;
    private SenderThread senderThread;

    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("EventPushService", this);
        sce.getServletContext().setAttribute("AsyncTaskService", new AsyncTaskService(this));

        heartBeatThread = new HeartBeatThread(this);
        heartBeatThread.start();

        senderThread = new SenderThread(this);
        senderThread.start();
    }

    public void register(String requestId, AsyncContext asyncContext) {
        watchers.put(requestId, asyncContext);
    }

    public void publish(String requestId, String event, String data, boolean finish) {
        try {
            messageQueue.put(new Message(requestId, event, data, finish));
        } catch (InterruptedException e) {
            System.out.println("publish failed: " + e.getLocalizedMessage());
        }
    }

    public void heartBeat() {
        for (String requestId : watchers.keySet()) {
            this.publish(requestId, "ping", "~", false);
        }
    }

    public void doSend() {
        try {
            Message message = messageQueue.take();
            AsyncContext asyncContext = watchers.get(message.getRequestId());
            if (asyncContext != null) {
                PrintWriter writer = asyncContext.getResponse().getWriter();
                writer.write(message.getContent());
                writer.flush();
            }
            if (message.isFinish()) {
                asyncContext.complete();
                watchers.remove(message.getRequestId());
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("send failed: " + e.getLocalizedMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (heartBeatThread != null) {
            try {
                heartBeatThread.halt();
                heartBeatThread.interrupt();
                heartBeatThread.join();
            } catch (InterruptedException e) {
                System.out.println("stop heartBeatThread: " + e.getLocalizedMessage());
            }
        }
        if (senderThread != null) {
            try {
                senderThread.halt();
                senderThread.interrupt();
                senderThread.join();
            } catch (InterruptedException e) {
                System.out.println("stop senderThread: " + e.getLocalizedMessage());
            }
        }
    }
}
