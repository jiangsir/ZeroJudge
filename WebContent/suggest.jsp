<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!--
	This is the HTML front-end of the AJAX Suggest Tutorial.

	You may use this code in your own projects as long as this
	copyright is left	in place.  All code is provided AS-IS.
	This code is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	For the rest of the code visit http://www.DynamicAJAX.com

	Copyright 2006 Ryan Smith / 345 Technical / 345 Group.
-->

<html lang="en-US">
	<head>
		<style type="text/css" media="screen">
			body {
				font: 11px arial;
			}
			.suggest_link {
				background-color: #FFFFFF;
				padding: 2px 6px 2px 6px;
			}
			.suggest_link_over {
				background-color: #3366CC;
				padding: 2px 6px 2px 6px;
			}
			#search_suggest {
				position: absolute;
				background-color: #FFFFFF;
				text-align: left;
				border: 1px solid #000000;
			}
		</style>
		<script language="JavaScript" type="text/javascript" src="./jscripts/ajax_search.js"></script>
	</head>
	<body>
			<form>
				<input type="text" id="txtSearch" name="txtSearch" alt="Search Criteria" onkeyup="searchSuggest();" autocomplete="off" />
				<input type="submit" id="cmdSearch" name="cmdSearch" value="Search" alt="Run Search" /><br />
				<div id="search_suggest">
				</div>
			</form>
	</body>
</html>
