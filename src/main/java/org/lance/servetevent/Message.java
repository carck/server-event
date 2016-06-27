package org.lance.servetevent;

import java.util.UUID;

/**
 * Created by lance.zhou on 2016/6/27.
 */
public class Message {
    private final String id;
    private final String requestId;
    private final String type;
    private final String data;
    private final boolean finish;

    public Message(String requestId, String type, String data, boolean finish) {
        this.id = UUID.randomUUID().toString();
        this.requestId = requestId;
        this.type = type;
        this.data = data;
        this.finish = finish;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isFinish() {
        return finish;
    }

    public String getContent() {
        return String.format("id: %s\nevent: %s\ndata: %s\n\n", getId(), getType(), getData()); //named event, id is require from chrome
    }
}
