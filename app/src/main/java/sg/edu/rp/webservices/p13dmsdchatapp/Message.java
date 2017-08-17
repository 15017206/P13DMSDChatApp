package sg.edu.rp.webservices.p13dmsdchatapp;

import java.io.Serializable;

/**
 * Created by 15017206 on 15/8/2017.
 */

public class Message implements Serializable{
//    String userId;
    String messageText;
    String messageTime;
    String userName;

    public Message() {
    }

    public Message(String messageText, String messageTime, String userName) {
//        this.userId = userId;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.userName = userName;
    }



//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
