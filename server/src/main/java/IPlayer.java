import java.util.ArrayList;

/**
 * Created by yuruh on 21/11/16.
 */
public interface IPlayer
{
    void    write(ServerData.Data data);
    void    addCard(Card card);
    ArrayList<Card> getCards();
    void    setNickname(String nickname);
    void    setTeam(Team team);
    String getNickname();
    void    setReady(boolean ready);
    boolean getReady();
    Team getTeam();
}
