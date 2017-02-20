/**
 * Created by wasta-geek on 14/11/16.
 */
public interface IEventManager
{
    void init();
    void interpretMessage(ServerData.Data data);
    void launchMenu();
    void launchGame();
}
