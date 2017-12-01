package Client;

public class ChatClient
{
    public static void main(String[] args)
    {
        ClientActions client = new ClientActions("127.0.0.1", 4000);
        client.runClient();
    }
}
