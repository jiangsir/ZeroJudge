package tw.zerojudge.Model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.zerojudge.DAOs.GeneralDAO;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Tables.Solution;

public class RunDiff implements Runnable {
	Solution basecode;

	ArrayList<?> comparecodes;

	public RunDiff(Solution basecode, ArrayList<?> comparecodes) {
		this.basecode = basecode;
		this.comparecodes = comparecodes;
	}

	public void run() {
		GeneralDAO db = new GeneralDAO();
		Integer basecodeid = this.basecode.getId();
		try {
			FileUtils.writeStringToFile(new File(ApplicationScope.getSystemTmp(), "code_base"),
					this.basecode.getCode());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < this.comparecodes.size(); i++) {
			HashMap<?, ?> comparecode = (HashMap<?, ?>) this.comparecodes.get(i);
			int comparecodeid = (Integer) comparecode.get("solutionid");
			if (db.executeCount("SELECT * FROM difflog WHERE basecodeid='" + basecodeid + "' AND comparecodeid='"
					+ comparecodeid + "' AND comparestatus='" + ServerOutput.JUDGEMENT.Waiting.name() + "'") == 0) { 
				continue;
			}
			try {
				FileUtils.writeStringToFile(new File(ApplicationScope.getSystemTmp(), "code_compare"),
						(String) comparecode.get("code"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String[] CMD_diff = new String[]{"/bin/sh", "-c",
					"sdiff -WBsid --strip-trailing-cr " + ApplicationScope.getSystemTmp() + "code_base "
							+ ApplicationScope.getSystemTmp() + "code_compare"};
			RunCommand diff = new RunCommand(CMD_diff, 0);
			diff.run();
			StringBuffer compareresult = new StringBuffer(50000);
			Iterator<String> it = diff.getOutStream().iterator();
			while (it.hasNext()) {
				compareresult = compareresult.append(it.next() + "\n");
			}

			String SQL = "UPDATE difflog SET compareresult='" + compareresult.toString() + "', resultlinenum="
					+ diff.getOutStream().size() + ", comparestatus='done' WHERE basecodeid=" + basecodeid
					+ " AND comparecodeid=" + comparecodeid;
			try {
				db.executeUpdate(SQL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
