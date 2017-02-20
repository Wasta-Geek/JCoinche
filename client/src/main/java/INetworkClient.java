/**
 * Created by wasta-geek on 14/11/16.
 */
public interface INetworkClient extends IClient{
    void run();
    void sendMessage(ClientData.Data data);
    boolean connectServer(String ip, int port);
    void shutdown();
    void setContext(Object context);
}
