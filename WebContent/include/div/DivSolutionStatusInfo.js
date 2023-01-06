jQuery(document).ready(function() {

	jQuery("span[id='judgement']:contains('Waiting')").each(function() {
		var solutionid = $(this).data("solutionid");
		// 這裡的 doRejudge1 先取消。因為會在使用者送出解題的時候產生 rejudge 的要求。
		// doRejudge1(solutionid);
		// var statusinfo = $("div#statusinfo[solutionid='" + solutionid +
		// "']");
		var statusinfo = $(this).closest("#statusinfo");
		statusinfo.find("img#Spinner").show();
		statusinfo.find("span[id='judgement']").hide();
		statusinfo.find("#summary").hide();
		doReflesh(statusinfo);
	});

	// jQuery("img[id='solution_rejudge']").click(function() {
	// var solutionid = $(this).data("solutionid");
	// // alert("click rejudge");
	// doRejudge1(solutionid);
	// });

	jQuery("div[id='rejudgeSolution']").click(function() {
		var solutionid = $(this).data("solutionid");
		console.log("rejudgeSolution: solutionid=" + solutionid);
		doRejudge1($(this));
	});

	jQuery(document).on("click", "span[id='judgement'] a", function(event) {
		event.preventDefault();
		var solutionid = $(this).data("solutionid");
		var modal = $('#Modal_ServerOutputs');
		modal.find("div[id='ServerOutput']:first").hide();
		modal.find('div#ServerOutput:not(:first)').remove();
		
		console.log("judgement solutionid=" + solutionid);
		var statusinfo = $(this).closest("#statusinfo");
		var problemscores = statusinfo.data("problemscores");
		console.log("problemscores=" + statusinfo.data("problemscores"));
		jQuery.ajax({
			type : "GET",
			url : "./Solution.json",
			data : "data=ServerOutputs&solutionid=" + solutionid,
			dataType : 'json',
			cache : false,
			timeout : 5000,
			success : function(data) {
				if (data == '') {
					var serverOutput = modal.find("div[id='ServerOutput']:last");
					serverOutput.find("#judgement").html("資料有誤，或格式已過期，請重測。");
					serverOutput.find("#summary").html("");
					serverOutput.show();
				}
				console.log("solutionid=" + solutionid + ", data=" + JSON.stringify(data));
				var index = 0;
				$.each(data, function(key, val) {
					var new_serverOutput = modal.find("div[id='ServerOutput']:last").clone();
					console.log("serverOutput : last="+modal.find("div#ServerOutput:last").html());
					console.log("key=" + key + ", val=" + val);
					console.log("val.judgement=" + val.judgement);
					console.log("val.summary=" + val.summary);
					console.log("val.reason=" + val.reason);
					console.log("val.hint=" + val.hint);
					console.log("val.debug=" + val.debug);
					console.log("index="+new_serverOutput.find("#index").html());
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
					if (val.debug!=''){
						new_serverOutput.find("#debug").html("<pre>"+val.debug+"</pre>");
						new_serverOutput.find("#debug").show();
					}else{
						new_serverOutput.find("#debug").html("<pre></pre>");
						new_serverOutput.find("#debug").hide();
					}
					//if (val.hint == '') {
					//	new_serverOutput.find("#hint").hide();
					//}
					new_serverOutput.show();
					new_serverOutput.insertAfter(modal.find("div#ServerOutput:last"));
				});

				modal.modal('toggle');
//				modal.on('shown', function() {
//					$('.accordion-body').each(function() {
//						$(this).collapse();
//					});
//				})
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
	});
});

function doRejudge1(rejudgeSolution) {
	console.log("rejudgeSolution=" + rejudgeSolution.html());

	// var statusinfo = $("div#statusinfo[solutionid='" + solutionid + "']");

	var statusinfo = rejudgeSolution.closest("#statusinfo");
	var solutionid = statusinfo.data("solutionid");
	console.log("solutionid=" + solutionid);
	// console.log("statusinfo=" + statusinfo.html());
	statusinfo.find("img#Spinner").show();
	statusinfo.find("span[id='judgement']").hide();
	statusinfo.find("#summary").hide();

	jQuery.ajax({
		type : "GET",
		url : "./ReJudge.ajax",
		data : "solutionid=" + solutionid,
		// async : false,
		timeout : 5000,
		beforeSend : function() {
		},
		success : function(result) {
			console.log("rejudge.ajax result=" + result);
			// it = setInterval("refresh_ajaxrejudge("+solutionid+");",1000);
			// doSolutionStatus(result);
			doReflesh(statusinfo);
		}
	});
}
// 純粹進行每秒鐘去更新，而不進行 rejudge
function doReflesh(statusinfo) {
	// var statusinfo = $("div#statusinfo[solutionid='" + solutionid + "']");
	var solutionid = statusinfo.data("solutionid");
	var it;
	it = jQuery.interval(function() {
		jQuery.ajax({
			type : "POST",
			url : "./Solution.api",
			data : "action=getSummary&solutionid=" + solutionid,
			// async : false,
			cache : false,
			timeout : 5000,
			success : function(result) {
				console.log("solutionid=" + solutionid + ", summary=" + result);
				var summary = JSON.parse(result);
				if (summary.judgement == "Waiting") {
					statusinfo.find("img#Spinner").show();
					statusinfo.find("span[id='judgement']").hide();
					statusinfo.find("#summary").hide();
				} else {
					jQuery.clear(it);
					var judgement = statusinfo.find("span[id='judgement']");

					if (summary.accessible == true) {
						judgement.empty();
						judgement.html("<a href=\"#\" data-solutionid=" + solutionid + ">" + summary.judgement + "</a>");
						if (summary.judgement == "AC") {
							judgement.children("a").addClass("acstyle");
						} else {
							judgement.children("a").removeClass("acstyle");
						}
					} else {
						judgement.html(summary.judgement);
						if (summary.judgement == "AC") {
							judgement.addClass("acstyle");
						} else {
							judgement.removeClass("acstyle");
						}
					}
					statusinfo.find("#summary").html(summary.summary);
					statusinfo.find("img#Spinner").hide();
					statusinfo.find("span#judgement").show();
					statusinfo.find("#summary").show();
					// 20141106 reload 這個雖然很方便，可是 spinner 轉完之後的 judgement 卻無法點擊
					// showDetail
					// reloadSolutionStatusInfo(statusinfo, solutionid);
					return;
				}
			}
		}); // jQuery ajax
	}, 2000 + Math.random() * 5000);
}

function reloadSolutionStatusInfo(statusinfo, solutionid) {
	jQuery.ajax({
		type : "POST",
		url : "./Solution.api",
		data : "action=SolutionStatusInfo&solutionid=" + solutionid,
		async : false,
		timeout : 5000,
		success : function(result) {
			// var statusinfo = $("div#statusinfo[solutionid='" + solutionid +
			// "']");
			// statusinfo.parent().html("<span id='judgement'><a
			// href='#'>aaa</a></span>");
			// statusinfo.parent().empty();
			statusinfo.parent().html(result);
			// parent.append(result);
			// alert(parent.html());
		}
	});
}