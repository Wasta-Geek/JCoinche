import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Created by yuruh on 21/11/16.
 */

public class Game implements IGame
{
    private IDealer                     dealer;
    private TreeMap<Integer, IPlayer>   players;
    private LinkedList<Team>            teams;
    private Stack<Round>                rounds;

    Game()
    {
        this.dealer = new Dealer();
        this.players = new TreeMap<Integer, IPlayer>();
        this.teams = new LinkedList<Team>();
        this.rounds = new Stack<Round>();
        this.teams.add(new Team());
        this.teams.add(new Team());
    }

    public boolean hasMaxPlayers()
    {
        return this.players.size() == 4;
    }

    public void addPlayer(int id, IPlayer player)
    {
        for (Team team : this.teams)
        {
            if (!team.isFull())
            {
                player.setTeam(team);
                break;
            }
        }
        this.players.put(id, player);
    }

    private ServerData.Data getNickAnswer(ClientData.Data data)
    {
        ServerData.Data.Builder builder = ServerData.Data.newBuilder();

        builder.setId(1);
        builder.setIsSuccess(true);
        for (IPlayer p : players.values())
        {
            if (p.getNickname().equals(data.getPseudo()))
            {
                builder.setIsSuccess(false);
                builder.setErrorMsg("Nickname already taken");
                return builder.build();
            }
        }
        builder.setClientType(ServerData.Data.ClientType.PLAYER);
        return builder.build();
    }

    private void setPlayerNick(ClientData.Data data, int id)
    {
        IPlayer player = this.players.get(id);
        player.setNickname(data.getPseudo());
        player.setReady(true);
        for (IPlayer p: this.players.values())
            if (!p.getReady() || !this.hasMaxPlayers())
                return;
        this.startGame();
    }

    public void removePlayer(int id)
    {
        this.teams.remove(this.players.get(id));
        this.players.remove(id);
        for (IPlayer player : this.players.values())
            player.getCards().clear();
        if (this.rounds.size() > 0)
        {
            this.rounds.clear();
            for (IPlayer player : this.players.values() )
            {
                player.write(ServerData.Data.newBuilder()
                        .setId(666)
                        .setErrorMsg("GAME RESTART")
                        .build());
            }
        }
    }

    private void startRound()
    {
        this.dealer.deal(this.players.values());
        for (IPlayer player : this.players.values())
        {
            ServerData.Data.Builder data = ServerData.Data.newBuilder();
            data.setId(2);
            ArrayList<Card> cards = player.getCards();
            for (Card card : cards)
                data.addCard(card.toServerDataCard());
            player.write(data.build());
        }
        if (this.rounds.size() == 0)
        {
            LinkedList<IPlayer> players = new LinkedList<IPlayer>();
            players.add(this.teams.getFirst().getPlayers().getFirst());
            players.add(this.teams.getLast().getPlayers().getFirst());
            players.add(this.teams.getFirst().getPlayers().getLast());
            players.add(this.teams.getLast().getPlayers().getLast());
            this.rounds.add(new Round(players));
        }
        else
            this.rounds.add(new Round(this.rounds.peek()));
        this.rounds.peek().notifyBid();
    }

    private void startGame()
    {
        for (IPlayer player : this.teams.getFirst().getPlayers())
        {
            ServerData.Data.Builder data = ServerData.Data.newBuilder();
            data.setId(3);
            data.setMyTeam(this.teams.getFirst().toServerData());
            data.setEnemyTeam(this.teams.getLast().toServerData());
            player.write(data.build());
        }
        for (IPlayer player : this.teams.getLast().getPlayers())
        {
            ServerData.Data.Builder data = ServerData.Data.newBuilder();
            data.setId(3);
            data.setMyTeam(this.teams.getLast().toServerData());
            data.setEnemyTeam(this.teams.getFirst().toServerData());
            player.write(data.build());
        }
        this.startRound();
    }

    public void interpretMessage(ClientData.Data data, int id)
    {
        System.out.println("Interpreting client message :");
        ServerData.Data answer = null;
        switch (data.getId())
        {
            case 1:
                answer = this.getNickAnswer(data);
                if (answer.getIsSuccess())
                    this.setPlayerNick(data, id);
                break;
            case 2:
                this.setBid(data, id);
                break;
            case 3:
                if (this.rounds.peek().setCoinche(data, id))
                    this.rounds.peek().notifyGameTurn();
                break;
            case 4:
                if (this.rounds.peek().setSurcoinche(data, id))
                    this.rounds.peek().notifyGameTurn();
                break;
            case 5:
                this.playCard(data, id);
                if (this.rounds.peek().isRoundOver())
                    this.startRound();
                break;
        }
        if (answer != null)
            this.players.get(id).write(answer);
    }

    private void playCard(ClientData.Data data, int id)
    {
        this.rounds.peek().playCard(data, this.players.get(id));
    }

    private void setBid(ClientData.Data data, int id)
    {
        if (this.rounds.peek().setBid(data, this.players.get(id)))
            if (this.rounds.peek().isBidOver())
            {
                this.rounds.peek().resetSkipped();
//                this.rounds.peek().setPreviousCurrentPlayer();
                this.rounds.peek().notifyCoinche();
            }
            else
                this.rounds.peek().notifyBid();
        else
        {
            this.rounds.remove(this.rounds.peek());
            this.startRound();
        }
    }
}