package com.liferay.workspace.testing;

import com.liferay.gradle.plugins.workspace.WorkspaceExtension;
import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;

import java.io.File;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.provider.Provider;

/**
 * @author Drew Brokke
 */
public class ServerUtil {

	public static File getBundleLogFile(Project project) {
		File logFile = null;

		int max = 0;

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
			}
		}

		return logFile;
	}

	public static Provider<File> getBundleLogFileProvider(Project project) {
		return project.provider(() -> getBundleLogFile(project));
	}

	public static boolean isReachable(String location) {
		try {
			URL url = new URL(location);

			HttpURLConnection httpURLConnection =
				(HttpURLConnection)url.openConnection();

			httpURLConnection.setRequestMethod("HEAD");

			int responseCode = httpURLConnection.getResponseCode();

			if ((responseCode > 0) && (responseCode < 400)) {
				return true;
			}
		}
		catch (IOException ioException) {
		}

		return false;
	}

}