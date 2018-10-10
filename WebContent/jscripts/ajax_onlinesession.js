var identifier1 = 'link1';
var identifier2 = 'link2';
var showDiv = 'onlinesessioncount';
var timerSec = 2000;

getData1("./Ajax.ajax?method=OnlineSessionCount", showDiv, timerSec);

// JavaScript Document
var XMLHttpRequestObject = false;
if(window.XMLHttpRequest){
	XMLHttpRequestObject = new XMLHttpRequest();
} else if (window.ActiveXObject){
	XMLHttpRequestObject = new ActiveXObject("Microsoft.XMLHTTP");
}


function getData1(source, divID, delay){
	var dataSource;
	if(XMLHttpRequestObject){
		dataSource = source +"&forIE="+new Date().getTime();
		try {
			XMLHttpRequestObject.open("GET", dataSource);
		} catch(e){
		}
		XMLHttpRequestObject.onreadystatechange = function(){
			try{
				if(XMLHttpRequestObject.readyState==4 && XMLHttpRequestObject.status == 200){
					var objDiv = document.getElementById(divID);
					objDiv.innerHTML = XMLHttpRequestObject.responseText;
				}
			}catch(e){
			}
		}
		try{
			XMLHttpRequestObject.send(null);
		}catch(e){
		}
	}
	setTimeout("getData1('"+source+"', '"+divID+"', '"+delay+"')", delay);
}
