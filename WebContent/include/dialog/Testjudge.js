jQuery(document).ready(function() {
	jQuery("input[name='language']").each(function() {
		// alert($(this).attr("userlanguage") + ", " + $(this).val());
		if ($(this).attr("userlanguage") == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("#submit").click(function() {
		$("#form1").submit();
	});

	jQuery("button#Testjudge").click(function() {
		$dialog = $("#Testjudge_dialog").dialog({
			disabled : true,
			autoOpen : false,
			width : '60%',
			title : '請貼上您要測試的程式碼以及測試資料',
			close : function(event, ui) {
				$(this).dialog("destroy");
			},
			buttons : {
				"測試執行" : function() {
					// $(this).dialog("close");
					// IndataDialog();
					doTestjudge($dialog);
				},
				"結束" : function() {
					$(this).dialog("close");
				}
			}
		});
		$dialog.dialog('open');
		return true;
	});
	jQuery("span[id='judgement']:contains('Waiting')").each(function() {
		var solutionid = $(this).attr("solutionid");
		doRejudge(solutionid);
	});

});

function doTestjudge(testjudge_dialog) {
	var showdetail_dialog = testjudge_dialog.find("div.showdetail_dialog");
	var it;

	var $waiting = showdetail_dialog.dialog({
		autoOpen : false,
		width : '60%',
		title : 'Server Outputs',
		close : function() {
			jQuery.clear(it);
			$(this).dialog("destroy"); // dialog("close")
		},
		buttons : {
			"返回" : function() {
				jQuery.clear(it);
				$(this).dialog("destroy"); // dialog("close")
				// 會把這個 dialog
				// 清除。通常要保留應使用 destroy
			}
		}
	});

	jQuery.ajax({
		type : "POST",
		url : "./Testjudge",
		data : $('#form_Testjudge').serialize(),
		async : true,
		timeout : 5000,
		beforeSend : function() {
			$waiting.find("#spinner").show();
			$waiting.find("#serverOutputs").html("");
			$waiting.dialog('open');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(jqXHR.responseText);
			if (jqXHR.responseText !== '') {
				// showError(jqXHR.responseText);
				$waiting.find("#spinner").hide();
				$waiting.find("#serverOutputs").html(jqXHR.responseText);
				$waiting.dialog('open');
			} else {
				// showError(errorThrown);
			}
		},
		success : function(serverOutputs) {
			var serveroutputs = JSON.parse(serverOutputs);
			var solutionid = serveroutputs[0].solutionid;
			// alert(solutionid);
			// if (solutionid != 0) {
			it = jQuery.interval(function() {
				jQuery.ajax({
					type : "POST",
					url : "./Solution.api",
					data : "action=getSummary&solutionid=" + solutionid,
					async : false,
					cache : false,
					timeout : 5000,
					error : function(jqXHR, textStatus, errorThrown) {
					},
					success : function(result) {
						var summary = JSON.parse(result);
						// alert(summary.judgement);
						if (summary.judgement == "Waiting") {
						} else {
							$waiting.dialog("destroy");
							jQuery.clear(it);
							showSolutionDetail(showdetail_dialog, solutionid);
							return;
						}
					}
				}); // jQuery ajax
			}, 1000);

		}
	});
	return false;
}

var jsondetails_string;

// function getStatusinfo(solutionid) {
// var it;
// it = jQuery
// .interval(
// function() {
// jQuery
// .ajax({
// type : "POST",
// url : "./Solution.api",
// data : "action=getSummary&solutionid="
// + solutionid,
// async : false,
// // cache: false,
// timeout : 5000,
// success : function(result) {
// var summary = JSON.parse(result);
// if (summary.judgement == "Waiting") {
// } else {
// jQuery.clear(it);
// jQuery(
// "#showdetail_dialog #spinner")
// .hide();
// getServerOutputBeans(solutionid);
// // alert(jsondetails_string);
// while (jQuery("div[id=jsondetail]").length > 1) {
// jQuery(
// "div[id=jsondetail]:last")
// .remove();
// }
// // alert(result);
// var jsondetails = JSON
// .parse(jsondetails_string);
// if (jsondetails.length == 1) {
// jQuery(
// "div[id=jsondetail]:last span[id=line1]")
// .hide();
// } else {
// jQuery(
// "div[id=jsondetail]:last span[id=line1]")
// .show();
// }
//
// for ( var i = 0; i < jsondetails.length
// && jsondetails[i] != null; i++) {
// if (jQuery("div[id=jsondetail]").length < i + 1) {
// jQuery(
// "div[id=jsondetail]:last")
// .clone(true)
// .insertAfter(
// jQuery("div[id=jsondetail]:last"));
// }
// jQuery(
// "div[id=jsondetail]:last")
// .show();
// jQuery(
// "div[id=jsondetail]:last span[id=testlength]")
// .text(i + 1);
// jQuery(
// "div[id=jsondetail]:last span[id=score]")
// .text(
// jsondetails[i].score
// + "%");
//
// if (jsondetails[i].judgement == "AC") {
// jQuery(
// "div[id=jsondetail]:last span[id=judgement]")
// .addClass("acstyle")
// .text(
// jsondetails[i].judgement);
// } else {
// jQuery(
// "div[id=jsondetail]:last span[id=judgement]")
// .removeClass(
// "acstyle")
// .text(
// jsondetails[i].judgement);
// }
// jQuery(
// "div[id=jsondetail]:last span[id=info]")
// .text(
// jsondetails[i].summary);
// jQuery(
// "div[id=jsondetail]:last span[id=reason]")
// .text(
// jsondetails[i].reasontext);
// jQuery(
// "div[id=jsondetail]:last pre[id=hint]")
// .text(
// jsondetails[i].hint);
// jQuery("div[id=jsondetail]")
// .show();
// }
// return;
// }
// }
// }); // jQuery ajax
// }, 1000);
// }

function getServerOutputBeans(solutionid) {
	jQuery.ajax({
		type : "POST",
		url : "./Solution.api",
		data : "action=getServerOutputBeans&solutionid=" + solutionid,
		async : false,
		// cache: false,
		timeout : 5000,
		success : function(result) {
			// alert(result);
			jsondetails_string = result;
			return;
		}
	});

}
