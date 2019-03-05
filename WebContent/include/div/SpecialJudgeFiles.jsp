<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<div id="SpecialJudgeFiles" style="text-align: left;">
	<c:choose>
		<c:when test="${fn:length(problem.specialJudgeFiles)>0}">
			<div style="display: block; clear: both;">
				<c:forEach var="specialJudgeFile"
					items="${problem.specialJudgeFiles}">
					<div id="SpecialJudgeFile"
						style="font-weight: bold; font-size: large">${specialJudgeFile.name}
						<span style="font-weight: normal; font-size: small;">:
							${specialJudgeFile.length} Bytes (<fmt:formatDate
								value="${specialJudgeFile.lastModified }"
								pattern="yyyy-MM-dd HH:mm:ss" />)
						</span>
						<button type="button" class="btn btn-default btn-sm">
							<span class="glyphicon glyphicon-remove" id="deleteSpecialFile"
								title="刪除檔案"></span>
						</button>
					</div>

				</c:forEach>
			</div>
		</c:when>
		<c:otherwise>
			<h4>目前本題目沒有 Special Judge 檔案。</h4>
		</c:otherwise>
	</c:choose>
</div>
