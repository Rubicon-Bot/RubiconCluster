package fun.rubicon.cluster.ssh;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class SSHController {

    private final String host;
    private final Session session;
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    public SSHController(String host, String user, String password) throws JSchException {
        this.host = host;

        JSch jSch = new JSch();
        JSch.setConfig("StrictHostKeyChecking", "no");
        session = jSch.getSession(user, host, 22);
        session.setPassword(password);

        session.connect();
    }

    public void send(String command) {
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.setCommand(command);
            channel.connect();
            BufferedReader output = new BufferedReader(new InputStreamReader(channel.getErrStream()));
            logger.info(String.format("SSH (%s): %s", host, output.lines().collect(Collectors.joining())));
            output.close();
            in.close();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) channel.disconnect();
        }
    }

    public void close() {
        session.disconnect();
    }
}
