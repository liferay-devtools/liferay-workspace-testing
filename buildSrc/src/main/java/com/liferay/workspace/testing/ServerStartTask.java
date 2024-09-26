package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.util.Collections;

import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;

/**
 * @author Drew Brokke
 */
public class ServerStartTask extends BaseServerTask {

	public ServerStartTask() {
		onlyIf(task -> !isReachable());

		Project project = getProject();

		ObjectFactory objects = project.getObjects();

		_waitForReachable = objects.property(Boolean.class);
	}

	@Input
	public Property<Boolean> getWaitForReachable() {
		return _waitForReachable;
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

		if (_waitForReachable.getOrElse(true)) {
			System.out.println("Trying to reach server...");

			GradleUtil.waitFor(
				this::isReachable, serverStatusCheckIntervalProperty.get(),
				serverStatusCheckTimeoutProperty.get());

			System.out.println("Success!");
		}
	}

	private Property<Boolean> _waitForReachable;

}