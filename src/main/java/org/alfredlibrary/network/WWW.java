/*
 * Alfred Library.
 * Copyright (C) 2011 Alfred Team
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.alfredlibrary.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Helper to get World Wide Web data like site content, files, headers...
 * 
 * @author Marlon Silva Carvalho
 * @since 2.0.0
 */
final public class WWW {

	/**
	 * Download a file from an URL.
	 * 
	 * @param u URL linking to a file.
	 * @return InputStream containing the file.
	 */
	public static InputStream getFile(String u) {
		URL url;
		try {
			url = new URL(u);
			URLConnection conn = url.openConnection();
			return new DataInputStream(conn.getInputStream());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the content of a site (html source code).
	 * 
	 * @param u Site's URL.
	 * @param encode Encoding of the site.
	 * @param parameters Parameters to be used in POST requests.
	 * @param headers Headers.
	 * @return String containing the site source code.
	 */
	public static String getContent(String u, String encode, Map<String, String> parameters, Map<String, String> headers) {
		URL url;
		try {
			url = new URL(u);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outputStreamWriter = null;

			if (parameters != null) {
				StringBuilder strParams = new StringBuilder();
				if (parameters != null) {
					for (String chave : parameters.keySet()) {
						strParams.append(URLEncoder.encode(chave, "UTF-8"));
						strParams.append("=");
						strParams.append(URLEncoder.encode(parameters.get(chave), encode));
						strParams.append("&");
					}
				}
				outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
				outputStreamWriter.write(strParams.toString());
				outputStreamWriter.flush();
			}

			if (headers != null) {
				for (String header : headers.keySet()) {
					connection.setRequestProperty(header, headers.get(header));
				}
			}

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(encode)));
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
				result.append("\n");
			}

			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			bufferedReader.close();
			return result.toString();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getContent(String strURL, String encode) {
		return getContent(strURL, encode, null, null);
	}

	public static String getContent(String u, Map<String, String> parameters) {
		return getContent(u, parameters, null);
	}

	public static String getContent(String u, Map<String, String> parameters, Map<String, String> headers) {
		return getContent(u, "UTF-8", parameters, headers);
	}

}
