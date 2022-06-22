package src.main.java.com.ssh;

import com.jcraft.jsch.Session;

/**
 * @author guolinyuan
 */
public class SSHTest
{
    public static void main(String[] args) throws Exception
    {
        Session session =SSHLinuxUtils.createSession("1.15.99.139",22,"root", "Qazwsx!@34");

        try{
            SSHLinuxUtils.exeCommand(session, "cd /home" );
        }finally {
            session.disconnect();
        }

    }
}
