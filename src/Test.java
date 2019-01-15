import server.constants.Constants;
import server.io.TxtFilesHelper;

public class Test {

    public static void main(String[] args) {
        TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, "riccardo,ciao,true");
        TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, "paola,come,false");
        TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, "carlo,va,true");

        for (String line : TxtFilesHelper.getAllLines(Constants.REGISTERED_USERS_FILE_NAME)) {
            System.out.println(line);
        }
    }

}
