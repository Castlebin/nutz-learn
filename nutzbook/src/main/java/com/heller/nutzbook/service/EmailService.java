package com.heller.nutzbook.service;

public interface EmailService {

    boolean send(String to, String subject, String html);

}
