package tw.jiangsir.Utils.TLDs;

import java.util.TreeSet;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Article;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Article.TYPE;

public class ArticleTLD {
	/**
	 * 取得這個 onlineUser 有權可以修改的 文章 HIDDEN 權限
	 * 
	 * @return
	 */
	public static TreeSet<Article.HIDDEN> getAccessibleHiddens(OnlineUser onlineUser, Article article) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		TreeSet<Article.HIDDEN> hiddens = new TreeSet<Article.HIDDEN>();
		hiddens.add(article.getHidden());

		if (onlineUser.getIsDEBUGGER() || onlineUser.getIsMANAGER()) {
			for (Article.HIDDEN hidden : Article.HIDDEN.values()) {
				hiddens.add(hidden);
			}
		}

//		if (onlineUser.isGeneralManager()) {
//			for (Article.HIDDEN hidden : Article.HIDDEN.values()) {
//				if (hidden.isHigherEqualThan(Article.HIDDEN.hide)) {
//					hiddens.add(hidden);
//				}
//			}
//		}
		return hiddens;
	}

	/**
	 * 取得這個 onlineUser 有權可以修改的 文章 Type
	 * 
	 * @return
	 */
	public static TreeSet<Article.TYPE> getAccessibleTypes(OnlineUser onlineUser, Article article) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (article == null) {
			article = new Article();
		}

		TreeSet<Article.TYPE> types = new TreeSet<Article.TYPE>();
		types.add(article.getArticletype());

		if (onlineUser.getIsDEBUGGER() || onlineUser.getIsMANAGER()) {
			for (Article.TYPE type : Article.TYPE.values()) {
				types.add(type);
			}
		}

//		if (onlineUser.isGeneralManager()) {
//			for (Article.TYPE type : Article.TYPE.values()) {
//				if (type.isHigherThan(TYPE.problemreport)) {
//					
//					
//					types.add(type);
//				}
//			}
//		}
		if (article.getProblem().getIsOwner(onlineUser)) {
			types.add(Article.TYPE.problemreport);
		}

		return types;

	}

	//
	//
	//

}
