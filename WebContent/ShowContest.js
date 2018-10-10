jQuery(document).ready(function() {
	$('.modal #button_ContestLogin').on('click', function() {
		console.log("login");
		var modal = $(this).closest('.modal');
		var account = modal.find('[name=account]').val();
		var password = modal.find('[name=password]').val();
		console.log(modal.find('form').serialize());
		jQuery.ajax({
			type : "POST",
			url : "./Contest.api",
			data : "action=doJoinContest&" + modal.find('form').serialize(),
			timeout : 5000,
			success : function(result) {
				console.log(result);
				modal.modal('hide');
				location.reload();
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

function sleep(milliseconds) {
	var start = new Date().getTime();
	for (var i = 0; i < 1e7; i++) {
		if ((new Date().getTime() - start) > milliseconds) {
			break;
		}
	}
}

function countdown(countdown) {
	var secs = (countdown - (countdown % 1000)) / 1000;
	if (secs <= 0) {
		return;
	}

	jQuery("#countdown").text(showstr(secs));
	var down = setInterval(function() {
		secs = secs - 1;
		if (secs <= 0) {
			// alert(contestStatus);
			clearInterval(down);
			sleep(1000);
			location.reload();
		}

		var str = showstr(secs);
		jQuery("#countdown").text(str);
	}, 1000);
}
