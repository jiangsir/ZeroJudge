package tw.zerojudge.Servlets;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Objects.Testdatafile;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/Download.api" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class DownloadServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum TARGET {
		HttpscrtFile, 
		ContestCSV, 
		Executable, 
		CodeFile, 
		Indata, 
		Outdata; 
	};

	private HttpServletRequest request;
	private HttpServletResponse response;
	OnlineUser onlineUser = null;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String target = request.getParameter("target");
		switch (TARGET.valueOf(target)) {
		case HttpscrtFile:
			break;
		case CodeFile:
			try {
				new ShowCodeServlet().AccessFilter(request);
			} catch (AccessException e) {
				throw new AccessException("您沒有權限下載程式檔案！");
			}
			break;
		case ContestCSV:
			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			isAccessible_DownloadCSV(onlineUser, contest);
			break;
		case Executable:
			Solution solution = new SolutionService()
					.getSolutionById(Integer.parseInt(request.getParameter("solutionid")));
			contest = new ContestService().getContestById(solution.getContestid());
			if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser))
					&& contest.isCheckedConfig(Contest.CONFIG.UploadExefile)) {
				break;
			}
			throw new AccessException("您沒有權限下載執行檔！");

		case Indata:
			new UpdateProblemServlet().AccessFilter(request);
			return;
		case Outdata:
			new UpdateProblemServlet().AccessFilter(request);
			return;
		default:
			throw new AccessException("未知的動作！(" + target + ")");
		}
	}

	public static boolean isAccessible_DownloadCSV(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			return true;
		}
		throw new AccessException("您沒有權限下載 CSV 檔案！");
	}

	public static boolean isAccessible_DownloadExefile(OnlineUser onlineUser, Contest contest) throws AccessException {
		if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser))
				&& contest.isCheckedConfig(Contest.CONFIG.UploadExefile)) {
			return true;
		}
		throw new AccessException("您沒有權限下載執行檔(Contest.CONFIG.UploadExefile)！");
	}

	/**
	 * 下載系統內部檔案，該檔案可以不放在 webapps 內，可以增加檔案的安全性
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	private void doLocalDownload(File file, String download_filename) {
		BufferedInputStream in = null;
		ServletOutputStream out = null;
		FileInputStream stream = null;

		if (!file.exists()) {
			Message message = new Message();
			message.setType(Message.MessageType_ERROR);
			message.setPlainTitle("檔案不存在！");
			try {
				new MessageDAO().dispatcher(request, response, message);
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		try {
			out = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + download_filename + "\"");
			stream = new FileInputStream(file);

			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) { 
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void downloadContestCSV(Integer contestid) throws UnsupportedEncodingException {
		Contest contest = new ContestService().getContestById(contestid);
		String FileName = contest.getTitle() + ".csv";
		FileName = new String(FileName.getBytes("Big5"), "ISO8859_1");
		ArrayList<Contestant> contestants = new ContestService().getContestantsForRanking(contestid);
		StringBuffer content = new StringBuffer(1000000);
		content.append("名次,account,姓名,真實姓名,備註,學校,答對,送出,時間,分數\n");
		for (Contestant contestant : contestants) {
			content.append(contestant.getCurr_rank());
			content.append("," + contestant.getUser().getAccount());
			content.append("," + contestant.getTeamname());
			content.append("," + contestant.getUser().getTruename());
			content.append("," + contestant.getUser().getComment());
			content.append("," + contestant.getSchool());
			content.append("," + contestant.getAc());
			content.append("," + contestant.getSubmits());
			content.append("," + contestant.getPenalty());
			content.append("," + contestant.getScore());
			content.append("\n");
		}

		response.reset(); 
		response.setContentType("application/vnd.ms-excel; charset=Big5");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + FileName + "\"");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.write(content.toString());
			writer.flush();
			writer.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.request = request;
		this.response = response;
		HttpSession session = request.getSession(false);
		String target = request.getParameter("target");
		this.onlineUser = UserFactory.getOnlineUser(session);

		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Integer contestid = Contest.parseContestid(request.getParameter("contestid"));
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		Contest contest = new ContestService().getContestById(solution.getContestid());
		AppConfig appConfig = ApplicationScope.getAppConfig();
		switch (TARGET.valueOf(target)) {
		case HttpscrtFile:
			this.downloadHttpscrt();
			break;
		case CodeFile:
			break;
		case ContestCSV:
			this.downloadContestCSV(contestid);
			break;
		case Executable:
			this.downloadUpfileBySolutionid(solution.getId());
			break;
		case Indata:
			int index = Parameter.parseInteger(request.getParameter("index"));
			Testdatafile infile = problem.getTestdatapair(index).getInfile();
			this.doLocalDownload(infile, infile.getDownloadName());
			return;
		case Outdata:
			index = Parameter.parseInteger(request.getParameter("index"));
			Testdatafile outfile = problem.getTestdatapair(index).getOutfile();
			this.doLocalDownload(outfile, outfile.getDownloadName());
			return;
		default:
			throw new DataException("參數錯誤！");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		this.onlineUser = UserFactory.getOnlineUser(session);
	}

	private static final int BUFSIZE = 2048;

	/**
	 * 
	 * Sends a file to the ServletResponse output stream. Typically
	 * 
	 * you want the browser to receive a different name than the name the file
	 * has been saved in your local database, since your local names need to be
	 * unique.
	 * 
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 * 
	 * @param filename
	 *            The name of the file you want to download.
	 * 
	 * @param original_filename
	 *            The name the browser should receive.
	 * @throws AccessException
	 */

	private void doDownload(HttpServletRequest request, HttpServletResponse response, File file,
			String download_filename) throws IOException {

		if (!(file.isFile() && file.exists())) {
			throw new DataException("檔案不存在(" + file.getPath() + ")!");
		}
		int length = 0;
		ServletOutputStream op = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(file.getName());
		response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(download_filename, "UTF-8") + "\"");

		byte[] bbuf = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		while ((in != null) && ((length = in.read(bbuf)) != -1)) {
			op.write(bbuf, 0, length);
		}
		in.close();
		op.flush();
		op.close();
	}

	/**
	 * 從資料庫當中下載 Blob 成為一個檔案。
	 * 
	 * @param upfileid
	 */
	public void downloadUpfileBySolutionid(int solutionid) {
		Upfile upfile = new UpfileDAO().getUpfileBySolutionid(solutionid);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			String filename = new String(upfile.getFilename().getBytes("UTF-8"), "ISO8859_1");
			response.setHeader("Content-Type", upfile.getFiletype());
			response.setContentType(upfile.getFiletype());
			response.setHeader("Content-Disposition", "filename=\"" + filename + "\"");
			out.write(upfile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 從資料庫 byte[] 下載成檔案
	 * 
	 */
	public void downloadHttpscrt() {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			String filename = new String("zerojudge.crt".getBytes("UTF-8"), "ISO8859_1");
			response.setHeader("Content-Type", "APPLICATION/OCTET-STREAM");
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "filename=\"" + filename + "\"");
			out.write(ApplicationScope.getAppConfig().getHttpscrt());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
