package fun.rubicon.cluster.ssh;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SSHBotCredentials {

    private final String user;
    private final String host;
    private final String password;
    private final String path;

    public static SSHBotCredentials parse(String raw) {
        String user = raw.split("@")[0];
        String host = raw.split("@")[1].split(":")[0];
        String password = raw.split("@")[1].split(":")[1].split("=")[0];
        String path = raw.split("@")[1].split(":")[1].split("=")[1];

        return new SSHBotCredentials(user, host, password, path);
    }
}
