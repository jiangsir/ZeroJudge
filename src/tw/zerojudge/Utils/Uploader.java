package tw.zerojudge.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Factories.UserFactory;

public class Uploader {
	public static final int MAX_FILESIZE = 100 * 1024 * 1024; 
	private HashMap<String, ArrayList<String>> formfields = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<FileItem>> filefields = new HashMap<String, ArrayList<FileItem>>();
	private List<?> items = null;
	private boolean hasException;
	private String Exception;

	public boolean isHasException() {
		return hasException;
	}

	public void setHasException(boolean hasException) {
		this.hasException = hasException;
	}

	public String getException() {
		return Exception;
	}

	public void setException(String exception) {
		Exception = exception;
	}

	public Uploader(HttpServletRequest request, HttpServletResponse response)
			throws DataException {
		DiskFileItemFactory factory = new DiskFileItemFactory(MAX_FILESIZE,
				new File("/tmp"));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(Uploader.MAX_FILESIZE); 

		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
			throw new DataException("上傳錯誤！檔案上限為 " + Uploader.MAX_FILESIZE
					+ " bytes. 請勿上傳過大檔案。e=" + e1.getLocalizedMessage());
		}
		if (items == null) {
			throw new DataException("上傳錯誤(items==null)！檔案上限為 "
					+ Uploader.MAX_FILESIZE + " bytes. 請勿上傳過大檔案。");
		}

		Iterator<?> it = items.iterator();
		while (it.hasNext()) {
			FileItem item = (FileItem) it.next();
			if (item.isFormField()) {
				ArrayList<String> values = new ArrayList<String>();
				if (this.formfields.containsKey(item.getFieldName())) {
					values = this.formfields.get(item.getFieldName());
				}
				try {
					values.add(item.getString("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				this.formfields.put(item.getFieldName(), values);
			} else {
				ArrayList<FileItem> items = new ArrayList<FileItem>();
				if (this.filefields.containsKey(item.getFieldName())) {
					items = this.filefields.get(item.getFieldName());
				}
				items.add(item);
				this.filefields.put(item.getFieldName(), items);
			}
		}
	}


	/**
	 * 取得多個同名 field
	 * 
	 * @param fieldname
	 * @return
	 */
	public ArrayList<String> getFormFieldValues(String fieldname) {
		return this.formfields.get(fieldname);
	}

	/**
	 * 當確定該 fieldname 只有一個值的話，就直接取一個值比較簡便
	 * 
	 * @param fieldname
	 * @return
	 */
	public String getParameter(String fieldname) {
		if (this.formfields.containsKey(fieldname)) {
			return this.formfields.get(fieldname).get(0);
		}
		return null;
	}

	public ArrayList<FileItem> getFileItems(String fieldname) {
		ArrayList<FileItem> items = new ArrayList<FileItem>();
		ArrayList<FileItem> fieldnames = this.filefields.get(fieldname);
		if (fieldnames == null) {
			return items;
		}
		Iterator<FileItem> it = fieldnames.iterator();
		while (it.hasNext()) {
			items.add(it.next());
		}
		return items;
	}

	public ArrayList<String> getFilenames(String fieldname) {
		ArrayList<String> filenames = new ArrayList<String>();
		Iterator<?> it = this.filefields.get(fieldname).iterator();
		while (it.hasNext()) {
			filenames.add(((FileItem) it.next()).getName());
		}
		return filenames;
	}

	public ArrayList<String> getFileContentTypes(String fieldname) {
		ArrayList<String> ContentTypes = new ArrayList<String>();
		Iterator<?> it = this.filefields.get(fieldname).iterator();
		while (it.hasNext()) {
			ContentTypes.add(((FileItem) it.next()).getContentType());
		}
		return ContentTypes;
	}

	public ArrayList<Long> getFileSizes(String fieldname) {
		ArrayList<Long> filesizes = new ArrayList<Long>();
		Iterator<?> it = this.filefields.get(fieldname).iterator();
		while (it.hasNext()) {
			filesizes.add(((FileItem) it.next()).getSize());
		}
		return filesizes;
	}

	public void checkUploadFiles(ArrayList<FileItem> fileItems)
			throws DataException {
		Iterator<?> it = fileItems.iterator();
		while (it.hasNext()) {
			FileItem fileItem = (FileItem) it.next();
			String name = fileItem.getName();
			if (name != null) {
				name = FilenameUtils.getName(name);
			}
			this.checkUploadFile(fileItem);
		}
	}

	public void checkUploadFile(FileItem fileItem) throws DataException {
		if (fileItem.getSize() > Uploader.MAX_FILESIZE) {
			throw new DataException("檔案超過上限");
		}
		if (new Utils().hasFullSize(fileItem.getName())) {
			throw new DataException("不接受中文檔名");
		}
	}

	/**
	 * 檔名加上 prefix 才能保證檔名不會相同造成覆蓋, 一般可以傳入 upfilesid
	 * 
	 * @param fieldname
	 * @param prefix
	 * @throws AccessException
	 * @throws AccessException
	 */
	public void uploadFile(FileItem fileItem, File uploadedfile)
			throws AccessException {
		this.checkUploadFile(fileItem);

		if (!uploadedfile.getParentFile().exists()) {
			if (!uploadedfile.getParentFile().mkdirs()) {
				throw new DataException("無法建立目錄, 請檢查存取權限！");
			}
		}
		try {
			fileItem.write(uploadedfile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccessException(UserFactory.getNullOnlineUser(), e
					.getLocalizedMessage().replaceAll(uploadedfile.getParent(),
							""));
		}
	}
}
