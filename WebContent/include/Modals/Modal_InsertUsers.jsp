<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_InsertUsers" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">新增一群使用者</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div>
								<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
								請依據以下格式指定要新增的使用者。<br />
								不指定學校時，學校id = 0
									<br />
									<samp>#帳號,學校id,匿稱,真實姓名,密碼明碼,生日,E-mail</samp>
									<br /> 例：<br />
									<samp>
										Tommy091,0,Tommy,王湯米1,tommy1234,1990,tommy091@gmail.com<br>
										Tommy092,0,Tommy,王湯米2,tommy1234,1990,tommy092@gmail.com
									</samp>
									<textarea id="tcontent" rows="20" class="form-control"
										name="userscripts"
										placeholder="Tommy091,0,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com"></textarea>
								</c:if>
								<c:if test="${applicationScope.appConfig.isCLASS_MODE}">
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
								</c:if>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_insertUsers_change" data-vclassid="${param.vclassid}">新增/更新使用者</button>
			</div>
		</div>
	</div>
</div>
