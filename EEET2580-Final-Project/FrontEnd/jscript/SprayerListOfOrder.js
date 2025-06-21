var currentPage = 0;
var lastPage = 0;
var page_size = 5;
var countOrder;
var confirmID = 0;
var sprayerIDList = Array(2);
var sprayerEmailList = Array(2);
var select_flag = 0;
var total_cost = 0;
var receivedAmount = 0;

// Define the base URL for the spray orders service
const BASE_ORDER_SERVICE_URL = 'http://localhost:8081/sprayer/spray-orders';

// Function to build the paginated URL
function ORDER_SERVICE_URL_PAGINATION(page, size) {
    return `${BASE_ORDER_SERVICE_URL}?page=${page}&size=${size}`;
}

// Function to display orders by page
// Function to display orders by page
async function showOrderByPage(pageNo, pageSize) {
    lastPage = currentPage;
    currentPage = pageNo;

    // Get the URL from ORDER_SERVICE_URL_PAGINATION
    let orderResponse = await sendHttpRequest(ORDER_SERVICE_URL_PAGINATION(pageNo, pageSize), 'GET');
    
    // Access the json data correctly
    let orderData = orderResponse.json;
    
    // Extract orders and total elements
    currentOrderList = orderData.content;  // Extract orders for the current page
    countOrder = orderData.totalElements;  // Get total order count

    // Render table and pagination
    renderOrderTable(currentOrderList);
    pagination(orderData.totalPages);  // Update pagination with the total number of pages

    // Update active page styling
    document.getElementById('pagination' + lastPage).classList.remove("active");
    document.getElementById('pagination' + currentPage).classList.add("active");
}


// Function to render order rows
function renderOrderTable(OrderList) {
    let OrderTblBody = document.getElementById('OrderTblBody');
    OrderTblBody.innerHTML = ''; // Clear previous content

    // Check if content exists
    if (!OrderList || OrderList.length === 0) {
        OrderTblBody.innerHTML = '<tr><td colspan="10">No orders found</td></tr>';
        return;
    }

    // Iterate through each order and render the row
    OrderList.forEach(Order => {
        OrderTblBody.innerHTML += renderOrderRow(Order);
    });
}


// Helper function to render each order row
function renderOrderRow(Order) {
    let str = `
       <tr id="OrderRow${Order.sprayId}">
            <td id="idValue${Order.sprayId}">${Order.sprayId}</td>
            <td id="farmerEmailValue${Order.sprayId}">${Order.farmerEmail}</td>
            <td id="CostValue${Order.sprayId}">${Order.totalCost}</td>
            <td id="paymentValue${Order.sprayId}">${Order.paymentType}</td>
            <td id="StatusValue${Order.sprayId}">${Order.orderStatus}</td>
            <td id="LocationValue${Order.sprayId}">${Order.location}</td>
            <td id="cropTypeValue${Order.sprayId}">${Order.cropType}</td>
            <td id="DateValue${Order.sprayId}">${Order.date}</td>
            <td id="CalendarValue${Order.sprayId}">${Order.calenderType}</td>
            <td id="action">${renderActionButtons(Order.sprayId,Order.paymentType, Order.orderStatus, Order.totalCost)}</td>
        </tr>`;
    return str;
}

// Helper function to render action buttons for each order row
function renderActionButtons(id, paymentType, status, totalCost) {
    if (status === "ASSIGNED") {
        return `<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#confirmationWorkModal" onclick="confirmSetupWork(${id})">Start work</button>`;
    } else if (status === "IN_PROGRESS" && paymentType === "CASH") {
        return `<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#confirmationCashModal" onclick="confirmSetupPayment(${id},${totalCost}, '${paymentType}')">Confirm completed</button>`;
    } else if (status === "IN_PROGRESS" && paymentType === "CARD") {
        return `<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#confirmationOnlineModal" onclick="confirmSetupPayment(${id},${totalCost}, '${paymentType}')">Confirm completed</button>`;
    }
    return '';
}

// Pagination control function
function pagination(totalPages) {
    let string = '<ul class="pagination justify-content-center">';
    for (let i = 0; i < totalPages; i++) {
        string += `<li class="page-item"><a class="page-link" id="pagination${i}" onclick="showOrderByPage(${i}, ${page_size});">${i + 1}</a></li>`;
    }
    string += '</ul>';
    document.getElementById("dynamic_pagination").innerHTML = string;
    document.getElementById("dynamic_pagination").style.display = "inline";
}

// Function to confirm work setup
function confirmSetupWork(id) {
    confirmID = id;
    document.getElementById("userModalLabel").innerHTML = `Start to work on order ${confirmID}`;
}


// Function to set up payment confirmation
function confirmSetupPayment(id, totalCost, paymentType) {
    confirmID = id;
    total_cost = totalCost;
   

    if (paymentType === "CASH") {
        document.getElementById("totalCost").innerHTML = `${total_cost}`;
        document.getElementById("receivedAmount").value = ''; // Clear previous value
    } else if (paymentType === "CARD") {
        receivedAmount = total_cost;
        document.getElementById("OnlineConfirmation").innerHTML = `Confirmation: You have finished your work and will receive ${receivedAmount} in your online account`;
    }

    // Attach the confirmOrder function to the confirm button's onclick event
    

    console.log('Order ID for confirmation:', confirmID); // Debugging line
    document.getElementById("userModalLabel").innerHTML = `Confirm order ${confirmID} has finished`;
}


// Function to load the page initially
async function loadPage() {
    showOrderByPage(0, page_size);
}

// Pagination next and previous page controls
function nextPage() {
    currentPage++;
    showOrderByPage(currentPage, page_size);
}

function prevPage() {
    currentPage--;
    if (currentPage < 0) currentPage = 0;
    showOrderByPage(currentPage, page_size);
}


// Helper function to handle HTTP requests
async function sendHttpRequest(url, method = 'GET', body = null) {
    let response = await fetch(url, {
        headers: {
            "Content-Type": "application/json"
        },
        method: method,
        body: body,
        credentials: 'include'
    });

    let jsonData;
    try {
        jsonData = await response.json();
    } catch (e) {
        console.error("Error parsing JSON response", e);
        jsonData = {};
    }

    return {
        json: jsonData,
        status: response.status,
        responseHeader: response.headers
    };
}


// Function to assign sprayer
async function assignSprayer() {
    const response = await fetch("http://localhost:8081/spray-order/assign-sprayers", {
        method: "POST",
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "sprayId": assignOrder,
            "sprayerEmails": sprayerEmailList
        }),
    });
    console.log(response);
    showOrderByPage(currentPage, page_size);
}



function getChange() {
    let totalCost = parseFloat(document.getElementById('totalCost').innerText) || 0;
    let receivedAmount = parseFloat(document.getElementById('receivedAmount').value) || 0;
    let change = receivedAmount - totalCost;
    document.getElementById('change').innerText = change.toFixed(2); // Update change amount
}

async function confirmOrder() {
    // Check if orderId and receivedAmount are valid
    if (typeof confirmID !== 'number' || isNaN( confirmID)) {
        console.error('Invalid order ID:',  confirmID);
        alert('Invalid order ID.');
        return;
    }

    if (typeof receivedAmount !== 'number' || isNaN(receivedAmount)) {
        console.error('Invalid received amount:', receivedAmount);
        alert('Invalid received amount.');
        return;
    }

    // URL for the backend endpoint
    const url = `http://localhost:8081/sprayer/confirm/${ confirmID}`;

    // Create the request body
    const requestBody = {
        receivedAmount: receivedAmount
    };

    try {
        // Send the PUT request to the backend
        let response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody),
            credentials: 'include'
        });

        // Check if the response is OK
        if (response.ok) {
            let responseData = await response.json();
            console.log('Order confirmed:', responseData);

            // You may want to update the UI or notify the user
            alert('Order confirmed successfully!');
            showOrderByPage(currentPage, page_size); // Refresh the order list
        } else {
            console.error('Failed to confirm order:', response.statusText);
            alert('Failed to confirm order. Please try again.');
        }
    } catch (error) {
        console.error('Error confirming order:', error);
        alert('An error occurred while confirming the order. Please try again.');
    }
}

async function confirmWork() {
    const response = await fetch(`http://localhost:8081/spray-order/sprayer-confirmed/${confirmID}`, {
        method: "POST",
        credentials: 'include', // Include cookies for authentication if necessary
        headers: {
            'Content-Type': 'application/json'
        }
    });
    console.log(response);
    showOrderByPage(currentPage, page_size);
}

