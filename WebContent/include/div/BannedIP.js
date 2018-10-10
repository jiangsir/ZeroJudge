jQuery(document).ready(function() {
	jQuery("img[id='bannedIP']").bind('click', function() {
		// var index = jQuery("span[id='img_ipdeny']").index(this);
		// var ip = jQuery("span[id='img_ipdeny']:eq(" + index +
		// ")").attr('name');
		var ip = $(this).attr("ip");
		if (!confirm("封鎖 " + ip)) {
			return;
		}
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=setIpdeny&ip=" + ip,
			async : false,
			timeout : 5000,
			success : function(result) {
				location.reload();
			}
		});
	});

	jQuery("img[id='unbannedIP']").bind('click', function() {
		var ip = $(this).attr("ip");
		if (!confirm("解鎖 " + ip)) {
			return;
		}
		// var index = jQuery("span[id='img_ipallow']").index(this);
		// var ip = jQuery("span[id='img_ipallow']:eq(" + index +
		// ")").attr('name');
		// if (!confirm("ipallow=" + ip)) {
		// return;
		// }
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=setIpallow&ip=" + ip,
			async : false,
			timeout : 5000,
			success : function(result) {
				location.reload();
			}
		});
	});

});
