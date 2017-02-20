import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

/**
 * Created by yuruh on 21/11/16.
 */
public class Player implements IPlayer
{
    private ChannelHandlerContext ctx;
    private String nickname;
    private ArrayList<Card> cards;
    private Team            team;
    private boolean         isReady;

    Player(ChannelHandlerContext ctx)
    {
        this.nickname = "Unknown nick";
        this.cards = new ArrayList<Card>();
        this.ctx = ctx;
        this.isReady = false;
    }

    public void write(ServerData.Data data)
    {
        System.out.print("Sending to ");
        System.out.print(this.nickname);
        System.out.println( " :");
        System.out.println(data);
        ctx.writeAndFlush(data);
    }

    public void setReady(boolean ready)
    {
        isReady = ready;
    }

    public boolean getReady()
    {
        return this.isReady;
    }

    public Team getTeam()
    {
        return this.team;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public void setTeam(Team team)
    {
        this.team = team;
        this.team.addPlayer(this);
    }

    public String getNickname()
    {
        return this.nickname;
    }

    public void addCard(Card card)
    {
        this.cards.add(card);
    }

    public ArrayList<Card> getCards()
    {
        return this.cards;
    }

}
