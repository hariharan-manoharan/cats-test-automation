package main.java.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.relevantcodes.extentreports.LogStatus;

import main.java.reporting.HtmlReport;

public class CopyLatestResult extends Utility {

	public void copyLatestResultFolder() {
		emptyLatestResultFolder(HtmlReport.relativePath + "/LatestResult");
		File srcFolder = new File(HtmlReport.relativePath + "/Results/" + HtmlReport.reportFolderName);
		File destFolder = new File(HtmlReport.relativePath + "/LatestResult");

		if (!srcFolder.exists()) {
			test.log(LogStatus.WARNING, "Folder " + HtmlReport.reportFolderName + " does not exists", "");
		} else {
			try {
				copyFolder(srcFolder, destFolder);
			} catch (IOException e) {
				test.log(LogStatus.WARNING, "Exception occured while copying result folder "
						+ HtmlReport.reportFolderName + " to LatestResult folder", "");
				test.log(LogStatus.WARNING, e);
			}
		}
	}

	private void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			if (!dest.exists()) {
				dest.mkdir();
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}
		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		}
	}

	private void emptyLatestResultFolder(String latestResult) {
		File directory = new File(latestResult);
		// make sure directory exists
		if (!directory.exists()) {
			// Do Nothing
		} else {
			try {
				delete(directory);
			} catch (IOException e) {
				test.log(LogStatus.WARNING, "Exception occured while deleting LatestResult folder", "");
				test.log(LogStatus.WARNING, e);
			}
		}

	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}
}
