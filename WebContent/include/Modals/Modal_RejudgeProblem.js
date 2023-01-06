jQuery(document).ready(function () {
	jQuery("[name='rejudge_checkbox']").click(function () {
		var problemid = $(this).closest("form").data("problemid");
		console.log("this.id:" + this.id + ", problemid=" + problemid);

		if (this.id == "rejudge_ALL") {
			if ($(this).is(":checked")) {
				$("[name='rejudge_checkbox']:not(#rejudge_ALL)").prop("disabled", true).prop("checked", false);
			} else {
				$("[name='rejudge_checkbox']:not(#rejudge_ALL)").prop("disabled", false).prop("checked", false);
				$("#SubmissionCount").html(count);
			}
		}
		var judgements = [];
		$("[name='rejudge_checkbox']:checked").each(function () {
			judgements.push($(this).val());
		});
		console.log("judgements=" + judgements);
		$("#judgements").text(judgements);

		var count = 0;
		if (judgements.indexOf("ALL") >= 0) {
			console.log("judgements=全部");
			count = countRejudgeCheckbox_ALL(problemid);
		} else {
			console.log("judgements=" + judgements);
			count = countRejudgeCheckbox(problemid, judgements);
			console.log("count=" + count);
		}
		$("#SubmissionCount").html(count);
	});

});

function countRejudgeCheckbox(problemid, checked_judgements) {
	console.log("checked_judgements=" + checked_judgements);
	var returnCount = "0";
	jQuery.ajax({
		type: "GET",
		url: "Problem.json",
		data: "data=RejudgeCountByJudgements&problemid=" + problemid + "&judgements=" + checked_judgements,
		dataType: 'json',
		async: false,
		cache: false,
		timeout: 5000,
		success: function (result) {
			console.log("result=" + result);
			returnCount = result;
		}
	}); // jQuery ajax
	return returnCount;
}

function countRejudgeCheckbox_ALL(problemid) {
	var returnCount = "0";
	jQuery.ajax({
		type: "GET",
		url: "Problem.json",
		data: "data=RejudgeCountByJudgements&problemid=" + problemid,
		dataType: 'json',
		async: false,
		cache: false,
		timeout: 5000,
		success: function (result) {
			returnCount = result;
			console.log("returnCount=" + returnCount);
		}
	}); // jQuery ajax
	return returnCount;
}
