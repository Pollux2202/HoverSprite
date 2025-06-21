var currentPage = 0;
var lastPage = 0;
var page_size = 5;
var countOrder = 0;
var assignOrder;
var sprayerIDList = Array(2);
var sprayerEmailList = Array(2);
var select_flag = 0;

// Input validation for checkboxes (Max 2 checked)
$('input[type=checkbox]').on('change', function (e) {
    if ($('input[type=checkbox]:checked').length > 2) {
        $(this).prop('checked', false);
        alert("Only 2 sprayers can be selected.");
    }
});

function goToOrder() {
    // Navigate to 'ReceptionistSprayOrder.html'
    window.location.href = '../html/SprayOrderReceptionist.html';
}

async function showOrderByPage(pageNo, pageSize) {
    lastPage = currentPage;
    currentPage = pageNo;

    const HOST_PORT = 8081;
    const ORDER_SERVICE_URL = `http://localhost:${HOST_PORT}/receptionist/spray-orders/all?page=${pageNo}&size=${pageSize}`;

    try {
        let orderResponse = await sendHttpRequest(ORDER_SERVICE_URL, 'GET');
        const orderData = await orderResponse.json();
        currentOrderList = orderData;

        if (currentOrderList && currentOrderList.content) {
            renderOrderTable(currentOrderList.content);
            countOrder = currentOrderList.totalElements;

            // Update pagination
            updatePagination();
            document.getElementById(`pagination${lastPage}`).classList.remove("active");
            document.getElementById(`pagination${currentPage}`).classList.add("active");
        } else {
            console.error('Invalid order response format:', orderData);
        }
    } catch (error) {
        console.error('Error fetching orders:', error);
    }
}

// Update pagination buttons dynamically
function updatePagination() {
    const totalPages = Math.ceil(countOrder / page_size);
    let paginationHtml = '<ul class="pagination justify-content-center">';

    for (let i = 0; i < totalPages; i++) {
        paginationHtml += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" id="pagination${i}" onclick="showOrderByPage(${i}, ${page_size});">${i + 1}</a>
            </li>`;
    }

    paginationHtml += '</ul>';
    document.getElementById('dynamic_pagination').innerHTML = paginationHtml;
    document.getElementById('dynamic_pagination').style.display = "inline";
}

// Render the table with orders
function renderOrderTable(orderList) {
    let orderTblBody = document.getElementById('OrderTblBody');
    orderTblBody.innerHTML = '';

    if (Array.isArray(orderList)) {
        orderList.forEach(order => {
            orderTblBody.innerHTML += renderOrderRow(order);
        });
    } else {
        console.error('Invalid order list:', orderList);
    }
}

// Render individual order row
function renderOrderRow(order) {
    return `
        <tr id="OrderRow${order.sprayId}">
            <td id="idValue${order.sprayId}">${order.sprayId}</td>
            <td id="FarmerEmailValue${order.sprayId}">${order.farmerEmail}</td>
            <td id="StatusValue${order.sprayId}">${order.orderStatus}</td>
            <td id="DateValue${order.sprayId}">${order.date}</td>
            <td id="CostValue${order.sprayId}">${order.calenderType}</td>
            <td id="CalendarTypeValue${order.sprayId}">${order.totalCost}</td>
            <td id="action">
                ${renderActionButtons(order)}
            </td>
        </tr>`;
}
function renderActionButtons(order) {
    if (order.orderStatus === "PENDING") {
        // Show both Confirm and Cancel buttons for pending orders
        return `
            <button type="button" class="btn btn-info" onClick="confirmOrder(${order.sprayId})">
                Confirm Order
            </button>
            <button type="button" class="btn btn-danger" onClick="cancelOrder(${order.sprayId})">
                Cancel Order
            </button>`;
    } else if (order.orderStatus === "CONFIRMED") {
        // Show Assign Sprayer button for confirmed orders
        return `
            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#assignSprayerModal" 
                data-spray-id="${order.sprayId}" onclick="setAssignOrderId(this)">
                Assign Sprayer
            </button>`;
    } 
    // Return an empty string for all other statuses (cancelled, assigned, completed)
    return '';
}

async function cancelOrder(id) {
    const HOST_PORT = 8081;
    const HOST_URL = `http://localhost:${HOST_PORT}`;
    const ORDER_SERVICE_URL = `${HOST_URL}/spray-order/cancel`;

    try {
        // Send a POST request to cancel the order
        let response = await fetch(`${ORDER_SERVICE_URL}/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                 // Include JWT token for authorization
            },
            credentials: 'include'
        });

        if (response.ok) {
            let data = await response.json();
            console.log('Order cancelled successfully:', data);
            // Optionally refresh the orders on the page after successful cancellation
            showOrderByPage(currentPage, page_size);
        } else {
            console.error('Error cancelling order:', response.statusText);
        }
    } catch (error) {
        console.error('Error cancelling order:', error);
    }
}

// Helper function to get the JWT token from cookies/local storage (replace with your implementation)
function getJwtToken() {
    // Example: Retrieve JWT token from local storage, cookie, or session storage
    return localStorage.getItem('token'); // or however you store the token
}


function setAssignOrderId(button) {
    assignOrder = button.getAttribute('data-spray-id');
    console.log("setAssignOrderId called with sprayId:", assignOrder);  // Debug log
}

// Pagination Controls
function pagination() {
    const prevButton = document.getElementById('prev');
    const nextButton = document.getElementById('next');
    const totalPages = Math.ceil(countOrder / page_size);

    // Enable/Disable buttons based on the current page
    if (prevButton) prevButton.style.display = currentPage > 0 ? 'inline' : 'none';
    if (nextButton) nextButton.style.display = (currentPage < totalPages - 1) ? 'inline' : 'none';
}

// Pagination Next/Previous Handlers
function nextPage() {
    currentPage++;
    showOrderByPage(currentPage, page_size);
}

function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        showOrderByPage(currentPage, page_size);
    }
}

// Load initial page
async function loadPage() {
    showOrderByPage(currentPage, page_size);
}

async function sendHttpRequest(url, method = 'GET', body = null) {
    let response = await fetch(url, {
        headers: { "Content-Type": "application/json" },
        method: method,
        body: body,
        credentials: 'include'
    });

    return response;
}

async function confirmOrder(id) {
    const HOST_PORT = 8081;
    const HOST_URL = `http://localhost:${HOST_PORT}`;
    const ORDER_SERVICE_URL = `${HOST_URL}/spray-order/confirm`;

    try {
        let response = await sendHttpRequest(`${ORDER_SERVICE_URL}/${id}`, 'PUT');
        console.log(response);
        if (response.status === 200) {
            showOrderByPage(currentPage, page_size);
        }
    } catch (error) {
        console.error('Error confirming order:', error);
    }
}


async function assignSprayer() {
    console.log("Assigning sprayers to sprayId:", assignOrder);  // Debug log

    const response = await fetch("http://localhost:8081/spray-order/assign-sprayers", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "sprayId": assignOrder,  // Pass the sprayId
            "sprayerEmails": sprayerEmailList
        }),
        credentials: 'include'
    });

    console.log(response);
    if (response.status === 200) {
        showOrderByPage(currentPage, page_size);
    }
}


function showSprayerSort(pageNo = 0) {

    const HOST_PORT = 8081;
    const HOST_URL = `http://localhost:${HOST_PORT}`;
    const SPRAYER_SERVICE_URL = `${HOST_URL}/sprayer/paged?week=1&page=${pageNo}&size=10`; // Example URL with pagination and week parameter

    sendHttpRequest(SPRAYER_SERVICE_URL, 'GET')
        .then(response => response.json())
        .then(data => {
            // Assuming data contains page information and content
            renderSprayerTable(data.content);
            renderPagination(data.totalPages, pageNo);
        })
        .catch(error => console.error('Error fetching sprayers:', error));
}

function renderSprayerTable(sprayerList) {
    let sprayerTblBody = document.getElementById('SprayerTblBody');
    sprayerTblBody.innerHTML = '';

    sprayerList.forEach(sprayer => {
        sprayerTblBody.innerHTML += renderSprayerRow(sprayer);

    });


}


function renderPagination(totalPages, currentPage) {
    let paginationHtml = '<ul class="pagination">';
    for (let i = 0; i < totalPages; i++) {
        paginationHtml += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                              <a class="page-link" href="#" onclick="showSprayerSort(${i})">${i + 1}</a>
                           </li>`;
    }
    paginationHtml += '</ul>';
    document.getElementById('sprayerPagination').innerHTML = paginationHtml;
}

function renderSprayerRow(sprayer) {
    // Combine first name and last name to form the full name
    const fullName = `${sprayer.firstName} ${sprayer.lastName}`;
    
    return `
        <tr id="SprayerRow${sprayer.sId}">
            <td id="nameValue${sprayer.sId}">${fullName}</td>
            <td id="emailValue${sprayer.sId}">${sprayer.email}</td>
            <td id="ExpertiseValue${sprayer.sId}">${sprayer.sprayerExpertise}</td>
            <td id="assign">
                <input type="checkbox" id="${sprayer.sId}" name="sprayer" onchange="getSprayer('${sprayer.sId}', '${sprayer.email}')">
            </td>
        </tr>
    `;
}


$('#assignSprayerModal').on('show.bs.modal', function (event) {
    showSprayerSort(); // Load the first page of sprayers when the modal opens
});

function getSprayer(id, email) {
    if (select_flag === 0) {
        sprayerIDList[0] = id;
        sprayerEmailList[0] = email;
        select_flag = 1;
    } else {
        sprayerIDList[1] = id;
        sprayerEmailList[1] = email;
        select_flag = 0;
    }
    console.log("Selected sprayer emails:", sprayerEmailList);  // Debug log
}





