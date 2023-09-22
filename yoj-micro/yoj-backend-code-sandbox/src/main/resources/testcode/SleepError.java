/**
 * 休眠，占用系统资源
 */
public class Main {
    public static void main(String[] args) {
        long oneHour = 60 * 60 * 1000;
        try {
            Thread.sleep(oneHour);
        } catch (Exception ignored) {}
        System.out.println("一小时后");
    }
}