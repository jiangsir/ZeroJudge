package tw.zerojudge.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploader {
	private Map<String, List<FileItem>> fileField = new TreeMap<String, List<FileItem>>();
	private Map<String, Object> formField = new TreeMap<String, Object>();
	private List<FileItem> list = new ArrayList<FileItem>();
	private int memoryBlock = 2048;
	private File tempFolder = null;
	private boolean multipart = false;
	private HttpServletRequest request = null;
	private final int maxSize = 100 * 1024 * 1024; 

	public FileUploader(File tempFolder, int memeoryBlock) {
		this.tempFolder = tempFolder;
		this.memoryBlock = memeoryBlock;
	}

	public FileUploader() {
	}

	public void parse(HttpServletRequest request, String charset)
			throws FileUploadException {
		this.request = request;
		multipart = FileUpload.isMultipartContent(request);
		if (multipart) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(memoryBlock);
			factory.setRepository(tempFolder);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxSize);
			List items;
			items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
					processFormField(item, charset);
				} else {
					processUploadedFile(item);
				}
			}
		}
	}

	private void processFormField(FileItem item, String charset) {
		try {
			String name = item.getFieldName();
			String value = item.getString(charset);
			Object objv = formField.get(name);
			if (objv == null) {
				formField.put(name, value);
			} else {
				List<String> values = null;
				if (objv instanceof List) {
					values = (List<String>) objv;
					values.add(value);
				} else {
					String strv = (String) objv;
					values = new ArrayList<String>();
					values.add(strv);
					values.add(value);
				}
				formField.put(name, values);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"the argument \"charset\" missing!");
		}
	}

	private void processUploadedFile(FileItem item) {
		String name = item.getFieldName();
		long size = item.getSize();
		if (size > 0)
			list.add(item);
		fileField.put(name, list);
	}

	public static boolean write2file(FileItem item, File file) {
		boolean flag = false;
		try {
			item.write(file);
			flag = true;
		} catch (Exception e) {
		}
		return flag;
	}

	public List<FileItem> getFileItemList(String name) {
		if (multipart) {
			return fileField.get(name);
		} else {
			return null;
		}
	}

	public String getParameter(String name) {
		String value = null;
		if (multipart) {
			Object obj = formField.get(name);
			if (obj != null && obj instanceof String) {
				value = (String) obj;
			}
		} else if (request != null) {
			value = request.getParameter(name);
		}
		return value;
	}

	public String[] getParameterValues(String name) {
		String[] values = null;
		if (multipart) {
			Object obj = formField.get(name);
			if (obj != null) {
				if (obj instanceof List) {
					values = (String[]) ((List) obj).toArray(new String[0]);
				} else {
					values = new String[] { (String) obj };
				}
			}
		} else if (request != null) {
			values = request.getParameterValues(name);
		}
		return values;
	}

	public File getRepository() {
		return this.tempFolder;
	}

	public int getSizeThreshold() {
		return this.memoryBlock;
	}

	public boolean isMultipart() {
		return this.multipart;
	}

	public int getMaxSize() {
		return maxSize;
	}
}
