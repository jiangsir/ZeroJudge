// google.load("language", "1");
//

// JavaScript Document
jQuery(document).ready(function() {
	jQuery("input[name='language']").each(function() {
		if ($(this).attr('userlanguage') == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("form[name='searchBackground']").click(function() {
		$(this).submit();
	});

	jQuery("form[name='searchReference']").click(function() {
		$(this).submit();
	});

	jQuery("button#runTestjudge").click(function(e) {
		e.preventDefault();
		var modal = $(this).closest(".modal");
		// modal_serveroutputs.css('zIndex', 100000);
		// modal_serveroutputs.modal('toggle');
		doTestjudge(modal);
	});

	jQuery("button#submitCode").click(function(e) {
		e.preventDefault();
		var modal = $(this).closest(".modal");
		var form = modal.find("form");
		console.log("formdata=" + form.serialize());
		jQuery.ajax({
			type : "POST",
			url : "./Solution.api",
			data : "action=SubmitCode&" + form.serialize(),
			async : false,
			timeout : 5000,
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				console.log("errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					// BootstrapDialog.alert(alert.title);
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
			},
			success : function(result) {
				console.log("result=" + result);
				var redirect = JSON.parse(result);
				window.location.href = redirect.uri;
			}
		});
	});

});

function doTestjudge(modal) {

	var form = modal.find("form");
	var modal_serveroutputs = $("#Modal_ServerOutputs");
	jQuery.ajax({
		type : "POST",
		url : "./Testjudge",
		data : form.serialize(),
		async : false,
		timeout : 5000,
		beforeSend : function() {
			console.log("beforeSend");
			modal_serveroutputs.find("div.progress").show();
			console.log("beforeSend show progress" + modal_serveroutputs.find("div.progress").html());
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			try {
				alert = jQuery.parseJSON(jqXHR.responseText);
				// BootstrapDialog.alert(alert.title);
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
		},
		success : function(serverOutputs) {
			console.log("serverOutputs=" + serverOutputs);
			var serveroutputs = JSON.parse(serverOutputs);
			var solutionid = serveroutputs[0].solutionid;
			console.log("solutionid=" + solutionid);
			// var modal_serveroutputs = $('#Modal_ServerOutputs');
			// alert("toggle");
			console.log("開始 setInterval");

			modal_serveroutputs.remove(); // 要先 remove() 一次，才不會變成疊加狀態。
			modal_serveroutputs.find("#ServerOutput:first").hide();
			console.log("modal_serveroutputs.find(#ServerOutput).size=" + modal_serveroutputs.find("#ServerOutput").size());
			modal_serveroutputs.find("#ServerOutput").not(':first').remove();
			console.log("modal_serveroutputs.find(#ServerOutput).size=" + modal_serveroutputs.find("#ServerOutput").size());
			modal_serveroutputs.find("div.progress").show();
			modal_serveroutputs.css('zIndex', 10000);
			modal_serveroutputs.modal('toggle');
			// alert("STEP1");
			/*
			 * $.getJSON("./Solution.json?data=ServerOutputs&solutionid=" +
			 * solutionid, function(data) { //alert("STEP2");
			 * //console.log("data1=" + data);
			 * modal_serveroutputs.find("#ServerOutput:first").hide();
			 * //modal_serveroutputs.not('#ServerOutput:first').remove();
			 * alert("STEP3"); var new_serverOutput =
			 * modal_serveroutputs.find("#ServerOutput:last").clone();
			 * console.log("new_serverOutput="+new_serverOutput);
			 * new_serverOutput.show();
			 * new_serverOutput.insertAfter(modal_serveroutputs.find("#ServerOutput:last"));
			 * new_serverOutput =
			 * modal_serveroutputs.find("#ServerOutput:last").clone();
			 * console.log("new_serverOutput="+new_serverOutput);
			 * new_serverOutput.show();
			 * new_serverOutput.insertAfter(modal_serveroutputs.find("#ServerOutput:last"));
			 * });
			 */

			it = setInterval(function() {
				// $('#Modal_ServerOutputs :not(#ServerOutput:first)').remove();
				// modal_serveroutputs.not('#ServerOutput:first').remove();
				console.log("step1: solutionid=" + solutionid);
				$.ajax({
					url : './Solution.json?data=ServerOutputs&solutionid=' + solutionid,
					dataType : 'json',
					success : function(data) {
						var index = 0;
						$.each(data, function(key, val) {
							var new_serverOutput = modal_serveroutputs.find("#ServerOutput:last").clone();
							console.log("key=" + key + ", val=" + val);
							console.log("val.judgement=" + val.judgement);
							console.log("val.isWaiting=" + val.isWaiting);
							console.log("val.summary=" + val.summary);
							console.log("val.reason=" + val.reason);
							console.log("val.hint=" + val.hint);
							//new_serverOutput.find("#index").html("#" + index);
							index++;
							if (val.isAccept) {
								new_serverOutput.find("#judgement").addClass("acstyle");
							} else {
								new_serverOutput.find("#judgement").removeClass("acstyle");
							}

							new_serverOutput.find("#judgement").html(val.judgement);
							new_serverOutput.find("#summary").html(" ("+val.summary+")");
							new_serverOutput.find("#reason").html(val.reason);
							new_serverOutput.find("#hint").html(val.hint);
							new_serverOutput.show();
							new_serverOutput.insertAfter(modal_serveroutputs.find("#ServerOutput:last"));

							if (!val.isWaiting) {
								modal_serveroutputs.find("div.progress").hide();
								// modal_serveroutputs.modal('toggle');
								// modal_serveroutputs.css('zIndex', 100000);
								// modal_serveroutputs.modal('toggle');
								clearInterval(it);
								return;
							}
						});
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log("jqXHR.responseText=" + jqXHR.responseText);
						console.log("errorThrown=" + errorThrown);
						console.log("textStatus=" + textStatus);
						try {
							alert = jQuery.parseJSON(jqXHR.responseText);
							// BootstrapDialog.alert(alert.title);
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
						modal_serveroutputs.modal('hide'); 
						clearInterval(it); // stop the interval
					}
				});
				// $.getJSON("./Solution.json?data=ServerOutputs&solutionid=" +
				// solutionid, function(data) {
				// });
			}, 1000);
		}
	});
}

function showServerOutputs(modal, solutionid) {

}
