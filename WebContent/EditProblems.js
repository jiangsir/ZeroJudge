// JavaScript Document
jQuery(document).ready(function() {

	// jQuery("span[id='prejudgement']").each(function(){
	// $(this).addClass("FakeLink");
	// if ($(this).text() == "AC") {
	// $(this).addClass("acstyle");
	// }
	// });
	// jQuery("tr").click(function() {
	// var checkbox = $(this).find("input[name='problems']");
	// if (checkbox.is(':checked')) {
	// checkbox.prop("checked", false);
	// } else {
	// checkbox.prop("checked", true);
	// }
	// });
	// console.log("2222");
	// var hash = window.location.hash;
	// console.log("hash=" + hash);
	// var search = window.location.search;
	// console.log("search=" + search);
	// hash && $('ul.nav a[href="' + hash + '"]').tab('show');
	// console.log("4444");
	// $('.nav-tabs a').click(function(e) {
	// console.log("search=" + search);
	// console.log("5555");
	// $(this).tab('show');
	// console.log("6666");
	// var scrollmem = $('body').scrollTop() || $('html').scrollTop();
	// console.log("7777");
	// window.location.hash = this.hash;
	// $('html,body').scrollTop(scrollmem);
	// });

	var url = document.location.toString();
	console.log("url=" + url);
	if (url.match('#')) {
		console.log("url.split('#')[1]=" + url.split('#')[1]);
		console.log('.nav-tabs a[aria-controls="' + url.split('#')[1] + '"]');
		// $('.nav-tabs').find('[aria-controls="' + url.split('#')[1] +
		// '"]').tab('show');
		$('.nav-tabs a[aria-controls="' + url.split('#')[1] + '"]').tab('show');
	}
	// window.scrollTo(0, 0);
	// Change hash for page-reload
	// $('.nav-tabs a').on('shown.bs.tab', function(e) {
	// window.location.hash = e.target.hash;
	// })

	jQuery("a[id='ExportProblems']").click(function(event) {
		// var problems = $("input[name='problems']:checked").toArray();
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

	// jQuery("span[id='backgrounds']").click(function() {
	// var index = jQuery("span[id='backgrounds']").index(this);
	// var background = jQuery("span[id='backgrounds']:eq(" + index +
	// ")").text();
	// var backgrounds = jQuery("input[name='backgrounds']").val();
	// if (backgrounds == '') {
	// backgrounds += background;
	// // jQuery("input[name='background']").val(background);
	// } else {
	// backgrounds += "," + background;
	// }
	// jQuery("input[name='backgrounds']").val(backgrounds);
	// });

//	jQuery("span[id='author']").each(function() {
//		if (jQuery("#session_account").text() == $(this).text()) {
//			$(this).css({
//				'font-weight' : "bold",
//				'color' : "#666666"
//			});
//		}
//	});

//	$('#jtabs').tabs({
//		load : function(event, ui) {
//			$('a', ui.panel).click(function() {
//				$(ui.panel).load(this.href);
//				return false;
//			});
//		}
//	});

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

	/*
	 * $("select#difficulty").children().each(function(){ if
	 * ($(this).parent().attr("difficulty") == $(this).val()) {
	 * $(this).attr("selected", "true"); return; } });
	 * 
	 * $("select#difficulty").change(function(){ //事件發生 var problemid =
	 * $(this).attr("problemid"); jQuery('select#difficulty
	 * option:selected').each(function(){ //印出選到多個值 if (problemid ==
	 * $(this).parent().attr("problemid")) { setDifficulty(problemid,
	 * $(this).val()); } }); location.reload(); });
	 */
	/*
	 * jQuery("span[id='htmlVerify']").bind('click', function(){ index =
	 * jQuery("span[id='htmlVerify']").index(this); problemid =
	 * $(this).attr("problemid"); var waiting = "<span>waiting...</span>"; if
	 * ($(this).children('span').attr("id") == "verifying") {
	 * openProblem(problemid); jQuery("span[id='htmlVerify']:eq(" + index +
	 * ")").html(getProblemVerify(problemid)); } else if
	 * ($(this).children('span').attr("id") == "open") {
	 * verifyProblem(problemid); jQuery("span[id='htmlVerify']:eq(" + index +
	 * ")").html(getProblemVerify(problemid)); } });
	 */

	// jQuery("span[id='prejudgement']").click(function(){
	// //var index = jQuery("span[id='PrejudgeResult']").index(this);
	// //var problemid = $(this).parent().attr("alt");
	// var problemid = $(this).attr("problemid");
	// //alert(problemid);
	// jQuery.ajax({
	// type: "GET",
	// url: "./Problem.api",
	// data: "action=getJsondetails&problemid=" + problemid,
	// async: false,
	// timeout: 5000,
	// beforeSend: function(){
	// //jQuery("td[problemid='" + problemid + "']
	// span[id='ajaxwaiting']").show();
	// //jQuery("td[problemid='" + problemid + "']
	// span[id='prejudgement']").hide();
	// //jQuery("td[problemid='" + problemid + "']
	// span[id='prejudgeinfo']").hide();
	// },
	// success: function(result){
	// //jQuery("#detail").val(result);
	// while (jQuery("div[id=jsondetail]").length > 1) {
	// jQuery("div[id=jsondetail]:last").remove();
	// }
	// ShowServerOutputBeans(result);
	// }
	// });
	// var $dialog = $("#showdetail_dialog").dialog({
	// autoOpen: false,
	// width: '60%',
	// title: 'Detail',
	// buttons: {
	// "返回": function(){
	// $(this).dialog("close");
	// }
	// }
	// });
	//        
	// $dialog.dialog('open');
	// return false;
	// });
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
