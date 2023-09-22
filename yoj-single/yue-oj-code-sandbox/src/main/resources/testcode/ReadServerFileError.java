import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 读取服务器文件或配置
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        // 获取配置文件
        String userDir = System.getProperty("user.dir");
        String path = userDir + File.separator + "src/main/resources/application.yml";
        List<String> lines = Files.readAllLines(Paths.get(path));
        System.out.println(String.join("\n", lines));
    }
}
