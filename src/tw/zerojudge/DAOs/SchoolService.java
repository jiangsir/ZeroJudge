package tw.zerojudge.DAOs;

import java.net.URL;
import java.util.ArrayList;
import java.util.TreeSet;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Factories.SchoolFactory;
import tw.zerojudge.Tables.School;

public class SchoolService {

	/**
	 * 空白代表 ASC
	 * 
	 * @param orderby
	 * @return
	 * @throws DataException
	 */
	public ArrayList<School> getSchoolsByOrder(String orderby) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("checkid=" + School.CHECKID_CHECKED);
		return new SchoolDAO().getSchoolsByRules(rules, orderby, 0);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<School> getSchoolsByPage(int page) {
		return new SchoolDAO().getSchoolsByRules(new TreeSet<String>(), "id DESC", page);
	}

	public ArrayList<School> getSchoolsByKeyword(String keyword) {
		if (keyword == null || "".equals(keyword.trim())) {
			return new ArrayList<School>();
		}
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("schoolname LIKE '%" + keyword.trim() + "%'");
		return new SchoolDAO().getSchoolsByRules(rules, "id DESC", 0);
	}

	public ArrayList<School> getUncheckedSchools(String orderby) throws DataException {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("checkid=" + School.CHECKID_NONECHECK);

		return new SchoolDAO().getSchoolsByRules(rules, orderby, 0);
	}

	public School getSchoolById(Integer schoolid) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("id=" + schoolid);
		for (School school : new SchoolDAO().getSchoolsByRules(rules, "", 0)) {
			return school;
		}
		return SchoolFactory.getNullSchool();
	}

	public School getSchoolByURL(URL url) throws DataException {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("url='" + url + "'");

		for (School school : new SchoolDAO().getSchoolsByRules(rules, "", 0)) {
			return school;
		}
		return SchoolFactory.getNullSchool();
	}

	public School getSchoolByName(String name) {
		return new SchoolDAO().getSchoolByName(name);
	}


	//

	/**
	 * 取得 指定的 schoolid 以外，URL 相同的學校。
	 * 
	 * @param url
	 * @param schoolid
	 * @return
	 * @throws DataException
	 */
	public School getOtherSchoolByUrl(URL url, Integer schoolid) throws DataException {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("url='" + url.toString() + "'");
		rules.add("id!=" + schoolid);

		for (School school : new SchoolDAO().getSchoolsByRules(rules, "", 0)) {
			return school;
		}
		return SchoolFactory.getNullSchool();
	}

	/**
	 * 取得指定的 schoolid以外的，且 School Name 相同的學校。
	 * 
	 * @param schoolname
	 * @param schoolid
	 * @return
	 */
	public School getOtherSchoolByName(String schoolname, Integer schoolid) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("schoolname='" + schoolname + "'");
		rules.add("id!=" + schoolid);
		for (School school : new SchoolDAO().getSchoolsByRules(rules, "", 0)) {
			return school;
		}
		return SchoolFactory.getNullSchool();
	}

	public ArrayList<School> getAllSchools() {
		return new SchoolDAO().getSchoolsByRules(new TreeSet<String>(), "id DESC", 0);
	}

	//

	/**
	 * 刪除學校，並更改使用者的 schoolid 設定。
	 * 
	 * @param schoolid
	 */
	public void deleteSchool(int schoolid) {
		new UserService().doCleanSchoolid(schoolid);
		new SchoolDAO().delete(schoolid);
	}

	/**
	 * 將兩個學校合併起來。<br>
	 * 更改 users 資料並刪除舊學校資料。
	 * 
	 * @param fromschoolid
	 *            aeeeeee4eceex¡ * @param to
	 */
	public void mergeSchool(int fromschoolid, School to) {
		new UserService().doMergeSchool(fromschoolid, to);
		this.deleteSchool(fromschoolid);
	}

	public void update(School school) {
		new SchoolDAO().update(school);
	}

	public int insert(School school) {
		return new SchoolDAO().insert(school);
	}
}
