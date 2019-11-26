// JavaScript Document
jQuery(document).ready(function() {

	jQuery("a[id='ExportProblems']").click(function(event) {
		var problems = "";
		$("input[name='problems']:checked").each(function() {
			if (problems == "") {
				problems += $(this).val();
			} else {
				problems += ", " + $(this).val();
			}
		});

		var exportProblems_dialog = $("div.ExportProblems_dialog");
		exportProblems_dialog.find("#problems").html(problems);
		var $dialog = exportProblems_dialog.dialog({
			autoOpen : false,
			width : '60%',
			title : '匯出題目',
			buttons : {
				"匯出" : function() {
					exportProblems(problems);
					$(this).dialog("destroy");
				},
				"返回" : function() {
					$(this).dialog("destroy");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});

	jQuery("span[id='clean_keyword']").click(function() {
		jQuery("input[name='keyword']").val("");
	});

	jQuery("span[id='keyword']").click(function() {
		var index = jQuery("span[id='keyword']").index(this);
		var keyword = jQuery("span[id='keyword']:eq(" + index + ")").text();
		jQuery("input[name='keyword']").val(keyword);
	});

	jQuery("input[id='selectAll']").on('click', function() {
		if ($(this).is(':checked')) {
			$("input[name='problems']").each(function() {
				$(this).prop("checked", true);
			});
		} else {
			$("input[name='problems']").each(function() {
				$(this).prop("checked", false);
			});
		}
	});

	jQuery("span[id='waitingRejudgeSize']").each(function() {
		var size = $(this).text();
		if (size != 0) {
			var problemid = $(this).attr("problemid");
			jQuery("tr[problemid='" + problemid + "'] span[id='rejudge_waiting']").show();
			jQuery("tr[problemid='" + problemid + "'] span[id='img_rejudge']").hide();
			getWaitingRejudgeSize(problemid);
		}
	});

	$("select#tab").children().each(function() {
		if ($(this).parent().attr("tabid") == $(this).val()) {
			$(this).attr("selected", "true");
			return;
		}
	});

	$("select#tab").change(function() { // 事件發生
		var problemid = $(this).attr("problemid");
		jQuery('select#tab option:selected').each(function() { // 印出選到多個值
			if (problemid == $(this).parent().attr("problemid")) {
				setTabid(problemid, $(this).val());
			}
		});
		location.reload();
	});

});

function getProblemVerify(problemid) {
	var text;
	jQuery.ajax({
		type : "GET",
		url : "./Problem.api",
		data : "action=getVerify&problemid=" + problemid,
		async : false,
		timeout : 5000,
		success : function(result) {
			text = result;
		}
	});
	// alert("getProblemVerify problemid=" + problemid + " text=" + text);
	return text;
}

function setTabid(problemid, tabid) {
	jQuery.ajax({
		type : "GET",
		url : "./Problem.api",
		data : "action=setTabid&problemid=" + problemid + "&tabid=" + tabid,
		async : false,
		timeout : 5000,
		success : function(result) {
		}
	});
}

function setDifficulty(problemid, difficulty) {
	jQuery.ajax({
		type : "GET",
		url : "./Problem.api",
		data : "action=setDifficulty&problemid=" + problemid + "&difficulty=" + difficulty,
		async : false,
		timeout : 5000,
		success : function(result) {
		}
	});
}

function openProblem(problemid) {
	jQuery.ajax({
		type : "GET",
		url : "./Problem.api",
		data : "action=openProblem&problemid=" + problemid,
		// async: false,
		timeout : 5000,
		success : function(result) {
			// text = result;
			// jQuery("span[id='htmlVerify']:eq("+index+")").html(getProblemVerify(problemid));
		}
	});

	// alert("getProblemVerify problemid=" + problemid + " text=" + text);
	// return text;
}

function exportProblems(problemids) {
	window.location = "Problem.api?action=exportProblems&problemids=" + problemids;
	// jQuery.ajax({
	// type : "GET",
	// url : "./Problem.api",
	// data : "action=exportProblems&problemids=" + problemids,
	// // async: false,
	// timeout : 5000,
	// success : function(result) {
	// }
	// });
}

function verifyProblem(problemid) {
	jQuery.ajax({
		type : "GET",
		url : "./Problem.api",
		data : "action=verifyProblem&problemid=" + problemid,
		// async: false,
		timeout : 5000,
		success : function(result) {
			// text = result;
			// jQuery("span[id='htmlVerify']:eq("+index+")").html(getProblemVerify(problemid));
		}
	});

	// alert("getProblemVerify problemid=" + problemid + " text=" + text);
}

//function getWaitingRejudgeSize(problemid, rejudgesize) {
//	var it;
//	it = jQuery.interval(function() {
//		var waitingRejudgeSize = $("tr[problemid='" + problemid + "'] span[id='waitingRejudgeSize']");
//		var rejudge_waiting = $("tr[problemid='" + problemid + "'] span[id='rejudge_waiting']");
//		var img_rejudge = $("tr[problemid='" + problemid + "'] span[id='img_rejudge']");
//		jQuery.ajax({
//			type : "GET",
//			url : "Problem.api",
//			data : "action=getWaitingRejudgeSize&problemid=" + problemid,
//			// async: false,
//			cache : false,
//			timeout : 5000,
//			success : function(waitingsize) {
//				// alert("waitingsize result="+result);
//				if (waitingsize != 0 || rejudgesize != 0) {
//					// 因為 thread 關係 waitingsize 會延遲一段時間才會抓到正確的 size.
//					// 所以引入 rejudgesize 協助判斷
//					if (waitingsize != 0) {
//						rejudgesize = 0; // 為了最後 waitingsize, rejudgesize 都
//						// =0 代表全部 rejudge 完畢。
//					}
//					waitingRejudgeSize.html(" (Queue: " + waitingsize + ")").show();
//				} else {
//					// alert("==0");
//					jQuery.clear(it);
//					waitingRejudgeSize.hide();
//					rejudge_waiting.hide();
//					img_rejudge.show();
//					return;
//				}
//			}
//		}); // jQuery ajax
//	}, 1000);
//}
