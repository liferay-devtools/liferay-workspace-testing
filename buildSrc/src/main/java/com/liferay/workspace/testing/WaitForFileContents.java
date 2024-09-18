package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * @author Drew Brokke
 */
public class WaitForFileContents<T extends DefaultTask> implements Action<T> {

	public WaitForFileContents(String expectedFileContents, Provider<File> fileProvider) {
		_expectedFileContents = expectedFileContents;
		_fileProvider = fileProvider;
	}

	private final String _expectedFileContents;
	private final Provider<File> _fileProvider;

	@Override
	public void execute(@NotNull T task) {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(
				_fileProvider.get(), "r")) {

			long length = randomAccessFile.length();

			randomAccessFile.skipBytes((int)length);

			String testLine = _expectedFileContents;

			System.out.printf("Waiting for expected output: %s%n", testLine);

			int checkInterval = 3 * 1000;
			int timeout = 2 * 60 * 1000;

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
				checkInterval, timeout);
		} catch (FileNotFoundException fileNotFoundException) {
			throw new GradleException("Could not read provided file", fileNotFoundException);
		} catch (Exception exception) {
			throw new GradleException("Did not find expected output in time", exception);
		}
	}
}
