jQuery(document).ready(function() {
	jQuery("button[id='btn_ContestSettings']").click(function() {
		event.preventDefault();
		var contestid = $(this).data("contestid");
		var modal = $('#Modal_ShowContestSettings_' + contestid);
		console.log("contestid=" + contestid);
		jQuery.ajax({
			type : "GET",
			url : "./Contest.json",
			data : "data=Settings&contestid=" + contestid,
			dataType : 'json',
			cache : false,
			timeout : 5000,
			success : function(data) {
				console.log("success data=" + JSON.stringify(data));
				modal.find("#JSONSettings").html(JSON.stringify(data));
				// modal.modal('toggle');
				// $('pre#code').each(function(i, block) {
				// hljs.highlightBlock(block);
				// });
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

	$('button#Modal_InsertStudents_confirm').on('click', function() {
		var modal = $(this).closest('.modal');
		// var action = $(this).data("action");
		// console.log("form:" +
		// modal.find('form').serialize());
		do_InsertStudents(modal);
	});

	// VContest sortable 拖曳排序
	$("#sortable").sortable({
		opacity : 0.7,
		delay : 150,
		cursor : 'move',
		revert : true,
		stop : function() {
			// 記錄sort後的id順序陣列
//			var arr = $("#sortable").sortable('toArray');
//			var vclassid = $(this).data('vclassid');
//			console.log(arr);
//			console.log(arr.toString());
//			do_SaveVContestSortable(vclassid, arr);
		}
	});
	$("#draggable").draggable({
		connectToSortable : "#sortable",
		helper : "clone",
		revert : "invalid"
	});
	$("ul, li").disableSelection();

	// jQuery("button[id='show_ContestProblems_TypeB']").click(function() {
	// console.log("show typeB");
	// var tbody = $(this).closest("tbody");
	// console.log(tbody);
	// typeB = tbody.find("tr#ContestProblems_TypeB");
	// if (typeB.is(':visible')) {
	// typeB.hide();
	// } else {
	// typeB.show();
	// }
	// });

	jQuery("button[id='show_ContestProblems_TypeB']").click(function() {
		console.log("show typeB");
		var tbody = $(this).closest("tbody"); // 加回 tbody
		console.log(tbody);
		var vclassid = $(this).data("vclassid");
		var userid = $(this).data("userid");
		var td = tbody.find("#ContestProblems_TypeB").find("td");
		var result = showContestProblems_TypeB(vclassid, userid, td);

		typeB = tbody.find("tr#ContestProblems_TypeB");
		if (typeB.is(':visible')) {
			typeB.hide();
		} else {
			typeB.show();
		}
	});

	jQuery("button[id='showall_ContestProblems_TypeB']").click(function() {
		// var table = $().closest("table");
		// console.log(table);
		// xxxx typeB = table.find("tr#ContestProblems_TypeB");
		var typeBs = $(document).find("tr[id='ContestProblems_TypeB']")
		console.log(typeBs.length);
		typeBs.each(function() {
			if ($(this).is(':visible')) {
				$(this).hide();
			} else {
				$(this).show();
			}
		});

	});

	jQuery("button[id='RemoveStudent']").click(function() {
		var title = $(this).attr('title');
		var button = $(this);
		BootstrapDialog.confirm('確定' + title + '?', function(result) {
			if (result) {
				do_removeStudent(button);
			}
		});
	});

	jQuery("button[id='ModalSubmit_InsertVContest']").click(function() {
		var modal = $(this).closest('.modal');
		var form = modal.find('form');
		do_insertVContest(form);
	});

	jQuery("button[id='save_SortableVContest']").click(function() {
		var vclassid = $(this).data("vclassid");
		var arr = $("#sortable").sortable('toArray');
		do_SaveVContestSortable(vclassid, arr);
	});

	// jQuery("button[id='InsertVContest']").click(function() {
	// var vclassid = $(this).data("vclassid");
	// do_insertVContest(vclassid);
	// });

	jQuery("button[id='cloneVContestById']").click(function() {
		var vclassid = $(this).data("vclassid");
		var contestid = $(this).data("contestid");
		do_cloneVContestById(vclassid, contestid);
	});

	jQuery("button[id='removeVContest']").click(function() {
		var title = $(this).attr('title');
		var contestid = $(this).data("contestid");
		BootstrapDialog.confirm('確定' + title + '?', function(result) {
			if (result) {
				do_removeVContest(contestid);
			}
		});
	});

});

function showContestProblems_TypeB(vclassid, userid, td) {
	console.log("vclassid=" + vclassid + ", userid=" + userid)
	jQuery.ajax({
		type : "POST",
		url : "./Vclass.api",
		data : "action=getContestProblems_TypeB&vclassid=" + vclassid + "&userid=" + userid,
		timeout : 5000,
		success : function(result) {
			console.log(result)
			// location.reload();
			td.html(result);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error!!!');
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			// alert = jQuery.parseJSON(jqXHR.responseText);
			// BootstrapDialog.alert(alert.title);
		}
	});
}

function do_removeVContest(contestid) {
	jQuery.ajax({
		type : "POST",
		url : "./Vclass.api",
		data : "action=removeVContest&contestid=" + contestid,
		timeout : 5000,
		success : function(result) {
			console.log(result)
			location.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error!!!');
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			alert = jQuery.parseJSON(jqXHR.responseText);
			BootstrapDialog.alert(alert.title);
		}
	});
}

function do_cloneVContestById(vclassid, contestid) {
	jQuery.ajax({
		type : "POST",
		url : "./Vclass.api",
		data : "action=cloneVContestById&vclassid=" + vclassid + "&contestid=" + contestid,
		timeout : 5000,
		success : function(result) {
			location.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error!!!');
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);

			alert = jQuery.parseJSON(jqXHR.responseText);
			BootstrapDialog.alert(alert.title);
		}
	});
}

function do_insertVContest(form) {
	console.log("form=" + form.serialize());

	jQuery.ajax({
		type : "POST",
		url : "./Vclass.api",
		data : "action=InsertVContestByJson&" + form.serialize(),
		timeout : 5000,
		success : function(result) {
			location.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error!!!');
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			// try {
			alert = jQuery.parseJSON(jqXHR.responseText);
			BootstrapDialog.alert(alert.title);
		}
	});
}

function do_removeStudent(button) {
	jQuery.ajax({
		type : "POST",
		url : "./Vclass.api?action=RemoveStudent",
		data : "vclassid=" + button.data("vclassid") + "&userid=" + button.data("userid"),
		timeout : 5000,
		success : function(result) {
			location.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			// try {
			alert = jQuery.parseJSON(jqXHR.responseText);
			BootstrapDialog.alert(alert.title);
		}
	});
}

function do_InsertStudents(modal) {
	jQuery.ajax({
		type : "POST",
		url : "InsertStudents",
		data : modal.find('form').serialize(),
		// async : false,
		timeout : 5000,
		success : function(result) {
			modal.modal('hide');
			location.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			alert = jQuery.parseJSON(jqXHR.responseText);
			BootstrapDialog.alert(alert.title);
		}
	});
}

function do_SaveVContestSortable(vclassid, arr) {
	console.log(arr.toString());

	jQuery.ajax({
		type : "POST",
		url : "./Vclass.api",
		data : "action=SaveVContestSortable&vclassid=" + vclassid + "+&vcontestids=" + arr.toString(),
		timeout : 5000,
		success : function(result) {
			location.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error!!!');
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			console.log("errorThrown=" + errorThrown);
			console.log("textStatus=" + textStatus);
			// try {
			alert = jQuery.parseJSON(jqXHR.responseText);
			BootstrapDialog.alert(alert.title);
		}
	});
}