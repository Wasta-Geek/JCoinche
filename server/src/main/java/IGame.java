/**
 * Created by yuruh on 22/11/16.
 */
public interface IGame
{
    void interpretMessage(ClientData.Data data, int id);
    boolean hasMaxPlayers();
    void    addPlayer(int id, IPlayer player);
    void    removePlayer(int id);
}
