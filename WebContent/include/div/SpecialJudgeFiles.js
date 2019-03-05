jQuery(document).ready(function() {
	jQuery("input[name='uploadSpecialJudgeFiles']").click(function() {
		this.closest("form").submit();
	});

	var problemid = jQuery("span[name=problemid]").text();
	console.log(problemid);
	getSpecialFiles(problemid);

});
function getSpecialFiles(problemid) {
	// jquery bind click after ajax call
	var SpecialJudgeFiles = jQuery("div#SpecialJudgeFiles");
	var problemid = SpecialJudgeFiles.data("problemid");
	jQuery.ajax({
		type : "GET",
		url : "Problem.api",
		data : "action=getSpecialJudgeFiles&problemid=" + problemid,
		async : false,
		cache : false,
		timeout : 5000,
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("jqXHR.responseText=" + jqXHR.responseText);
			var alert = jQuery.parseJSON(jqXHR.responseText);
			SpecialJudgeFiles.html(alert.title);
		},
		success : function(result) {
			SpecialJudgeFiles.html(result);
			SpecialJudgeFiles.on('click', 'span[id=deleteSpecialFile]', function() {
				console.log("deleteSpecialFile: problemid="+problemid+", index="+index);
				var index = jQuery("span[id=deleteSpecialFile]").index(this);
				jQuery.ajax({
					type : "POST",
					url : "Problem.api",
					data : "action=deleteSpecialFile&problemid=" + problemid + "&index=" + index,
					async : false,
					cache : false,
					timeout : 5000,
					success : function(result) {
						getSpecialFiles(problemid);
					}
				}); // jQuery ajax
			});
		}
	}); // jQuery ajax
}