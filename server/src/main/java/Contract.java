import java.util.ArrayList;

/**
 * Created by yuruh on 22/11/16.
 */
public class Contract
{
    private int          value;
    private Team         team;
    private CoincheState state;
    private boolean      isCapot;
    private int          finalResult;

    public Card.Color getTrump()
    {
        return trump;
    }

    private Card.Color   trump;

    public Contract()
    {
        this.value = 70;
        this.state = CoincheState.NONE;
        this.isCapot = false;
        this.team = null;
    }

    public void computeResult()
    {
        boolean fulfilled = false;
        if (this.team.getScore() >= this.value)
            fulfilled = true;
        if (fulfilled)
        {
            this.finalResult = 160 - this.team.getScore();
            if (this.isCapot)
                this.team.setScore(500);
            if (this.state == CoincheState.SURCOINCHE)
                this.team.setScore(this.team.getScore() + this.value * 3);
            else
                this.team.setScore(this.team.getScore() + this.value);
        }
        else
        {
            this.team.setScore(0);
            if (this.state == CoincheState.COINCHE)
                this.finalResult = this.value * 2 + 160;
            else
                this.finalResult = this.value + 160;
        }
    }

    public ServerData.Data.Bid  getBid()
    {
        ServerData.Data.Bid.Builder builder = ServerData.Data.Bid.newBuilder();
        int value = this.value + 10;

        if (value <= 160 && !this.isCapot)
        {
            builder.setMin(value)
                    .setMax(160);
        }
        else
        {
            builder.setMin(80)
                    .setMax(160);
        }
        return builder.build();
    }

    public boolean isContractReady()
    {
        return (this.team != null);
    }

    public void setBid(ClientData.Data data, IPlayer player)
    {
        if (data.getBid().getType() == ClientData.Data.BidType.BID)
        {
            this.value = data.getBid().getBet();
            if (this.value >= 160)
                this.isCapot = true;
            this.team = player.getTeam();
            this.trump = Card.convert(data.getBid().getTrump());
        }
        else if (data.getBid().getType() == ClientData.Data.BidType.CAPOT)
        {
            this.isCapot = true;
            this.team = player.getTeam();
            this.trump = Card.convert(data.getBid().getTrump());
        }
    }

    public boolean isCapot()
    {
        return this.isCapot;
    }

    public ArrayList<Card> getPlayableCards(ArrayList<Card> cards, Card.Color main, Card leading)
    {
        ArrayList<Card> playable = new ArrayList<Card>();

        for (Card card : cards)
            if (main == null || card.getColor() == main)
                playable.add(card);
        if (playable.size() == 0)
            for (Card card : cards)
                if ((card.getColor() == this.trump) && ((leading == null) ||
                        (leading.getColor() == this.trump && card.getHighValue() > leading.getHighValue())))
                    playable.add(card);
        if (playable.size() == 0)
            for (Card card : cards)
                if (card.getColor() == this.trump)
                    playable.add(card);
        if (playable.size() == 0)
            for (Card card : cards)
                playable.add(card);
        return playable;
    }

    public void setState(CoincheState state)
    {
        this.state = state;
    }

    public Team getTeam()
    {
        return team;
    }

    public int getFinalResult()
    {
        return finalResult;
    }

    public enum CoincheState
    {
        NONE,
        COINCHE,
        SURCOINCHE
    }
}
