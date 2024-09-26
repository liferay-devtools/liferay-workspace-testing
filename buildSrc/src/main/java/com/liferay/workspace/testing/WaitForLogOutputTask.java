package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.WorkspaceExtension;
import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.io.File;
import java.io.RandomAccessFile;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

/**
 * @author Drew Brokke
 */
public class WaitForLogOutputTask extends DefaultTask {

	public WaitForLogOutputTask() {
		Project project = getProject();

		ObjectFactory objects = project.getObjects();

		_expectedLines = objects.setProperty(String.class);

		_file = objects.fileProperty();

		ProjectLayout layout = project.getLayout();

		_file.convention(
			layout.file(project.provider(this::_getBundleServerFile)));

		_readFromEndOfFile = objects.property(Boolean.class);
	}

	@TaskAction
	public void checkLogs() throws Exception {
		RegularFile regularFile = _file.get();

		try (RandomAccessFile randomAccessFile = new RandomAccessFile(
				regularFile.getAsFile(), "r")) {

			if (_readFromEndOfFile.getOrElse(true)) {
				long length = randomAccessFile.length();

				randomAccessFile.skipBytes((int)length);
			}

			Set<String> expectedLines = new HashSet<>(_expectedLines.get());

			System.out.println("Waiting for expected output:");

			int checkInterval = 3 * 1000;
			int timeout = 2 * 60 * 1000;

			boolean waitFor = GradleUtil.waitFor(
				() -> {
					String line = randomAccessFile.readLine();

					while (line != null) {
						System.out.printf("Checking line: %s%n", line);

						Iterator<String> iterator = expectedLines.iterator();

						while (iterator.hasNext()) {
							String expectedLine = iterator.next();

							if (line.contains(expectedLine)) {
								System.out.printf(
									"%nFound expected line: %s%n%n",
									expectedLine);

								iterator.remove();
							}
						}

						if (expectedLines.isEmpty()) {
							return true;
						}

						line = randomAccessFile.readLine();
					}

					return false;
				},
				checkInterval, timeout);

			if (!waitFor) {
				throw new GradleException(
					String.format(
						"Could not find expected output: %s", expectedLines));
			}
		}
	}

	@Input
	public SetProperty<String> getExpectedLines() {
		return _expectedLines;
	}

	@InputFile
	public RegularFileProperty getFile() {
		return _file;
	}

	@Input
	public Property<Boolean> getReadFromEndOfFile() {
		return _readFromEndOfFile;
	}

	private File _getBundleServerFile() {
		File logFile = null;

		int max = 0;

		Project project = getProject();

		WorkspaceExtension workspaceExtension = GradleUtil.getExtension(
			project.getGradle(), WorkspaceExtension.class);

		ConfigurableFileTree files = project.fileTree(
			workspaceExtension.getHomeDir(),
			configurableFileTree -> configurableFileTree.include(
				"logs/liferay.*.log"));

		for (File file : files.getFiles()) {
			String name = file.getName();

			String[] fileNameParts = name.split("\\.");

			String datePart = fileNameParts[1];

			int date = Integer.parseInt(datePart.replaceAll("-", ""));

			if (date > max) {
				logFile = file;
				max = date;
			}
		}

		return logFile;
	}

	private final SetProperty<String> _expectedLines;
	private final RegularFileProperty _file;
	private final Property<Boolean> _readFromEndOfFile;

}