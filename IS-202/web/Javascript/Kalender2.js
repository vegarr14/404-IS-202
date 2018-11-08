/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var nextOrPrev = 0;
var addedYears = 0;

function displayCalendar(){
 
 
 var htmlContent ="";
 var FebNumberOfDays ="";
 var counter = 1;
 
 var dateNow = new Date();
 var currentMonth = dateNow.getMonth() + nextOrPrev;
 
 var year = dateNow.getFullYear() + addedYears;

console.log(nextOrPrev);
 var dateThen = new Date(year, currentMonth);
 var month = dateThen.getMonth();
 
 console.log(month);  
 if(month > 11){
     month = 0; 
     addedYears++;
     console.log("år" + addedYears);
 }
 else if(month < 0){
     month = 11;
     addedYears--;
     console.log("år"+ addedYears);
 };
 var nextMonth = month;

 console.log(month);
 console.log(nextMonth);
 console.log(addedYears);
   //Used to match up the current month with the correct start date.
 var prevMonth = month -1;
 var day = dateNow.getDate();

 
 
 //Determing if February (28,or 29)  
 if (month == 1){
    if ( (year%100!=0) && (year%4==0) || (year%400==0)){
      FebNumberOfDays = 29;
    }else{
      FebNumberOfDays = 28;
    }
 }
 
 
 // names of months and week days.
 var monthNames = ["January","February","March","April","May","June","July","August","September","October","November", "December"];
 var dayNames = ["Sunday","Monday","Tuesday","Wednesday","Thrusday","Friday", "Saturday"];
 var dayPerMonth = ["31", ""+FebNumberOfDays+"","31","30","31","30","31","31","30","31","30","31"];
 
 
 // days in previous month and next one , and day of week.
 var nextDate = new Date(year, month, 0);
 var weekdays= nextDate.getDay();
 var weekdays2 = weekdays;
 var numOfDays = dayPerMonth[month];
 /*var prevDate = new Date(prevMonth +' -1,' +year); 
 var weekdays3= prevDate.getDay();
 var weekdays4 = weekdays3;*/
 
 



 
 // this leave a white space for days of pervious month.
 while (weekdays>0){
    htmlContent += "<td class='monthPre' onMouseOver='this.style.background=\"#FF0000\"'"+
        " onMouseOut='this.style.background=\"#FFFFFF\"'></td>";
 
 // used in next loop.
     weekdays--;
   
 }
 
 // loop to build the calander body.
 while (counter <= numOfDays){
     
     // When to start new line.
    if (weekdays2 > 6){
        weekdays2 = 0;
        htmlContent += "</tr><tr>";
    }
 
 
 
    // if counter is current day.
    // highlight current day using the CSS defined in header.
    if (counter == day){
        htmlContent +="<td class='dayNow'  onMouseOver='this.style.background=\"#FF0000\"; this.style.color=\"#FFFFFF\"' "+
        "onMouseOut='this.style.background=\"#FFFFFF\"; this.style.color=\"brown\"'>"+counter+"</td>";
    } else{
        htmlContent +="<td class='monthNow' onMouseOver='this.style.background=\"#FF0000\"'"+
        " onMouseOut='this.style.background=\"#FFFFFF\"'>"+counter+"</td>";    
    }
    weekdays2++;
    counter++;
 }
 

 // building the calendar html body.
 var calendarBody = "<table class='calendar'> <tr class='monthNow'><th colspan='7'>"
 +monthNames[month]+" "+ year +"</th></tr>";
 calendarBody +="<tr class='dayNames'>  <td>Man</td>  <td>Tir</td> <td>Ons</td>"+
 "<td>Tor</td> <td>Fre</td> <td>Lør</td> <td>Søn</td> </tr>";
 calendarBody += "<tr>";
 calendarBody += htmlContent;
 calendarBody += "</tr></table>";
 // set the content of div .
 document.getElementById("calendar").innerHTML=calendarBody;
}
window.onload = function(){
    displayCalendar();
document.getElementById("test").onclick =  function(){
    console.log("test");
};
document.getElementById("nextMonthButton").onclick = function nextMonthFunc(){
    nextOrPrev = nextOrPrev + 1;
    displayCalendar();
    console.log("Klikk next");
};
document.getElementById("prevMonthButton").onclick = function prevMonthFunc(){
    nextOrPrev = nextOrPrev - 1;
    displayCalendar();
    console.log("Klikk prev");
};
};