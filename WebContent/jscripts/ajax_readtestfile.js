// JavaScript Document
var XMLHttpRequestObject = false;
if(window.XMLHttpRequest){
	XMLHttpRequestObject = new XMLHttpRequest();
} else if (window.ActiveXObject){
	XMLHttpRequestObject = new ActiveXObject("Microsoft.XMLHTTP");
}

function getData(url, divID){
	var fullurl=url+"&forIE="+new Date().getTime();
	XMLHttpRequestObject.open("GET", url, true);
	XMLHttpRequestObject.onreadystatechange = function(){
		if(XMLHttpRequestObject.readyState == 4 && XMLHttpRequestObject.status == 200){
			text = XMLHttpRequestObject.responseText;
			var obj = document.getElementById(divID);
			obj.innerHTML = text;
		}
	};
	XMLHttpRequestObject.send(null);	
}