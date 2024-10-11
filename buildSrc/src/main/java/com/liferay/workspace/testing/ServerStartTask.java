package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.time.Duration;

import java.util.Collections;

import org.gradle.api.GradleException;
import org.gradle.api.Project;

/**
 * @author Drew Brokke
 */
public class ServerStartTask extends BaseServerTask {

	public ServerStartTask() {
		onlyIf(task -> !isReachable());
	}

	@Override
	public void performServerAction() throws Exception {
		_startServer();
	}

	private void _startServer() throws Exception {
		Project project = getProject();

		project.exec(
			execSpec -> {
				execSpec.setWorkingDir(binDirProvider.get());
				execSpec.setExecutable(executableFileProvider.get());
				execSpec.setArgs(Collections.singletonList("start"));
				execSpec.environment(
					"CATALINA_PID", catalinaPidFileProvider.get());
			});

		System.out.println("Trying to reach server...");

		Duration interval = serverStatusCheckIntervalProperty.get();
		Duration timeout = serverStatusCheckTimeoutProperty.get();

		if (!GradleUtil.waitFor(
				this::isReachable, interval.toMillis(), timeout.toMillis())) {

			throw new GradleException("Could not reach server in time");
		}

		System.out.println("Success!");
	}

}