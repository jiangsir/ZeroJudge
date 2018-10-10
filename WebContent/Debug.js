jQuery(document).ready(function() {
	jQuery("#addSchema").click(function() {
		addSchema();
	});

	jQuery("span[id='compareSchema']").click(function() {
		var index = $(this).attr("index");
		compareSchema(index);
	});

	jQuery("span[id='removeSchema']").click(function() {
		var index = $(this).attr("index");
		removeSchema(index);
	});

	jQuery("#updateDatabaseVersion").click(function() {
		updateDatabaseVersion();
	});

	jQuery("input[name='submit4']").bind('click', function() {
		var url = jQuery("input[id='url']").val();
		// var url = "http://zerojudge.tw/";
		var querystring = jQuery("input[id='querystring']").val();
		var charset = jQuery("select[id='charset']").val();
		var method = jQuery("select[id='method']").val();
		// alert("before ajax url=" + url + ", charset=" +
		// charset
		// +", method="+ method);

		jQuery.ajax({
			type : "GET",
			url : "http://127.0.0.1/ZeroJudge_Dev/GetWebdata",
			// url:
			// "http://lib.swsh.tpc.edu.tw/WebPAC/Bin/WC_opac.ASP",
			data : "url=" + url + "&method=" + method + "&charset=" + charset + "&" + $('#form_webobject').serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				// alert(result);
				jQuery("textarea[id='webresult']").val(result);
			}
		});
	});
	jQuery("button[id='restartMysql']").click(function() {
		jQuery.ajax({
			type : "POST",
			url : "./Debug",
			data : "action=restartMysql",
			async : false,
			timeout : 5000,
			success : function(result) {
			}
		});
	});
	jQuery("button[id='restartTomcat']").click(function() {
		jQuery.ajax({
			type : "POST",
			url : "./Debug",
			data : "action=restartTomcat",
			async : false,
			timeout : 5000,
			success : function(result) {
			}
		});
	});
	jQuery("button[id='reboot']").click(function() {
		jQuery.ajax({
			type : "POST",
			url : "./Debug",
			data : "action=reboot",
			async : false,
			timeout : 5000,
			success : function(result) {
			}
		});
	});

	jQuery("span[id='img_ipdeny']").bind('click', function() {
		var index = jQuery("span[id='img_ipdeny']").index(this);
		var ip = jQuery("span[id='img_ipdeny']:eq(" + index + ")").attr('name');
		if (!confirm("ipdeny=" + ip)) {
			return;
		}
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=setIpdeny&ip=" + ip,
			async : false,
			timeout : 5000,
			success : function(result) {
			}
		});
	});

	jQuery("span[id='img_ipallow']").bind('click', function() {
		var index = jQuery("span[id='img_ipallow']").index(this);
		var ip = jQuery("span[id='img_ipallow']:eq(" + index + ")").attr('name');

		if (!confirm("ipallow=" + ip)) {
			return;
		}
		jQuery.ajax({
			type : "GET",
			url : "./Debugger.api",
			data : "action=setIpallow&ip=" + ip,
			async : false,
			timeout : 5000,
			success : function(result) {
			}
		});
	});

	jQuery("span[id='Testjudge'] a").click(function() {
		var $dialog = $("#Testjudge_dialog").dialog({
			autoOpen : false,
			width : '60%',
			title : 'Code',
			buttons : {
				"取消" : function() {
					$(this).dialog("close");
				},
				"下一步" : function() {
					$(this).dialog("close");
					IndataDialog();
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});
});

function IndataDialog() {
	// alert($('#form_Testjudge').serialize());
	var $dialog2 = $("#Testjudge_dialog2").dialog({
		autoOpen : true,
		width : '60%',
		title : 'Indata',
		buttons : {
			"取消" : function() {
				$(this).dialog("close");
			},
			"測試" : function() {
				$(this).dialog("close");
				doTestjudge();
			}
		}

	});
}

function addSchema() {
	jQuery.ajax({
		type : "GET",
		url : "./Debugger.api",
		data : "action=showDbSchema",
		async : false,
		timeout : 5000,
		success : function(result) {
			var schema = JSON.parse(result);
			showSchema(schema);
		}
	});

	var $dialog = $("#addSchema_dialog").dialog({
		autoOpen : false,
		width : '60%',
		title : 'Add Schema',
		buttons : {
			"取消" : function() {
				$(this).dialog("close");
			},
			"新增" : function() {
				$(this).dialog("close");
				var version = jQuery("input[name='schemaVersion']").val();
				jQuery.ajax({
					type : "GET",
					url : "./Debugger.api",
					data : "action=addSchema&version=" + version,
					async : false,
					timeout : 5000,
					success : function(result) {
						location.reload();
					}
				});
			}
		}
	});
	$dialog.dialog('open');
	return false;

}

function compareSchema(index) {
	jQuery.ajax({
		type : "GET",
		url : "./Debugger.api",
		data : "action=compareSchema&index=" + index,
		async : false,
		timeout : 5000,
		success : function(result) {
			var $dialog = $("#compareSchema_dialog").dialog({
				autoOpen : false,
				width : '60%',
				title : 'Compare Schema',
				buttons : {
					"返回" : function() {
						$(this).dialog("close");
					}
				}
			});
			var schemas = JSON.parse(result);
			for (var i = 0; i < schemas.length; i++) {
				showSchemas(schemas[i], i);
			}
			$dialog.dialog('open');
			return false;
		}
	});
}

function removeSchema(index) {
	jQuery.ajax({
		type : "GET",
		url : "./Debugger.api",
		data : "action=removeSchema&index=" + index,
		async : false,
		timeout : 5000,
		success : function(result) {
			location.reload();
		}
	});
}

function showSchema(schema) {
	var schemadiv = jQuery("div[id=schema]");
	var tables = schema.tables;
	var tablecount = 0;
	for ( var table in tables) {
		var table1 = $("<div/>").html(++tablecount + ". " + table);
		$(table1).css({
			'font-weight' : "bold",
			'color' : "#FF0000"
		});

		schemadiv.append(table1);
		var fields = tables[table];
		var fieldcount = 0;
		for ( var field in fields) {
			var field1 = $("<div/>").html(++fieldcount + ". " + field + " - " + fields[field]);
			$(field1).css({
				'padding-left' : "10px"
			});
			schemadiv.append(field1);
		}
	}

}

function showSchemas(schema, index) {
	var schema1 = jQuery("div[id=schema" + (index + 1) + "]");
	var version = $("<div/>").html("Version: " + schema.version);
	schema1.append(version);

	var tables = schema.tables;
	var tablecount = 0;
	for ( var table in tables) {
		var table1 = $("<div/>").html(++tablecount + ". " + table);
		$(table1).css({
			'font-weight' : "bold",
			'color' : "#FF0000"
		});

		schema1.append(table1);
		var fields = tables[table];
		var fieldcount = 0;
		for ( var field in fields) {
			var field1 = $("<div/>").html(++fieldcount + ". " + field + " - " + fields[field]);
			$(field1).css({
				'padding-left' : "10px"
			});
			schema1.append(field1);
		}
	}
}

function updateDatabaseVersion() {
	jQuery.ajax({
		type : "GET",
		url : "./Debugger.api",
		data : "action=updateDatabaseVersion",
		async : false,
		timeout : 5000,
		success : function(result) {
			location.reload();
		}
	});
}

function doTestjudge() {
	// alert($('#form_Testjudge').serialize());
	// alert($('#form2_Testjudge').serialize());
	jQuery.ajax({
		type : "POST",
		url : "./Testjudge",
		data : $('#form_Testjudge').serialize(),
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
