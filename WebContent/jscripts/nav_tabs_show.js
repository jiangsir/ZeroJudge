$.urlParam = function(name) {
	var results = new RegExp('[\?&]' + name + '=([^&#]*)')
			.exec(window.location.href);
	return results[1] || 0;
}

function param_value(param) {
	console.log('param=' + param)
	console.log(param + '= ' + $.urlParam(param))
	return $.urlParam(param);
}

function tabs_show(param){
	$('.nav-tabs a:first').tab('show');
	param_value = param_value(param);
	console.log('param_value='+param_value);
	if (param_value != null) {
		$('.nav-tabs a[aria-controls="aria_' + param_value + '"]').tab('show');
	}
	
}
