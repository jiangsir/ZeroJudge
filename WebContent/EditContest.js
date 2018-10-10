jQuery(document).ready(function() {
	first();
	starttime();
	getContestProblemTitles(jQuery("input[name='problemids']").val());

	/*
	 * var x = ${contest.config};
	 * jQuery("input[name='contestconfig']").each(function() { if(x%2){
	 * $(this).attr("checked", true); } x = (x-x%2)/2; });
	 */

	/*
	 * jQuery("input[name='send']").click(function() { // 事件發生 var problemids =
	 * $(this).closest("form").find("input[name='problemids']").val(); var
	 * scores = $(this).closest("form").find("input[name='scores']").val(); if
	 * (problemids.split(",").length != scores.split(",").length) {
	 * alert("題目數量與配分數量不相符，請修正。"); } else {
	 * console.log($(this).closest("form").html() + " submit");
	 * $(this).closest("form").submit(); } });
	 */
	$("button[type='submit']").bind("click", function(e) {
		e.preventDefault();

		var form = $(this).closest("form");
		// var action = form.attr("action");
		var action = window.location.pathname.split('/').pop();
		console.log("action=" + action);
		console.log("form=" + form.serialize());
		// 有其他 formdata 無 <form> 寫法。
		// http://www.jianshu.com/p/46e6e03a0d53
		jQuery.ajax({
			type : "POST",
			url : action,
			// data:
			// $('#form').serialize()+"&picture="+$('input[type="file"]').val(),
			cache : false,
			// data : new FormData(form[0]),
			data : form.serialize(),
			// processData : false,
			// contentType : false,
			async : true,
			timeout : 5000,
			success : function(result) {
				console.log("result=" + result);
				// window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				BootstrapDialog.alert("儲存完成！");
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
			}
		});

	});

	//var config = $("span[id='contestconfig']").attr("contestconfig");

	jQuery("input[name='contestconfig']").each(function() {
		var index = parseInt($(this).val());
		var config = $(this).data("contestconfig");
		if (config & (1 << index)) {
			$(this).attr("checked", true);
			config = config - (1 << index);
		}
	});

	jQuery("input[id='config']").each(function() {
		var name = $(this).attr('name');
		var index = parseInt(name.split('_')[1]);
		var config = $(this).data("contestconfig");
		// alert($(this).val());
		if (config & (1 << index) && $(this).val() == "1") {
			$(this).attr("checked", true);
			// config = config -(1<<index);
		} else if ((config & (1 << index)) == 0 && $(this).val() == "0") {
			$(this).attr("checked", true);
		}
	});

	jQuery("input[name='contestconfig']").click(function() {
		if ($(this).attr("checked") == false) {
			jQuery("#BatchRegistContestant").show();
		} else {
			jQuery("#BatchRegistContestant").hide();
		}
	});

	jQuery("input[name='config_${contest.config_AutoRunning}']").click(function() {
		if ($(this).val() == 1) {
			$("#NonAutoRunning").show();
		} else {
			$("#NonAutoRunning").hide();
		}
	});

	jQuery("input[name='contestvisible']").each(function() {
		if ($(this).data("contestvisible") == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("input[name='problemids']").blur(function() {
		getContestProblemTitles($(this).val());
	});

	jQuery("input[name='rankingmode']").each(function() {
		if ($(this).data('rankingmode') == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("input[id='reservation_starttime']").datetimepicker({
		dateFormat : 'yy-mm-dd',
		timeFormat : 'HH:mm:ss'
	});

});

function getContestProblemTitles(problemids) {
	var div = jQuery("div#contest_problems");
	div.html("Loading...");
	jQuery.ajax({
		type : "GET",
		url : "./Problem.api",
		data : "action=getProblemTitles&problemids=" + problemids,
		async : false,
		timeout : 5000,
		success : function(result) {
			div.html(result);
		}
	});
}

function first() {
	jQuery("input[name=starttime]").val(jQuery.trim(jQuery("input[name=starttime]").val()));
	jQuery("input[name=hours]").val(jQuery.trim(jQuery("input[name=hours]").val()));
	jQuery("input[name=mins]").val(jQuery.trim(jQuery("input[name=mins]").val()));
	jQuery("#userrules").val(jQuery.trim(jQuery("#userrules").val()));
}

function starttime() {
	jQuery("#reservation_starttime").focus(function() {
		$("#reservation").attr("checked", true);
	});
	jQuery("#changetime").focus(function() {
		$("#reservation").attr("checked", true);
	});
}

function contains(string, substr, isIgnoreCase) {
	if (isIgnoreCase) {
		string = string.toLowerCase();
		substr = substr.toLowerCase();
	}
	var startChar = substr.substring(0, 1);
	var strLen = substr.length;
	for (var j = 0; j < string.length - strLen + 1; j++) {
		if (string.charAt(j) == startChar)// 如果匹配起始字符,开始查找
		{
			if (string.substring(j, j + strLen) == substr)// 如果从j开始的字符与str匹配，那ok
			{
				return true;
			}
		}
	}
	return false;
}
