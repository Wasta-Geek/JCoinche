import com.badlogic.gdx.Gdx;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Created by wasta-geek on 14/11/16.
 */
public class JCoincheClient implements INetworkClient {

    private ChannelHandlerContext   ctx;
    private EventLoopGroup _workerGroup;

    public void run()
    {
        this.ctx = null;
    }

    public void tryConnection(String host, int port)
    {
        try
        {
            this._workerGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(_workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(ServerData.Data.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufEncoder());
                    ch.pipeline().addLast(new clientNetty());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
        }
        catch (Exception error)
        {
            this._workerGroup.shutdownGracefully();
        }
    }

    public void shutdown()
    {
        if (this._workerGroup != null)
        this._workerGroup.shutdownGracefully();
        Gdx.app.exit();
    }

    public void setContext(Object context)
    {
        this.ctx = (ChannelHandlerContext) context;
    }

    public void sendMessage(ClientData.Data data)
    {
        if (data != null && this.ctx != null)
        {
            System.out.println("J'ECRIS A UN SERVER");
            this.ctx.writeAndFlush(data);
        }
    }

    public boolean connectServer(String ip, int port)
    {
        this.tryConnection(ip, port);
        if (this._workerGroup.isShuttingDown())
            return false;
        return true;
    }
}
