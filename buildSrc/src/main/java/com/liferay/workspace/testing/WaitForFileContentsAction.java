package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Provider;

import org.jetbrains.annotations.NotNull;

/**
 * @author Drew Brokke
 */
public class WaitForFileContentsAction<T extends DefaultTask>
	implements Action<T> {

	public WaitForFileContentsAction(
		String expectedFileContents, Provider<File> fileProvider) {

		this(
			expectedFileContents, fileProvider, _DEFAULT_INTERVAL,
			_DEFAULT_TIMEOUT);
	}

	public WaitForFileContentsAction(
		String expectedFileContents, Provider<File> fileProvider, int interval,
		int timeout) {

		_expectedFileContents = expectedFileContents;
		_fileProvider = fileProvider;
		_timeout = timeout;

		_checkInterval = interval;
	}

	@Override
	public void execute(@NotNull T task) {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(
				_fileProvider.get(), "r")) {

			long length = randomAccessFile.length();

			randomAccessFile.skipBytes((int)length);

			String testLine = _expectedFileContents;

			System.out.printf("Waiting for expected output: %s%n", testLine);

			GradleUtil.waitFor(
				() -> {
					String line = randomAccessFile.readLine();

					while (line != null) {
						if (line.contains(testLine)) {
							System.out.println("FOUND!");

							return true;
						}

						line = randomAccessFile.readLine();
					}

					return false;
				},
				_checkInterval, _timeout);
		}
		catch (FileNotFoundException fileNotFoundException) {
			throw new GradleException(
				"Could not read provided file", fileNotFoundException);
		}
		catch (Exception exception) {
			throw new GradleException(
				"Did not find expected output in time", exception);
		}
	}

	private static final int _DEFAULT_INTERVAL = 3 * 1000;

	private static final int _DEFAULT_TIMEOUT = 2 * 60 * 1000;

	private final int _checkInterval;
	private final String _expectedFileContents;
	private final Provider<File> _fileProvider;
	private final int _timeout;

}