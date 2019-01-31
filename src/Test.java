import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {
        String cwd = System.getProperty("user.dir");
        System.out.println("Current working directory : " + Paths.get(System.getProperty("user.dir"), "registered_users.txt"));
    }

}