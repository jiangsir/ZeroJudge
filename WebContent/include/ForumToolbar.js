jQuery(document).ready(function () {
	$("select[name=articletype]").each(function () {
		var articletype = $(this).data("articletype");
		$(this).children().each(function () {
			//console.log(articletype + " " + $(this).val())
			if (articletype == $(this).val()) {
				$(this).attr("selected", "true");
				return;
			}
		});
	});
	$("select[name=articlehidden]").each(function () {
		var articlehidden = $(this).data("articlehidden");
		$(this).children().each(function () {
			//console.log(articlehidden + " " + $(this).val())
			if (articlehidden == $(this).val()) {
				$(this).attr("selected", "true");
				return;
			}
		});
	});

	$("select[name=articletype]").change(function () { // 事件發生
		var articleid = $(this).data("articleid");
		jQuery('select[name=articletype] option:selected').each(function () { // 印出選到多個值
			if (articleid == $(this).parent().data("articleid")) {
				setArticleType(articleid, $(this).val());
			}
		});
		location.reload();
	});

	$("select[name=articlehidden]").change(function () { // 事件發生
		var articleid = $(this).data("articleid");
		jQuery('select[name=articlehidden] option:selected').each(function () { // 印出選到多個值
			if (articleid == $(this).parent().data("articleid")) {
				setArticleHidden(articleid, $(this).val());
			}
		});
		location.reload();
	});

});

function setArticleType(articleid, articletype) {
	console.log(articleid + ' setArticleType = ' + articletype)
	jQuery.ajax({
		type: "POST",
		url: "./Article.api",
		data: "action=setType&postid=" + articleid + "&articletype=" + articletype,
		async: false,
		timeout: 5000,
		success: function (result) {
		}
	});
}

function setArticleHidden(articleid, articlehidden) {
	console.log(articleid + ' setArticleHidden = ' + articlehidden)
	jQuery.ajax({
		type: "POST",
		url: "./Article.api",
		data: "action=setHidden&postid=" + articleid + "&articlehidden=" + articlehidden,
		async: false,
		timeout: 5000,
		success: function (result) {
		}
	});
}
