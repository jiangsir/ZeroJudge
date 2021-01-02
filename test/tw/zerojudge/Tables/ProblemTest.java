package tw.zerojudge.Tables;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Objects.Pair;

public class ProblemTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testCheckTimelimits() {
		expectedEx.expect(DataException.class);
		expectedEx.expectMessage("測資時間設定必須為浮點數");

		new Problem().setTimelimits("1, 1, a".split(","));
	}

	@Test
	public void testCheckTimelimits_null() {
		expectedEx.expect(DataException.class);
		expectedEx.expectMessage("timelimits 參數不存在!!");

		new Problem().setTimelimits(new String[] {});
	}

	@Test
	public void testCheckTimelimits_case1() {
		expectedEx.expect(DataException.class);
		expectedEx.expectMessage("最低時間限制為 "
				+ Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MIN_TIMELIMIT) + "秒!");
		new Problem().setTimelimits("0.05, 0.1, 1".split(","));
	}

	@Test
	public void testCheckTimelimits_case2() {
		expectedEx.expect(DataException.class);
		expectedEx.expectMessage("最低時間限制為 "
				+ Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MIN_TIMELIMIT) + "秒!");
		new Problem().setTimelimits("0.05".split(","));
	}

	@Test
	public void testCheckTimelimits_case3() {
		expectedEx.expect(DataException.class);
		expectedEx.expectMessage("測資時間設定必須為浮點數");
		new Problem().setTimelimits("xs".split(","));
	}

	@Test
	public void testCheckTimelimits_caseOK() {
		new Problem().setTimelimits("0.1, 0.1, 1".split(","));
	}
}
