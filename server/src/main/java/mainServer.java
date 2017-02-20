/**
 * Created by wasta-geek on 16/11/16.
 */

public class mainServer
{
    public static void main(String[] args) throws Exception
    {
//        int port;
//        if (args.length > 0)
//            port = Integer.parseInt(args[0]);
//        else
//            port = 8080;
        Core.getInstance().run();
//        IServer server = new serverNettyHandler(port);
//        server.run();
    }
}
