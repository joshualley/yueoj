import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 运行其他程序 (危险代码)
 */
public class Main {
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        String path = userDir + File.separator + "temp/inject.bat";
        String dangerCode = "java -version 2>&1";
        try {
            Files.write(Paths.get(path), Arrays.asList(dangerCode));
            System.out.println("写入文件成功");
            Process process = Runtime.getRuntime().exec(path);
            int exit = process.waitFor();
            if (exit == 0) {
                String output = readInputStream(process.getInputStream());
                System.out.println("执行代码输出：" + output);
            }
        } catch (Exception e) {}
    }

    public static String readInputStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        } catch (IOException ignored) {}
        return output.toString().trim();
    }
}