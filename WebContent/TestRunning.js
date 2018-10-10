jQuery(document).ready(function() {
	jQuery("input[name='language']").each(function() {
		// 字串比對的時候，.val() 必須跟 .val() 相比才會相等。
		if ( $("input[name='userlanguage']").val() == $(this).val() ) {
		  $(this).attr("checked", true);
		}
  	});
	jQuery("img[id='execute']").bind('click', function() {
		// var code = jQuery("testarea[name='code']").text();
			// 用 .val() 可以讀到已經打上去，但是還沒有送出的 textarea 內容
			var code = jQuery("#code").val();
			var indata = jQuery("#indata").val();
			var language = jQuery("input[name='language']:checked").val();
			doExecute(code, indata, language);
	});
});

function doExecute(code, indata, language) {
	// 處理 HTML 特殊符號 如 
	code = encodeURIComponent(code);
	jQuery.ajax( {
		type : "POST",
		url : "TestRunning",
		data : "code=" + code + "&indata=" + indata + "&language=" + language,
		// 注意：這裡 async 必須為 true, 否則一切動作都會在 ajax 後進行。即使寫在前面也一樣。
		async : true,
		timeout : 5000,
		beforeSend : function() {
			jQuery("#execute").hide();
			jQuery("#waiting").show();
			jQuery("pre[id='outdata']").html("");
		},
		success : function(result) {
			jQuery("#execute").show();
			jQuery("#waiting").hide();
			jQuery("pre[id='outdata']").html(result);
		}
	});
}