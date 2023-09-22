package com.valley.yueojcodesandbox.utils;

import cn.hutool.core.date.StopWatch;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import com.valley.yueojcodesandbox.docker.MemoryCollector;
import com.valley.yueojcodesandbox.docker.ResultCollector;
import com.valley.yueojcodesandbox.model.ExecuteMessage;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DockerUtil {

    /**
     * 容器初始化
     * @param client
     * @param imageName
     * @param containerName
     * @param hostPath
     * @param volumePath
     * @return
     */
    public static String initDockerContainer(
            DockerClient client, String imageName, String containerName,
            String hostPath, String volumePath) {
        List<Image> images = client.listImagesCmd().exec();
        List<String> imageNames = images.stream()
                .map((image) -> String.join(" ", image.getRepoTags()))
                .collect(Collectors.toList());
        // 1. 如果镜像不存在，拉取java镜像
        if (!imageNames.contains(imageName)) {
            try {
                client.pullImageCmd(imageName).exec(new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        super.onNext(item);
                        System.out.println(item.getProgressDetail());
                    }
                }).awaitCompletion();
            } catch (InterruptedException ignored) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException();
            }
        }
        // 2. 如果容器不存在，创建容器
        List<Container> containers = client.listContainersCmd().exec();
        String containerId = null;
        for (Container container : containers) {
            // 如果存在容器，则获取容器ID
            if (String.join(" ", container.getNames()).contains(containerName)) {
                containerId = container.getId();
                break;
            }
        }
        if (containerId == null) {
            HostConfig hostConfig = new HostConfig();
            // 将用户代码的路径映射到docker容器中的 /app 目录下
            hostConfig.setBinds(new Bind(hostPath, new Volume(volumePath)));
            // 限制容器内存 1000 M
            hostConfig.withMemory(1000 * 1024 * 1024L);
            // 限制仅能使用单核cpu
            hostConfig.withCpuCount(1L);
            // 设置Linux的安全操作权限
            // hostConfig.withSecurityOpts(Arrays.asList("seccomp="));
            CreateContainerResponse containerResponse = client.createContainerCmd(imageName)
                    .withName(containerName)
                    .withHostConfig(hostConfig)
                    // 关闭网络
                    .withNetworkDisabled(true)
                    // 限制用户不能向root根目录写文件
                    .withReadonlyRootfs(true)
                    // 设置控制台输出
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    // 创建可交互的终端，避免容器终止运行
                    .withTty(true)
                    .exec();
            // 获取容器ID
            containerId = containerResponse.getId();
            // 3. 启动容器
            client.startContainerCmd(containerId).exec();
        }
        return containerId;
    }

    /**
     * 执行运行java代码的命令
     * @param client
     * @param containerId
     * @param cmd
     * @param timeout
     * @return
     */
    public static ExecuteMessage execRunJavaCodeCmd(DockerClient client, String containerId, String[] cmd, long timeout) {
        ExecCreateCmdResponse execCreateCmdResponse = client.execCreateCmd(containerId)
                .withCmd(cmd)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        String cmdId = execCreateCmdResponse.getId();
        ExecuteMessage runOutput = ExecuteMessage.builder()
                .time(0L)
                .memory(0L)
                .exitValue(0)
                .build();
        // 创建计时器
        StopWatch stopWatch = new StopWatch();
        ResultCollector resultCollector = new ResultCollector();
        try {
            ExecStartCmd execStartCmd = client.execStartCmd(cmdId);
            stopWatch.start();
            if (timeout == 0) {
                execStartCmd.exec(resultCollector).awaitCompletion();
            } else {
                execStartCmd.exec(resultCollector).awaitCompletion(timeout, TimeUnit.MILLISECONDS);
            }
            stopWatch.stop();
        } catch (InterruptedException e) {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            String msg = "Docker容器中执行命令时发生异常: " + e.getMessage();
            System.out.println(msg);
            runOutput.setExitValue(-1);
            runOutput.setError(msg);
        }
        if (!resultCollector.getError().isEmpty()) {
            runOutput.setExitValue(-1);
        }
        runOutput.setOutput(resultCollector.getOutput());
        runOutput.setError(resultCollector.getError());
        if (!resultCollector.isFinish()) {
            runOutput.setError("程序执行超时");
        }
        runOutput.setTime(stopWatch.getLastTaskTimeMillis());
        System.out.println(runOutput);
        return runOutput;
    }
}
