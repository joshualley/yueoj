package com.valley.yojbackendcodesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {

        DockerClient client = DockerClientBuilder
                .getInstance("tcp://127.0.0.1:2375")
                .build();
        DockerDemo demo = new DockerDemo();
        demo.listContainers(client);

        Container container = DockerUtil.findContainer(client, "java");
        if (container != null) {
            DockerUtil.copyFileToContainer(client, container.getId(),
                    "E:\\WorkSpace\\ALearning\\OJ\\yue-oj-code-sandbox\\temp\\inject.txt",
                    "/app");
        }


    }

    public void listContainers(DockerClient client) {
        ListContainersCmd listContainersCmd = client.listContainersCmd().withShowAll(true);
        List<Container> containers = listContainersCmd.exec();
        System.out.println(containers.get(0).getNames()[0] + "," + containers.get(0).getState());
        System.out.println(containers.get(2).getNames()[0] + "," + containers.get(2).getState());
        List<String> containerNames = containers.stream()
                .map((container) -> String.join(" ", container.getNames()))
                .collect(Collectors.toList());
        System.out.println(containerNames);
    }

    public void listImages(DockerClient client) {
        List<Image> images = client.listImagesCmd().exec();
        for (Image image : images) {
            System.out.println(image);
        }
    }

    public void ping(DockerClient client) {
        PingCmd pingCmd = client.pingCmd();
        pingCmd.exec();
    }

    public void createContainer(DockerClient client) {
        CreateContainerCmd containerCmd = client
                .createContainerCmd("golang:1.19")
                .withName("go19")
                .withCmd("echo", "hello");
        containerCmd.exec();
    }


    public void pullImage(DockerClient client) throws InterruptedException {
        PullImageCmd pullImageCmd = client.pullImageCmd("golang:1.19");
        pullImageCmd.exec(new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                super.onNext(item);
                System.out.println(item.getProgressDetail());
            }
        }).awaitCompletion();
        System.out.println("下载完成");
    }
}
