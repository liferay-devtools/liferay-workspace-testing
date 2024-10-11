package com.liferay.workspace.testing;

import com.liferay.gradle.util.GradleUtil;

import java.io.File;

import java.time.Duration;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Provider;

/**
 * @author Drew Brokke
 */
public class WaitForFileExistsAction implements Action<DefaultTask> {

	public WaitForFileExistsAction(Provider<Object> fileProvider) {
		this(fileProvider, _DEFAULT_INTERVAL, _DEFAULT_TIMEOUT);
	}

	public WaitForFileExistsAction(
		Provider<Object> fileProvider, Duration interval, Duration timeout) {

		_fileProvider = fileProvider;
		_interval = interval;
		_timeout = timeout;
	}

	@Override
	public void execute(DefaultTask defaultTask) {
		Project project = defaultTask.getProject();

		File file = project.file(_fileProvider.get());

		Logger logger = defaultTask.getLogger();

		logger.lifecycle("Waiting for file: {}", file);

		try {
			if (!GradleUtil.waitFor(
					file::exists, _interval.toMillis(), _timeout.toMillis())) {

				throw new GradleException("Could not find file: " + file);
			}
		}
		catch (Exception exception) {
			throw new GradleException(
				"Could not find file: " + file, exception);
		}

		logger.lifecycle("Found file");
	}

	private static final Duration _DEFAULT_INTERVAL = Duration.ofSeconds(3);

	private static final Duration _DEFAULT_TIMEOUT = Duration.ofMinutes(2);

	private final Provider<Object> _fileProvider;
	private final Duration _interval;
	private final Duration _timeout;

}