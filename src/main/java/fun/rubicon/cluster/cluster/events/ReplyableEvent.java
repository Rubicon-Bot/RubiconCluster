package fun.rubicon.cluster.cluster.events;

public interface ReplyableEvent {

    void reply(String invoke, String message);
}
