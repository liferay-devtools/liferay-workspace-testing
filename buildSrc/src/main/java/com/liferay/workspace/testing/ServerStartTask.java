package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.util.Collections;

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

		GradleUtil.waitFor(
			this::isReachable, serverStatusCheckIntervalProperty.get(),
			serverStatusCheckTimeoutProperty.get());

		System.out.println("Success!");
	}

}