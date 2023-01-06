/*
	This is the JavaScript file for the AJAX Suggest Tutorial

	You may use this code in your own projects as long as this
	copyright is left	in place.  All code is provided AS-IS.
	This code is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	For the rest of the code visit http://www.DynamicAJAX.com

	Copyright 2006 Ryan Smith / 345 Technical / 345 Group.

*/
//Gets the browser specific XmlHttpRequest Object


function getXmlHttpRequestObject() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if(window.ActiveXObject) {
		return new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		alert("Your Browser Sucks!\nIt's about time to upgrade don't you think?");
	}
}

//Our XmlHttpRequest object to get the auto suggest
var searchReq = getXmlHttpRequestObject();

//Called from keyup on the search textbox.
//Starts the AJAX request.
function searchSuggest() {
	if (searchReq.readyState == 4 || searchReq.readyState == 0) {
			document.getElementById("suggestproblemid").style.visibility='visible';
			var str = escape(document.getElementById("txtSearch").value);
			searchReq.open("GET", 'SuggestProblemid.ajax?problemid=' + str, true);
			searchReq.onreadystatechange = handleSearchSuggest;
			searchReq.send(null);
	}
}

//Called when the AJAX response is returned.
function handleSearchSuggest() {
	if (searchReq.readyState == 4) {
		var ss = document.getElementById("suggestproblemid");
		ss.innerHTML = '';
		var result = searchReq.responseText;
		if(result=="null"){
			document.getElementById("suggestproblemid").style.visibility='hidden';
		} else {
			document.getElementById("suggestproblemid").style.visibility='visible';
		}
		var str = searchReq.responseText.split("\n");
		for(i=0; i < str.length - 1; i++) {
			//Build our element string.  This is cleaner using the DOM, but
			//IE doesn't support dynamically added attributes.
			var suggest = '<div onmouseover="javascript:suggestOver(this);" ';
			suggest += 'onmouseout="javascript:suggestOut(this);" ';
			suggest += 'onclick="javascript:setSearch(this.innerHTML);" ';
			suggest += 'class="suggest_link">' + str[i] + '</div>';
			ss.innerHTML += suggest;
		}
	}
}

//Mouse over function
function suggestOver(div_value) {
	div_value.className = 'suggest_link_over';
}
//Mouse out function
function suggestOut(div_value) {
	div_value.className = 'suggest_link';
}
//Click function
function setSearch(value) {
	var userinfo = value.split(" ");
	document.getElementById('txtSearch').value = userinfo[0];
	document.getElementById('txtSearch').focus();
	document.getElementById('suggestproblemid').innerHTML = '';
	document.getElementById('suggestproblemid').style.visibility='hidden';
}