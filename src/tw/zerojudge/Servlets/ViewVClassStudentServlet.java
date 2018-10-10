package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.VClass;
import tw.zerojudge.Tables.VClassStudent;

@WebServlet(urlPatterns = { "/ViewVClassStudent" })
@RoleSetting
public class ViewVClassStudentServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 取得所有該 vclass 曾出過的題目，並且搭配 user 已答對的 aclist 來顯示統計表<br>
	 * 較接近 "解題統計", 但 "所有曾出過的題目", 並不好取。已經出了，但是還沒開始的 contest <br>
	 * 照理說題目應該要保密，因此不應該出現。這就會造成在 updateProblemids 的時機不好找<br>
	 * 
	 * @param vclass
	 * @param student
	 * @return
	 * @throws DataException
	 */
	private ArrayList<String[]> getVProblems(VClass vclass,
			VClassStudent student) throws DataException {
		TreeSet<Problemid> aclists = student.getVclassaclist();
		TreeSet<Problemid> problemids = vclass.getProblemids();
		ArrayList<String[]> problems = new ArrayList<String[]>();
		for (Problemid problemid : problemids) {
			String[] problem;
			if (aclists.contains(problemid)) {
				problem = new String[] {
						problemid.toString(),
						new ProblemService().getProblemByProblemid(problemid)
								.getTitle(), ServerOutput.JUDGEMENT.AC.name() };
			} else {
				problem = new String[] {
						problemid.toString(),
						new ProblemService().getProblemByProblemid(problemid)
								.getTitle(), "" };
			}
			problems.add(problem);
		}
		return problems;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String account = request.getParameter("account");
		User user = new UserService().getUserByAccount(account);
		Integer vclassid = Parameter.parseInteger(request
				.getParameter("vclassid"));
		VClass vclass = new VClassDAO().getVClassById(vclassid);
		request.setAttribute("user", user);
		VClassStudent student = vclass.getStudent(user.getId());
		request.setAttribute("vproblems", this.getVProblems(vclass, student));
		request.setAttribute("vclass", vclass);
		request.getRequestDispatcher("ViewVClassStudent.jsp").forward(request,
				response);
	}
}
