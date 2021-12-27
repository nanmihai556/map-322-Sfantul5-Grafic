package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Message extends Entity<Long> {
    private Long from;
    private ArrayList<Long> to;
    private String message;
    private LocalDateTime date;
    private int reply;

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getReply() {
        return reply;
    }

    public Message(Long from, ArrayList<Long> to, String message, LocalDateTime date, Integer reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = reply;
    }

    public Long getFrom() {
        return from;
    }

    public ArrayList<Long> getTo() {
        return to;
    }

    public String getToHashed() {
        return to.stream().map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

}
