	jQuery(document).on("click", "button[id='btn_SolutionCode']", function(event) {
		event.preventDefault();
		var solutionid = $(this).data("solutionid");
		var modal = $('#Modal_ShowSolutionCode_'+solutionid)
		console.log("solutionid=" + solutionid);
		jQuery.ajax({
			type : "GET",
			url : "./Solution.json",
			data : "data=Code&solutionid=" + solutionid,
			dataType : 'json',
			cache : false,
			timeout : 5000,
			success : function(data) {
				modal.find("#solutionid").html(data.id);
				modal.find("#code").html(data.code);
				//modal.modal('toggle');
				$('pre#code').each(function(i, block) {
				    hljs.highlightBlock(block);
				});
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
