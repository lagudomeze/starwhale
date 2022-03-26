package ai.starwhale.mlops.agent.container.impl;

import ai.starwhale.mlops.agent.container.HardwareAware;
import ai.starwhale.mlops.agent.container.ImageConfig;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerAndNvidiaAware implements HardwareAware {

    private final DockerHttpClient client;

    public DockerAndNvidiaAware(DockerHttpClient client) {
        this.client = client;
    }

    @Override
    public void createContainer(ImageConfig config, String taskId, String gpuId) {
        // docker run --label starwhale --label task_id=${taskId} --gpus='"device=${gpuId}"' ..
        //todo
    }

    @Override
    public boolean stopContainer(String taskId) {
        // call containers() and find label=${taskId} container and stop it
        //todo
        return false;
    }

    @Override
    public List<Container> containers() {
        // convert labels to Container
        //todo
        return null;
    }

    @Override
    public List<Gpu> gpus() {
        //todo nvidia-smi to json??
        return null;
    }
}
