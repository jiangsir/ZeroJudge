jQuery(document).ready(function() {
	$(".hoverDiv").hover(function() {
		bcolor = $(".table tbody tr:hover").css("background-color")
		$(this).css("background", bcolor);
	}, function() {
		var bcolor = $(".table tbody tr").css("background-color");
		$(this).css("background", bcolor);
	});

	$("#sortable").sortable({
		revert : true
	});
	$("#draggable").draggable({
		connectToSortable : "#sortable",
		helper : "clone",
		revert : "invalid"
	});
	$("ul, li").disableSelection();

	$("div#sortable").sortable({
		revert : true
	});
	/*
	 * $( "#addNewTemplateContest" ).draggable({ connectToSortable:
	 * "#TemplateContestForm", helper: "clone", revert: "invalid" });
	 */
	$("div, span").disableSelection();

	jQuery("input[name='TemplateContestProblemids']").blur(function() {
		getContestProblemTitles($(this));
	});

	jQuery("button[id='addNewTemplateContest']").on('click', function() {
		console.log('ssssccccc');
		var tcform = $(this).closest("form");
		console.log('count=' + tcform.find("span.count").length);
		var last_tcontest = tcform.find("div[id='TemplateContestForm']:last");
		var clone = last_tcontest.clone(true);
		clone.find("#contest_problems").text("");
		clone.find("span.count").html(tcform.find("span.count").length + 1)
		var all_Inputs = clone.find("input, textarea");
		all_Inputs.val("");
		clone.insertAfter(last_tcontest);

	});

	jQuery("button[id='deleteTemplateContest']").click(function() {
		if (jQuery("div[id='TemplateContestForm']").length > 1) {
			var dd = $(this).closest("div[id='TemplateContestForm']");
			dd.remove();
		}
	});

	$("button[id='save']").bind("click", function(e) {
		e.preventDefault();

		var form = $(this).closest("form");
		// var action = form.attr("action");
		// var action = window.location.pathname.split('/').pop();
		// console.log("action=" + action);
		console.log("form=" + form.serialize());
		// 有其他 formdata 無 <form> 寫法。
		// http://www.jianshu.com/p/46e6e03a0d53
		jQuery.ajax({
			type : "POST",
			url : 'EditVClassTemplate',
			// data:
			// $('#form').serialize()+"&picture="+$('input[type="file"]').val(),
			cache : false,
			// data : new FormData(form[0]),
			data : form.serialize(),
			// processData : false,
			// contentType : false,
			// async : true,
			timeout : 5000,
			success : function(result) {
				console.log("result=" + result);
				window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				// BootstrapDialog.alert(result);
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
});

function getContestProblemTitles(problemids) {
	var dd = problemids.closest("div[id='TemplateContestForm']");
	var div = dd.find("div#contest_problems");
	div.html("Loading...");
	console.log("problemids=" + problemids.val());
	jQuery.ajax({
		type : "POST",
		url : "./Problem.api",
		data : "action=getProblemTitles&problemids=" + problemids.val(),
		async : false,
		timeout : 5000,
		success : function(result) {
			div.html(result);
		}
	});
}
