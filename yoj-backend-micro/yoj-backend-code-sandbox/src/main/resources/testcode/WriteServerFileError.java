import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 向服务器写入文件，植入危险程序
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String path = userDir + File.separator + "temp/inject.text";
        String dangerCode = "java -version 2>&1";
        Files.write(Paths.get(path), Arrays.asList(dangerCode));
        System.out.println("写入文件成功");
    }
}
