package tw.jiangsir.Utils.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.servlet.http.Part;
import org.apache.commons.io.FileUtils;

public class FileTools {
	/**
	 * 取得指定 path 下的所有檔案, 並進入目錄底下尋找
	 * 
	 * @param path
	 * @return
	 */
	public ArrayList<File> findFiles(File path, String regex) {
		ArrayList<File> files = new ArrayList<File>();
		for (File file : path.listFiles()) {
			int length = String.valueOf(file.getPath()).length() + 4;
			while (length-- > 0) {
			}

			if (file.isDirectory() && file.canRead()) { 
				files.addAll(findFiles(file, regex));
			} else if (file.isFile() && file.toString().matches(regex)) {
				files.add(file);
			}
		}
		return files;
	}

	/**
	 * 取得指定目錄下的檔案, 不含目錄
	 */
	public TreeMap<String, Long> getFilenames(String path, String regex) {
		File file = new File(path);
		TreeMap<String, Long> fileList = new TreeMap<String, Long>();
		if (!file.exists()) {
			return fileList;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filestring = files[i].toString();
			if (!files[i].isDirectory() && filestring.matches(regex)) { 
				String filename = filestring
						.substring(filestring.lastIndexOf(System.getProperty("file.separator")) + 1);
				fileList.put(filename, new File(path, filename).length());
			}
		}
		return fileList;
	}

	private ArrayList<String> readFilelines(String path, String filename, String encode) {
		filename = filename.replaceAll("\\.\\.", "");
		path = path.replaceAll("\\.\\.", "");
		if (!path.endsWith(System.getProperty("file.separator"))) {
			path = path + System.getProperty("file.separator");
		}
		String line = null;
		ArrayList<String> text = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(path + filename);
			BufferedReader breader = new BufferedReader(new InputStreamReader(fis, encode));
			while ((line = breader.readLine()) != null) {
				if (!"".equals(line.trim())) {
					text.add(line);
				}
			}
			fis.close();
			breader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	public static void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data);
		new RunCommand().exec("sudo chmod g+rw " + file.getAbsolutePath());
	}

	public static void writeStringToFile(File file, String data, String rsyncaccount) throws IOException {
		FileUtils.writeStringToFile(file, data);
		new RunCommand().exec("sudo chmod g+rw " + file.getAbsolutePath());
		new RunCommand().exec("sudo chown -R " + rsyncaccount + " " + file.getParentFile().getAbsolutePath());
		new RunCommand().exec("sudo chmod g+rwx " + file.getParentFile().getAbsolutePath());
	}

	public static void writePartToFile(File file, Part part, String rsyncaccount) throws IOException {
		part.write(file.getAbsolutePath());
		new RunCommand().exec("sudo chmod g+rw " + file.getAbsolutePath());
		new RunCommand().exec("sudo chown -R " + rsyncaccount + " " + file.getParentFile().getAbsolutePath());
		new RunCommand().exec("sudo chmod g+rwx " + file.getParentFile().getAbsolutePath());
	}

	/**
	 * 以 tomcat 身分建立 dir, 開放 group 存取權，以便 同 group 的 zero 也可以讀取
	 * 
	 * @param file
	 * @param rsyncaccount
	 * @throws IOException
	 */
	public static void forceMkdir(File file, String rsyncaccount) {
		try {
			FileUtils.forceMkdir(file);
			new RunCommand().exec("sudo chmod g+rwx " + file.getAbsolutePath());
			new RunCommand().exec("sudo chown -R " + rsyncaccount + " " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
