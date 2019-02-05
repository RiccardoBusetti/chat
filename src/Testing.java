import java.nio.file.Paths;

public class Testing {

    public static void main(String[] args) {
        System.out.println(Paths.get(System.getProperty("user.dir"), "src", "client", "assets", "oof.mp3"));
    }

}
