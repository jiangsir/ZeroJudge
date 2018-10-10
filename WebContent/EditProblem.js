jQuery(document).ready(function() {

	jQuery("#save_ProblemSettings").click(function(e) {
		console.log("#save_ProblemSettings");
		save_ProblemSettings($(this))
	});
	
	jQuery("input[name='saveUploadSpecialJudgeFiles']").click(function(e) {
		// this.closest("form").submit();
		e.preventDefault();
		console.log($(this).val());
		var form = $(this).closest("form");
		var problemid = form.data("problemid");
		console.log("problemid="+problemid);
		console.log("form=" + form.html());
		jQuery.ajax({
			type : "POST",
			url : "UploadFiles.api?action=uploadSpecialJudgeFiles&problemid="+problemid,
			// data: form.serialize(),
			// data : form.serializeArray(),
			data : new FormData(form[0]),
			cache : false,
			processData : false,
			contentType : false,
			async : true,
			timeout : 30000,
			success : function(result) {
				console.log(result);
				// window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				// location.reload(); // 本頁重讀。
				//BootstrapDialog.alert("儲存成功。");
				// reload
				location.reload(); // 本頁重讀。
			},
			beforeSend : function(jqXHR, settings) {
				jqXHR.url = settings.url;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				// console.log("jqXHR.responseText=" +
				// jqXHR.responseText);
				
				console.log("errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				console.log("jqXHR.responseText="+jqXHR.responseText);
				try {
					alert1 = jQuery.parseJSON(jqXHR.responseText);
					
					
					BootstrapDialog.show({
						title : alert1.type,
						message : alert1.title,
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
					BootstrapDialog.alert(errorThrown + ", url=" + jqXHR.url);
				}
			}
		});
	});

	jQuery("input[name='writeScoresTimelimits']").click(function(e) {
		e.preventDefault();
		var form = $(this).closest("form");
		var action = form.attr("action");
		console.log("action=" + action);
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
				console.log(result);
				// window.location.href = document.referrer; //
				// 跳轉到前一頁，並
				// reload
				// location.reload(); // 本頁重讀。
				BootstrapDialog.alert("儲存完成！");
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
					BootstrapDialog.alert(errorThrown + ", url=" + jqXHR.url);
				}
			}
		});

	});

	jQuery("input[name='uploadFiles']").click(function(e) {
		// this.closest("form").submit();
		e.preventDefault();
		var form = $(this).closest("form");
		console.log("form=" + form);
		var problemid = $(this).data("problemid");
		jQuery.ajax({
			type : "POST",
			url : "UploadFiles.api?action=uploadTestdatas&problemid=" + problemid,
			// data: form.serialize(),
			// data : form.serializeArray(),
			data : new FormData(form[0]),
			cache : false,
			processData : false,
			contentType : false,
			async : true,
			timeout : 60000, // 測資上傳時限
			success : function(result) {
				console.log(result);
				// window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				// location.reload(); // 本頁重讀。
				BootstrapDialog.alert("上傳成功。");
				// reload
				location.reload(); // 本頁重讀。
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
					BootstrapDialog.alert(errorThrown + ", url=" + jqXHR.url);
				}
			}
		});
	});

	
	
	jQuery("input[name='locale']").each(function() {
		// alert(jQuery("span[name='locale']").text());
		if (jQuery("span[name='locale']").text() == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("input[name='wa_visible']").each(function() {
		if (jQuery("span[name='wa_visible']").text() == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("input[name='judgemode']").each(function() {
		if (jQuery("span[name='judgemode']").text() == $(this).val()) {
			$(this).attr("checked", true);
			// if ($(this).val() == "Special") {
			// jQuery("#SpecialJudgeHint").show();
			// } else {
			// jQuery("#SpecialJudgeHint").hide();
			// }
		}
	});

	// jQuery("input[name='judgemode']").click(function() {
	// if ($(this).val() == "Special") {
	// jQuery("#SpecialJudgeHint").show();
	// } else {
	// jQuery("#SpecialJudgeHint").hide();
	// }
	// });

	// jQuery("input[name='specialjudge_language']").each(function() {
	// if ($(this).attr("specialjudge_language") == $(this).val()) {
	// $(this).attr("checked", true);
	// }
	// });
	// jQuery("input[name='language']").each(function() {
	// if (jQuery("span[name='language']").text() == $(this).val()) {
	// $(this).attr("checked", true);
	// }
	// });
	// jQuery("input[name='tabid']").each(function() {
	// if (jQuery("div[id='problemtabs']").attr("tabid") ==
	// $(this).val()) {
	// $(this).attr("checked", true);
	// }
	// });

	// jQuery("input[name='tabid']").each(function() {
	// alert($(this).attr("tabid") + "==" + $(this).val());
	// if ($(this).attr("tabid") == $(this).val()) {
	// $(this).attr("checked", true);
	// return;
	// }
	// });

	jQuery("span[id='clean_reference']").click(function() {
		jQuery("input[name='reference']").val("");
	});

	// jQuery("span[id='background']").click(function(){
	// var index = jQuery("span[id='background']").index(this);
	// var background = jQuery("span[id='background']:eq(" + index +
	// ")").text();
	// var backgrounds = jQuery("input[name='background']").val();
	// if (backgrounds == '') {
	// backgrounds += background;
	// // jQuery("input[name='background']").val(background);
	// }
	// else {
	// backgrounds += "," + background;
	// }
	// jQuery("input[name='background']").val(backgrounds);
	// });

	jQuery("input[name='updateProblem']").click(function(event) { // 事件發生
		var action = $(this).attr('action');

		var form1 = $("form[id='updateProblem']");
		var form2 = $("form[id='writeScoresTimelimits']");
		// var judgemode = $("input[name='judgemode']:checked").val();
		// var specialjudge_code =
		// $("textarea[name='specialjudge_code']").val();
		// jQuery.ajax({
		// type : "POST",
		// url : action,
		// data : form1.serialize() + "&" + form2.serialize() +
		// "&judgemode=" +
		// judgemode + "&specialjudge_code=" + specialjudge_code,
		// async : false,
		// timeout : 5000,
		// success : function(result) {
		// location.reload();
		// }
		// }); // jQuery ajax

		form1.submit();

		// form2.submit();
		// doCheckProblem();
	});

	jQuery("span[id='reference']").click(function() {
		var index = jQuery("span[id='reference']").index(this);
		var reference = jQuery("span[id='reference']:eq(" + index + ")").text();
		jQuery("input[name='reference']").val(reference);
	});

	// jQuery("span[id='img_prejudge']").bind('click', function(){
	// var problemid = jQuery("span[name='problemid']").text();
	// doPrejudge(problemid);
	// });

	// jQuery("span[id='img_rejudge']").click(function(){
	// var problemid = $(this).attr("alt");
	// jQuery.ajax({
	// type: "GET",
	// url: "Problem.api",
	// data: "action=getRejudgeSize&problemid=" + problemid,
	// async: false,
	// cache: false,
	// timeout: 5000,
	// success: function(result){
	// jQuery("#rejudgesize").html(result);
	// }
	// }); // jQuery ajax
	// var $dialog = $("#rejudge_dialog").dialog({
	// autoOpen: false,
	// width: '40%',
	// title: 'Rejudge',
	// buttons: {
	// "取消": function(){
	// $(this).dialog("close");
	// },
	// "確定": function(){
	// doRejudge(problemid);
	// $(this).dialog("close");
	// }
	// }
	// });
	//        
	// $dialog.dialog('open');
	// return false;
	// });

	jQuery("span[id='prejudge_status'] a").click(function() {
		var problemid = $(this).parent().attr("alt");
		jQuery.ajax({
			type : "GET",
			url : "./Problem.api",
			data : "action=getDetail&problemid=" + problemid,
			async : false,
			timeout : 5000,
			success : function(result) {
				jQuery("#prejudge_detail").val(result);
			}
		});
		var $dialog = $("#prejudgedetail_dialog").dialog({
			autoOpen : false,
			width : '60%',
			title : 'Prejudge Detail',
			buttons : {
				"返回" : function() {
					$(this).dialog("close");
				}
			}
		});

		$dialog.dialog('open');
		return false;
	});

	jQuery("span[id=readtestdata]").click(function() {
		var index = jQuery("span[id=readtestdata]").index(this);
		var problemid = $(this).attr("alt");
		jQuery.ajax({
			type : "GET",
			url : "Problem.api",
			data : "action=getInputTestdataInfo&problemid=" + problemid + "&index=" + index,
			async : false,
			cache : false,
			timeout : 5000,
			success : function(result) {
				jQuery("#indataInfo").html(result);
			}
		}); // jQuery ajax
		jQuery.ajax({
			type : "GET",
			url : "Problem.api",
			data : "action=getOutputTestdataInfo&problemid=" + problemid + "&index=" + index,
			async : false,
			cache : false,
			timeout : 5000,
			success : function(result) {
				jQuery("#outdataInfo").html(result);
			}
		}); // jQuery ajax
		getIndata(problemid, index);
		getOutdata(problemid, index);

		var $dialog = $("#readtestdata_dialog").dialog({
			autoOpen : false,
			width : '80%',
			title : 'Testdata',
			buttons : {
				"返回" : function() {
					$(this).dialog("close");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});

	/*
	 * jQuery("input[name='locale']").each(function(){ alert("qx_locale=" +
	 * jQuery.query.get('locale') + ", $(this).val()=" + $(this).val()); if
	 * (jQuery.query.get('locale') == $(this).val()) { alert("==" +
	 * $(this).val()); $(this).attr("selected", "true"); } });
	 */

	// 底下這幾行是錯誤的。會導致後面的 js 都無法運作。20141102
	// if (jQuery.query.get('locale') != '') {
	// // 若 querystring 存在 locale 則優先選擇
	// select_qs_Locale();
	// } else {
	// select_problem_Locale();
	// }
});

function select_qs_Locale() {
	jQuery("#locale").children().each(function() {
		// alert("已選中 "+$("select#locale option:selected").val());
		// alert("$(this).val()=" + $(this).val() + "\nget('locale')=" +
		// jQuery.query.get('locale') + "\njQuery('#problem_locale')=" +
		// jQuery("#problem_locale").text());
		if ($(this).val() == jQuery.query.get('locale')) {
			// alert($(this).val() + " selected!!");
			// jQuery給法
			$(this).attr("selected", "true"); // 或是給selected也可
			return false;
		}
	});
}

function select_problem_Locale() {
	jQuery("#locale").children().each(function() {
		// alert("已選中 "+$("select#locale option:selected").val());
		// alert("$(this).val()=" + $(this).val() + "\nget('locale')=" +
		// jQuery.query.get('locale') + "\njQuery('#problem_locale')=" +
		// jQuery("#problem_locale").text());
		if ($(this).val() == jQuery("#problem_locale").text()) {
			// alert($(this).val() + " selected!!");
			$(this).attr("selected", "true"); // 或是給selected也可
			return false;
		}
	});

}

function getIndata(problemid, index) {
	jQuery.ajax({
		type : "GET",
		url : "Problem.api",
		data : "action=getIndata&problemid=" + problemid + "&index=" + index,
		// async: false,
		cache : false,
		timeout : 5000,
		success : function(result) {
			// alert(result);
			jQuery("#indata").val(result);
		}
	}); // jQuery ajax
}

function getOutdata(problemid, index) {
	jQuery.ajax({
		type : "GET",
		url : "Problem.api",
		data : "action=getOutdata&problemid=" + problemid + "&index=" + index,
		// async: false,
		cache : false,
		timeout : 5000,
		success : function(result) {
			// alert(result);
			jQuery("#outdata").val(result);
		}
	}); // jQuery ajax
}

// function doPrejudge(problemid){
// var it;
// jQuery.ajax({
// type: "POST",
// url: "./PreJudge.ajax",
// data: "problemid=" + problemid,
// async: false,
// timeout: 5000,
// beforeSend: function(){
// jQuery("span[id='prejudge_waiting']").show();
// jQuery("span[id='prejudge_status']").hide();
// },
// success: function(result){
// it = jQuery.interval(function(){
// jQuery.ajax({
// type: "GET",
// url: "./Problem.api",
// data: "action=getPrejudge&problemid=" + problemid,
// async: false,
// cache: false,
// timeout: 5000,
// success: function(result){
// if (result == "Waiting") {
// }
// else {
// jQuery.clear(it);
// jQuery("span[id='prejudge_waiting']").hide();
// jQuery("span[id='prejudge_status']").html(result);
// jQuery("span[id='prejudge_status']").show();
// return;
// }
// }
// }); // jQuery ajax
// }, 1000);
// }
// });
// }

// function doRejudge(problemid){
// var it;
// jQuery.ajax({
// type: "GET",
// url: "./ReJudge.ajax",
// data: "problemid=" + problemid,
// async: false,
// timeout: 5000,
// beforeSend: function(){
// jQuery("span[id='rejudge_waiting']").show();
// jQuery("span[id='img_rejudge']").hide();
// },
// success: function(result){
// it = jQuery.interval(function(){
// jQuery.ajax({
// type: "GET",
// url: "Problem.api",
// data: "action=getWaitingRejudgeSize&problemid=" + problemid,
// async: false,
// cache: false,
// timeout: 5000,
// beforeSend: function(){
// jQuery("span[id='rejudge_waiting']").show();
// jQuery("span[id='img_rejudge']").hide();
// },
// success: function(result){
// if (result != "0") {
// }
// else {
// jQuery.clear(it);
// jQuery("span[id='rejudge_waiting']").hide();
// jQuery("span[id='img_rejudge']").show();
// // return;
// }
// }
// }); // jQuery ajax
// }, 1000);
// }
// }); // jQuery ajax;
// }

function doCheckProblem() {
	event.preventDefault();
	alert($('#form').serialize());
	jQuery.ajax({
		type : "GET",
		url : "CheckProblem.api",
		data : "action=Insert&" + $('#form').serialize(),
		dataType : "json",
		async : false,
		timeout : 5000,
		success : function(result) {
			if (result.length == 0) {
				$("#form").submit();
			} else {
				$("div[id='check_dialog']").dialog({
					position : [ 'top', 50 ],
					width : 350,
					resizable : true,
					modal : true,
					buttons : {
						"確定" : function() {
							$(this).dialog("close");
						}
					}
				});
				var dialog = $("#check_dialog");
				dialog.text("");
				$.each(result, function(index) {
					// alert(result[index]);
					dialog.append(result[index] + "<br>");
				});
			}
		}
	});

}
