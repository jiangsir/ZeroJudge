jQuery(document).ready(function() {
	var url = document.location.toString();
	console.log("url=" + url);
	if (url.match('#')) {
		console.log("url.split('#')[1]=" + url.split('#')[1]);
		console.log('.nav-tabs a[aria-controls="' + url.split('#')[1] + '"]');
		// $('.nav-tabs').find('[aria-controls="' + url.split('#')[1] +
		// '"]').tab('show');
		$('.nav-tabs a[aria-controls="' + url.split('#')[1] + '"]').tab('show');
	}

	jQuery("a[id='readIM']").click(function() {
		event.preventDefault(); // 讓預設的動作失效！
		var imessageid = $(this).attr("imessageid");
		doRead(imessageid);
	});

	jQuery("td[id='subject']").click(function() {
		// alert("subject");
		// var trindex = $(this).cloest("tr").index();
		// alert(trindex);
		// var nextindex = trindex + 1;
		// jQuery('tr:eq(' + nextindex + ') pre').toggle("slow");
		var imessageid = $(this).attr("imessageid");
		jQuery("pre[imessageid='" + imessageid + "']").toggle("slow");
		doRead(imessageid);
	});

	$("button[data-target='#Modal_SendIMessage']").on('click', function() {
		var modal = $('#Modal_SendIMessage');
		modal.find("input[name=to]").val($(this).data("receiver"));
		modal.find("input[name=subject]").val($(this).data("subject"));
		modal.find("textarea[name=content]").val($(this).data("content"));
		modal.modal("toggle");
	});

	$('button#modal_sendIMessage').on('click', function() {
		// var modal = $("#Modal_InsertVClass");
		var modal = $(this).closest('.modal');
		var action = $(this).data("action");
		console.log("action=" + action);
		jQuery.ajax({
			type : "POST",
			url : action,
			data : modal.find('form').serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
				// modal.removeData();
				// location.reload();
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

function doRead(imessageid) {
	jQuery.ajax({
		type : "POST",
		url : "IMessage.api",
		data : "action=doRead&imessageid=" + imessageid,
		async : false,
		timeout : 5000,
		success : function(result) {
			jQuery("img[imessageid='" + imessageid + "']").attr("src", "images/IMessage_readed.svg");
		}
	});
}