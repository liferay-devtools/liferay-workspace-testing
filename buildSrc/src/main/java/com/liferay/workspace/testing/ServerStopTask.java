package com.liferay.workspace.testing;

import java.util.Collections;

import org.gradle.api.Project;

/**
 * @author Drew Brokke
 */
public class ServerStopTask extends BaseServerTask {

	public ServerStopTask() {
		onlyIf(
			task -> {
				if (!binDirProvider.isPresent()) {
					return false;
				}

				if (!isReachable()) {
					return false;
				}

				return true;
			});
	}

	@Override
	public void performServerAction() throws Exception {
		_stopServer();
	}

	private void _stopServer() throws Exception {
		Project project = getProject();

		project.exec(
			execSpec -> {
				execSpec.setWorkingDir(binDirProvider.get());
				execSpec.setExecutable(executableFileProvider.get());
				execSpec.setArgs(Collections.singletonList("stop"));
				execSpec.environment(
					"CATALINA_PID", catalinaPidFileProvider.get());
			});
	}

}