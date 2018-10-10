package tw.zerojudge.Api;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.TLDs.ProblemTLD;
import tw.jiangsir.Utils.Tools.ZipTool;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Servlets.VerifyingProblemsServlet;
import tw.zerojudge.Servlets.Utils.DebugServlet;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Problem.api" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ProblemApiServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	private static enum GETACTION {
		getProblemTitles, 
		downloadTestdatasToZip, 
		getSpecialJudgeFiles, 
		getTestdataFiles, 
		getTestdataPairs, 
		rsyncAllTestdatas, 
		rsyncTestdatasByProblem, 
		getWaitingRejudgeSize, //
		getSubmitnum, //
		deleteTestdata, //
		getIndata, //
		getOutdata, //
		getInputTestdataInfo, //
		getOutputTestdataInfo, //
		getDetail, //
		getJsondetails, //
		getJsoninfo, //
		getPrejudge, //
		getVerify, //
		openProblem, //
		verifyProblem, //
		setProblemDisplay, 
		setTabid, //
		setDifficulty, //
		getKeywords, 
		getProblemSettings, 
		exportProblems, 
		exportProblem, 
		recountAcceptedProblem, 
		recountAllAcceptedProblem 
	};

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		if (request.getMethod().equals("GET")) {
			String action = request.getParameter("action");
			switch (GETACTION.valueOf(action)) {
			case getSpecialJudgeFiles:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case downloadTestdatasToZip:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case rsyncAllTestdatas:
				new DebugServlet().AccessFilter(request);
				break;
			case rsyncTestdatasByProblem:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case deleteTestdata:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case exportProblem:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case exportProblems:
//					OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
//					String problemids = request.getParameter("problemids");
//					if (problemids == null) {
//						break;
//					}
//					for (String problem_id : problemids.split(",")) {
//						new UpdateProblemServlet().AccessFilter(onlineUser,
//								new ProblemService().getProblemByProblemid(new Problemid(problem_id.trim())));
//						
//						
//					}

				break;
			case getDetail:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getIndata:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getInputTestdataInfo:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getJsondetails:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getJsoninfo:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getKeywords:
				break;
			case getOutdata:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getOutputTestdataInfo:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getPrejudge:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case getProblemSettings:
				break;
			case getProblemTitles:
				break;
			case getSubmitnum:
				break;
			case getVerify:
				new VerifyingProblemsServlet().AccessFilter(request);
				break;
			case getWaitingRejudgeSize:
				break;
			case openProblem:
				new VerifyingProblemsServlet().AccessFilter(request);
				break;
			case recountAcceptedProblem:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case recountAllAcceptedProblem:
				new DebugServlet().AccessFilter(request);
				break;
			case setDifficulty:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case setProblemDisplay:
				new VerifyingProblemsServlet().AccessFilter(request);
				break;
			case setTabid:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			case verifyProblem:
				new VerifyingProblemsServlet().AccessFilter(request);
				break;
			default:
				break;
			}
		} else if (request.getMethod().equals("POST")) {
			new UpdateProblemServlet().AccessFilter(request);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String action = request.getParameter("action");
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		switch (GETACTION.valueOf(action)) {
		case getProblemTitles:
			String problemids = request.getParameter("problemids");
			Contest contest = new Contest();
			try {
				contest.setProblemids(problemids);
			} catch (Exception e) {
			}
			request.setAttribute("problems", contest.getProblems());
			request.getRequestDispatcher("include/div/ProblemTitles.jsp").forward(request, response);
			return;
		case downloadTestdatasToZip:
			new UpdateProblemServlet().AccessFilter(request);

			byte[] zip = ZipTool.zipTestdataFiles(problem.getTestdataPairs());
			ServletOutputStream sos = response.getOutputStream();
			response.setContentType(ContentType.zip.getValue());
			response.setHeader("Content-Disposition", "attachment; filename=" + problemid + ".zip");
			sos.write(zip);
			sos.flush();
			return;
		case getSpecialJudgeFiles:
			new UpdateProblemServlet().AccessFilter(request);

			this.doGet_getSpecialJudgeFiles(request, response);
			return;
		case getTestdataFiles:
			new UpdateProblemServlet().AccessFilter(request);

			request.setAttribute("problem", problem);
			request.getRequestDispatcher("include/div/TestdataFiles.jsp").forward(request, response);
			return;
		case getTestdataPairs:
			new UpdateProblemServlet().AccessFilter(request);

			return;
		case rsyncAllTestdatas:
			if (onlineUser.getIsDEBUGGER())
				new ProblemService().rsyncAllTestdatas();
			break;
		case rsyncTestdatasByProblem:
			new UpdateProblemServlet().AccessFilter(request);

			try {
				new ProblemService().rsyncTestdataByProblem(problem);
			} catch (Exception e) {
				throw new JQueryException(e.getLocalizedMessage());
			}
			break;
		case deleteTestdata:
			new UpdateProblemServlet().AccessFilter(request);

			int index = Integer.parseInt(request.getParameter("index"));
			new ProblemService().doDeleteTestdataPair(problem, index);
			break;
		case exportProblem:
			new UpdateProblemServlet().AccessFilter(request);

			problemid = new Problemid(request.getParameter("problemid"));
			File file = new ProblemDAO().ExportProblemJson(problemid, onlineUser);
			this.downloadFile(response, file, ContentType.octet);
			break;
		case exportProblems:
//				problemids = request.getParameter("problemids");
//				if (problemids == null) {
//					break;
//				}
//				ArrayList<File> files = new ArrayList<File>();
//				for (String problem_id : problemids.split(",")) {
//					files.add(new ProblemDAO().ExportProblemJson(new Problemid(problem_id.trim())));
//				}
//
//				ServletOutputStream sos2 = response.getOutputStream();
//				response.setContentType(ContentType.zip.getValue());
//				response.setHeader("Content-Disposition",
//						"attachment; filename=" + problemids.replaceAll(" ", "").replaceAll(",", "") + ".zip");
//				sos2.write(ZipTool.zipFiles(files));
//				sos2.flush();
//
//				
//				
//				
//				
//				
//				
//				
//				
			break;
		case getDetail:
			new UpdateProblemServlet().AccessFilter(request);
			response.getWriter().print(problem.getServeroutputs());
			break;
		case getIndata:
			new UpdateProblemServlet().AccessFilter(request);
			index = Integer.parseInt(request.getParameter("index"));
			response.getWriter().print(problem.getTestdatapair(index).getInfile().getData());
			break;
		case getInputTestdataInfo:
			new UpdateProblemServlet().AccessFilter(request);

			index = Integer.parseInt(request.getParameter("index"));
			response.getWriter().print(problem.getTestdatapair(index).getInfile());
			break;
		case getJsondetails:
			new UpdateProblemServlet().AccessFilter(request);

			try {
				response.getWriter().print(mapper.writeValueAsString(problem.getServerOutputBeans()));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			}
			break;
		case getJsoninfo:
			new UpdateProblemServlet().AccessFilter(request);
			JSONObject json = new JSONObject();
			try {
				json.put("prejudgement", problem.getPrejudgement().name());
				json.put("prejudgeinfo", problem.getSummary());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			response.getWriter().print(json.toString());
			break;
		case getKeywords:
			response.getWriter().print(mapper
					.writeValueAsString(new ProblemService().getSuggestKeywords(session, request.getServletPath())));
			break;
		case getOutdata:
			new UpdateProblemServlet().AccessFilter(request);
			index = Integer.parseInt(request.getParameter("index"));
			response.getWriter().print(problem.getTestdatapair(index).getOutfile().getData());
			break;
		case getOutputTestdataInfo:
			new UpdateProblemServlet().AccessFilter(request);
			index = Integer.parseInt(request.getParameter("index"));
			response.getWriter().print("problem.getOutTestfileInfo(index)");
			break;
		case getPrejudge:
			new UpdateProblemServlet().AccessFilter(request);
			response.getWriter().print(problem.getHtml_PrejudgeResult());
			break;
		case getProblemSettings:
			json = new JSONObject();
			try {
				json.put("tabid", problem.getTabid());
				json.put("difficulty", problem.getDifficulty());
				json.put("backgrounds", problem.getBackgrounds().toString());
				json.put("keywords", problem.getKeywords());
				json.put("sortable", problem.getSortable());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			response.getWriter().print(json.toString());
			break;
		case getSubmitnum:
			response.getWriter().print(new ProblemService().getCountByProblemSubmissions(problem));
			break;
		case getVerify:
			new VerifyingProblemsServlet().AccessFilter(request);
			response.getWriter().print(ProblemTLD.getHtmlVerify(onlineUser, problem));
			break;
		case getWaitingRejudgeSize:
			response.getWriter().print(problem.getWaitingRejudgeSize());
			break;
		case openProblem:
			new VerifyingProblemsServlet().AccessFilter(request);

			if (onlineUser.getIsQualifiedAuthor()) {
				problem.setDisplay(Problem.DISPLAY.open);
				new ProblemService().update(problem);
			}
			break;
		case setDifficulty:
			new UpdateProblemServlet().AccessFilter(request);

			problem.setDifficulty(Integer.parseInt(request.getParameter("difficulty")));
			new ProblemService().update(problem);
			break;
		case setProblemDisplay:
			new VerifyingProblemsServlet().AccessFilter(request);

			try {
				Problem.DISPLAY display = Problem.DISPLAY.valueOf(request.getParameter("display"));
				if ((display == Problem.DISPLAY.open && onlineUser.getIsQualifiedAuthor())
						|| display != Problem.DISPLAY.open) {
					problem.setDisplay(display);
					new ProblemService().update(problem);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case setTabid:
			new UpdateProblemServlet().AccessFilter(request);

			problem.setTabid(request.getParameter("tabid"));
			new ProblemService().update(problem);
			break;
		case verifyProblem:
			new UpdateProblemServlet().AccessFilter(request);

			problem.setDisplay(Problem.DISPLAY.verifying);
			new ProblemService().update(problem);
			break;
		case recountAcceptedProblem:
			new UpdateProblemServlet().AccessFilter(request);

			problemid = new Problemid(request.getParameter("problemid"));
			new ProblemService().recountProblem_UserCount(problemid);
			break;
		case recountAllAcceptedProblem:
			if (onlineUser.getIsHigherEqualThanMANAGER()) {
				new ProblemService().recountAllProblem_UserCount();
			}
			break;
		default:
			break;

		}
	}

	private void doGet_getSpecialJudgeFiles(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		if (!problemid.getIsLegal()) {
			throw new JQueryException("problemid 有誤(problemid=" + problemid + ")！無法讀取。");
		}
		request.setAttribute("problem", new ProblemService().getProblemByProblemid(problemid));
		request.getRequestDispatcher("include/div/SpecialJudgeFiles.jsp").forward(request, response);
	}

	private static enum POSTACTION {
		writeScoresTimelimits, 
		deleteSpecialFile, //
		deleteTestdataPair, 
		getServerOutputs, 
		DetailDialog, 
		setProblemSettings, //
		doDeprecateProblem, //
		doRejudge; 
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		switch (POSTACTION.valueOf(action)) {
		case writeScoresTimelimits:
			new UpdateProblemServlet().AccessFilter(request);
			try {
				problem.checkScores(request.getParameterValues("scores"));
				problem.setScores(request.getParameterValues("scores"));

				problem.checkTimelimits(request.getParameterValues("timelimits"));
				problem.setTimelimits(request.getParameterValues("timelimits"));

				problem.setTestfilelength(Math.min(problem.getScores().length, problem.getTimelimits().length));
				new ProblemService().update(problem);
			} catch (Exception e) {
				e.printStackTrace();
				throw new JQueryException(e.getLocalizedMessage());
			}
			return;
		case deleteSpecialFile:
			new UpdateProblemServlet().AccessFilter(request);

			int index = Integer.parseInt(request.getParameter("index"));
			problem.getSpecialJudgeFiles().get(index).delete();
			return;
		case deleteTestdataPair:
			new UpdateProblemServlet().AccessFilter(request);

			index = Integer.parseInt(request.getParameter("index"));
			new ProblemService().doDeleteTestdataPair(problem, index);
			return;
		case getServerOutputs:
			request.setAttribute("serveroutputs", problem.getServeroutputs());
			request.setAttribute("problem", problem);
			request.getRequestDispatcher("api/ServerOutputs.jsp").forward(request, response);
			return;
		case DetailDialog:
			request.setAttribute("serveroutputs", problem.getServeroutputs());
			request.getRequestDispatcher("include/dialog/ShowDetail.jsp").forward(request, response);
			return;
		case doDeprecateProblem:
			new UpdateProblemServlet().AccessFilter(request);
			problem.setDisplay(Problem.DISPLAY.deprecated);
			new ProblemService().update(problem);
			break;
		case setProblemSettings:
			new UpdateProblemServlet().AccessFilter(request);
			try {
				problem.setMemorylimit(request.getParameter("memorylimit"));
				problem.setDifficulty(request.getParameter("difficulty"));
				problem.setBackgrounds(request.getParameter("backgrounds"));
				problem.setKeywords(request.getParameter("keywords"));
				problem.setReference(request.getParameter("reference"));
				problem.setSortable(request.getParameter("sortable"));
				problem.setWa_visible(request.getParameter("wa_visible"));
				problem.setTabid(request.getParameter("tabid"));
				problem.setJudgemode(request.getParameter("judgemode"));
				new ProblemService().updateProblemSettings(problem);
			} catch (Exception e) {
				e.printStackTrace();
				throw new JQueryException(e.getLocalizedMessage());
			}
			break;
		case doRejudge:
			new UpdateProblemServlet().AccessFilter(request);
			this.reJudgeProblemid(request, response, problem);
			break;
		default:
			break;
		}
	}

	public static enum ContentType {
		octet("application/octet-stream"), //
		zip("application/zip");

		private String value;

		private ContentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	private void downloadFile(HttpServletResponse response, File file, ContentType type)
			throws UnsupportedEncodingException {
		String filename = file.getName();

		filename = new String(filename.getBytes("Big5"), "ISO8859_1");

		response.reset(); 
		response.setContentType(type.getValue() + ";charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.write(FileUtils.readFileToString(file));
			writer.flush();
			writer.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下載 ExportProblem.zip
	 * 
	 * @param upfileid
	 */
	public void downloadExportProblem(HttpServletResponse response, File file) {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			String filename = new String(file.getName().getBytes("UTF-8"), "ISO8859_1");
			response.setHeader("Content-Type", "application/zip");
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "filename=\"" + filename + "\"");
			out.write(FileUtils.readFileToByteArray(file));
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
	 * 針對一個 problem 的所有 solution 進行重測
	 * 
	 * @param request
	 * @param response
	 * @param problemid
	 * @throws IOException
	 * @throws ServletException
	 */
	private void reJudgeProblemid(HttpServletRequest request, HttpServletResponse response, Problem problem)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		for (Solution solution : new SolutionService().getSolutionsByProblemid(problem.getProblemid())) {
			solution.doRejudge(onlineUser);
		}
		return;
	}

}
