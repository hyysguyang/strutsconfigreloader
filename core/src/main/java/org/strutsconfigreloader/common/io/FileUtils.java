/**
 * Copyright 2004-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * History: 2007-2-4 13:24:10 Created by guyang
 */
package org.strutsconfigreloader.common.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * History: 2007-2-4 13:24:10Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class FileUtils {

	/**
	 * Recursive rename the child file with given extension to another extension
	 * of root directory.Return <code>true</code> when rename
	 * successfully,otherwise return <code>false</code>
	 * 
	 * @param file The root file
	 * @param sourcePostfix The scource file extension
	 * @param destPostfix The destination file extension
	 * @return The file object that failde rename
	 */
	public static File[] renameFiles(String rootDir, String sourcePostfix, String destPostfix) {
		File[] renamedFailedFiles = new File[0];
		List<File> fileList = new ArrayList<File>();
		File rootFile = new File(rootDir);
		File[] subFiles = rootFile.listFiles();
		for (File file : subFiles) {

			if (file.isDirectory()) {
				File[] tempRenamedFailedFiles = renameFiles(file.getPath(), sourcePostfix, destPostfix);
				for (File tempFile : tempRenamedFailedFiles) {
					fileList.add(tempFile);
				}
				continue;
			}

			String tempPath = file.getPath();
			String distFilePath = tempPath.replaceAll(sourcePostfix, destPostfix);
			File distFile = new File(distFilePath);
			if (!file.renameTo(distFile)) {
				fileList.add(file);
			}
		}
		return fileList.toArray(renamedFailedFiles);
	}

	public static String getFullPath(String classPath) {

		String fileClassPath = classPath;

		if (fileClassPath == null) {
			fileClassPath = "";
		}

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(fileClassPath);
		if (url != null) {
			return url.getFile();
		}
		return "";
	}

	/**
	 * Delete file or directory recursive
	 * 
	 * @param file file or directory that to be delete
	 * @return TODO
	 */
	public static void deleteFileRecursive(File file) {
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			for (File file2 : subFiles) {
				deleteFileRecursive(file2);
			}
		}
		file.delete();
	}

	/**
	 * Create new file with the subFileName
	 * 
	 * @param file The parent file
	 * @param subFileName The new file short name return Return the create
	 * result
	 */
	public static boolean createSubFile(File file, String subFileName) {
		String filePath = file.getPath() + "/" + subFileName;
		try {
			return new File(filePath).createNewFile();
		}
		catch (IOException e) {
			return false;
		}
	}

}
