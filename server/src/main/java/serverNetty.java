/**
 * Created by wasta-geek on 14/11/16.
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class serverNetty extends SimpleChannelInboundHandler<ClientData.Data>
{
    private int     id;

    serverNetty(int id)
    {
        this.id = id;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.print("CLIENT ");
        System.out.print(this.id);
        System.out.println(" CONNECTED");
        if (Core.getInstance().acceptConnection())
            Core.getInstance().addPlayer(id, new Player(ctx));
        else
        {
            ctx.writeAndFlush(ServerData.Data.newBuilder()
                .setId(501)
                .setIsSuccess(false)
                .setErrorMsg("The server is full")
                .build());
            System.out.print("CLIENT ");
            System.out.print(this.id);
            System.out.println(" WAS REFUSED CONNECTION");
            ctx.disconnect();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.print("CLIENT ");
        System.out.print(this.id);
        System.out.println(" DISCONNECTED");
        Core.getInstance().removePlayer(this.id);
        super.channelInactive(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientData.Data data) throws Exception
    {
        System.out.println("READ ZERO : ");
        System.out.println(data);
        Core.getInstance().interpretMessage(data, this.id);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}
