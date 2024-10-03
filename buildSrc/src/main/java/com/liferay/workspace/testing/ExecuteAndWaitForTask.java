package com.liferay.workspace.testing;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Method;

import java.nio.file.Files;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;
import org.zeroturnaround.exec.listener.ProcessListener;
import org.zeroturnaround.exec.stream.LogOutputStream;

/**
 * @author Drew Brokke
 */
public class ExecuteAndWaitForTask extends DefaultTask {

	public ExecuteAndWaitForTask() {
		Project project = getProject();

		ObjectFactory objects = project.getObjects();

		ProjectLayout layout = project.getLayout();

		DirectoryProperty buildDirectory = layout.getBuildDirectory();

		_pidFile = buildDirectory.file(String.format("%s/pid", getName()));

		_pid = _pidFile.map(
			RegularFile::getAsFile
		).map(
			pidFile -> {
				if (!pidFile.exists()) {
					return null;
				}

				try {
					return Integer.parseInt(
						new String(Files.readAllBytes(pidFile.toPath())));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		);
		_stdoutFile = buildDirectory.file(
			String.format("%s/stdout.txt", getName()));
		_expectedOutput = objects.property(String.class);
		_execArgs = objects.setProperty(String.class);

		_waitForTimeout = objects.property(Integer.class);

		_waitForTimeout.convention(30 * 1000);

		onlyIf(task -> !_pid.isPresent());
	}

	@Input
	public SetProperty<String> getExecArgs() {
		return _execArgs;
	}

	@Input
	@Optional
	public Provider<String> getExpectedOutput() {
		return _expectedOutput;
	}

	@Internal
	public Provider<Integer> getPid() {
		return _pid;
	}

	@OutputFile
	public Provider<RegularFile> getPidFile() {
		return _pidFile;
	}

	@OutputFile
	public Provider<RegularFile> getStdoutFile() {
		return _stdoutFile;
	}

	@Input
	public Property<Integer> getWaitForTimeout() {
		return _waitForTimeout;
	}

	@TaskAction
	public void run() throws Exception {
		File stdoutFile = _stdoutFile.map(
			RegularFile::getAsFile
		).get();
		Integer timeout = _waitForTimeout.get();

		ProcessExecutor processExecutor = new ProcessExecutor(_execArgs.get());

		CountDownLatch countDownLatch = new CountDownLatch(1);

		if (_expectedOutput.isPresent()) {
			processExecutor.redirectOutputAlsoTo(
				new LogOutputStream() {

					@Override
					protected void processLine(String line) {
						if (countDownLatch.getCount() == 0) {
							return;
						}

						System.out.println(line);

						if (line.contains(_expectedOutput.get())) {
							countDownLatch.countDown();
						}
					}

				});
		}

		processExecutor.listener(
			new ProcessListener() {

				@Override
				public void afterStop(Process process) {
					if (countDownLatch.getCount() > 0) {
						countDownLatch.countDown();
					}
				}

			});

		processExecutor.redirectOutputAlsoTo(
			Files.newOutputStream(stdoutFile.toPath()));

		StartedProcess startedProcess = processExecutor.start();

		Process process = startedProcess.getProcess();

		if (_expectedOutput.isPresent()) {
			System.out.printf(
				"Waiting for expected output: %s%n", _expectedOutput.get());

			boolean await = countDownLatch.await(
				timeout, TimeUnit.MILLISECONDS);

			if (!await) {
				if (process.isAlive()) {
					process.destroyForcibly();
				}

				throw new GradleException("Could not find the expected output");
			}

			System.out.println("FOUND!");
		}

		Class<Process> processClass = Process.class;

		for (Method method : processClass.getMethods()) {
			if (Objects.equals(method.getName(), "pid")) {
				String pidString = String.valueOf(method.invoke(process));

				File pidFile = _pidFile.map(
					RegularFile::getAsFile
				).get();

				Files.write(pidFile.toPath(), pidString.getBytes());

				System.out.println("pid = " + pidString);

				break;
			}
		}
	}

	private final SetProperty<String> _execArgs;
	private final Property<String> _expectedOutput;
	private final Provider<Integer> _pid;
	private final Provider<RegularFile> _pidFile;
	private final Provider<RegularFile> _stdoutFile;
	private final Property<Integer> _waitForTimeout;

}