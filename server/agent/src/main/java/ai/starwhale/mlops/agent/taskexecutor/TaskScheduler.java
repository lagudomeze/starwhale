package ai.starwhale.mlops.agent.taskexecutor;

import ai.starwhale.mlops.agent.container.HardwareAware;
import ai.starwhale.mlops.agent.container.HardwareAware.Container;
import ai.starwhale.mlops.agent.container.HardwareAware.Gpu;
import ai.starwhale.mlops.agent.container.ImageConfig;
import ai.starwhale.mlops.domain.task.EvaluationTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component("ml_task_scheduler")
public class TaskScheduler {
    private final Path taskBasePath;

    private final HardwareAware aware;

    public TaskScheduler(AgentProperties agentProperties, HardwareAware aware) {
        this.taskBasePath = Path.of(agentProperties.getTask().getInfoPath());
        this.aware = aware;
    }

    private static final Object lock = new Object();

    private Optional<String> findIdleGpu() {
        List<Gpu> gpus = aware.gpus();

        Set<String> busyGpus = new HashSet<>(16);
        for (Container container : aware.containers()) {
            if (container.getStatus() == HardwareAware.ContainerStatus.RUNNING) {
                busyGpus.add(container.getGpuId());
            }
        }

        for (Gpu gpu : gpus) {
            if (gpu.getStatus() == HardwareAware.GpuStatus.Idle) {
                if (busyGpus.contains(gpu.getGpuId())) {
                } else {
                    return Optional.of(gpu.getGpuId());
                }
            }
        }
        return Optional.empty();

    }

    @SneakyThrows
    public void submit(EvaluationTask task) {
        String taskId = String.valueOf(task.getTask().getId());

        ImageConfig config = buildImageConfig(task);

        Path taskPath = taskBasePath.resolve(taskId);

        Files.createDirectories(taskPath);

        // write task to task info file
        // or other

        synchronized (lock) {
            Optional<String> optional = findIdleGpu();
            if (optional.isEmpty()) {
                // no gpu
                throw new RuntimeException();
            } else {
                aware.createContainer(config, taskId, optional.get());
            }
        }

    }

    private ImageConfig buildImageConfig(EvaluationTask task) {
        //todo
        return null;
    }

    public boolean cancel(String taskId) {
        boolean ret = aware.stopContainer(taskId);
        if (ret) {
            Path taskPath = taskBasePath.resolve(taskId);
            // write cancel flag file into ${taskPath}
        }
        return ret;
    }

    @Scheduled
    public void report() {
        // List<Result> result
        for (Container container : aware.containers()) {
            if (container.getStatus() == HardwareAware.ContainerStatus.STOPPED) {
                String taskId = container.getTaskId();
                Path taskPath = taskBasePath.resolve(taskId);

                // check success? failed? cancel?
                // if success upload result?
                // results push
            }

        }
        // report result
        // clean containers
    }
}
