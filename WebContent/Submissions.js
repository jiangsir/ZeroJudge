// JavaScript Document
jQuery(document).ready(function() {
	

});

function doShowCode(solutionid, index) {

}

function getCodelocker(solutionid) {
	var text;
	jQuery.ajax({
		type : "POST",
		url : "./Solution.api",
		data : "action=getImgCodelocker&solutionid=" + solutionid,
		async : false,
		timeout : 5000,
		success : function(result) {
			text = result;
			// alert(result);
			// jQuery("#alert").html(result);
			// jQuery("span[id='imglock']:eq(" + index + ")").html(result);
		}
	});
	// alert("getCodelocker solutionid="+solutionid+" text="+text);
	return text;
}

// function closelock(solutionid, index){
// // var text = getCodelocker();
// // jQuery("#alert").text(text);
//    
// jQuery.ajax({
// type: "GET",
// url: "./Codelock.ajax",
// data: "solutionid=" + solutionid,
// async: false,
// timeout: 5000,
// success: function(result){
// jQuery("img[id='closelock']:eq(" + index + ")").hide();
// jQuery("img[id='openlock']:eq(" + index + ")").show();
// }
// });
// jQuery("span[id='imglock']:eq(" + index +
// ")").html(getCodelocker(solutionid));
// }
