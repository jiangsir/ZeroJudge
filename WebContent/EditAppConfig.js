// JavaScript Document
jQuery(document).ready(
		function() {
			$('#myTabs a').click(function(e) {
				e.preventDefault()
				$(this).tab('show')
			});

			getProblemtabs();

			jQuery("span[id='uploadimage']").click(function() {
				var $dialog = $("#upload_dialog").dialog({
					autoOpen : false,
					width : '60%',
					title : 'Upload Image',
					buttons : {
						"取消" : function() {
							$(this).dialog("close");
						},
						"上傳" : function() {
							$(this).dialog("close");
							UploadImage();
						}
					}
				});
				$dialog.dialog('open');
				return false;
			});

			jQuery("button[id='readServerConfig']").click(function() {
				console.log("readServerConfig!");
				jQuery.ajax({
					type : "GET",
					url : "./Debugger.api",
					data : "action=readServerConfig",
					async : false,
					timeout : 5000,
					success : function(result) {
						console.log("read ServerConfig. \n" + result);
						alert("read ServerConfig 完成。");
					}
				});
			});

			jQuery("span[id='deleteHttpscrt']").click(function() {
				jQuery.ajax({
					type : "GET",
					url : "./Debugger.api",
					data : "action=deleteHttpscrt",
					async : false,
					timeout : 5000,
					success : function(result) {
						location.reload();
					}
				});
			});

			$("button[id='checkServerConfig']").on("click", function() {
				// var step1 =
				// checkServerConnection_AllowedIP($("span[id='checkAllowedIP']"),
				// function(alert, status, result) {
				// console.log("result=" + result);
				// alert.removeClass(alert.attr('class').split('
				// ').pop()).addClass("alert-success");
				// status.removeClass(status.attr('class').split('
				// ').pop()).addClass("glyphicon-ok");
				//
				// });
				// console.log("STEP1=" + typeof (step1) + ", step1=" + step1);
				checkServerConnection_AllowedIP($("span[id='checkAllowedIP']"))
				checkServerConnection_ReadServerConfig($("span[id='checkReadServerConfig']"))
				checkServerConnection_Nopassrsync($("span[id='checkNopassRsync']"))
				checkServerConnection_Locker($("span[id='checkLocker']"));
				// if
				// (checkServerConnection_AllowedIP($("span[id='checkAllowedIP']")))
				// {
				// console.log("STEP1 OK");
				// if
				// (checkServerConnection_Nopassrsync($("span[id='checkNopassRsync']")))
				// {
				// console.log("STEP2 OK");
				// checkServerConnection_Locker($("span[id='checkLocker']"));
				// }
				// }
			});

			$("span[id='checkAllowedIP']").on("click", function() {
				checkServerConnection_AllowedIP($(this));
			});
			$("span[id='checkReadServerConfig']").on("click", function() {
				checkServerConnection_ReadServerConfig($(this));
			});
			$("span[id='checkNopassRsync']").on("click", function() {
				checkServerConnection_Nopassrsync($(this));
			});
			$("span[id='checkLocker']").on("click", function() {
				checkServerConnection_Locker($(this));
			});

			jQuery("button[id='EditServerConfig']").click(function() {
				var serverUrl = $("input[name='serverUrl']").val();
				// window.location.href = serverUrl;
				window.open(serverUrl + "/EditServerConfig");
			});

			jQuery("input[name=ServerEnabled]").each(function() {
				if ($(this).attr("ServerEnabled") == "true") {
					$(this).attr("checked", "true");
				}
			});

			$("#tabs").tabs({
				select : function(event, ui) {
					var url = $.data(ui.tab, 'load.tabs');
					if (url) {
						location.href = url;
						return false;
					}
					return true;
				}
			});

			jQuery("#addTab").click(
					function() {
						var i = jQuery("tr[id=tab]").size() + jQuery("tr[id=newTab]").size();
						console.log("jQuery(tr[id=tab]).size()=" + jQuery("tr[id=tab]").size() + ", jQuery(tr[id=newTab]).size()="
								+ jQuery("tr[id=newTab]").size());
						if (i < 10) {
							if (jQuery("tr[id=newTab]").size() == 0) {
								jQuery("tr[id='tab']:last").clone(true).insertAfter(jQuery("tr[id='tab']:last"));
								jQuery("tr[id='tab']:last").attr("id", "newTab");
								// jQuery("a[id=deleteTab]:last").attr("id",
								// "deleteTab");
								// jQuery("a[id=deleteTab]:last").attr("tabid",
								// "");
							} else {
								var lastNewTab = jQuery("tr[id='newTab']:last");
								lastNewTab.clone(true).insertAfter($(this).closest("tr").find("tr[id='newTab']:last"));
							}
							lastNewTab = jQuery("tr[id='newTab']:last");
							lastNewTab.find("input[name='tabid']").val("");
							lastNewTab.find("input[name='tabname']").val("");
							lastNewTab.find("input[name='tabdescript']").val("");
							lastNewTab.find("input").attr("value", "");
							jQuery("tr[id=newTab]:last select").attr("name", "orderby");
						}
					});

			$('button#modal_deleteProblemtab').on('click', function() {
				var modal = $(this).closest('.modal');
				var renameto = modal.find('[name=renametotabid]').val();
				// var tabid = modal.find('[name=tabid]').val();
				var tabid = $(this).data("tabid");
				console.log("tabid=" + tabid);
				console.log("renameto=" + modal.find("input[name='renametotabid']:checked").val());
				jQuery.ajax({
					type : "POST",
					// url : "./xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
					// data : modal.find('form').serialize(),
					type : "GET",
					url : "Debugger.api",
					data : "action=deleteProblemtab&tabid=" + tabid + "&renametotabid=" + modal.find("input[name='renametotabid']:checked").val(),

					timeout : 5000,
					success : function(result) {
						modal.modal('hide');
						// location.reload();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log("jqXHR.responseText=" + jqXHR.responseText);
						console.log("errorThrown=" + errorThrown);
						console.log("textStatus=" + textStatus);
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
								} ]
							});
						} catch (err) {
							BootstrapDialog.alert(errorThrown);
						}
					}
				});
			});

			// 以下準備取消。 a#deleteTab 改用 button#deleteTab
			jQuery("a#deleteTab").click(
					function(event) {
						if (jQuery("tr[id=tab]").size() + jQuery("tr[id=newTab]").size() <= 1) {
							alert("至少必須有一個標籤");
							return false;
						}
						if ($(this).closest("tr").attr("id") == "newTab") {
							// alert($(this).closest("tr").html());
							$(this).closest("tr").remove();
							return false;
						}
						// var tabid = $(this).attr("tabid");
						var tabid = $(this).closest("tr").find("input[name='tabid']").val();
						var delete_Confirm = $(this).closest("tr").find("div[id='delete_confirm']");
						delete_Confirm.find("#delete_tabid").html(tabid);

						delete_Confirm.find("input[name='renametotabid']").each(function() {
							if ($(this).val() == tabid) {
								$(this).attr("disabled", true);
							}
						});

						var $dialog = delete_Confirm.dialog({
							autoOpen : false,
							width : '30%',
							title : 'Confirm',
							buttons : {
								"確定" : function() {
									jQuery.ajax({
										type : "GET",
										url : "Debugger.api",
										data : "action=deleteProblemtab&tabid=" + tabid + "&renametotabid="
												+ delete_Confirm.find("input[name='renametotabid']:checked").val(),
										async : false,
										timeout : 5000,
										context : this, // success 內部要使用 $(this)
										// 要加這一行。
										success : function(result) {
											// alert($(this).closest("tr").html());
											$(this).closest("tr").remove();
											location.reload();
											// getProblemtabs();
										},
										error : function(jqXHR, textStatus, errorThrown) {
											if (jqXHR.responseText !== '') {
												showError(jqXHR.responseText);
											} else {
												showError(errorThrown);
											}
										}
									});
									$(this).dialog("close");
								},
								"取消" : function() {
									$(this).dialog("close");
								}
							}
						});
						$dialog.dialog('open');
						return false;
					});

			jQuery.getJSON("AppConfig.api", function(appConfig) {
				var tabs = appConfig.problemtabs;
				for (var i = 0; i < tabs.length; i++) {
					var tab = tabs[i];
					jQuery("input[name=tabid]:eq(" + i + ")").val(tab.id);
					jQuery("input[name=tabname]:eq(" + i + ")").val(tab.name);
					jQuery("input[name=tabdescript]:eq(" + i + ")").val(tab.descript);
					var orderby = tab.orderby.split(",");

					var by = 0;
					for (var j = 0; j < 3; j++) {
						// alert("i=" + i + ", j=" + j + ", index=" + (i * 3 +
						// j));
						$("select[name=orderby]:eq(" + (i * 3 + j) + ")").each(function() {
							// alert("i=" + i + ", j=" + j + ", index=" + (i * 3
							// + j) +
							// $(this).attr("orderby"));
							$(this).children().each(function() {
								if (orderby.length > by && orderby[by].trim() == $(this).val()) {
									$(this).attr("selected", "true");
								}
							});
							by++;
						});
					}
				}
			});

			jQuery("#system_closed_message").focus(function() {
				$("#SYSTEM_CLOSE").attr("checked", true);
			});

			jQuery("#problemid_prefix").children().each(function() {
				// alert(jQuery("span[name='locale']").text());
				if ($(this).parent().attr("problemid_prefix") == $(this).val()) {
					$(this).attr("selected", true);
				}
			});

			jQuery("input[name='EnableMailer']").each(function() {
				if ($(this).attr("EnableMailer") == $(this).val()) {
					$(this).attr("checked", true);
				}
			});
			jQuery("input[name='rejudgeable']").each(function() {
				if ($(this).attr("rejudgeable") == $(this).val()) {
					$(this).attr("checked", true);
				}
			});
			jQuery("input[name='systemMode']").each(function() {
				if ($(this).attr("systemMode") == $(this).val()) {
					$(this).attr("checked", true);
				}
			});
			$("button[type='submit']").bind("click", function(e) {
				e.preventDefault();
				var form = $(this).closest("form");
				console.log("form=" + form);
				jQuery.ajax({
					type : "POST",
					url : "EditAppConfig",
					// data: form.serialize(),
					// data : form.serializeArray(),
					data : new FormData(form[0]),
					cache : false,
					processData : false,
					contentType : false,
					async : true,
					timeout : 5000,
					success : function(result) {
						console.log(result);
						// window.location.href = document.referrer; // 跳轉到前一頁，並
						// reload
						location.reload(); // 本頁重讀。
					},
					beforeSend : function(jqXHR, settings) {
						jqXHR.url = settings.url;
					},
					error : function(jqXHR, textStatus, errorThrown) {
						// console.log("jqXHR.responseText=" +
						// jqXHR.responseText);
						console.log("errorThrown=" + errorThrown);
						console.log("textStatus=" + textStatus);
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
								} ]
							});
						} catch (err) {
							BootstrapDialog.alert(errorThrown + ", url=" + jqXHR.url);
						}
					}
				});
			});
		});

// function sleep (time) {
// return new Promise((resolve) => setTimeout(resolve, time));
// }

function checkServerConnection_AllowedIP(refresh) {
	var alert = refresh.closest(".alert");
	var status = alert.find("#status");
	var check = false;

	alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-default");
	status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-glyphicon-question-sign");
	refresh.addClass("glyphicon-refresh-animate");

	setTimeout(function() {
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=checkAllowedIP&judgerUrl=" + $("input[name='serverUrl']").val(),
			async : true,
			timeout : 5000,
			beforeSend : function() {
				console.log("beforeSend");
			},
			success : function(result) {
				console.log("success, result=" + result);
				// checkStep1(alert, status, result);
				if (result == "true") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-success");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-ok");
					return true;
				} else if (result == "false") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				} else {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				}
			},
			error : function() {
				console.log("error");
				alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
				status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
			},
			complete : function() {
				console.log("complete");
				refresh.removeClass("glyphicon-refresh-animate");
			}
		});

	}, 1000);

	// })

	console.log("3. check=" + check);
	return check;
}

function checkServerConnection_ReadServerConfig(refresh) {
	var alert = refresh.closest(".alert");
	var status = alert.find("#status");
	var check = false;

	alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-default");
	status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-glyphicon-question-sign");
	refresh.addClass("glyphicon-refresh-animate");

	setTimeout(function() {
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=checkReadServerConfig&judgerUrl=" + $("input[name='serverUrl']").val(),
			async : true,
			timeout : 5000,
			beforeSend : function() {
				console.log("beforeSend");
			},
			success : function(result) {
				console.log("success, result=" + result);
				// checkStep1(alert, status, result);
				if (result == "true") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-success");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-ok");
					return true;
				} else if (result == "false") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				} else {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				}
			},
			error : function() {
				console.log("error");
				alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
				status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
			},
			complete : function() {
				console.log("complete");
				refresh.removeClass("glyphicon-refresh-animate");
			}
		});

	}, 1000);

	// })

	console.log("3. check=" + check);
	return check;
}


function checkServerConnection_Nopassrsync(refresh, callback) {
	var alert = refresh.closest(".alert");
	var status = alert.find("#status");
	var check = false;

	alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-default");
	status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-glyphicon-question-sign");
	refresh.addClass("glyphicon-refresh-animate");
	setTimeout(function() {
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=checkNopassRsync&serverUrl=" + $("input[name='serverUrl']").val(),
			async : true,
			timeout : 5000,
			success : function(result) {
				if (result == "true") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-success");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-ok");
					check = true;
				} else if (result == "false") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				} else {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				}
			},
			error : function() {
				alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
				status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
			},
			beforeSend : function() {
				// alert.removeClass(alert.attr('class').split('
				// ').pop()).addClass("alert-default");
				// status.removeClass(status.attr('class').split('
				// ').pop()).addClass("glyphicon-glyphicon-question-sign");
				// refresh.addClass("glyphicon-refresh-animate");
			},
			complete : function() {
				refresh.removeClass("glyphicon-refresh-animate");
			}
		});
	}, 2000);
	return check;
}

function checkServerConnection_Locker(refresh) {
	var alert = refresh.closest(".alert");
	var status = alert.find("#status");
	var check = false;

	alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-default");
	status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-glyphicon-question-sign");
	refresh.addClass("glyphicon-refresh-animate");
	setTimeout(function() {
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=checkLocker&judgerUrl=" + $("input[name='serverUrl']").val() + "&cryptKey=" + $("input[name='cryptKey']").val(),
			async : true,
			timeout : 5000,
			success : function(result) {
				if (result == "true") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-success");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-ok");
					check = true;
				} else if (result == "false") {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				} else {
					alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
					status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
				}
			},
			error : function() {
				alert.removeClass(alert.attr('class').split(' ').pop()).addClass("alert-danger");
				status.removeClass(status.attr('class').split(' ').pop()).addClass("glyphicon-remove");
			},
			beforeSend : function() {
				// alert.removeClass(alert.attr('class').split('
				// ').pop()).addClass("alert-default");
				// status.removeClass(status.attr('class').split('
				// ').pop()).addClass("glyphicon-glyphicon-question-sign");
				// refresh.addClass("glyphicon-refresh-animate");
			},
			complete : function() {
				refresh.removeClass("glyphicon-refresh-animate");
			}
		});
	}, 3000);
	return check;
}

function UploadImage() {
	jQuery.ajax({
		type : "POST",
		url : "./UploadImage",
		data : $('#form').serialize(),
		async : false,
		timeout : 5000,
		success : function(result) {
			// alert(result);
			var json = JSON.parse(result);
			jQuery("#Testjudge_dialog3 #Testjudge_htmlstatus").text(json.htmlstatus);
			jQuery("#Testjudge_dialog3 #Testjudge_result").text(json.result);
			var $dialog3 = $("#Testjudge_dialog3").dialog({
				autoOpen : true,
				width : '60%',
				title : 'Result',
				close : function(event, ui) {
					location.reload();
				},
				buttons : {
					"返回" : function() {
						$(this).dialog("close");
					}
				}
			});
		}
	});
}

function getProblemtabs() {
	jQuery.ajax({
		type : "GET",
		url : "Debugger.api",
		data : "action=getProblemtabs",
		async : false,
		cache : false,
		timeout : 5000,
		success : function(result) {
			jQuery("div[id='Problemtabs']").html(result);
			jQuery("div[id='Problemtabs']").on('click', 'a[id=deleteTab]', function() {
				// var index = jQuery("a[id=deleteTab]").index(this);
				var tabid = $(this).closest("tr").find("input[name='tabid']");
				jQuery.ajax({
					type : "GET",
					url : "Debugger.api",
					data : "action=deleteProblemtab&tabid=" + tabid,
					async : false,
					cache : false,
					timeout : 5000,
					success : function(result) {
						getProblemtabs();
					}
				}); // jQuery ajax
			});
		}
	}); // jQuery ajax
}
