jQuery(document).ready(function () {

	$('button#modal_exportContest_submit').on('click', function () {
		var modal = $(this).closest('.modal');
		var contestid = $(this).data('contestid');
		console.log("contestid: " + contestid);
		console.log("contestid: " + modal.find("form").serialize());
		var formdata = modal.find("form").serialize();
		// 透過 jquery 下載檔案，直接轉 URL 即可
		// 若透過 jQuery.ajax 會無法下載。
		window.location = "./Contest.api?action=doExportContest&" + formdata;

		modal.modal('hide');
		// jQuery.ajax({
		// 	type : "GET",
		// 	url : "./Contest.api?action=doExportContest&contestid=" + contestid,
		// 	data : modal.find("form").serialize(),
		// 	async : false,
		// 	//timeout : 5000,
		// 	success : function(result) {
		// 		console.log("success: "+result);
		// 		modal.modal('hide');
		// 		location.reload();
		// 	},
		// 	error: function (jqXHR, errorThrown) {
		// 		console.log("error: ");
		// 		try {
		// 			alert = jQuery.parseJSON(jqXHR.responseText);
		// 			BootstrapDialog.alert(alert.title);
		// 		} catch (error) {
		// 			BootstrapDialog.alert(errorThrown);
		// 		}
		// 	}
		//});
	});


	jQuery("button[id='btn_ContestSettings']").click(function () {
		event.preventDefault();
		var contestid = $(this).data("contestid");
		var modal = $('#Modal_ShowContestSettings_' + contestid);
		console.log("contestid=" + contestid);
		jQuery.ajax({
			type: "GET",
			url: "./Contest.json",
			data: "data=Settings&contestid=" + contestid,
			dataType: 'json',
			cache: false,
			timeout: 10000,
			success: function (data) {
				console.log("success data=" + JSON.stringify(data));
				modal.find("#JSONSettings").html(JSON.stringify(data));
				// modal.modal('toggle');
				// $('pre#code').each(function(i, block) {
				// hljs.highlightBlock(block);
				// });
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				// console.log("errorThrown=" + errorThrown);
				// console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					console.log("alert=" + alert);
					BootstrapDialog.show({
						title: alert.type,
						message: alert.title,
						buttons: [{
							id: 'btn-ok',
							icon: 'glyphicon glyphicon-check',
							label: 'OK',
							cssClass: 'btn-primary',
							autospin: false,
							action: function (dialogRef) {
								dialogRef.close();
							}
						}]
					});
				} catch (err) {
					console.log("ContestToolbar.js err=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});

	jQuery("button#exportContest").click(function (event) {
		var contestid = $(this).data("contestid");
		console.log('exportContest: contestid=' + contestid)
	});

	jQuery("button#rejudgeContest").click(function (event) {
		var contestid = $(this).data("contestid");
		console.log('contestid=' + contestid)

		countRejudgeContest = -1;
		getCountRejudgeContest(contestid).done(function (result) {
			console.log("getCountRejudgeContest result=" + result)
			countRejudgeContest = result
		});

		BootstrapDialog.confirm({
			title: "重測整個競賽",
			message: "重測整個競賽共有 <span style='font-size:2em;'> " + countRejudgeContest + " </span> 個程式碼必須重測，請問確定重測嗎？",
			type: BootstrapDialog.TYPE_PRIMARY, // <-- Default value is
			// BootstrapDialog.TYPE_PRIMARY
			closable: true, // <-- Default value is false
			draggable: true, // <-- Default value is false
			btnCancelLabel: '取消', // <-- Default value is
			// 'Cancel',
			btnOKLabel: '確定重測', // <-- Default value is 'OK',
			callback: function (result) {
				// result will be true if button was click, while it will be
				// false if users close the dialog directly.
				console.log('countRejudgeContest 222=' + countRejudgeContest)
				var type = "POST";
				var url = "Contest.api";
				var qs = { action: "doRejudgeContest", contestid: contestid };
				if (result) {
					jQuery.ajax({
						type: type,
						url: url,
						data: qs,
						cache: false,
						timeout: 5000,
						success: function () {
							console.log('countRejudgeContest 222=' + countRejudgeContest)
						},
						error: function (jqXHR, textStatus, errorThrown) {
							console.log("jqXHR.responseText=" + jqXHR.responseText);
							console.log("errorThrown=" + errorThrown);
							console.log("textStatus=" + textStatus);
							try {
								alert = jQuery.parseJSON(jqXHR.responseText);
								// BootstrapDialog.alert(alert.title);
								// console.log("ContestToolbar.js err=" + err);
								BootstrapDialog.show({
									title: alert.type,
									message: alert.title,
									buttons: [{
										id: 'btn-ok',
										icon: 'glyphicon glyphicon-check',
										label: 'OK',
										cssClass: 'btn-primary',
										autospin: false,
										action: function (dialogRef) {
											dialogRef.close();
										}
									}]
								});
							} catch (err) {
								console.log("ContestToolbar.js err=" + err);
								BootstrapDialog.alert(errorThrown);
							}
						}
					}); // jQuery ajax;
				}
			}
		});

	});

	jQuery("button#Modal_confirm").click(function (event) {
		var title = $(this).data("title");
		var content = $(this).data("content");
		var type = $(this).data("type");
		var url = $(this).data("url");
		var qs = $(this).data("qs");

		BootstrapDialog.confirm({
			title: title,
			message: content,
			type: BootstrapDialog.TYPE_PRIMARY, // <-- Default value is
			// BootstrapDialog.TYPE_PRIMARY
			closable: true, // <-- Default value is false
			draggable: true, // <-- Default value is false
			btnCancelLabel: '取消', // <-- Default value is
			// 'Cancel',
			btnOKLabel: '確定', // <-- Default value is 'OK',
			callback: function (result) {
				// result will be true if button was click, while it will be
				// false if users close the dialog directly.
				if (result) {
					jQuery.ajax({
						type: type,
						url: url,
						data: qs,
						cache: false,
						//timeout: 10000,
						success: function () {
							console.log("success title=" + title + ", url=" + url + qs);
						},
						error: function (jqXHR, textStatus, errorThrown) {
							console.log("jqXHR.responseText=" + jqXHR.responseText);
							console.log("errorThrown=" + errorThrown);
							console.log("textStatus=" + textStatus);
							try {
								alert = jQuery.parseJSON(jqXHR.responseText);
								// BootstrapDialog.alert(alert.title);
								// console.log("ContestToolbar.js err=" + err);
								BootstrapDialog.show({
									title: alert.type,
									message: "Dialog.show=" + alert.title,
									buttons: [{
										id: 'btn-ok',
										icon: 'glyphicon glyphicon-check',
										label: 'OK',
										cssClass: 'btn-primary',
										autospin: false,
										action: function (dialogRef) {
											console.log("ContestToolbar.js show=" + err);
											dialogRef.close();
										}
									}]
								});
								location.reload();
							} catch (err) {
								console.log("ContestToolbar.js err=" + err);
								BootstrapDialog.alert("errorThrown=" + errorThrown);
								location.reload();
							}
						}
					}); // jQuery ajax;
				}
			}
		});
	});

});


function getCountRejudgeContest(contestid) {
	console.log('function getCountRejudgeContest contestid=' + contestid)
	return $.ajax({
		type: 'POST',
		dataType: "json",
		url: 'Contest.api',
		data: { action: 'countRejudgeContest', contestid: contestid },
		async: false,
		success: function (result) {
			console.log('function getCountRejudgeContest result=' + result)
		}
	});

}