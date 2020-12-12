package com.achiever.menschenfahren.service;

/**
 * CreatedBy : edangol
 * CreatedOn : 27/11/2020
 * Description :
 **/
public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String attachMentName, String pathToAttachment);
}
