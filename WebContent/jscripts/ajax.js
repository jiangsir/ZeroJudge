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

// 停用 已由 jQuery 版本替代 20090118
function checkAccount_OLD(account){
	var url="./CheckAccount.ajax?account="+account+"&forIE="+new Date().getTime();
	if(account==""){
		return;
	}
	XMLHttpRequestObject.open("GET", url, true);
	XMLHttpRequestObject.onreadystatechange = function(){
		if(XMLHttpRequestObject.readyState == 4 && XMLHttpRequestObject.status == 200){
			s = XMLHttpRequestObject.responseText;
			var obj = document.getElementById("CheckAccount");
			if(s=="notavailable"){
				obj.innerHTML = "這個帳號已被使用!!";
			}else if(s=="illegal"){
				obj.innerHTML = "帳號不合法";
			} else{
				obj.innerHTML = "可用";
			}
		}
	};
	
	XMLHttpRequestObject.send(null);
	/*
	if(xmlHttp.readyState==4){
		s = XMLHttpRequestObject.responseText;
		var obj = document.getElementById("CheckAccount");
		if(XMLHttpRequestObject.status == 200){
			if(s=="true"){
				obj.innerHTML = "這個帳號已被使用!!";
			}
		}
	}
	*/
}

function readIM(url, divID, imageid, imagesrc){
	//var url = "ReadIMessage?imessageid="+imessageid+"&status=unread";
	url += "&forIE="+new Date().getTime();
	if(XMLHttpRequestObject){
		var obj = document.getElementById(divID);
		var imageobj = document.getElementById(imageid);

		XMLHttpRequestObject.open("GET", url, true);
		XMLHttpRequestObject.onreadystatechange = function(){
				if(XMLHttpRequestObject.readyState == 4 && XMLHttpRequestObject.status == 200){
					//obj.innerHTML = XMLHttpRequestObject.responseText;
					//eval(XMLHttpRequestObject.responseText);
				}
			}
		XMLHttpRequestObject.send(null);

		imageobj.setAttribute("src",imagesrc);
		//accountfromobj.style.fontweight="normal";
//		document.writeln(window.navigator.userAgent);
		if(obj.style.display=="none"){
			if(window.navigator.userAgent.indexOf("Firefox")>=1){
				obj.style.display="table-row"; // qx FF ??, ? IE ????
			}else if(window.navigator.userAgent.indexOf("MSIE 6.0")>=1){
				obj.style.display="block"; // qx IE ??, ? FF ???????????
			}else{
				obj.style.display="table-row";
			}
		}else{
			obj.style.display="none";
		}
	}
}