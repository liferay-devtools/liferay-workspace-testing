package com.liferay.workspace.testing;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Drew Brokke
 */
public class ServerUtil {

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