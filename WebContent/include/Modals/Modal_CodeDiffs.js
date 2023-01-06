jQuery(document).ready(function() {
	jQuery("button[id='getCodeDiffs']").on('click', function() {
		//event.preventDefault();
		var solutionid = $(this).data("solutionid");
		var modal = $(this).parent().find('#Modal_CodeDiffs_' + solutionid);
		console.log("call getCodeDiffs, solutionid=" + solutionid);
		var tbody = modal.find("tbody");
		jQuery.ajax({
			type: "POST",
			url: "./Solution.api",
			data: "action=getCodeDiffs&solutionid=" + solutionid,
			cache: false,
			timeout: 5000,
			success: function(html) {
				console.log("success: html=" + html);
				//tbody.find("tr").remove();
				tbody.empty();
				tbody.append(html);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					BootstrapDialog.alert(alert.title);
				} catch (err) {
					console.log("ContestToolbar.js err=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});
});
