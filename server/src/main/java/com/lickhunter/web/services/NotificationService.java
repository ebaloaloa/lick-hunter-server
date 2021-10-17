package com.lickhunter.web.services;

public interface NotificationService<MESSAGE> {
    void send(MESSAGE message);
}
