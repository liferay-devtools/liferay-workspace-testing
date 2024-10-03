package com.liferay.workspace.testing;

import com.liferay.gradle.util.GradleUtil;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Provider;

import java.io.File;

/**
 * @author Drew Brokke
 */
public class WaitForFileExistsAction implements Action<DefaultTask> {

	public WaitForFileExistsAction(Provider<Object> fileProvider) {
		this(fileProvider, _DEFAULT_INTERVAL, _DEFAULT_TIMEOUT);
	}

	public WaitForFileExistsAction(
			Provider<Object> fileProvider, int interval, int timeout) {

		_fileProvider = fileProvider;
		_interval = interval;
		_timeout = timeout;
	}

	@Override
	public void execute(DefaultTask defaultTask) {
		Project project = defaultTask.getProject();

		File file = project.file(_fileProvider.get());

		Logger logger = defaultTask.getLogger();

		logger.info("Waiting for file: ${file}");

		try {
			GradleUtil.waitFor(file::exists, _interval, _timeout);
		} catch (Exception exception) {
			throw new GradleException("Could not find file: " + file, exception);
		}

		logger.info("Found file");
	}

	private static final int _DEFAULT_INTERVAL = 3 * 1000;
	private static final int _DEFAULT_TIMEOUT = 2 * 60 * 1000;

	private final int _interval;
	private final Provider<Object> _fileProvider;
	private final int _timeout;

}