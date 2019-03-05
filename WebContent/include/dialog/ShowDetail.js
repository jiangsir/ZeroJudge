function showSolutionDetail(showdetail_dialog, solutionid) {
	jQuery.ajax({
		type : "POST",
		url : "./Solution.api",
		// data : "action=DetailDialog&solutionid=" + solutionid,
		data : "action=getServerOutputs&solutionid=" + solutionid,
		async : false,
		timeout : 5000,
		error : function() {

		},
		success : function(serverOutputs) {

			// //
			// alert($("div.statusinfo[solutionid='"+solutionid+"']").html());
			// var showdetail_dialog = $(
			// "div.statusinfo[solutionid='" + solutionid + "']").append(
			// result);
			// // alert(showdetail_dialog.html());
			// alert(serverOutputs);
			// var showdetail_dialog = statusinfo.find("div.showdetail_dialog");
			showdetail_dialog.find("#serverOutputs").html(serverOutputs);

			showdetail_dialog.find("#spinner").hide();
			var $dialog = showdetail_dialog.dialog({
				autoOpen : false,
				width : '60%',
				title : 'Server Outputs(' + solutionid + ')',
				close : function() {
					$(this).dialog("destroy"); // dialog("close")
				},
				buttons : {
					"返回" : function() {
						$(this).dialog("destroy"); // dialog("close")
						// 會把這個 dialog
						// 清除。通常要保留應使用 destroy
					}
				}
			});
			$dialog.dialog('open');
			return false;
		}
	});
}

function showProblemDetail(showdetail_dialog, problemid) {

	jQuery.ajax({
		type : "POST",
		url : "Problem.api",
		// data : "action=DetailDialog&problemid=" + problemid,
		data : "action=getServerOutputs&problemid=" + problemid,
		async : false,
		timeout : 5000,
		error : function() {

		},
		success : function(serverOutputs) {
			showdetail_dialog.find("#serverOutputs").html(serverOutputs);
			showdetail_dialog.find("#spinner").hide();
			var $dialog = showdetail_dialog.dialog({
				autoOpen : false,
				width : '60%',
				title : 'Server Outputs(' + problemid + ')',
				close : function() {
					$(this).dialog("destroy"); // dialog("close")
				},
				buttons : {
					"返回" : function() {
						$(this).dialog("destroy"); // dialog("close")
						// 會把這個 dialog
						// 清除。通常要保留應使用 destroy
					}
				}
			});
			$dialog.dialog('open');
			return false;
		}
	});

}