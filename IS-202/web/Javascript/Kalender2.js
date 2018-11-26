/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var nextOrPrev = 0;
var addedYears = 0;
var prevMonth = 0;
var kalH;
function displayCalendar(){
 
 
 var htmlContent ="";
 var FebNumberOfDays ="";
 var counter = 1;
 
 var dateNow = new Date();
 var currentMonth = dateNow.getMonth() + nextOrPrev;
 
 var year = dateNow.getFullYear();


 var dateThen = new Date(year, currentMonth);
 var month = dateThen.getMonth();
 
 if(month == 0 && prevMonth == 11){
     addedYears++;

 }else if(month == 11 && prevMonth == 0){
     addedYears--;

 };
 
 year = year + addedYears;
 
 prevMonth = month;

 
   //Used to match up the current month with the correct start date.

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
 var monthNames = ["Januar","Februar","Mars","April","Mai","Juni","Juli","August","September","Oktober","November", "Desember"];
 var dayNames = ["Mandag","Tirsdag","Onsdag","Torsdag","Fedag","Lørdag", "Søndag"];
 var dayPerMonth = ["31", ""+FebNumberOfDays+"","31","30","31","30","31","31","30","31","30","31"];
 
 
 // days in previous month and next one , and day of week.
 var nextDate = new Date(year, month, 0);
 var weekdays = nextDate.getDay();
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
    var i;
    var event = "";
    if(kalH != null){
        for(i = 0; i < kalH.length;i++){
            var khDato = new Date(kalH[i][1]);
            if(counter == khDato.getDate() && khDato.getMonth() == month){
                event = "<br>" + kalH[i][0];
            }
        }
    }
 
 
    // if counter is current day.
    // highlight current day using the CSS defined in header.
    if (counter == day){
        htmlContent +="<td class='dayNow'  onMouseOver='this.style.background=\"#FF0000\"; this.style.color=\"#FFFFFF\"' "+
        "onMouseOut='this.style.background=\"#FFFFFF\"; this.style.color=\"red\"'>"+counter + event+"</td>";
    } else{
        htmlContent +="<td class='monthNow' onMouseOver='this.style.background=\"#FF0000\"'"+
        " onMouseOut='this.style.background=\"#FFFFFF\"'>"+counter + event+"</td>";    
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
    kalH = kalenderHendelser;
    displayCalendar();

document.getElementById("nextMonthButton").onclick = function nextMonthFunc(){
    nextOrPrev = nextOrPrev + 1;
    displayCalendar();
};
document.getElementById("prevMonthButton").onclick = function prevMonthFunc(){
    nextOrPrev = nextOrPrev - 1;
    displayCalendar();
};
};
