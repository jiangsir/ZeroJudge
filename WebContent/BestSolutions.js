// JavaScript Document
jQuery(document).ready(function() {

	var index;
	var solutionid;
	jQuery("img[id='openlock']").bind('click', function() {
		index = jQuery("img[id='openlock']").index(this);
		solutionid = jQuery("td[id='solutionid']:eq(" + index + ")").text();
		$.blockUI({
			draggable : true, // draggable
			message : $('#dialog'),
			css : {
				// top: ($(window).height() -
				// 500) / 2 + 'px',
				// left: ($(window).width() -
				// 500) / 2 + 'px',
				top : 20 + '%',
				left : 30 + '%',
				width : '40%'
			}
		});

	});
	$('#yes').click(function() {
		// openlock(solutionid, index);
		// jQuery("form[id='form1']").submit();
		jQuery.ajax({
			type : "POST",
			url : "./Codelock.ajax",
			data : "solutionid=" + solutionid + "&delay=" + jQuery('input[name=delay]:checked').val(),
			async : false,
			timeout : 5000,
			success : function(result) {
				jQuery("img[id='closelock']:eq(" + index + ")").show();
				jQuery("img[id='openlock']:eq(" + index + ")").hide();
			}
		});

		$.unblockUI();
	});

	$('#no').click(function() {
		$.unblockUI();
		return false;
	});

	$('#return1').click(function() {
		$.unblockUI();
		return false;
	});

	$('#return2').click(function() {
		$.unblockUI();
		return false;
	});

//	jQuery("img[id='closelock']").bind('click', function() {
//		var index = jQuery("img[id='closelock']").index(this);
//		var solutionid = jQuery("td[id='solutionid']:eq(" + index + ")").text();
//		closelock(solutionid, index);
//	});

});

//function closelock(solutionid, index) {
//	jQuery.ajax({
//		type : "GET",
//		url : "./Codelock.ajax",
//		data : "solutionid=" + solutionid,
//		async : false,
//		timeout : 5000,
//		success : function(result) {
//			jQuery("img[id='closelock']:eq(" + index + ")").hide();
//			jQuery("img[id='openlock']:eq(" + index + ")").show();
//		}
//	});
//}

//function getCodelocker() {
//	jQuery.ajax({
//		type : "POST",
//		url : "./Solution.api",
//		data : "action=getImgCodelocker&solutionid=" + solutionid,
//		async : false,
//		timeout : 5000,
//		success : function(result) {
//			jQuery("span[id='codelocker']:eq(" + index + ")").html(result);
//		}
//	});
//}
