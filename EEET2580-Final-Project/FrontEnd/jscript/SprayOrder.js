
var cropType = document.getElementById("dropdownMenu").innerHTML;
var currentPayment = "CASH";
var farmArea;
var farmLocation;
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

const stripe = Stripe("pk_test_51PuaxhP0Z87jNI1yWBOQzW0574HQdA5iJZg83nRmruLYIWRjNJFdqeQniEOyLatmJbHoL0rDySymMZQb350jt7ad0018crpsBh");
var currentTab = 0; // Current tab is set to be the first tab (0)
var flag = false;

showTab(currentTab); // Display the current tab


function showTab(n) {
    // This function will display the specified tab of the form...
    var x = document.getElementsByClassName("tab");
    x[n].style.display = "block";
    
    //... and fix the Previous/Next buttons:
    if (n == 0) {
        document.getElementById("prevBtn").style.display = "none";
    } else {
        document.getElementById("prevBtn").style.display = "inline";
    }

    if (n == (x.length - 1)) {
        document.getElementById("prevBtn").style.display = "none"; // Optional: hide prev on final step
        document.getElementById("nextBtn").innerHTML = "Submit";
        // Add redirection logic when "Submit" is clicked
        document.getElementById("nextBtn").onclick = function() {
            // Redirect to FarmerHomePage
            window.location.href = "FarmerHomePage.html"; // Change to your actual file path
        };
    } else {
        document.getElementById("nextBtn").innerHTML = "Next";
        // Reset the "Next" button functionality to move between tabs
        document.getElementById("nextBtn").onclick = function() {
            nextPrev(1); // Assuming you have a nextPrev function for navigation
        };
    }

    
    fixStepIndicator(n);
}


function stripePayment() {
    getOrderDetail();
    if ("CASH".localeCompare(currentPayment) == 0) {
        document.getElementById("cash").style.display = "inline";
        document.getElementById("checkout").style.display = "none";
        flag = true;
        document.getElementById("prevBtn").style.display = "none";
        document.getElementById("nextBtn").innerHTML = "Main Page";

    }
    else {
        document.getElementById("cash").style.display = "none";
        document.getElementById("checkout").style.display = "inline";
        initialize();
    }




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
                stripePayment(); // Handle payment processing for CARD
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





function fixStepIndicator(n) {
    // This function removes the "active" class of all steps...
    var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        x[i].className = x[i].className.replace(" active", "");
    }
    //... and adds the "active" class on the current step:
    x[n].className += " active";
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



function getCalendarMode(value){
    calendar = value;
    window.alert("You picked " + calendar + " calendar");
    getSchedule(); // Fetch and display the updated schedule
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


async function initialize() {
    const paymentButton = document.getElementById("dropdownMenu1");
    const paymentType = paymentButton.innerText.trim(); // "CASH" or "CARD"

    if (sesOneAvailable) {
        subSessionType = "SUB_SESSION_1";
    } else if (sesTwoAvailable) {
        subSessionType = "SUB_SESSION_2";
    }

    const cleanedCropType = cropType.trim().replace(/\n/g, "");
    const formattedDate = new Date(date).toISOString().split('T')[0];
    const formatTime = (time) => time < 10 ? `0${time}:00` : `${time}:00`;
    const formattedStartTime = formatTime(start);
    const formattedEndTime = formatTime(end);

    const payload = {
        cropType: cleanedCropType,
        farmArea: farmArea,
        paymentType: paymentType,
        location: farmLocation,
        timeSlot: {
            date: formattedDate,
            startTime: formattedStartTime,
            endTime: formattedEndTime
        },
        calenderType: calendar,
        subSessionType: subSessionType
    };

    console.log("Payload being sent to server:", payload);

    try {
        const response = await fetch("http://localhost:8081/spray-order/farmer_created", {
            method: "POST",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
        });

        if (response.ok) {
            const responseBody = await response.json();
            console.log("Full response from backend:", responseBody);

            const sessionId = responseBody.stripeId; // Adjust as per actual response

            if (paymentType === "CARD") {
                if (sessionId) {
                    const result = await stripe.redirectToCheckout({ sessionId: sessionId });

                    if (result.error) {
                        console.error(result.error.message);
                    }
                } else {
                    console.error("Session ID not found in response");
                }
            } else {
                console.log("Payment method is CASH, skipping Stripe checkout.");
            }
        } else {
            console.error("Failed to create a checkout session. Response status:", response.status);
        }
    } catch (error) {
        console.error("Error during Stripe checkout initialization:", error);
    }
}





//WebHook

async function triggerStripeWebhook(sessionId, eventType) {
    try {
        const testWebhookPayload = {
            id: sessionId,
            object: 'event',
            type: eventType,
            data: {
                object: {
                    id: sessionId,
                    payment_status: eventType === 'checkout.session.completed' ? 'paid' : 'canceled'
                }
            }
        };

        const response = await fetch("http://localhost:8081/webhook/stripe", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Stripe-Signature': 'test_signature' // Use a valid signature or skip for testing
            },
            body: JSON.stringify(testWebhookPayload)
        });

        if (response.ok) {
            console.log("Webhook successfully handled. Response:", await response.text());
        } else {
            console.error("Failed to handle webhook. Status:", response.status, "Response:", await response.text());
        }
    } catch (error) {
        console.error("Error triggering Stripe webhook:", error);
    }
}






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
 
// Call getSchedule when the date or calendar type is changed
$("#datepicker").on("changeDate", function () {
    getDate(); // Update the date variable
    getSchedule(); // Fetch and display the updated schedule
});



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

