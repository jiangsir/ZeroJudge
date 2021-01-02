jQuery(document).ready(function() {
	jQuery("span#ICON_prejudge").click(function() {
		// var statusinfo = $(this).parent();
		var problemToolbar = $(this).closest("#ProblemToolbar");
		doPrejudgeProblem(problemToolbar);
	});

	jQuery(document).on("click", "span[id='problemJudgement'] a", function(event) {
		event.preventDefault();
		var problemid = $(this).data("problemid");
		console.log("judgement problemid=" + problemid);
		var modal = $('#Modal_ServerOutputs');
		console.log("modal.html()="+modal.html());
//		modal.find("#ServerOutput:first").hide();
//		modal.not('#ServerOutput:first').remove();
		modal.find("div[id='ServerOutput']:first").hide();
		modal.find('div#ServerOutput:not(:first)').remove();
		
		console.log("judgement problemid=" + problemid);
		var statusinfo = $(this).closest("#statusinfo");
		var problemscores = statusinfo.data("problemscores");
		console.log("problemscores=" + statusinfo.data("problemscores"));

		jQuery.ajax({
			type : "GET",
			url : "./Problem.json",
			data : "data=ServerOutputs&problemid=" + problemid,
			dataType : 'json',
			cache : false,
			timeout : 5000,
			success : function(data) {
				if (data == '') {
					var serverOutput = modal.find("div#ServerOutput:last");
					serverOutput.find("#judgement").html("資料有誤，或格式已過期，請重測。");
					serverOutput.find("#summary").html("");
					serverOutput.show();
				}
				console.log("problemid=" + problemid + ", data=" + data);
				var index = 0;
				$.each(data, function(key, val) {
					var new_serverOutput = modal.find("div#ServerOutput:last").clone();
					console.log("key=" + key + ", val=" + val);
					console.log("val.judgement=" + val.judgement);
					console.log("val.summary=" + val.summary);
					console.log("val.reason=" + val.reason);
					console.log("val.hint=" + val.hint);

					new_serverOutput.find("#index").html("#" + index + ": " + problemscores[index] + "% ");
					index++;
					if (val.isAccept) {
						new_serverOutput.find("#judgement").addClass("acstyle");
					} else {
						new_serverOutput.find("#judgement").removeClass("acstyle");
					}
					new_serverOutput.find("#judgement").html(val.judgement);
					new_serverOutput.find("#summary").html(" (" + val.summary + ")");
					new_serverOutput.find("#reason").html(val.reason);
					new_serverOutput.find("#hint").html(val.hint);
					//if (val.hint == '') {
					//	new_serverOutput.find("#hint").hide();
					//}
					new_serverOutput.show();
					new_serverOutput.insertAfter(modal.find("div#ServerOutput:last"));
				});
				modal.modal('toggle');
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				// console.log("errorThrown=" + errorThrown);
				// console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					console.log("alert=" + alert);
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
					console.log("ContestToolbar.js err=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}
		});

		// $.getJSON("./Problem.json?data=ServerOutputs&problemid=" + problemid,
		// function(data) {
		// console.log("data=" + data);
		// $.each(data, function(key, val) {
		// var new_serverOutput = modal.find("#ServerOutput:last").clone();
		// console.log("key=" + key + ", val=" + val);
		// console.log("val.judgement=" + val.judgement);
		// console.log("val.summary=" + val.summary);
		// console.log("val.reason=" + val.reason);
		// console.log("val.hint=" + val.hint);
		// new_serverOutput.find("#judgement").html(val.judgement);
		// new_serverOutput.find("#summary").html(val.sommary);
		// new_serverOutput.find("#reason").html(val.reason);
		// new_serverOutput.find("#hint").html(val.hint);
		// new_serverOutput.show();
		// new_serverOutput.insertAfter(modal.find("#ServerOutput:last"));
		// });
		//
		// });
		// modal.modal('toggle');
	});

});

function doPrejudgeProblem(problemToolbar) {
	var problemid = problemToolbar.data("problemid");
	var statusinfo = problemToolbar.find("#statusinfo");

	var spinner = statusinfo.find("#prejudge_spinner");
	var prejudgement = statusinfo.find("#problemJudgement");
	var summary = statusinfo.find("#summary");
	// var prejudgeinfo = problemToolbar.find("span[id='prejudgeinfo']");
	var img_prejudge = statusinfo.find("span[id='img_prejudge']");
	var it;
	jQuery.ajax({
		type : "POST",
		url : "./PreJudge.ajax",
		data : "problemid=" + problemid,
		async : false,
		timeout : 5000,
		beforeSend : function() {
			// alert("Prejudge.ajax?" + problemid);
			// ajaxwaiting.show();
			spinner.show();
			prejudgement.hide();
			summary.hide();
			// prejudgeinfo.hide();
			img_prejudge.hide();
		},
		success : function(prejudgeresult) {
			it = jQuery.interval(function() {
				$.getJSON("./Problem.json?data=PrejudgeInfo&problemid=" + problemid, function(data) {
					console.log("data=" + data);
					console.log("data.prejudgement=" + data.prejudgement);
					if (data.prejudgement == "Waiting") {
					} else {
						jQuery.clear(it);
						spinner.hide();
						console.log("prejudgement="+prejudgement);
						prejudgement.html("<a href=# data-problemid="+problemid+">" + data.prejudgement + "</a>");
						if (data.prejudgement == "AC") {
							prejudgement.children("a").addClass("acstyle");
						} else {
							prejudgement.children("a").removeClass("acstyle");
						}
						summary.html(data.prejudgeinfo);
						summary.show();
						prejudgement.show();
						img_prejudge.show();
					}
				});
			}, 1000);
		}
	});
	return false;
}
