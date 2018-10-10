package tw.jiangsir.Utils.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.http.HttpServlet;

import tw.jiangsir.Utils.Servlets.ShowSessionsServlet;
import tw.zerojudge.Api.ProblemApiServlet;
import tw.zerojudge.Api.Checks.CheckProblemServlet;
import tw.zerojudge.Servlets.DeleteVClassServlet;
import tw.zerojudge.Servlets.DeprecateProblemServlet;
import tw.zerojudge.Servlets.EditProblemsServlet;
import tw.zerojudge.Servlets.InsertContestServlet;
import tw.zerojudge.Servlets.InsertProblemServlet;
import tw.zerojudge.Servlets.InsertStudentsServlet;
import tw.zerojudge.Servlets.InsertUsersServlet;
import tw.zerojudge.Servlets.InsertVClassServlet;
import tw.zerojudge.Servlets.InsertVContestServlet;
import tw.zerojudge.Servlets.EditContestsServlet;
import tw.zerojudge.Servlets.EditContestantsServlet;
import tw.zerojudge.Servlets.EditVClassesServlet;
import tw.zerojudge.Servlets.RebuiltContestServlet;
import tw.zerojudge.Servlets.RebuiltVClassServlet;
import tw.zerojudge.Servlets.RemoveVClassStudentServlet;
import tw.zerojudge.Servlets.ShowCodeServlet;
import tw.zerojudge.Servlets.ShowVClassServlet;
import tw.zerojudge.Servlets.SubmissionsServlet;
import tw.zerojudge.Servlets.UpdateForumServlet;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Servlets.UpdateVClassServlet;
import tw.zerojudge.Servlets.VClassContestServlet;
import tw.zerojudge.Servlets.VerifyingProblemsServlet;
import tw.zerojudge.Servlets.Ajax.PreJudgeAjax;
import tw.zerojudge.Servlets.Utils.KickedUserServlet;
import tw.zerojudge.Servlets.Utils.NewThreadServlet;
import tw.zerojudge.Servlets.Utils.ReplyServlet;
import tw.zerojudge.Servlets.Utils.SendIMessageServlet;
import tw.zerojudge.Servlets.Utils.ShowIMessagesServlet;
import tw.zerojudge.Servlets.Utils.ShowThreadServlet;
import tw.zerojudge.Servlets.Utils.SystemMonitorServlet;
import tw.zerojudge.Servlets.Utils.UpdateUserServlet;
import tw.zerojudge.Servlets.Utils.EditUserConfigServlet;

/**
 * User.ROLE 會決定 user 的基本權限。<br/>
 * 這裡用來處理 user 進入不同的狀態時，在此設定權限的增加、或減少。<br/>
 * 
 * 1. ProblemManager<br>
 * 2. GeneralManager<br>
 * 3. VClassManager<br>
 * 4. UserInContest: User 進入 Contest 會減少的權限。<br>
 * 
 * @author jiangsir
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Privilege {
	Class<? extends HttpServlet>[] add_ProblemManager() default { CheckProblemServlet.class, EditProblemsServlet.class,
			UpdateProblemServlet.class, DeprecateProblemServlet.class, ProblemApiServlet.class, PreJudgeAjax.class,
			UpdateForumServlet.class, SubmissionsServlet.class };

//	Class<? extends HttpServlet>[]add_GeneralManager() default {VerifyingProblemsServlet.class, EditUserConfigServlet.class,
//			SystemMonitorServlet.class, InsertUsersServlet.class};

	Class<? extends HttpServlet>[] add_VClassManager() default { EditVClassesServlet.class, InsertVClassServlet.class,
			UpdateVClassServlet.class, InsertStudentsServlet.class, InsertUsersServlet.class, ShowVClassServlet.class,
			VClassContestServlet.class, RemoveVClassStudentServlet.class, DeleteVClassServlet.class,
			RebuiltVClassServlet.class, InsertVContestServlet.class };

	Class<? extends HttpServlet>[] add_ContestManager() default { InsertContestServlet.class, EditContestsServlet.class,
			RebuiltContestServlet.class, InsertContestServlet.class, KickedUserServlet.class,
			EditContestantsServlet.class };

	Class<? extends HttpServlet>[] add_InsertProblem() default { InsertProblemServlet.class };

	/**
	 * 這裡是要排除的權限。應由現有權限 remove 以下權限。
	 * 
	 * @return
	 */
	Class<? extends HttpServlet>[] remove_UserInContest() default { ShowCodeServlet.class, ShowSessionsServlet.class,
			NewThreadServlet.class, ShowThreadServlet.class, ReplyServlet.class, ShowIMessagesServlet.class,
			SendIMessageServlet.class, UpdateUserServlet.class };
}
