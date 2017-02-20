import com.badlogic.gdx.Gdx;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * Created by yuruh on 22/11/16.
 */
public class clientNetty extends SimpleChannelInboundHandler<ServerData.Data>
{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerData.Data data)
            throws Exception
    {
        System.out.println("READ0 CLIENT");
        System.out.println(data);
        Core.getInstance().interpretMessage(data);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        Core.getInstance().setContext(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        System.out.println("SERVER OFFLINE");
        ctx.close();
        Gdx.app.exit();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
