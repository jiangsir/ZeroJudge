package tw.zerojudge.JsonObjects;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.ProblemFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;

public class ServerOutputBean extends ServerOutput {
	/**
	 * 20130326<br>
	 * JsonDetail 透過 SessionUser 來重新整理 ServerOutput 所要呈現出來的內容。<br/>
	 * 1. ServerOutput 裡的 reason 一律用 Resource key 呈現，如
	 * Server.REASON.SYSTEMERROR, JsonDetail 裡則轉為多國語言版本<br>
	 * 2.如果題目有設定為測資封鎖，在這裡就必須要過濾掉。這件事不應該由 ServerOutput 本身來作。<br>
	 */
	private String reasontext = "";
	private String hint = "";
	@JsonIgnore
	private Problem problem = ProblemFactory.getNullproblem();
	@JsonIgnore
	HashMap<String, Field> fields = new HashMap<String, Field>();
	private String summary = null;

	public ServerOutputBean(ServerOutput serverOutput) {
		if (serverOutput == null) {
			return;
		}
		this.problem = new ProblemService().getProblemByProblemid(new Problemid(serverOutput.getProblemid()));
		Object value;
		for (Field field : ServerOutput.class.getDeclaredFields()) {
			Method getter;
			try {
				if (field.getAnnotation(JsonIgnore.class) != null) {
					continue;
				}
				getter = ServerOutput.class.getMethod(
						"get" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1));
				value = getter.invoke(serverOutput);
				if (value == null) {
					continue;
				}
				Method setter = ServerOutput.class.getMethod(
						"set" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1),
						new Class[] { value.getClass() });
				setter.invoke(this, new Object[] { value });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	//

	public String getReasontext() {
		return this.reasontext;
	}

	/**
	 * 從 ServerOutput[] 裡面去分析出要呈現出來的統計資訊。如<br>
	 * AC -> 1ms, 1MB<br>
	 * TLE -> 1.5s<br>
	 * MLE -> 128MB<br>
	 * WA -> line:6<br>
	 * RE -> SIGKILL<br>
	 * 
	 * @return
	 */
	public String getSummary() {
		if (summary != null) {
			return summary;
		}
		switch (this.getJudgement()) {
		case AC:
			summary = Solution.parseTimeusage(getTimeusage()) + ", " + Solution.parseMemoryusage(getMemoryusage());
			break;
		case TLE:
			summary = Solution.parseTimeusage(this.getTimeusage());
			break;
		case MLE:
			summary = Solution.parseMemoryusage(this.getMemoryusage());
			break;
		case RE:
			summary = getInfo();
			break;
		case WA:
			summary = getInfo();
			break;
		case Waiting:
			return "";
		default:
			return "";
		}
		return summary;
	}

	//

	@Override
	public void setHint(String hint) {
		this.hint = hint;
	}

	@Override
	public String getHint() {
		if (problem.getWa_visible() == Problem.WA_visible_HIDE && getJudgement() != ServerOutput.JUDGEMENT.CE) {
			return "";
		}
		return this.hint;
	}
}
