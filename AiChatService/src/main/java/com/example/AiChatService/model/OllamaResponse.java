package com.example.AiChatService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OllamaResponse {
    private String action;
    private Ticket ticket;
    private Tour tour;
    private String text; // Новое поле для ModelToText action

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "OllamaResponse{" +
                "action='" + action + '\'' +
                ", ticket=" + ticket +
                ", tour=" + tour +
                ", text='" + text + '\'' +
                '}';
    }
}