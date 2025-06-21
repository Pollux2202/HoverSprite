var currentTab = 0; // Current tab is set to be the first tab (0)
var flag = false;
var attentive = '0' //value for attentiveness rating
var friendly = '0' //value for friendliness rating
var pro = '0' // value for professionalism rating
var sastified = '0'; //value for satisfaction rating
var orderID = localStorage.getItem("orderID"); //value for  orderID (obtained through sessionStorage)
var description = " ";
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
        document.getElementById("nextBtn").innerHTML = "Submit";
    } else {
        document.getElementById("nextBtn").innerHTML = "Next";
    }
    //... and run a function that will display the correct step indicator:
    fixStepIndicator(n)
}

function logResponse() {


    var title = document.getElementById("title");
    title.innerHTML = "Thank you for your feedback";

    var description = document.getElementById("description");
    description.innerHTML = "We look forward to serve you again";

    document.getElementById("prevBtn").style.display = "none";
    document.getElementById("nextBtn").innerHTML = "Main Page";
    flag = true;
}
function nextPrev(n) {
    // This function will figure out which tab to display
    if (!flag) {
        var x = document.getElementsByClassName("tab");
        // Exit the function if any field in the current tab is invalid:
        if (n == 1 && !validateForm()){ 
            window.alert("Please fill in all responses");
            return false;
        }
        // Hide the current tab:
        x[currentTab].style.display = "none";
        // Increase or decrease the current tab by 1:
        currentTab = currentTab + n;

        //  Reached the end of the form
        if (currentTab >= x.length && !flag) {
            // ... the form gets submitted:
            // document.getElementById("form").submit();
            logResponse();
            return false;
        }
        // Otherwise, display the correct tab:
        showTab(currentTab);
    }
    else if (flag) {
        flag = false;
        sendFeedBackToBackend();
    }

}

function validateForm() {
    // This function deals with validation of the form fields
    var valid = false;
    
    if (currentTab == 0) {
        var x, y, z;
        let attentiveArray = document.getElementsByName('rating_attentive');

        let friendlyArray = document.getElementsByName('rating_friend');
        let proArray = document.getElementsByName('rating_pro');
        for (let i = 0; i < 5; i++) {
            if (attentiveArray[i].checked) {
                x = true;
                
            }
            if (friendlyArray[i].checked) {
                y = true;
               
            }
            if (proArray[i].checked) {
                z = true;
               
            }

        }
        if (x == true && y == true && z == true) {
            valid = true;
        }
    }
    else if (currentTab == 1) {
        let t, f; //placeholder variable
        let sasArray = document.getElementsByName('rating_satisfaction');
        for (let i = 0; i < 5; i++) {
            if (sasArray[i].checked) {
                t = true;
                break;
            }
        }
           description = document.getElementById("description_textArea").value;
           if(description.length != 0){
                f = true;
           }
           else{
            f = false;
           }
           if(t && f){
            valid = true;
           }
    }


    // If the valid status is true, mark the step as finished and valid:
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    console.log(valid);
    return valid; // return the valid status
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

function getFeedBack() {
    let attentiveArray = document.getElementsByName('rating_attentive');

    let friendlyArray = document.getElementsByName('rating_friend');
    let proArray = document.getElementsByName('rating_pro');

    let sasArray = document.getElementsByName('rating_satisfaction');
    for (let i = 0; i < 5; i++) {
        if (attentiveArray[i].checked) {
            attentive = attentiveArray[i].value;
        }
        if (friendlyArray[i].checked) {
            friendly = friendlyArray[i].value;
        }
        if (proArray[i].checked) {
            pro = proArray[i].value;
        }
        if (sasArray[i].checked) {
            sastified = sasArray[i].value;
        }
    }
    console.log("Order Id: " + orderID);
    console.log("Description: " + description);
    console.log("Attentive rate: " + attentive);
    console.log("Friendly rate: " + friendly);
    console.log("Pro rate: " + pro);
    console.log("Satisfaction rate: " + sastified);
}

async function sendFeedBackToBackend(){
    getFeedBack(); //Get feedback
    var response;
   
   
    response = await sendHttpRequest('http://localhost:8081/feedback/submit',
        "POST",
        JSON.stringify({
            orderId: orderID,
            description: description,
            attentiveRating: Number(attentive),
            friendlyRating: Number(friendly),
            professionalRating: Number(pro)

        })
    )

console.log(response);
if (response.status == 200) {
    orderID = '';
    description = " ";
    attentive = '';
    friendly = '';
    pro = '';
   
}
window.alert("Return to main menu");
location.replace("FarmerHomePage.html");
}

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
        body: body,
        credentials: 'include'
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

