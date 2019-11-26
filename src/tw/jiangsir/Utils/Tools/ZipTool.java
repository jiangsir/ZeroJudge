package tw.jiangsir.Utils.Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Objects.TestdataPair;
import tw.zerojudge.Objects.Testdatafile;

/**
 * 處理檔案壓縮。
 * 
 * @author jiangsir
 * 
 */
public class ZipTool {

	/**
	 * 
	 * @param baseDir
	 *            所要压缩的目录名（包含绝对路径）
	 * @param objFileName
	 *            压缩后的文件名
	 * @throws Exception
	 */
	public static File createZip(ArrayList<File> files, File zipFile) throws Exception {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		int readLen = 0;
		for (File f : files) {
			ze = new ZipEntry(f.getName());
			ze.setSize(f.length());
			ze.setTime(f.lastModified());
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.close();
		return zipFile;
	}

	/**
	 * Compress the given files.
	 */
	public static byte[] zipTestdataFiles(ArrayList<TestdataPair> testdataPairs) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte bytes[] = new byte[2048];
		ArrayList<Testdatafile> testdataFiles = new ArrayList<Testdatafile>();
		for (TestdataPair pair : testdataPairs) {
			testdataFiles.add(pair.getInfile());
			testdataFiles.add(pair.getOutfile());
		}
		for (Testdatafile file : testdataFiles) {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			zos.putNextEntry(new ZipEntry(file.getDownloadName()));
			int bytesRead;
			while ((bytesRead = bis.read(bytes)) != -1) {
				zos.write(bytes, 0, bytesRead);
			}
			zos.closeEntry();
			bis.close();
			fis.close();
		}
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();

		return baos.toByteArray();
	}

	/**
	 * Compress the given files.
	 */
	public static byte[] zipFiles(ArrayList<File> files) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte bytes[] = new byte[2048];
		for (File file : files) {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			zos.putNextEntry(new ZipEntry(file.getName()));
			int bytesRead;
			while ((bytesRead = bis.read(bytes)) != -1) {
				zos.write(bytes, 0, bytesRead);
			}
			zos.closeEntry();
			bis.close();
			fis.close();
		}
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();

		return baos.toByteArray();
	}

}
