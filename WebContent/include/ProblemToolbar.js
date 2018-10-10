jQuery(document).ready(function() {

	jQuery("button[id='rejudgeProblem']").click(function() {
		var modal = $('#Modal_RejudgeProblem');
		var problemid = $(this).data("problemid");
		console.log("problemid=" + problemid);
		$.getJSON("./Problem.json?data=SubmissionCount&problemid=" + problemid, function(data) {
			console.log("data=" + data);
			modal.find("#SubmissionCount").html(data);
		});
		modal.data("problemid", problemid);
		modal.modal("toggle");
	});

	$('button#modal_RejudgeProblem_confirm').on('click', function() {
		var modal = $(this).closest('.modal');
		var problemid = modal.data("problemid");
		console.log("problemid=" + problemid);
		doRejudge(problemid);
		modal.modal('hide');
	});

	jQuery("button#recountAcceptedProblem").click(function() {
		var problemid = $(this).data("problemid");
		console.log("problemid=" + problemid);
		// href="./Problem.api?action=recountAcceptedProblem&problemid=${problem.problemid}"></a>
		jQuery.ajax({
			type : "GET",
			url : "./Problem.api",
			data : "action=recountAcceptedProblem&problemid=" + problemid,
			timeout : 5000,
			success : function(result) {
				BootstrapDialog.alert("重新計算完畢！");
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				console.log("errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					BootstrapDialog.show({
						title : alert.type,
						message : alert.title,
						buttons : [ {
							id : 'btn-ok',
							icon : 'glyphicon glyphicon-check',
							label : 'OK',
							cssClass : 'btn-primary',
							autospin : false,
							action : function(dialogRef) {
								dialogRef.close();
							}
						} ]
					});
				} catch (err) {
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});

	jQuery("button#rsyncTestfiles").click(function() {
		var problemid = $(this).data("problemid");
		console.log("problemid=" + problemid);
		jQuery.ajax({
			type : "GET",
			url : "./Problem.api",
			data : "action=rsyncTestdatasByProblem&problemid=" + problemid,
			timeout : 5000,
			success : function(result) {
				BootstrapDialog.alert("同步成功");
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				console.log("errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					BootstrapDialog.show({
						title : alert.type,
						message : alert.title,
						buttons : [ {
							id : 'btn-ok',
							icon : 'glyphicon glyphicon-check',
							label : 'OK',
							cssClass : 'btn-primary',
							autospin : false,
							action : function(dialogRef) {
								dialogRef.close();
							}
						} ]
					});
				} catch (err) {
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});

	// jQuery("#rsyncTestfiles").click(function() {
	// var problemid = $(this).attr("problemid");
	// alert(problemid);
	// jQuery.ajax({
	// type : "GET",
	// url : "./Problem.api",
	// data : "action=rsyncTestdatasByProblem&problemid=" + problemid,
	// async : false,
	// timeout : 5000,
	// success : function(result) {
	// }
	// });
	// });

});

function doRejudgeProblem(problemid) {

	jQuery.ajax({
		type : "POST",
		url : "./Problem.api?action=doRejudge",
		data : "problemid=" + problemid,
		async : true,
		cache : false,
		timeout : 5000,
		beforeSend : function() {
			rejudge_waiting.show();
			img_rejudge.hide();
		},
		success : function(rejudgesize) {
			// alert("rejudge result="+result);
			if (rejudgesize > 0) {
				getWaitingRejudgeSize(problemid, rejudgesize);
				// getWaitingRejudgeSize(problemid);
			}
		}
	}); // jQuery ajax;

}

function doRejudge(problemid) {
	console.log("doRejudge = " + problemid);
	var problemToolbar = $("#ProblemToolbar");
	var spinner = problemToolbar.find("#ProblemRejudge_spinner");
	var icon = problemToolbar.find("#ProblemRejudge_icon");
	// var rejudge_waiting = $("tr[problemid='" + problemid + "']
	// span[id='rejudge_waiting']");
	// var img_rejudge = $("tr[problemid='" + problemid + "']
	// span[id='img_rejudge']");
	// var it;
	jQuery.ajax({
		type : "POST",
		url : "./Problem.api?action=doRejudge",
		data : "problemid=" + problemid,
		async : true,
		cache : false,
		timeout : 5000,
		beforeSend : function() {
			spinner.show();
			icon.hide();
		},
		success : function(rejudgesize) {
			// alert("rejudge result="+result);
			if (rejudgesize > 0) {
				getWaitingRejudgeSize(problemid, rejudgesize);
				// getWaitingRejudgeSize(problemid);
			}
		}
	}); // jQuery ajax;
}

function getWaitingRejudgeSize(problemid) {
	var it;
	it = jQuery.interval(function() {
		jQuery.ajax({
			type : "GET",
			url : "Problem.api",
			data : "action=getWaitingRejudgeSize&problemid=" + problemid,
			// async: false,
			cache : false,
			timeout : 5000,
			beforeSend : function() {
				jQuery("tr[problemid='" + problemid + "'] span[id='rejudge_waiting']").show();
				jQuery("tr[problemid='" + problemid + "'] span[id='img_rejudge']").hide();
			},
			success : function(result) {
				if (result != "0") {
					// jQuery("span[id='rejudge_waiting']:eq("+index+")").hide();
					// jQuery("span[id='img_rejudge']:eq("+index+")").show();
				} else {
					jQuery.clear(it);
					jQuery("tr[problemid='" + problemid + "'] span[id='rejudge_waiting']").hide();
					jQuery("tr[problemid='" + problemid + "'] span[id='img_rejudge']").show();
					// return;
				}
			}
		}); // jQuery ajax
	}, 1000);
}
