package ai.starwhale.mlops.agent.container;


import lombok.Data;

import java.util.List;

public interface HardwareAware {

    /**
     * create container and run right now, if failed then throw exception
     *
     * @param config ImageConfig
     * @param taskId task id (must be unique? todo)
     * @param gpuId which gpu to run
     */
    void createContainer(ImageConfig config, String taskId, String gpuId);

    /**
     * stop container by taskId only for cancel
     *
     * @param taskId taskId
     */
    boolean stopContainer(String taskId);

    List<Container> containers();

    List<Gpu> gpus();

    @Data
    class Container {
        private String id;
        private String taskId;
        private ContainerStatus status;
        private String gpuId;
    }

    enum ContainerStatus {

        /**
         * running
         */
        RUNNING,

        /**
         * stopped
         */
        STOPPED,
    }

    @Data
    class Gpu {

        private String gpuId;

        private GpuStatus status;
    }

    enum GpuStatus {
        Idle,
        Busy,
    }
}
