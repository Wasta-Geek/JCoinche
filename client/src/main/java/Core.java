import io.netty.channel.ChannelHandlerContext;

/**
 * Created by yuruh on 22/11/16.
 */
public class Core
{
    private static Core instance = new Core();

    private INetworkClient  client;
    private IEventManager   eventManager;

    private Core()
    {
//        param en dur pour l'instant
        this.client = new JCoincheClient();
        this.eventManager = new JCoincheEvents();
    }

    public static Core getInstance()
    {
        return instance;
    }

    public void shutdown()
    {
        this.client.shutdown();
    }
    public void run()
    {
        try
        {
            client.run();
            this.eventManager.init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean connectServer(String ip, int port)
    {
        return this.client.connectServer(ip, port);
    }

    public void interpretMessage(ServerData.Data data)
    {
        this.eventManager.interpretMessage(data);
    }

    public void sendMessage(ClientData.Data data)
    {
        this.client.sendMessage(data);
    }

    public void setContext(ChannelHandlerContext context)
    {
        this.client.setContext(context);
    }
}
