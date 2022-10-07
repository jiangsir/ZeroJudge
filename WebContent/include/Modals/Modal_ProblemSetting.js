jQuery(document).ready(function() {

	jQuery("button#Modal_ProblemSettings_confirm").click(function(e) {
		e.preventDefault();
		save_ProblemSettings($(this))
	});
/*
	jQuery("img[id='ProblemSettings']").click(function() {
		// var index = $("img[id='ProblemSettings']").index(this);
		// var problemid = $(".ProblemSettings_dialog:eq(" + index +
		// ")").attr("problemid");
		var problemid = $(this).attr("problemid");
		// var title = $(".ProblemSettings_dialog:eq(" + index +
		// ")").attr("title");
		jQuery("input[name='tabid']").each(function() {
			if ($(this).attr("tabid") == $(this).val()) {
				$(this).attr("checked", true);
			}
		});
		jQuery("input[name='difficulty']").each(function() {
			if ($(this).attr("difficulty") == $(this).val()) {
				$(this).attr("checked", true);
			}
		});
		jQuery("input[name='wa_visible']").each(function() {
			if ($(this).attr("wa_visible") == $(this).val()) {
				$(this).attr("checked", true);
			}
		});

		jQuery("span[id='clean_background']").click(function() {
			jQuery("input[name='backgrounds']").val("");
		});

		// jQuery.ajax({
		// type: "GET",
		// url: "Problem.api",
		// data: "action=getProblemSettings&problemid=" + problemid,
		// async: false,
		// timeout: 5000,
		// success: function(result){
		// var json_ProblemSettings = JSON.parse(result);
		// jQuery("input[name='tabid']").each(function(){
		// if (json_ProblemSettings.tabid == $(this).val()) {
		// $(this).attr("checked", true);
		// }
		// });
		// jQuery("input[name='difficulty']").each(function(){
		// if (json_ProblemSettings.difficulty == $(this).val()) {
		// $(this).attr("checked", true);
		// }
		// });
		// jQuery("input[name='background']").val(json_ProblemSettings.background);
		// jQuery("input[name='keywords']").val(json_ProblemSettings.keywords);
		// jQuery("input[name='sortable']").val(json_ProblemSettings.sortable);
		// }
		// }); // jQuery ajax
		var problemSettings_dialog = $(this).closest("#problemToolbar").find(".ProblemSettings_dialog");
		var $dialog = problemSettings_dialog.dialog({
			// var $dialog = $(".ProblemSettings_dialog:eq(" + index +
			// ")").dialog({
			autoOpen : false,
			width : '60%',
			title : problemid + ': ',
			close : function() {
				$(this).dialog("destroy");
			},
			buttons : {
				"設定" : function() {
					// alert($("form[problemid='" + problemid +
					// "']").serialize());

					var form = problemSettings_dialog.find("form[problemid='" + problemid + "']");
					// alert(form.serialize());
					jQuery.ajax({
						type : "POST",
						url : "Problem.api",
						data : "action=setProblemSettings&problemid=" + problemid + "&" + form.serialize(),
						async : false,
						timeout : 5000,
						success : function(result) {
						}
					}); // jQuery ajax
					// location.replace(location.href);
					// history.go(-1);
					// location.reload();
					$(this).dialog("destroy");
				},
				"取消" : function() {
					$(this).dialog("destroy");
				}
			}
		});

		$dialog.dialog('open');
		return false;
	});
	*/
});

function save_ProblemSettings(button) {
	var problemid = button.data("problemid");
	var form = button.closest("#ProblemSettings").find("form");
	//var form = $("form#Form_ProblemSetting_" + problemid);
	//console.log("form=" + form.html());
	var action = form.attr("action");
	console.log("action=" + action);
	//console.log("data=" + form.serialize());
	jQuery.ajax({
		type : "POST",
		url : action,
		data : form.serialize(),
		// data : form.serializeArray(),
		// data : new FormData(form[0]),
		cache : false,
		// processData : false,
		// contentType : false,
		async : true,
		timeout : 5000,
		success : function(result) {
			// console.log(result);
			// window.location.href = document.referrer; // 跳轉到前一頁，並
			// reload
			// location.reload(); // 本頁重讀。
			BootstrapDialog.alert("儲存成功。");
			// reload
			// location.reload(); // 本頁重讀。
		},
		beforeSend : function(jqXHR, settings) {
			jqXHR.url = settings.url;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// console.log("jqXHR.responseText=" +
			// jqXHR.responseText);
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
				console.log("catch(err)=" + err);
				BootstrapDialog.alert("發生錯誤(" + err + ")！" + errorThrown + ", url=" + jqXHR.url);
			}
		}
	});
}
