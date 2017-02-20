/**
 * Created by wasta-geek on 15/11/16.
 */
public class main {
    public static void main(String[] args) {
        try
        {
            Core.getInstance().run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
