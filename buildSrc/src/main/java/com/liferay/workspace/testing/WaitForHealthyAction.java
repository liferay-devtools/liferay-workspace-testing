package com.liferay.workspace.testing;

import com.bmuschko.gradle.docker.tasks.container.DockerExistingContainer;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.HealthState;
import com.github.dockerjava.api.command.HealthStateLog;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.liferay.gradle.util.GradleUtil;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;

import java.time.Duration;
import java.util.Objects;

/**
 * @author Drew Brokke
 */
public class WaitForHealthyAction implements Action<DockerExistingContainer> {
	private final Duration _interval;
	private final Duration _timeout;

	public WaitForHealthyAction(Duration interval, Duration timeout) {
		_interval = interval;
		_timeout = timeout;
	}

	@Override
	public void execute(DockerExistingContainer dockerExistingContainer) {
		DockerClient dockerClient = dockerExistingContainer.getDockerClient();

		Logger logger = dockerExistingContainer.getLogger();

		logger.lifecycle(
			"Task {} waiting for healthy...%n",
			dockerExistingContainer.getPath());

		try {
			if (!GradleUtil.waitFor(
				() -> {
					InspectContainerCmd inspectContainerCmd = dockerClient.inspectContainerCmd(dockerExistingContainer.getContainerId().get());

					InspectContainerResponse inspectContainerResponse = inspectContainerCmd.exec();

					InspectContainerResponse.ContainerState containerState = inspectContainerResponse.getState();

					HealthState health = containerState.getHealth();

					String status = health.getStatus();

					System.out.println(status);

					return Objects.equals(status, "healthy");
				},
				_interval.toMillis(),
				_timeout.toMillis()
			)) {
				throw new GradleException("Did not find healthy state in time!");
			}
		} catch (Exception e) {
			throw new GradleException("Did not find healthy state", e);
		}

		logger.lifecycle("Task {} is healthy!", dockerExistingContainer.getPath());
	}
}
