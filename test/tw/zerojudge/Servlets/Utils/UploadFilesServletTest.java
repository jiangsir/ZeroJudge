package tw.zerojudge.Servlets.Utils;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Objects.Pair;

public class UploadFilesServletTest {
	public boolean checkFilepattern(String infilepattern, String outfilepattern) {
		try {
			new UploadFilesServlet().checkFilepattern(infilepattern, outfilepattern);
			return true;
		} catch (DataException e) {
			return false;
		}
	}

	@Test
	public void testCheckFilepattern() {
		ArrayList<Pair<String, String>> testcases = new ArrayList<>();
		testcases.add(Pair.create("true", "c001_#.in, c001_#.out"));
		testcases.add(Pair.create("true", "c001_##.in, c001_##.out"));
		testcases.add(Pair.create("false", "c001_####.in, c001_####.out"));
		testcases.add(Pair.create("false", "c001_01.in, c001_01.out"));
		testcases.add(Pair.create("false", "c00#_##.in, c00#_##.out"));
		testcases.add(Pair.create("true", "#.in, #.out"));
		testcases.add(Pair.create("true", "##.in, ##.out"));
		testcases.add(Pair.create("true", "##.testin, ##.ans"));
		testcases.add(Pair.create("false", "##, #")); // 沒有副檔名
		testcases.add(Pair.create("false", "#___#.in, #___#.out"));
		testcases.add(Pair.create("true", "a001_#_ok.in, a001_#_ok.out")); // # 代位不接 . 也應該可以
		testcases.add(Pair.create("false", "#.in, ##.out")); // 代位# 數目不一致
//		"true, c001_##.in, c001_##.out", //
//		"false, c001_####.in, c001_####.out", //
//		"false, c001_01.in, c001_01.out", //
//		"false, c00#_##.in, c00#_##.out", //
//		"true, #.in, #.out", //
//		"true, ##.in, ##.out", //
//		"true, ##.testin, ##.ans", //
//		"false, ##, #", //
//		"false, #___#.in, #___#.out", //

		for (Pair<String, String> testcase : testcases) {
			String[] testarray = testcase.getRight().split(",");
			System.out.println(testcase.getLeft() + " : " + testcase.getRight());
			assertEquals(Boolean.valueOf(testcase.getLeft()),
					this.checkFilepattern(testarray[0].trim(), testarray[1].trim()));
		}
		System.out.println("testcases 完成!!");

//		assertEquals(true, this.checkFilepattern(infilepattern_case1, outfilepattern_case1));
//		assertEquals(true, this.checkFilepattern(infilepattern_case2, outfilepattern_case2));
//		assertEquals(false, this.checkFilepattern(infilepattern_case4, outfilepattern_case4));
//		assertEquals(false, this.checkFilepattern(infilepattern_case9, outfilepattern_case9));
//		assertEquals(true, this.checkFilepattern(infilepattern_case6, outfilepattern_case6));
//		assertEquals(true, this.checkFilepattern(infilepattern_case7, outfilepattern_case7));
//		assertEquals(true, this.checkFilepattern(infilepattern_case8, outfilepattern_case8));
//		assertEquals(false, this.checkFilepattern(infilepattern_case3, outfilepattern_case3));
//		assertEquals(false, this.checkFilepattern(infilepattern_case5, outfilepattern_case5));
//		assertEquals(false, this.checkFilepattern(infilepattern_case10, outfilepattern_case10));
	}

	class TestdataPair {
		private String pair_in;
		private String pair_out;

		public TestdataPair(String pair_in, String pair_out) {
			this.pair_in = pair_in;
			this.pair_out = pair_out;
		}

		@Override
		public String toString() {
			return "TestdataPair [pair_in=" + pair_in + ", pair_out=" + pair_out + "]";
		}

	}

	/**
	 * 用序號填入 regex 的數字位置，並生成檔名。<br>
	 * regex: a001_##.in a001_##.out <br>
	 * localfilename: a001_00.in, a001_00.out, a001_01.in, a001_01.out<br>
	 * replacament: 00, 01, 02, ...
	 * 
	 * @param regex
	 * @param filename
	 * @param replacement
	 * @return
	 */
	public String replaceFilename(String regex, String filename, String replacement) {
		StringBuffer sb = new StringBuffer(filename);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(filename);
		if (m.find()) {
			sb.replace(m.start(1), m.end(1), replacement);
			return sb.toString();
		}
		return filename;
	}

	@Test
	public void testcheckFilelist() {
		String[] testdatas = { //
				"3, c001_#.in, c001_#.out, c001_0.in, c001_0.out, c001_1.in, c001_1.out, c001_2.in, c001_2.out",
				// 正規的檔案順序
				"0, c001_#.in, c001_#.out, c001_1.in, c001_1.out, c001_2.in, c001_2.out, c001_3.in, c001_3.out",
				// 不是從 0 開始
				"1, c001_#.in, c001_#.out, c001_0.in, c001_0.out, c001_2.in, c001_2.out, c001_3.in, c001_3.out",
				// 序號中斷, 計算到中斷前
				"3, c001_#.in, c001_#.out, c001_1.in, c001_1.out, c001_0.in, c001_2.in, c001_2.out, c001_0.out",
				// 序號完整，但順序亂掉，仍應該正確讀出來
				"0, c001_#.in, c001_#.out, a001_1.in, a001_1.out, a001_0.in, a001_2.in, a001_2.out, a001_0.out",
				// 格式不匹配
				"0, c001_#.in, c001_#.out, a001_0.in, a001_0.out, a001_1.in, a001_1.out, a001_2.in, a001_2.out",
				// 序號正確，但格式不匹配
				"0, c001_#.in, c001_#.out, c001_000.in, c001_000.out, c001_001.in, c001_001.out, c001_002.in, c001_002.out",
				// 序號格式錯誤，不支援 3 碼
				"1, c001_##.in, c001_##.out, a001_00.in, a001_00.out, b001_01.in, b001_01.out, c001_00.in, c001_00.out",
				// 檔名不一致，只應獲得匹配的
				"3, c001_##.in, c001_##.out, c001_00.in, c001_00.out, c001_01.in, c001_01.out, c001_02.in, c001_02.out",
				// 正確
				"0, c001_##.in, c001_##.out, c001_01.in, c001_01.out, c001_02.in, c001_02.out, c001_03.in, c001_03.out",
				// 序號沒有從 0 開始
				"2, c001_##.in, c001_##.out, c001_00.in, c001_00.out, c001_01.in, c001_01.out, c001_03.in, c001_03.out",
				// 序號跳號

		};
		String[] infilepattern_cases = { "c001_#.in", "c001_##.in" }; // 標準 regex
		String[] outfilepattern_cases = { "c001_#.out", "c001_##.out" }; // 標準 regex

		UploadFilesServlet servlet = new UploadFilesServlet();
		for (String testdata : testdatas) {
			String[] testarray = testdata.split("\\s*,\\s*");
			List<String> list = new ArrayList<>(Arrays.asList(testarray));
			String expect = list.remove(0);
			String infilepattern = list.remove(0);
			String outfilepattern = list.remove(0);
			Set<String> localfileSet = new TreeSet<>(list);
			Pair<ArrayList<Pair<String, String>>, Set<String>> returnPair = servlet
					.matchedLocalfilePairs(infilepattern.trim(), outfilepattern.trim(),
							localfileSet);
			assertEquals(Integer.valueOf(expect.trim()).intValue(), returnPair.getLeft().size());
		}

	}

	public static void main(String[] argv) {
		new UploadFilesServletTest().testcheckFilelist();
	}
}
