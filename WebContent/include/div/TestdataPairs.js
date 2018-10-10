jQuery(document).ready(
		function() {
			var problemid = jQuery("span[name='problemid']").text();
			// getTestdataPairs(problemid);

			recount();

			jQuery("#averagescore").click(function() {
				averageScore();
				recount();
			});

			jQuery("#averagetimelimit").click(function() {
				averageTimelimit();
				recount();
			});
			var pairs = jQuery("div#TestdataPairs");
			// pairs.html(result);
			// recount();
			pairs.on('blur', 'input[name=scores]', function() {
				recount();
			});
			pairs.on('blur', 'input[name=timelimits]', function() {
				recount();
			});

			jQuery("span[id=readTestdataPair]").click(function() {
				readTestdataPair($(this));
			});

			jQuery("[data-target='#Modal_TestdataPair']").click(
					function() {
						// readTestdataPair($(this));
						var modal = $(".modal#Modal_TestdataPair");
						var problemid = $(this).data("problemid");
						var index = $(this).data("index");
						console.log(problemid + "_" + index);
						$.getJSON("./Problem.json?data=TestdataPair&problemid="
								+ problemid + "&index=" + index,
								function(data) {
									console.log("data=" + data);
									modal.find("#infilename").html(
											data.infilename);
									modal.find("#outfilename").html(
											data.outfilename);
									modal.find("#indata").val(data.indata);
									modal.find("#outdata").val(data.outdata);
								});
						modal.modal("toggle");
					});

			jQuery('span[id=deleteTestdataPair]').click(function() {
				deleteTestdataPair($(this));
			});

			// jQuery("input[name='writeScoresTimelimits']").click(function() {
			// this.closest("form").submit();
			// });

		});

// function getTestdataPairs(problemid) {
// // jquery bind click after ajax call
// recount();
// jQuery.ajax({
// type : "GET",
// url : "Problem.api",
// data : "action=getTestdataPairs&problemid=" + problemid,
// async : false,
// cache : false,
// timeout : 5000,
// success : function(result) {
// var pairs = jQuery("div#TestdataPairs");
// pairs.html(result);
// recount();
// pairs.on('blur', 'input[name=scores]', function() {
// recount();
// });
// pairs.on('blur', 'input[name=timelimits]', function() {
// recount();
// });
//
// pairs.on('click', 'span[id=deleteTestdataPair]', function() {
// var index = jQuery("span[id=deleteTestdataPair]").index(this);
// jQuery.ajax({
// type : "POST",
// url : "Problem.api",
// data : "action=deleteTestdataPair&problemid=" + problemid + "&index=" +
// index,
// async : false,
// cache : false,
// timeout : 5000,
// success : function(result) {
// getTestdataPairs(problemid);
// }
// }); // jQuery ajax
// });
// }
// }); // jQuery ajax
// }

function readTestdataPair(pair) {
	var $dialog = pair.closest("#testdataPair").find("#readtestdata_dialog")
			.dialog({
				autoOpen : false,
				width : '80%',
				title : 'Testdata',
				// closeOnEscape : false, // 預設為 true
				close : function() {
					// 這裡定義所有要在 dialog close 時所要完成的事。包含 esc, X 按鈕, 返回鈕
					$dialog.dialog("destroy");
				},
				buttons : {
					"返回" : function() {
						$dialog.dialog("close"); // 會呼叫 close 定義的方法來進行
						// "close"
					}
				}
			});
	$dialog.dialog('open');
	return false;
}

function deleteTestdataPair(pair) {

	// var index = jQuery("span[id=deleteTestdataPair]").index(this);
	var index = pair.data("index");
	var problemid = pair.data("problemid");
	console.log("problemid=" + problemid + ", index=" + index);
	jQuery.ajax({
		type : "POST",
		url : "Problem.api",
		data : "action=deleteTestdataPair&problemid=" + problemid + "&index="
				+ index,
		async : false,
		cache : false,
		timeout : 5000,
		success : function(result) {
			location.reload();
			// getTestdataFiles(problemid);
		}
	}); // jQuery ajax

}

function averageScore() {
	var size = jQuery("input[id='score']").size();
	var average = 100 / size;

	var count = 0;
	jQuery("input[id='score']").each(
			function() {
				if (100 % size == 0 || count < size - 100 % size) {
					jQuery("input[id='score']:eq(" + count + ")").val(
							parseInt(average));
				} else {
					jQuery("input[id='score']:eq(" + count + ")").val(
							parseInt(average) + 1);
				}
				count++;
			});
}

function averageTimelimit() {
	jQuery("input[id='timelimit']").val(parseFloat(1.0));
}

function recount() {
	var totalscore = 0;
	var totaltimelimit = 0;
	console.log('recount');

	jQuery("div[id='testdataPair']")
			.each(
					function() {
						totalscore += parseInt($(this).find(
								"input[name=scores]").val());
						jQuery("span#totalscore").html(totalscore);
						totaltimelimit += parseFloat($(this).find(
								"input[name=timelimits]").val());
						jQuery("span#totaltimelimit").html(totaltimelimit);
					});

}
