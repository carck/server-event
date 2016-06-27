package org.lance.servetevent;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lance.zhou on 2016/6/12.
 * https://docs.oracle.com/javaee/7/tutorial/servlets012.htm
 */
public class AsyncTaskServlet extends HttpServlet { //thread per request: tomcat

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0); //default 10s

        ServletContext servletContext = req.getServletContext();

        resp.setHeader("Content-Type", "text/event-stream; charset=UTF-8"); //chrome don't other than utf-8

        String requestId = UUID.randomUUID().toString();

        EventPushService eventPushService = (EventPushService) servletContext.getAttribute("EventPushService");
        eventPushService.register(requestId, asyncContext);

        AsyncTaskService asyncTaskService = (AsyncTaskService) servletContext.getAttribute("AsyncTaskService");
        asyncTaskService.doLongTimeTask(requestId);
    }
}
