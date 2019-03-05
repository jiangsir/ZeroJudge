package tw.zerojudge.Servlets.Json;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Problem.SpecialJudgeFile;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = {"/Problem.json"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class ProblemJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 

	public static enum DATA {
		GetSpecialJudgeCode, 
		TestdataPair, 
		WaitingSubmissionCount, 
		SubmissionCount, 
		PrejudgeInfo, 
		ServerOutputs; 
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String data = request.getParameter("data");
		try {
			Method method = this.getClass().getMethod("getJSON_" + DATA.valueOf(data), HttpServletRequest.class);
			String jsonString = (String) method.invoke(this, new Object[]{request});
			response.getWriter().write(jsonString);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new JQueryException(new Alert(e.getTargetException()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String getJSON_GetSpecialJudgeCode(HttpServletRequest request) {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		Problemid problemid = Problemid.parseProblemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		if (!onlineUser.isCanUpdateProblem(problem)) {
			throw new DataException("權限不足。");
		}
		Problem pp = new Problem();
		pp.setSpecialjudge_language(request.getParameter("specialjudge_language"));
		SpecialJudgeFile file = problem.getSpecialJudgeFile(pp.getSpecialjudge_language());
		try {
			return mapper.writeValueAsString(String.valueOf(FileUtils.readFileToString(file)));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "xxxxx";
	}

	/**
	 * 提供前端 JQuery.getJSON 使用。
	 * 
	 * @return
	 */
	public String getJSON_ServerOutputs(HttpServletRequest request) {
		Problemid problemid = Problemid.parseProblemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		try {
			if (problem.getServeroutputs().length == 0) {
				return mapper.writeValueAsString(new ProblemDAO().getProblemByPid(problem.getId()).getServeroutputs());
			}
			ResourceBundle resource = ResourceBundle.getBundle("resource",
					new SessionScope(request).getSession_locale());
			ServerOutput[] serverOutputs = problem.getServeroutputs();
			for (ServerOutput serverOutput : serverOutputs) {
				String hint = serverOutput.getHint().equals("")
						? resource.getString("Server.REASON." + serverOutput.getReason().name())
						: serverOutput.getHint();
				serverOutput.setHint(hint);
			}
			return mapper.writeValueAsString(problem.getServeroutputs());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}


	/**
	 * 提供前端 JQuery.getJSON 使用。
	 * 
	 * @return
	 */
	public String getJSON_PrejudgeInfo(HttpServletRequest request) {
		Problemid problemid = Problemid.parseProblemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		class PrejudgeInfo {
			private String prejudgement;
			private String prejudgeinfo;

			public String getPrejudgement() {
				return prejudgement;
			}

			public void setPrejudgement(String prejudgement) {
				this.prejudgement = prejudgement;
			}

			public String getPrejudgeinfo() {
				return prejudgeinfo;
			}

			public void setPrejudgeinfo(String prejudgeinfo) {
				this.prejudgeinfo = prejudgeinfo;
			}
		}
		PrejudgeInfo prejudgeinfo = new PrejudgeInfo();
		prejudgeinfo.setPrejudgement(problem.getPrejudgement().name());
		prejudgeinfo.setPrejudgeinfo(problem.getSummary());
		try {
			return mapper.writeValueAsString(prejudgeinfo);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}


	public String getJSON_SubmissionCount(HttpServletRequest request) {
		Problemid problemid = Problemid.parseProblemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		try {
			return mapper
					.writeValueAsString(String.valueOf(new ProblemService().getCountByProblemSubmissions(problem)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}


	public String getJSON_WaitingSubmissionCount(HttpServletRequest request) {
		Problemid problemid = Problemid.parseProblemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		try {
			return mapper.writeValueAsString(String.valueOf(problem.getWaitingRejudgeSize()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}


	public String getJSON_TestdataPair(HttpServletRequest request) {
		class JSON_TestdataPair {
			private String infilename;
			private String outfilename;
			private String indata;
			private String outdata;

			public String getInfilename() {
				return infilename;
			}

			public void setInfilename(String infilename) {
				this.infilename = infilename;
			}

			public String getOutfilename() {
				return outfilename;
			}

			public void setOutfilename(String outfilename) {
				this.outfilename = outfilename;
			}

			public String getIndata() {
				return indata;
			}

			public void setIndata(String indata) {
				this.indata = indata;
			}

			public String getOutdata() {
				return outdata;
			}

			public void setOutdata(String outdata) {
				this.outdata = outdata;
			}

		}
		Problemid problemid = Problemid.parseProblemid(request.getParameter("problemid"));
		int index = Integer.parseInt(request.getParameter("index"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		JSON_TestdataPair pair = new JSON_TestdataPair();
		pair.setInfilename(problem.getTestdatapair(index).getInfile().getName());
		pair.setOutfilename(problem.getTestdatapair(index).getOutfile().getName());
		pair.setIndata(problem.getTestdatapair(index).getInfile().getData());
		pair.setOutdata(problem.getTestdatapair(index).getOutfile().getData());
		try {
			return mapper.writeValueAsString(pair);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
