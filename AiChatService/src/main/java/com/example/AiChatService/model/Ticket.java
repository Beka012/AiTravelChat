package com.example.AiChatService.model;

public class Ticket {
    String from;
    String to;
    String data;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
