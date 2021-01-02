jQuery(document).ready(function() {
	$("input[name='tabid']").each(function() {
		if ($(this).val() == $(this).data("tabid")) {
			$(this).prop("checked", true);
		}
	});
	$("input[name='judgemode']").each(function() {
		if ($(this).val() == $(this).data("judgemode")) {
			$(this).prop("checked", true);
		}
	});
	$("input[name='wa_visible']").each(function() {
		if ($(this).val() == $(this).data("wa_visible")) {
			$(this).prop("checked", true);
		}
	});

});
