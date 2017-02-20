import java.util.LinkedList;

/**
 * Created by yuruh on 21/11/16.
 */

public class Team
{
    private LinkedList<IPlayer> players;
    private int maxSize;
    private int score;

    Team()
    {
        this.players = new LinkedList<IPlayer>();
        this.maxSize = 2;
        this.score = 0;
    }

    void addPlayer(Player player)
    {
        this.players.add(player);
    }

    boolean isFull()
    {
        return this.players.size() == this.maxSize;
    }

    public LinkedList<IPlayer> getPlayers()
    {
        return players;
    }

    public ServerData.Data.Team toServerData()
    {
        ServerData.Data.Team.Builder builder = ServerData.Data.Team.newBuilder();
        builder.setPlayer1(players.getFirst().getNickname());
        builder.setPlayer2(players.getLast().getNickname());
        return builder.build();
    }

    public void addScore(int value)
    {
        this.score += value;
    }

    public ServerData.Data writeScores(Team enemyTeam)
    {
        ServerData.Data.Builder builder = ServerData.Data.newBuilder();
        builder.setId(10);
        builder.setScore(ServerData.Data.Score.newBuilder()
                .setMyScore(this.score)
                .setEnemyScore(enemyTeam.score)
                .build());
        return builder.build();
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
}
