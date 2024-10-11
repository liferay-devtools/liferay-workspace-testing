package com.liferay.workspace.testing;

import com.bmuschko.gradle.docker.tasks.container.DockerExistingContainer;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.HealthState;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;

import com.liferay.gradle.util.GradleUtil;

import java.time.Duration;

import java.util.Objects;
import java.util.concurrent.Callable;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;

/**
 * @author Drew Brokke
 */
public class WaitForHealthyAction implements Action<DockerExistingContainer> {

	public WaitForHealthyAction(Duration interval, Duration timeout) {
		_interval = interval;
		_timeout = timeout;
	}

	@Override
	public void execute(DockerExistingContainer dockerExistingContainer) {
		DockerClient dockerClient = dockerExistingContainer.getDockerClient();

		Property<String> containerIdProperty =
			dockerExistingContainer.getContainerId();

		String containerId = containerIdProperty.get();

		Logger logger = dockerExistingContainer.getLogger();

		logger.lifecycle(
			"Container {} for task {} waiting for healthy state...",
			containerId, dockerExistingContainer.getPath());

		Callable<Boolean> callable = () -> {
			InspectContainerCmd inspectContainerCmd =
				dockerClient.inspectContainerCmd(containerId);

			InspectContainerResponse inspectContainerResponse =
				inspectContainerCmd.exec();

			InspectContainerResponse.ContainerState containerState =
				inspectContainerResponse.getState();

			HealthState health = containerState.getHealth();

			String status = health.getStatus();

			System.out.println(status);

			return Objects.equals(status, "healthy");
		};

		try {
			if (!GradleUtil.waitFor(
					callable, _interval.toMillis(), _timeout.toMillis())) {

				throw new GradleException("Did not find healthy state in time");
			}
		}
		catch (Exception exception) {
			throw new GradleException("Did not find healthy state", exception);
		}
	}

	private final Duration _interval;
	private final Duration _timeout;

}