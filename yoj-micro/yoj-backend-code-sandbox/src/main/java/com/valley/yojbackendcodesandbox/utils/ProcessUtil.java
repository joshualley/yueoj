package com.valley.yojbackendcodesandbox.utils;

import cn.hutool.core.date.StopWatch;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteMessage;

import java.io.*;

/**
 * 执行命令工具类
 */
public class ProcessUtil {
    /**
     * 从输入流中读取数据
     * @param inputStream
     * @return
     */
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

    /**
     * 执行命令
     * @param cmd
     * @param timeout
     * @return
     */
    public static ExecuteMessage runCmd(String cmd, long timeout) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            // 判断程序是否超时
            if (timeout > 0) {
                new Thread(() -> {
                    try {
                        Thread.sleep(timeout);
                        if (process.isAlive()) {
                            process.destroy();
                        }
                    } catch (InterruptedException ignored) {}
                }).start();
            }
            int exitCode = process.waitFor();
            stopWatch.stop();
            // 收集输出信息
            String output = "";
            String error = "";
            if (exitCode == 0) {
                output = readInputStream(process.getInputStream());
            } else {
                error = readInputStream(process.getErrorStream());
            }
            process.destroy();
            return ExecuteMessage.builder()
                    .exitValue(exitCode)
                    .output(output)
                    .error(error)
                    .time(stopWatch.getTotalTimeMillis())
                    .memory(0L)
                    .build();
        } catch (Exception e) {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            return ExecuteMessage.builder()
                    .exitValue(-1)
                    .error(e.getMessage())
                    .time(stopWatch.getTotalTimeMillis())
                    .build();
        }
    }

    /**
     * 执行交互式命令
     * @param cmd
     * @param input 输入参数
     * @return
     */
    public static ExecuteMessage runInteractCmd(String cmd, String input) {
        Process process = null;
        int exitCode = -1;
        try {
            process = Runtime.getRuntime().exec(cmd);
            // 输入数据
            OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
            // 拼接输入的格式，需要输入换行符，才能算输入一个参数
            String args = String.join("\n", input.split(" ")) + "\n";
            writer.write(args);
            writer.flush();
            writer.close();
            // 等待程序返回结果
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            return ExecuteMessage.builder()
                    .exitValue(exitCode)
                    .error(e.getMessage())
                    .build();
        }

        // 收集输出信息
        String output = "";
        if (exitCode == 0) {
            output = readInputStream(process.getInputStream());
        } else {
            output = readInputStream(process.getErrorStream());
        }
        return ExecuteMessage.builder()
                .exitValue(exitCode)
                .output(output)
                .build();
    }
}
