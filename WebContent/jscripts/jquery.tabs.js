/*
 * jquery的Tabs控件
 * http://blog.joytown.tw
 * Date: 2009-03-25
 */

jQuery.noConflict();
jQuery(document).ready(function(){
jQuery(".jquery-tabs span:first").addClass("current");  //為第一個span加入 .current 的樣式，預設選取
jQuery(".jquery-tabs ul:not(:first)").hide();  //ul 不是第一個時隱藏
jQuery(".jquery-tabs span").mouseover(function(){  //滑鼠移到 span 上時觸發函數
jQuery(".jquery-tabs span").removeClass("current");  //為第一個 span 移除 .current 樣式
jQuery(this).addClass("current");  //為觸發的 span 加入樣式
jQuery(".jquery-tabs ul").hide();  //隱藏 ul
jQuery("."+jQuery(this).attr("id")).fadeIn("slow");  //這句是核心，class(.) 和觸發 span 的ID 一致的 fadeIn(漸顯)
});});