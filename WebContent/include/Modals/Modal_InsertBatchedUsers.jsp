<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_InsertBatchedUsers" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">批次新增使用者</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div>
								<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
								<ul>批次新增使用者注意事項:
									<li>每一欄位以 tab 隔開，使用 TSV 格式。可直接從試算表環境剪貼進來。</li>
									<li>不指定學校時，學校id = 0。</li>
									<li>每行第一個字元為 # 者視為註解略過。</li>
								</ul>
									<div class="well well-sm"><pre class="pre-scrollable">#帳號	學校id	匿稱	真實姓名	密碼明碼
Tommy01	0	Tommy	王湯米1	tommy1234
Tommy02	0	Tommy	王湯米2	tommy1234</pre></div>
									<textarea rows="20" class="form-control" name="userscripts"
										placeholder="#帳號	學校id	匿稱	真實姓名	密碼明碼
Tommy01	0	Tommy	王湯米1	tommy1234
Tommy02	0	Tommy	王湯米2	tommy1234"></textarea>
								</c:if>
								<!-- <c:if test="${applicationScope.appConfig.isCLASS_MODE}">
								請先建立學校，再取得 schoolid，若不指定學校，則 學校id = 0<br />
									<h3>
										<a href="./EditSchools">前往「建立學校」</a>
									</h3>
									<br />
									<samp>#學號,學校id,公開名稱,真實姓名,密碼明碼,生日,E-mail</samp>
									<br /> 例：<br />
									<samp>
										1081101,1,01王阿明,王小明,password01,1990,1081101@school.edu.tw<br>
										1081102,1,02黃阿牛,黃大牛,password02,1990,1081101@school.edu.tw
									</samp>
									<p class="bg-warning">
										請注意！若[學號,學校id] 一樣，則會<strong>更新</strong>此身分的資料！
									</p>
									<textarea id="tcontent" rows="20" class="form-control"
										name="userscripts"
										placeholder="1081101,1,01王阿明,王小明,password01,1990,1081101@school.edu.tw"></textarea>
								</c:if> -->
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="modal_InsertBatchedUsers_change"
					data-vclassid="${param.vclassid}">批次新增/更新使用者</button>
			</div>
		</div>
	</div>
</div>
