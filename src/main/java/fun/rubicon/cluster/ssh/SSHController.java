package fun.rubicon.cluster.ssh;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;

import java.io.IOException;

public class SSHController {

    private final SSHClient sshClient;
    private final Session session;

    public SSHController(String host, String user, String password) throws IOException {
        sshClient = new SSHClient();
        sshClient.loadKnownHosts();

        sshClient.connect(host);
        sshClient.authPassword(user, password);
        session = sshClient.startSession();
    }

    public String send(String command) {
        try {
            Session.Command c = session.exec(command);
            return IOUtils.readFully(c.getInputStream()).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            session.close();
            sshClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
