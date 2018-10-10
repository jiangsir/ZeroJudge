package Standalone.Upgrade201307;

import java.io.File;
import java.util.Scanner;

public class MoveTestdata {

	public void run() {
		Scanner scanner = new Scanner(System.in);
		File testdataPath = new File(scanner.next());
		if (!testdataPath.isDirectory()) {
		} else {
			for (File file : testdataPath.listFiles()) {
				if (file.getName().matches("^[a-z][0-9][0-9][0-9].*")
						&& file.isFile()) {
					File dir = new File(file.getParent(), File.separator
							+ file.getName().substring(0, 4));
					if (!dir.exists()) {
						if (dir.mkdirs()) {
						} else {
							continue;
						}
					}
					File newname = new File(dir, file.getName());
					if (file.renameTo(newname)) {
					} else {
					}
				}
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
