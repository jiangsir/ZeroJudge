function showDetails(solutionid) {

	jQuery.ajax({
		type : "POST",
		url : "./Solution.api",
		data : "action=getServerOutputBeans&solutionid=" + solutionid,
		async : false,
		timeout : 5000,
		success : function(result) {
			// jQuery("span[id=result]").text(result);
			// alert(jQuery("textarea[id=detail]").length);
			while (jQuery("div[id=jsondetail]").length > 1) {
				jQuery("div[id=jsondetail]:last").remove();
			}
			// alert(result);
			ShowServerOutputBeans(result);
		}
	});
}

function ShowServerOutputBeans(result) {
	var details = JSON.parse(result);
	if (details.length == 0) {
		jQuery("div[id=jsondetail]").hide();
	} else if (details.length == 1) {
		jQuery("div[id=jsondetail]:last span[id=line1]").hide();
	} else {
		jQuery("div[id=jsondetail]:last span[id=line1]").show();
	}

	for ( var i = 0; i < details.length && details[i] != null; i++) {
		if (jQuery("div[id=jsondetail]").length < i + 1) {
			jQuery("div[id=jsondetail]:last").clone(true).insertAfter(
					jQuery("div[id=jsondetail]:last"));
		}
		jQuery("div[id=jsondetail]:last").show();
		jQuery("div[id=jsondetail]:last span[id=testlength]").text(i + 1);
		jQuery("div[id=jsondetail]:last span[id=score]").text(
				details[i].score + "%");

		if (details[i].judgement == "AC") {
			jQuery("div[id=jsondetail]:last span[id=judgement]").addClass(
					"acstyle").text(details[i].judgement);
		} else {
			jQuery("div[id=jsondetail]:last span[id=judgement]").removeClass(
					"acstyle").text(details[i].judgement);
		}
		jQuery("div[id=jsondetail]:last span[id=info]").text(
				"(" + details[i].summary + ")");
		jQuery("div[id=jsondetail]:last span[id=reason]").text(
				details[i].reasontext);
		jQuery("div[id=jsondetail]:last pre[id=hint]").text(details[i].hint);
	}

}
