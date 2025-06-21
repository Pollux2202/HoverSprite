
var cropType = document.getElementById("dropdownMenu").innerHTML;
var farmArea;
var currentPayment = "CASH";
var farmLocation;
var farmerEmail;
var date = new Date();
var calendar = "GREGORIAN";
var start; //start time
var end; //end time
var sessionNum;
var sesOneAvailable; //availability of session 1
var sesTwoAvailable; //availability of session 2
var subSessionType;

////////////////////////////////////
const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

///////////////////////////////////
$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip()
})




$(function () {
    $("#datepicker").datepicker({
        autoclose: true,
        todayHighlight: true,
    }).datepicker('update', new Date());
});


var currentTab = 0; // Current tab is set to be the first tab (0)
var flag = false;

showTab(currentTab); // Display the current tab


function showTab(n) {
    // Display the specified tab
    var tabs = document.getElementsByClassName("tab");
    if (tabs.length > 0) {
        tabs[n].style.display = "block";
    }

    // Fix Previous/Next buttons
    var prevBtn = document.getElementById("prevBtn");
    var nextBtn = document.getElementById("nextBtn");
    
    if (n == 0) {
        prevBtn.style.display = "none";
    } else {
        prevBtn.style.display = "inline";
    }
    
    if (n == (tabs.length - 1)) {
        nextBtn.innerHTML = "Submit";
        // Add click listener to redirect when "Submit" is clicked
        nextBtn.onclick = function() {
            // Redirect to ReceptionistHomePage
            window.location.href = "ReceptionistHomePage.html"; // Change to your actual page URL
        };
    } else {
        nextBtn.innerHTML = "Next";
        // Reset the next button behavior to move to the next tab
        nextBtn.onclick = function() {
            nextPrev(1); // Function that moves to the next tab
        };
    }
    
    // Update step indicators
    fixStepIndicator(n);
}




function validateForm() {
    // This function deals with validation of the form fields
    var x, y, i, valid = true;
    x = document.getElementsByClassName("tab");
    y = x[currentTab].getElementsByTagName("input");
    // A loop that checks every input field in the current tab:
    for (i = 0; i < y.length; i++) {
        // if empty field
        if (y[i].value == "") {
            // add an "invalid" class to the field:
            y[i].className += " invalid";
            // and set the current valid status to false
            valid = false;
        }
    }
    // If the valid status is true, mark the step as finished and valid:
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    return valid; // return the valid status
}

function nextPrev(n) {
    // This function will figure out which tab to display
    if (!flag) {
        // Get all the tabs
        var x = document.getElementsByClassName("tab");

        // Exit the function if any field in the current tab is invalid:
        if (n == 1 && !validateForm()) return false;

        // Hide the current tab
        x[currentTab].style.display = "none";

        // Increase or decrease the current tab by 1
        currentTab = currentTab + n;

        // If reached the end of the form, return false
        if (currentTab >= x.length && !flag) {
            return false; // Stop the function here
        }

        // In the second step, show the summary (adjust this according to your flow)
        if (currentTab == 1) {
            setSummary();
        }

        // On the last step, call API and process payment
        if (currentTab == x.length - 1) {
            setSummary(); // Ensure the summary is set
            initialize(); // Trigger the farmer_created API call

            // Get the selected payment type from the dropdown
            var paymentButton = document.getElementById("dropdownMenu1");
            var selectedPayment = paymentButton.innerText.trim(); // The selected payment type (CASH or CARD)

            // Check if the payment method is CARD before initializing Stripe
            if (selectedPayment === "CARD") {
                console.log("Receptionist cannot choose CARD");// Handle payment processing for CARD
            }
        }

        // Show the current tab
        showTab(currentTab);
    } else if (flag) {
        // If flag is true, show an alert and reset the flag
        window.alert("Return to main menu");
        flag = false;
        // Optionally, handle return to main menu functionality
        // return_initialize();
    }
}

function setSummary() {
    // Ensure the date variable is properly initialized and adjusted
    if (date) {
        // Create a copy of the date and subtract one day
        let adjustedDate = new Date(date); // Create a copy to avoid modifying the original
        adjustedDate.setDate(adjustedDate.getDate() - 1);

        // Get the month and day names for the adjusted date
        let monthValue = months[adjustedDate.getMonth()];
        let dayValue = days[adjustedDate.getDay()];

        // Update the HTML elements with the adjusted values
        document.getElementById("farmerEmail_variable").innerHTML = farmerEmail;
        document.getElementById("calendar_variable").innerHTML = calendar;
        document.getElementById("dateTime_variable").innerHTML = `${dayValue}, ${adjustedDate.getDate()}/${monthValue}/${adjustedDate.getFullYear()} - ${start}:00 to ${end}:00`;
        document.getElementById("session_variable").innerHTML = sessionNum;
        document.getElementById("area_variable").innerHTML = `${farmArea} decares`;
        document.getElementById("crop_variable").innerHTML = cropType;
        document.getElementById("location_variable").innerHTML = farmLocation;
        document.getElementById("payment_variable").innerHTML = currentPayment;
        document.getElementById("total_variable").innerHTML = `<b>Total: ${30000 * farmArea} VND</b>`;
    } else {
        console.error("Date is not defined.");
    }
}

function fixStepIndicator(n) {
    // Get all the step indicators
    var steps = document.getElementsByClassName("step");

    // Ensure n is within bounds
    if (n >= 0 && n < steps.length) {
        // Remove the "active" class from all steps
        for (var i = 0; i < steps.length; i++) {
            steps[i].className = steps[i].className.replace(" active", "");
        }
        // Add the "active" class on the current step
        steps[n].className += " active";
    } else {
        console.error("Invalid step index:", n);
    }
}



function toggleButton() {
    var x = document.getElementById("myDIV");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}
//CHANGE CROP TYPE
function changeItem(item) {
    document.getElementById("dropdownMenu").innerHTML = item.innerHTML;
    cropType = item.innerHTML;
}

//CHANGE PAYMENT TYPE
function changePayment(item) {

    document.getElementById("dropdownMenu1").innerHTML = item.innerHTML;
    currentPayment = item.innerHTML;
}

function getFarmArea() {
    farmArea = document.getElementById("area").value;

}

function getFarmLocation() {
    farmLocation = document.getElementById("location").value;
}



function getCalendarMode(value){
    calendar = value;
    window.alert("You picked " + calendar + " calendar");
}

function getDate() {
    // Get the selected date from the date picker
    let selectedDate = $("#datepicker").datepicker("getDate");
    
    if (selectedDate) {
        // Subtract one day from the selected date
        selectedDate.setDate(selectedDate.getDate() + 1);
        
        // Update the global `date` variable with the adjusted date
        date = selectedDate;

        // Return the adjusted date
        return date;
    }
    
    // Return null if no date is selected
    return null;
}



///////////////////////////// 
// function modal() {
//     // Get the modal
//     var modal = document.getElementById("myModal");

//     // Get the link that opens the modal
//     var link = document.getElementById("myLink");

//     // Get the <span> element that closes the modal
//     var span = document.getElementsByClassName("close")[0];

//     // When the user clicks the button, open the modal 

//     modal.style.display = "block";


//     // When the user clicks on <span> (x), close the modal
//     span.onclick = function () {
//         modal.style.display = "none";
//     }

//     // When the user clicks anywhere outside of the modal, close it
//     window.onclick = function (event) {
//         if (event.target == modal) {
//             modal.style.display = "none";
//         }
//     }
// }

function getOrderDetail() {
   
    getFarmArea();
    getFarmLocation();
    getDate();

    console.log("Calendar: " + calendar);
    console.log("Payment Type: " + currentPayment);
    console.log("Farm area: " + farmArea);
    console.log("Farm location: " + farmLocation);
    console.log("Date: " + date);
}

function getFarmerEmail() {
    // Get the input element by its ID
    const emailInput = document.getElementById('farmerEmail');

    // Retrieve the value of the input field
    farmerEmail = emailInput.value;

    // For demonstration, you can log the email value to the console
    console.log('Farmer email:', farmerEmail);
}

async function initialize() {
    if (sesOneAvailable){
        subSessionType = "SUB_SESSION_1";
    }
    else if(sesTwoAvailable){
        subSessionType = "SUB_SESSION_2";
    }
    const formatTime = (time) => {
        // Assuming time is in 24-hour format. Adjust if needed.
        return time < 10 ? `0${time}:00` : `${time}:00`;
    };
    const cleanedCropType = cropType.trim().replace(/\n/g, "");
    const formattedStartTime = formatTime(start);
    const formattedEndTime = formatTime(end);
    const formattedDate = new Date(date).toISOString().split('T')[0]; 
    const payload = {
        "farmer": {
            "email": farmerEmail
          },
        "cropType": cleanedCropType,
        "farmArea": farmArea,
        "paymentType": currentPayment,
        "location": farmLocation,
        "timeSlot": {
            "date": formattedDate, // Date as a string
            "startTime": formattedStartTime,
            "endTime": formattedEndTime
        },
        "calenderType": calendar,
        "subSessionType": subSessionType
    };
    
    console.log("Payload being sent to server:", payload);

    try {
        const response = await fetch("http://localhost:8081/spray-order/create", {
            method: "POST",
            credentials: 'include', // Include cookies for authentication if necessary
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
        });

        if (response.ok) {
            const responseBody = await response.json();

            // Log the full response to check its structure
            console.log("Full response from backend:", responseBody);

            // Handle the response from the backend
            // For example, you might want to display a success message or update the UI
            alert("Order successfully created!");

            // If needed, you can use responseBody to further process the result
        } else {
            console.error("Failed to create a checkout session. Response status:", response.status);
        }
    } catch (error) {
        console.error("Error during initialization:", error);
    }
}


//WebHook








async function return_initialize() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const sessionId = urlParams.get('session_id');
    const response = await fetch(`/session-status?session_id=${sessionId}`);
    const session = await response.json();

    if (session.status == 'open') {
        window.replace('http://localhost:8081/Homepage.html')
    } else if (session.status == 'complete') {
        document.getElementById('success').classList.remove('hidden');
        document.getElementById('customer-email').textContent = session.customer_email
    }
}
////////////////NOTE: THE BELOW FUNCTIONS HAVEN'T BEEN TESTED YET//////////////////////////////

async function getSchedule() {
    // Construct the URL with the selected date and calendar type
    const formattedDate = date.toISOString().split('T')[0]; // Format date to yyyy-mm-dd
    const url = `http://localhost:8081/spray-sessions/getDate?date=${formattedDate}&calendarType=${calendar}`;
   
    try {
        const scheduleResponse = await sendHttpRequest(url, 'GET');
        const scheduleList = scheduleResponse.json;

        renderscheduleTable(scheduleList);
    } catch (error) {
        console.error("Error fetching schedule:", error);
    }
}

function renderscheduleTable(scheduleList) {
    let countRow = 0;

    let scheduleBody = document.getElementById('scheduleBody');
    scheduleBody.innerHTML = '';

    scheduleList.forEach(schedule => {
        if (countRow === 0) {
            scheduleBody.innerHTML += `  <tr>
                                <th scope="row" class="table-warning">Morning</th>
                                <th scope="row" class="table-warning"></th>
                                <th scope="row" class="table-warning"></th>
                            </tr>`;
        } else if (countRow === 4) {
            scheduleBody.innerHTML += `   <tr>
                                <th scope="row" class="table-info">Afternoon</th>
                                <th scope="row" class="table-info"></th>
                                <th scope="row" class="table-info"></th>
                            </tr>`;
        }
        scheduleBody.innerHTML += renderscheduleRow(schedule);
        countRow++;
    });
}

//schedule = spraying session
function renderscheduleRow(schedule) {
    return renderscheduleRowHelper(schedule.startTime, schedule.endTime, schedule.subSession1Booked, schedule.subSession2Booked);
}

function renderscheduleRowHelper(startTime, endTime, sessionOneStatus, sessionTwoStatus) {
    let returnString = `  <tr> <th scope="row">${startTime} to ${endTime}</th>`;
    returnString += renderAvailablity(startTime, endTime, sessionOneStatus, 1); // Session 1
    returnString += renderAvailablity(startTime, endTime, sessionTwoStatus, 2); // Session 2
    returnString += `</tr>`;

    return returnString;
}

//Get sub session availability
function renderAvailablity(startTime, endTime, status, sub) {
    let string;
    if (status) { // If session status is already booked
        string = ` <td class="Unavailable_Cell"><span class="Unavailable"> (Unavailable)</span></td>`;
    } else {
        string = `<td><a href="javascript:getTime('${startTime}','${endTime}',${sub})" id="myLink" class="disguise_link"> <span class="Available">(Available)</span></a></td>`;
    }
    return string;
}

function getTime(startTime, endTime, session) {
    var startConvert = startTime.split(':');
    var endConvert = endTime.split(':');
    start = Number(startConvert[0]);
    end = Number(endConvert[0]);
 
    if (session == 1) {
        sesOneAvailable = true;
 
    }
    else if (session == 2) {
        sesTwoAvailable = true;
    }
 
    sessionNum = session;
    window.alert(`You have picked ${start}:00 - ${end}:00 at session ${sessionNum}`);
 
}

$("#datepicker").on("changeDate", function () {
    getDate(); // Update the date variable
    getSchedule(); // Fetch and display the updated schedule
});
/////////////////////////////////////////////////////////////////////
async function sendHttpRequest(
    url,
    method = 'GET',
    body = null
) {

    let response = await fetch(url, {
        headers: {
            "Content-Type": "application/json"
        },
        method: method,
        body: body

    })

    let jsonData = ""

    try {
        jsonData = await response.json()
    } catch {
        jsonData = "{}"
    }

    return {
        json: jsonData,
        status: response.status,
        responseHeader: response.headers
    }
}

