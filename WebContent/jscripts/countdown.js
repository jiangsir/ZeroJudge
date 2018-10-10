// JavaScript Document
// MF

 ClientDateElapsed=0;
 
 function server_time(){
   CurrTime = new Date();
   timeDiff = ClientDateElapsed; //(CurrTime.valueOf()-ClientDate);
   CurrServerDate=new Date(ServerDate.valueOf()+timeDiff);

   msPerSec = 1000;
   msPerMin = 60 * msPerSec;
   msPerHour= 60 * msPerMin;
   msPerDay = 24 * msPerHour;
 
   year=  CurrServerDate.getFullYear();
   month= CurrServerDate.getMonth()+1;
   day  = CurrServerDate.getDate();

   hours= CurrServerDate.getHours();
   mins = CurrServerDate.getMinutes();
   secs = CurrServerDate.getSeconds();
   if (month<10) month="0"+month;
   if (day<  10) day  ="0"+day;
   if (hours<10) hours="0"+hours;
   if (mins< 10) mins= "0"+mins;
   if (secs< 10) secs= "0"+secs;
   
   wiad=year+'-'+month+'-'+day+' '+hours+':'+mins+':'+secs;
   document.getElementById("stime").innerHTML=wiad;
   ClientDateElapsed+=msPerSec;
   setTimeout("server_time()",msPerSec)
 }

 
 elapsed=0;
 function countdown(){

   var strLeft="Do koñca zosta³o: "
   var strEnd ="Zawody zakoñczone."
   var BigDay =new Date(BigDate)
   var SerDay =new Date(ServerDate)

   msPerSec = 1000;
   msPerMin = 60 * msPerSec;
   msPerHour= 60 * msPerMin;
   msPerDay = 24 * msPerHour;

   timeLeft = (BigDay.getTime() - SerDay.getTime() - elapsed);
   
   if (timeLeft>0)
   {
    daysLeft = Math.floor(timeLeft/msPerDay);
    timeLeft-= daysLeft*msPerDay;
    hrsLeft  = Math.floor(timeLeft/msPerHour);
    timeLeft-= hrsLeft*msPerHour;
    minLeft  = Math.floor(timeLeft/msPerMin);
    timeLeft-= minLeft*msPerMin;
    secLeft  = Math.floor(timeLeft/msPerSec);
    timeLeft-= secLeft*msPerSec;

    if (secLeft<=9) secLeft="0"+secLeft;
    if (minLeft<=9) minLeft="0"+minLeft;
    if (hrsLeft<=9) hrsLeft="0"+hrsLeft;

    wiad=strLeft;
    if (daysLeft==1) wiad+="1 dzieñ ";
      else {if (daysLeft>1) wiad+=daysLeft+" dni ";}
    wiad+= hrsLeft+":"+minLeft+":" + secLeft;
   }
   else
    wiad= strEnd;

   document.getElementById("clockcomp").innerHTML=wiad;

   elapsed+=msPerSec;
   setTimeout("countdown()",msPerSec)
 }
