/**
 * 
 */
jQuery(document).ready(function() {
	$('button[id="doJoinContestByOnlineUser"]').on('click',	function() {
		var contestid = $(this).data('contestid');
		jQuery.ajax({
			type : "POST",
			url : "./Contest.api",
			data : "action=doJoinContestByOnlineUser&contestid="+ contestid,
			timeout : 5000,
			success : function(result) {
				//console.log("returnPage=" + result);
				//returnPage = jQuery.parseJSON(result);
				//window.location.href = returnPage.currentPage;
				//window.location.href = document.referrer;
				window.location.href = "./ShowContest?contestid="+contestid;
		    },
			error : function(jqXHR, textStatus,	errorThrown) {
				console.log("jqXHR.responseText="+ jqXHR.responseText);
				console.log("errorThrown="+ errorThrown);
				console.log("textStatus="+ textStatus);
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
						} ]});
					} catch (err) {
						BootstrapDialog.alert(errorThrown);
					}
				}
			});
		});
});
