jQuery(document).ready(function () {
	do_NewThread();
});

function do_NewThread() {
	$("input[type='submit']").bind("click", function (e) {
		e.preventDefault();
		var form = $(this).closest("form");
		var tinytextarea_Content = tinymce.get("content").getContent();
		form.find('#content').val(tinytextarea_Content);
		console.log("form=" + form.serialize() + "&content=" + tinytextarea_Content);
		console.log("new FormData(form[0])=" + new FormData(form[0]));
		jQuery.ajax({
			type: "POST",
			url: "./NewThread",
			data: form.serialize(),
			//data: new FormData(form[0]),
			//timeout: 5000,
			success: function (result) {
				//json = jQuery.parseJSON(result);
				BootstrapDialog.alert('儲存完成！', function () {
					window.location.href = document.referrer; // 跳轉到前一頁
				});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					BootstrapDialog.alert(alert.title);
				} catch (error) {
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});
}
