package ua.kiev.prog;

public class PrivateMessage {
    private String to;
    private String text;
    public PrivateMessage(String to, String text) {
        this.to = to;
        this.text = text;
    }

    public PrivateMessage() {
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String send() {
        return new StringBuilder().append(to).append("@").append(text)
                .toString();
    }
}
