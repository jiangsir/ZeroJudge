jQuery(document).ready(function() {
	first();
	starttime();
	totalscore();
	totalproblem();

	// getContestProblemTitles(jQuery("input[name='problemids']").val());
	jQuery("input[name='problemids']").keyup(function() {
		totalproblem();
//		problemids = jQuery("input[name='problemids']").val().split(',');
//		var scores = new Array(problemids.length) ;
//		for(var i =0; i<scores.length; i++){
//			scores[i] = Math.floor(100/scores.length) + (i<100%scores.length);
//		}
//		jQuery("input[name=scores]").val(scores.join(', '));
		
		getContestProblemTitles($(this).val());
	});

	jQuery("input[name=scores]").keyup(function(){
		totalscore();
	});

	
	jQuery("#doaveragescore").click(function(){
		doAverageScore();
		totalscore();
	});

	jQuery("#doscore100").click(function(){
		doScore100();
		totalscore();
	});

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
				// BootstrapDialog.alert("儲存完成！");
				BootstrapDialog.alert('儲存完成！', function(){
		            window.location.href = document.referrer; // 跳轉到前一頁，並
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
			}
		});

	});

	// var config = $("span[id='contestconfig']").attr("contestconfig");

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
		type : "POST",
		url : "./Problem.api",
		data : "action=getProblemTitles&problemids=" + problemids,
		async : false,
		timeout : 5000,
		success : function(result) {
			div.html(result);
		}
	});
}

/**
 * 平均配分
 * @returns
 */
function doAverageScore(){
	problemids = jQuery("input[name='problemids']").val().split(',');
	var scores = new Array(problemids.length) ;
	for(var i =0; i<scores.length; i++){
		scores[i] = Math.floor(100/scores.length) + (i<100%scores.length);
	}
	jQuery("input[name=scores]").val(scores.join(', '));
}

/**
 * 每一題都配 100
 * @returns
 */
function doScore100(){
	problemids = jQuery("input[name='problemids']").val().split(',');
	var scores = new Array(problemids.length) ;
	for(var i =0; i<scores.length; i++){
		scores[i] = 100;
	}
	jQuery("input[name=scores]").val(scores.join(', '));
}

function totalproblem(){
	problemids = jQuery("input[name='problemids']").val().split(',');
	jQuery('#totalproblem').text(problemids.length);
}

function totalscore(){
	var scores = jQuery("input[name=scores]").val().split(',');
	var total = 0;
	for(const score of scores){
		total += parseInt(score);
	}
	console.log("total="+total);
	jQuery("#totalscore").text(total);
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
