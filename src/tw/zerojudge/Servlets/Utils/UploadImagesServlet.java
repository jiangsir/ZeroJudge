package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemimageDAO;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.Problemimage;
import tw.zerojudge.Tables.User.ROLE;

@MultipartConfig(maxFileSize = 1 * 1024 * 1024, maxRequestSize = 1 * 1024 * 1024)
@WebServlet(urlPatterns = { "/UploadImages.api" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class UploadImagesServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass().getName());
	public static final HashMap<String, String> allowImageTypes = new HashMap<String, String>();
	static {
		allowImageTypes.put("FFD8FF", "jpg");
		allowImageTypes.put("89504E47", "png");
		allowImageTypes.put("47494638", "gif");
		allowImageTypes.put("49492A00", "tif");
		allowImageTypes.put("424D", "bmp");
	}
	public static final HashSet<String> allowContentTypes = new HashSet<String>();
	static {
		allowContentTypes.add("image/jpeg");
		allowContentTypes.add("image/png");
		allowContentTypes.add("image/gif");
	}

	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	public String getFilename(Part part) {
		String header = part.getHeader("Content-Disposition");
		String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
		return filename;
	}

	/**
	 * @author guoxk
	 * 
	 *         方法描述：將要讀取檔案頭資訊的檔案的byte陣列轉換成string型別表示
	 * @param src 要讀取檔案頭資訊的檔案的byte陣列
	 * @return 檔案頭資訊
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}

	/**
	 * @author guoxk
	 * 
	 *         方法描述：根據檔案路徑獲取檔案頭資訊
	 * @param filePath 檔案路徑
	 * @return 檔案頭資訊
	 * @throws IOException
	 */
	private static String getFileHeader(Part part) throws IOException {
		InputStream is = part.getInputStream();
		String value = null;
		try {
			byte[] b = new byte[4];
			/*
			 * int read() 從此輸入流中讀取一個數據位元組。int read(byte[] b) 從此輸入流中將最多 b.length
			 * 個位元組的資料讀入一個 byte 陣列中。 int read(byte[] b, int off, int len)
			 * 從此輸入流中將最多 len 個位元組的資料讀入一個 byte 陣列中。
			 */
			is.read(b, 0, b.length);
			value = bytesToHexString(b);
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}

	/**
	 * @author guoxk
	 * 
	 *         方法描述：根據檔案路徑獲取檔案頭資訊
	 * @param filePath 檔案路徑
	 * @return 檔案頭資訊
	 * @throws IOException
	 */
	public static String getImageType(Part part) throws IOException {
		return allowImageTypes.get(getFileHeader(part));
	}

	public static enum POSTACTION {
		uploadProblemImage, 
		uploadForumImage, 
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		try {
			switch (POSTACTION.valueOf(action)) {
				case uploadProblemImage:
					new UpdateProblemServlet().AccessFilter(request);
					int imageid = doPost_uploadProblemImage(request, response);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"location\":\"ShowImage?id=" + imageid + "\"}");
					break;
				case uploadForumImage:
					int forumimageid = doPost_uploadForumImage(request, response);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"location\":\"ShowImage?id=" + forumimageid + "\"}");
					break;
				default:
					response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
					response.getWriter().print("未知的上傳行為! " + action);
					logger.warning("未知的上傳行為! " + action);
					response.flushBuffer();
					break;
			}
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
			response.getWriter().print("未知的上傳行為! action=" + action);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
			logger.warning("上傳圖片有錯誤! " + e.getLocalizedMessage());
			response.getWriter().print("上傳圖片有錯誤! " + e.getLocalizedMessage());
		}
		response.flushBuffer();
		return;

	}

	/**
	 * 上傳題目圖片!
	 * 
	 * @param request
	 * @param response
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	private int doPost_uploadProblemImage(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, ServletException, DataException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		ProblemimageDAO imageDao = new ProblemimageDAO();
		for (Part part : request.getParts()) {
			if ("file".equals(part.getName())) {
				String filename = this.getFilename(part);
				if (filename == null || "".equals(filename)) {
					break;
				}

				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String contentType = part.getContentType().toLowerCase();

				String suffixName = contentType.substring(contentType.indexOf("/") + 1);
				String imageName = uuid + "." + suffixName;
				String imageType = getImageType(part);

				if (!allowContentTypes.contains(contentType)) {
					throw new DataException("不支援的圖片檔(" + filename + ")。");
				} else {
					String problemid = request.getParameter("problemid");
					Problemimage problemimage = new Problemimage();
					problemimage.setProblemid(problemid);
					problemimage.setFilename(filename);
					problemimage.setFiletype(contentType);
					problemimage.setFile(part.getInputStream());
					problemimage.setDescript("imageType:" + imageType);
					int id = imageDao.insert(problemimage);
					return id;
				}
			}
		}
		return 0;
	}

	/**
	 * 上傳討論區圖片
	 * 
	 * @param request
	 * @param response
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	private int doPost_uploadForumImage(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, ServletException, DataException {
		ProblemimageDAO imageDao = new ProblemimageDAO();
		for (Part part : request.getParts()) {
			if ("file".equals(part.getName())) {
				String filename = this.getFilename(part);
				if (filename == null || "".equals(filename)) {
					break;
				}

				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String contentType = part.getContentType().toLowerCase().trim();

				String suffixName = contentType.substring(contentType.indexOf("/") + 1);
				String imageName = uuid + "." + suffixName;
				String imageType = getImageType(part);

				if (!allowContentTypes.contains(contentType)) {
					throw new DataException("不支援的圖片檔(" + filename + ")。");
				} else {
					String articleid = request.getParameter("articleid");
					Problemimage problemimage = new Problemimage();
					problemimage.setFilename(filename);
					problemimage.setFiletype(contentType);
					problemimage.setFile(part.getInputStream());
					problemimage.setDescript("articleid=" + articleid);
					int id = imageDao.insert(problemimage);
					return id;
				}
			}
		}
		return 0;
	}

}
