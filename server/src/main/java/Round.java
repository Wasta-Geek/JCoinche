import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by yuruh on 22/11/16.
 */
public class Round
{
    private LinkedList<IPlayer> players;
    private Contract            contract;
    private int                 skipped;
    private int                 currentIdx;
    private TreeMap<ClientData.Data.BidType, ServerData.Data.BidType> typeCorresp;
    private Card.Color          plisColor;
    private Card                leading;
    private ArrayList<Card>     cardsPlayed;
    private IPlayer             winningPlayer;


    public Round(LinkedList<IPlayer> players)
    {
        this.init();
        this.players = players;
    }

    public Round(Round round)
    {
        this.init();
        this.players = round.getPlayers();
        this.currentIdx = round.currentIdx + 1;
        if (this.currentIdx == this.players.size())
            this.currentIdx = 0;
    }

    private void init()
    {
        this.cardsPlayed = new ArrayList<Card>();
        this.contract = new Contract();
        this.skipped = 0;
        this.currentIdx = 0;
        this.typeCorresp = new TreeMap<ClientData.Data.BidType, ServerData.Data.BidType>();
        this.typeCorresp.put(ClientData.Data.BidType.BID, ServerData.Data.BidType.BID);
        this.typeCorresp.put(ClientData.Data.BidType.CAPOT, ServerData.Data.BidType.CAPOT);
        this.typeCorresp.put(ClientData.Data.BidType.SKIP, ServerData.Data.BidType.SKIP);
        this.plisColor = null;
        this.leading = null;
    }

    public boolean isBidOver()
    {
        return (this.skipped == 3 && this.contract.isContractReady()) || this.contract.isCapot();
    }

    public boolean isRoundOver()
    {
        return this.players.get(this.currentIdx).getCards().size() == 0;
    }

    public LinkedList<IPlayer> getPlayers()
    {
        return players;
    }


    public boolean setBid(ClientData.Data data, IPlayer player)
    {
        if (data.getBid().getType() == ClientData.Data.BidType.SKIP)
            this.skipped++;
        else
            this.resetSkipped();
        this.contract.setBid(data, player);
        for (int i = 0; i < this.players.size(); i++)
        {
            if (i != this.currentIdx)
            {
                this.players.get(i).write(ServerData.Data.newBuilder()
                        .setId(5)
                        .setPlayerBid(ServerData.Data.PlayerBid.newBuilder()
                                .setType(this.typeCorresp.get(data.getBid().getType()))
                                .setValue(data.getBid().getBet())
                                .setTrump(Card.convert(Card.convert(data.getBid().getTrump())))
                                .setNick(player.getNickname())
                                .build())
                        .build());
            }
        }
        this.currentIdx++;
        if (this.currentIdx == this.players.size())
            this.currentIdx = 0;
        return this.skipped < 4;
    }

    public void notifyBid()
    {
        this.players.get(currentIdx).write(ServerData.Data.newBuilder()
            .setId(4)
            .setBid(this.contract.getBid())
            .build());
    }

    public void notifyCoinche()
    {
        if (this.contract.getTeam().equals(this.getPlayers().get(this.currentIdx).getTeam()))
        {
            this.currentIdx++;
            if (this.currentIdx == this.players.size())
                this.currentIdx = 0;
        }
        this.players.get(currentIdx).write(ServerData.Data.newBuilder()
            .setId(6)
            .build());
    }

    public void notifyGameTurn()
    {
        ArrayList<Card> playableCards = this.contract.getPlayableCards(this.players.get(currentIdx).getCards(), plisColor, leading);

        ServerData.Data.Builder builder = ServerData.Data.newBuilder();
        builder.setId(8);
        for (Card card: playableCards)
            builder.addCard(card.toServerDataCard());
        this.players.get(currentIdx).write(builder.build());
    }

    public void playCard(ClientData.Data data, IPlayer player)
    {
        Card played = null;

        for (Card card : player.getCards())
        {
            if (card.equals(data.getCard()))
            {
                played = card;
                player.getCards().remove(card);
                break;
            }
        }
        if (this.cardsPlayed.size() == 0)
        {
            assert played != null;
            this.plisColor = played.getColor();
            this.leading = played;
            this.winningPlayer = player;
        }
        else
        {
            assert played != null;
            if (played.beats(this.leading, this.contract.getTrump()))
            {
                this.leading = played;
                this.winningPlayer = player;
            }
        }
        this.cardsPlayed.add(played);

        for (int i = 0; i < this.players.size(); i++)
        {
            if (i != this.currentIdx)
            {
                this.players.get(i).write(ServerData.Data.newBuilder()
                        .setId(9)
                        .addCard(played.toServerDataCard())
                        .build());
            }
        }
        this.currentIdx++;
        if (this.currentIdx == this.players.size())
            this.currentIdx = 0;
        if (this.cardsPlayed.size() == this.players.size())
            this.startNewPlis();
        if (this.players.get(currentIdx).getCards().size() > 0)
            this.notifyGameTurn();
        else
            this.sendRoundResult();
    }

    private void startNewPlis()
    {
        int winnings = 0;
        for (Card card : this.cardsPlayed)
            winnings += card.getValue(this.contract.getTrump());

        this.winningPlayer.getTeam().addScore(winnings);
        for (int i = 0; i < this.players.size(); i++)
        {
            if (i != this.players.size() - 1)
                players.get(i).write(players.get(i).getTeam().writeScores(players.get(i + 1).getTeam()));
            else
                players.get(i).write(players.get(i).getTeam().writeScores(players.get(0).getTeam()));
        }
        this.leading = null;
        this.plisColor = null;
        this.winningPlayer = null;
        this.cardsPlayed.clear();
    }

//    retorune true s'il faut lancer la partie
    public boolean setCoinche(ClientData.Data data, int id)
    {
        if (data.getSuccess())
        {
            this.contract.setState(Contract.CoincheState.COINCHE);
            this.notifySurcoinche();
        }
        else
        {
            skipped++;
            if (skipped == 2)
                return (true);
            else
            {
                this.currentIdx++;
                if (this.currentIdx == this.players.size())
                    this.currentIdx = 0;
                this.notifyCoinche();
            }
        }
        return false;
    }

    private void notifySurcoinche()
    {
        this.currentIdx++;
        if (this.currentIdx == this.players.size())
            this.currentIdx = 0;
        this.players.get(currentIdx).write(ServerData.Data.newBuilder()
                .setId(7)
                .build());
    }

    public void resetSkipped()
    {
        this.skipped = 0;
    }

    public boolean setSurcoinche(ClientData.Data data, int id)
    {
        if (data.getSuccess())
        {
            this.contract.setState(Contract.CoincheState.SURCOINCHE);
            return true;
        }
        else
        {
            skipped++;
            if (skipped == 2)
                return (true);
            else
            {
                this.currentIdx++;
                if (this.currentIdx == this.players.size())
                    this.currentIdx = 0;
                this.notifySurcoinche();
            }
        }
        return false;
    }

    private void sendRoundResult()
    {
        this.contract.computeResult();
        for (IPlayer p : this.players)
        {
            ServerData.Data.Builder builder = ServerData.Data.newBuilder();
            builder.setId(11);
            if (p.getTeam().equals(this.contract.getTeam()))
                builder.setScore(ServerData.Data.Score.newBuilder()
                        .setMyScore(this.contract.getTeam().getScore())
                        .setEnemyScore(this.contract.getFinalResult())
                        .build());
            else
                builder.setScore(ServerData.Data.Score.newBuilder()
                        .setMyScore(this.contract.getFinalResult())
                        .setEnemyScore(this.contract.getTeam().getScore())
                        .build());
            p.write(builder.build());
        }
    }
}
