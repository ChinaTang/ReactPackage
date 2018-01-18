import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args){
        File file = new File("src/resource/page/test/test1/test.txt");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
