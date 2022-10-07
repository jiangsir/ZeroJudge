<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="jscripts/jquery-ui-1.10.4.custom/js/jquery-1.10.2.js"></script>
<script
	src="jscripts/jQuery-File-Upload-9.11.2/js/vendor/jquery.ui.widget.js"></script>
<script
	src="jscripts/jQuery-File-Upload-9.11.2/js/jquery.iframe-transport.js"></script>
<script src="jscripts/jQuery-File-Upload-9.11.2/js/jquery.fileupload.js"></script>
<script>
 $(document).ready(function(){   
  $('#fileupload').fileupload({
   dropZone: $('#drop'), //拖曳上傳區域
      url: 'ajax/upload.php',  //上傳處理的PHP
         dataType: 'json',
 
         //將要上傳的資料顯示
         add: function (e, data) {
             var tpl = $('<div class="working"><span class="pro" /><span class="info"></span><span class="ctrl">取消</span></div>');
             tpl.find('.info').text(data.files[0].name);
             data.context = tpl.appendTo($(".item"));
 
             tpl.find('.ctrl').click(function(){
                 //if(tpl.hasClass('working')){
                 //    jqXHR.abort();  //取消上傳
                 //}
 
                 tpl.fadeOut(function(){
                     tpl.remove();
                 });
             });
             //執行 data.submit() 開始上傳
             $("#start").click(function() {
              var jqXHR = data.submit();
             });
         },
 
         //單一檔案進度
         progress: function(e, data){
             var progress = parseInt(data.loaded / data.total * 100, 10);
             data.context.find('.pro').text(progress+"%　　").change();
             if(progress == 100){
                 data.context.removeClass('working');
             }
         },
 
   //整體進度
   progressall: function (e, data) {
    var progress = parseInt(data.loaded / data.total * 100, 10);
    $('#progress .bar').css('width', progress + '%');
    $('#progress .bar').text(progress + '%');
   },
 
         //上傳失敗
         fail:function(e, data){
             data.context.addClass('error');
         },
 
         //單一檔案上傳完成
         done: function (e, data) {
          var tmp = data.context.find('.pro').text();
             data.context.find('.pro').text(tmp + data.result.status + "　　");
         },
 
         //全部上傳完畢
         stop: function (e) {
          alert("上傳完畢");
         }
     });
  
  //拖曳成功讓框變色
  $("#drop").bind({
      dragenter: function() {
       $(this).addClass("active");
      },
   dragleave: function() {
       $(this).removeClass("active");
      }
  }); 
 });
 </script>
<style type="text/css">
#drop {
	width: 500px;
	height: 50px;
	background: #eee;
	border: 3px dashed;
	text-align: center;
	padding: 50;
}

.active {
	border: 3px dashed red !important;
}

.bar {
	height: 18px;
	background: red;
	text-align: center;
	font-weight: bold;
}
</style>

</head>
<body>
	<div id="drop">
		拖曳至此上傳 <input id="fileupload" multiple="" name="files" type="file">
	</div>
	<input id="start" type="button" value="開始上傳">
	<div id="progress">
		<div class="bar" style="width: 0%;"></div>
		<div class="item"></div>
	</div>

	<input id="fileupload" type="file" name="files[]"
		data-url="server/php/" multiple>
</body>
</html>
