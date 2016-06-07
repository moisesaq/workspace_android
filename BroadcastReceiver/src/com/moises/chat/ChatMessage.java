package com.moises.chat;

public class ChatMessage {

	public String message, time, pahtImage;
    public boolean isMine;
    public int audio;

    public ChatMessage(String message, String date, String pahtImage,int audio, boolean isMine){
        this.message = message;
        this.time = date;
        this.pahtImage = pahtImage;
        this.audio = audio;
        this.isMine = isMine;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public String getPahtImage() {
        return pahtImage;
    }

    public void setPahtImage(String pahtImage) {
        this.pahtImage = pahtImage;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }
}
