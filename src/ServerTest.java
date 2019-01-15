import server.MessagingService;

public class ServerTest {

    public static void main(String[] args) {
        new MessagingService.Builder()
                .onPort(8888)
                .build()
                .start();
    }

}
