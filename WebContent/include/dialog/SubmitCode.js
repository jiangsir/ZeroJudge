jQuery(document).ready(function() {
	jQuery("button#SubmitCode").click(function() {
		var problemid = $(this).attr("problemid");
		jQuery.ajax({
			type : "POST",
			url : "./Solution.api",
			data : "action=canSubmitCode&problemid=" + problemid,
			async : false,
			timeout : 5000,
			error : function(jqXHR, textStatus, errorThrown) {
				if (jqXHR.responseText !== '') {
					// showError(jqXHR.responseText);
					$waiting.find("#spinner").hide();
					$waiting.find("#serverOutputs").html(jqXHR.responseText);
					$waiting.dialog('open');
				} else {
					showError(errorThrown);
				}
			},
			success : function(result) {
				showSubmitDialog();
			}
		});

	});
});

function showSubmitDialog() {
	$dialog = $("div[id='SubmitCode_dialog']").dialog({
		disabled : true,
		autoOpen : false,
		width : '60%',
		title : '解題',
		close : function(event, ui) {
			$(this).dialog("destroy");
		},
		buttons : {
			"送出" : function() {
				var data = $(this).find("form").serialize();
				jQuery.ajax({
					type : "POST",
					url : "./Solution.api",
					data : "action=SubmitCode&" + data,
					async : false,
					timeout : 5000,
					error : function(jqXHR, textStatus, errorThrown) {
						if (jqXHR.responseText !== '') {
							// showError(jqXHR.responseText);
							// $waiting.find("#spinner").hide();
							// $waiting.find("#serverOutputs").html(jqXHR.responseText);
							// $waiting.dialog('open');
							// alert(jqXHR.responseText);
							$("#error_dialog").html(jqXHR.responseText);
							$("#error_dialog").dialog({
								buttons : {
									"Ok" : function() {
										$(this).dialog("close");
									}
								}
							});
							$("#error_dialog").dialog("open");
						} else {
							// showError(errorThrown);
						}
					},
					success : function(result) {
						var redirect = JSON.parse(result);
						window.location.href = redirect.uri;
					}
				});
				$(this).dialog("destroy");
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		}
	});
	$dialog.dialog('open');
	return true;
}

function doSubmit() {
	var showdetail_dialog = $("div.showdetail_dialog");
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

	var data = $(this).find("form").serialize();

	jQuery.ajax({
		type : "POST",
		url : "./Solution.api",
		data : "action=SubmitCode&" + data,
		async : true,
		timeout : 5000,
		beforeSend : function() {
			$waiting.find("#spinner").show();
			$waiting.find("#serverOutputs").html("");
			$waiting.dialog('open');
		},
		error : function(jqXHR, textStatus, errorThrown) {
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
						// alert(result);
						var summary = JSON.parse(result);
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
