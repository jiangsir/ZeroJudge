jQuery(document).ready(function () {
	$('button#modal_applyTeacher_submit').on('click', function () {
		var modal = $(this).closest('.modal');
		var action = $(this).data('action');
		console.log("action=" + action);
		console.log("form=" + modal.find("form").serialize());

		jQuery.ajax({
			type: "POST",
			url: "./Application?action=" + action,
			//data: modal.find("form").serialize(),
			// timeout : 5000,
			success: function (result) {
				modal.modal('hide');
				location.reload();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					console.log("^^^^^^^^^ "+jqXHR.responseText);
					BootstrapDialog.alert(alert.title, function () {
						console.log("error Json alert!");
						// btn.prop('disabled', false);
						// btn.html(btnhtml);
						modal.modal('hide');
						location.reload();
					});
				} catch (jsonerr) {
					BootstrapDialog.alert(errorThrown, function () {
						console.log("error catch err!");
						// btn.prop('disabled', false);
						// btn.html(btnhtml);
						modal.modal('hide');
						location.reload();
					});
				}
			}
		});
	});


	$('button#modal_applyProblemManager_submit').on('click', function () {
		var modal = $(this).closest('.modal');
		var action = $(this).data('action');
		console.log("action=" + action);

		jQuery.ajax({
			type: "POST",
			url: "./Application?action=" + action,
			//data: modal.find("form").serialize(),
			// timeout : 5000,
			success: function (result) {
				modal.modal('hide');
				location.reload();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					console.log("!!!!!!!!! "+jqXHR.responseText);
					BootstrapDialog.alert(alert.title, function () {
						console.log("error Json alert!");
						// btn.prop('disabled', false);
						// btn.html(btnhtml);
						modal.modal('hide');
						location.reload();
					});
				} catch (jsonerr) {
					BootstrapDialog.alert(errorThrown, function () {
						console.log("error catch err!");
						// btn.prop('disabled', false);
						// btn.html(btnhtml);
						modal.modal('hide');
						location.reload();
					});
				}
			}
		});
	});

});
