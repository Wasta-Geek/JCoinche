/**
 * Created by yuruh on 22/11/16.
 */
public class Core
{
    private static Core instance = new Core();

    private IServer         server;
    private IGame           game;

    private Core()
    {
        this.server = new serverNettyHandler(8080);
        this.game = new Game();
    }

    static Core getInstance()
    {
        return instance;
    }

    public boolean  acceptConnection()
    {
        return !this.game.hasMaxPlayers();
    }

    void addPlayer(int id, IPlayer player)
    {
        this.game.addPlayer(id, player);
    }

    void run()
    {
        try
        {
            server.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void interpretMessage(ClientData.Data data, int id)
    {
        this.game.interpretMessage(data, id);
    }

    void removePlayer(int id)
    {
        this.game.removePlayer(id);
    }
}
