package ai.starwhale.mlops.agent.container.impl;

import ai.starwhale.mlops.agent.container.ContainerClient;

import ai.starwhale.mlops.agent.container.ImageConfig;
import java.util.Optional;

/**
 * Docker client
 */
public class DockerClient implements ContainerClient {

    @Override
    public Optional<String> startContainer(String imageId, ImageConfig imageConfig) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> stopContainer(String containerId) {
        // todo: stop and clean middle dirty result
        return Optional.empty();
    }

    @Override
    public Optional<ContainerStatus> status(String containerId) {
        return Optional.empty();
    }
}
