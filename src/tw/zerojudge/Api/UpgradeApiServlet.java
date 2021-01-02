package tw.zerojudge.Api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;

@WebServlet(urlPatterns = { "/Upgrade.api" })
@RoleSetting
public class UpgradeApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6643345167116071255L;


	public static enum ACTION {
		upgradeSolutions(""), upgradeTLE("");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
