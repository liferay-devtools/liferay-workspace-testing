package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.WorkspaceExtension;
import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

/**
 * @author Drew Brokke
 */
public abstract class BaseServerTask extends DefaultTask {

	public BaseServerTask() {
		Project project = getProject();

		executableFileProvider = project.provider(
			() -> {
				WorkspaceExtension workspaceExtension = GradleUtil.getExtension(
					project.getGradle(), WorkspaceExtension.class);

				// TODO: return separate file for Windows

				String executableFileName = "catalina.sh";

				ConfigurableFileTree fileTree = project.fileTree(
					workspaceExtension.getHomeDir(),
					fileTree1 -> fileTree1.include(
						"**/tomcat*/bin/" + executableFileName));

				return fileTree.getSingleFile();
			});

		binDirProvider = executableFileProvider.map(File::getParentFile);

		catalinaPidFileProvider = binDirProvider.map(
			binDir -> new File(binDir, "catalina.pid"));

		ObjectFactory objects = project.getObjects();

		serverStatusCheckIntervalProperty = objects.property(Integer.class);

		serverStatusCheckIntervalProperty.set(1000);

		serverStatusCheckTimeoutProperty = objects.property(Integer.class);

		serverStatusCheckTimeoutProperty.set(120000);
	}

	@Input
	public Provider<File> getBinDir() {
		return binDirProvider;
	}

	@Input
	public Provider<File> getCatalinaPidFile() {
		return catalinaPidFileProvider;
	}

	@Input
	public Provider<File> getExecutableFile() {
		return executableFileProvider;
	}

	@Input
	public Property<Integer> getServerStatusCheckInterval() {
		return serverStatusCheckIntervalProperty;
	}

	@Input
	public Property<Integer> getServerStatusCheckTimeout() {
		return serverStatusCheckTimeoutProperty;
	}

	@TaskAction
	public abstract void performServerAction() throws Exception;

	@Internal
	protected boolean isReachable() {
		return ServerUtil.isReachable("http://localhost:8080");
	}

	protected final Provider<File> binDirProvider;
	protected final Provider<File> catalinaPidFileProvider;
	protected final Provider<File> executableFileProvider;
	protected final Property<Integer> serverStatusCheckIntervalProperty;
	protected final Property<Integer> serverStatusCheckTimeoutProperty;

}