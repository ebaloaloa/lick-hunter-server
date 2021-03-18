package com.lickhunter.web.events;

import org.springframework.context.ApplicationEvent;

public class BinanceEvents extends ApplicationEvent {
    private String message;

    public BinanceEvents(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
