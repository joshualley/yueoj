package com.valley.yojbackendcodesandbox.docker;

import cn.hutool.core.date.StopWatch;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DockerUtil {

    /**
     * 容器初始化
     * @param client
     * @param imageName
     * @param containerName
     * @return
     */
    public static String initDockerContainer(DockerClient client, String imageName, String containerName) {
        Image image = findImage(client, imageName);
        // 1. 如果镜像不存在，拉取java镜像
        if (image == null) {
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
        Container container = findContainer(client, containerName);
        if (container == null) {
            HostConfig hostConfig = new HostConfig();
            // 将用户代码的路径映射到docker容器中的 /app 目录下
            // hostConfig.setBinds(new Bind(hostPath, new Volume(volumePath)));
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
            String containerId = containerResponse.getId();
            // 启动容器
            client.startContainerCmd(containerId).exec();
            return containerId;
        } else {
            // 容器存在时，检查容器是否运行，未运行则启动容器
            if (container.getState().equals("exited")) {
                // 启动容器
                client.startContainerCmd(container.getId()).exec();
            }
            return container.getId();
        }
    }

    /**
     * 查找镜像
     * @param client
     * @param imageName
     * @return
     */
    public static Image findImage(DockerClient client, String imageName) {
        List<Image> images = client.listImagesCmd().exec();
        for (Image image : images) {
            if (String.join(" ", image.getRepoTags()).contains(imageName)) {
                return image;
            }
        }
        return null;
    }

    /**
     * 查找容器
     * @param client
     * @param containerName
     * @return
     */
    public static Container findContainer(DockerClient client, String containerName) {
        List<Container> containers = client.listContainersCmd()
                .withShowAll(true)
                .exec();
        for (Container container : containers) {
            if (String.join(" ", container.getNames()).contains(containerName)) {
                return container;
            }
        }
        return null;
    }

    public static boolean copyFileToContainer(DockerClient client, String containerId, String hostPath, String remotePath) {
        try {
            client.copyArchiveToContainerCmd(containerId)
                    .withHostResource(hostPath)
                    .withRemotePath(remotePath)
                    .exec();
        } catch (Exception e) {
            System.out.println("复制文件到Docker失败:" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 执行运行java代码的命令
     * @param client
     * @param containerId
     * @param cmd
     * @param timeout
     * @return
     */
    public static ExecuteMessage execRunCmd(DockerClient client, String containerId, String[] cmd, long timeout) {
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
