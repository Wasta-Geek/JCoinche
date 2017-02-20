import io.netty.channel.ChannelHandlerContext;
import org.junit.Assert;

import static org.junit.Assert.*;

/**
 * Created by yuruh on 22/11/16.
 */
public class GameTest
{
    @org.junit.Test
    public void hasMaxPlayers() throws Exception
    {
        Assert.assertEquals(1, 1);
    }

    @org.junit.Test
    public void addPlayer() throws Exception
    {

    }

    @org.junit.Test
    public void removePlayer() throws Exception
    {

    }

    @org.junit.Test
    public void interpretMessage() throws Exception
    {
        Game game = new Game();

        assertEquals(game.hasMaxPlayers(), false);
    }

    @org.junit.Test
    public void addPlayer1() throws Exception
    {

    }

}