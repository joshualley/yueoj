package com.valley.yueojcodesandbox.docker;

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
    }

    public void listContainers(DockerClient client) {
        ListContainersCmd listContainersCmd = client.listContainersCmd();
        List<Container> containers = listContainersCmd.exec();
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
