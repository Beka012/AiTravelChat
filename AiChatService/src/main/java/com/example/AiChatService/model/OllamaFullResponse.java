package com.example.AiChatService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OllamaFullResponse {
    private String model;
    private String created_at;
    private String response;
    private boolean done;
    private String done_reason;

    public OllamaFullResponse() {}

    public OllamaFullResponse(String model, String created_at, String response, boolean done, String done_reason) {
        this.model = model;
        this.created_at = created_at;
        this.response = response;
        this.done = done;
        this.done_reason = done_reason;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDone_reason() {
        return done_reason;
    }

    public void setDone_reason(String done_reason) {
        this.done_reason = done_reason;
    }

    @Override
    public String toString() {
        return "OllamaFullResponse{" +
                "model='" + model + '\'' +
                ", created_at='" + created_at + '\'' +
                ", response='" + response + '\'' +
                ", done=" + done +
                ", done_reason='" + done_reason + '\'' +
                '}';
    }
}
