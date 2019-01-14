import server.MessagingService;

public class MainCLI {

    public static void main(String[] args) {
        new MessagingService.Builder()
                .onPort(8888)
                .build()
                .start();
    }

}
