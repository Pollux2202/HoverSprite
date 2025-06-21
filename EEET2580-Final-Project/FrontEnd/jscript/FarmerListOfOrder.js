var assignOrder;
var currentPage = 0;
var lastPage = 0;
var page_size = 5;
var countProduct;
var chosenID = 0;
var sprayerIDList = Array.apply(null, Array(2));
var sprayerEmailList = Array.apply(null, Array(2));
var select_flag = 0;
var confirmID;
///////////////////////////////////


function goToOrder() {
    location.replace("SprayOrder.html");
}
async function showOrderByPage(pageNo, pageSize) {
    lastPage = currentPage;
    currentPage = pageNo;
    const HOST_PORT = 8081;
    const HOST_URL = "http://localhost:" + HOST_PORT
    const ORDER_SERVICE_URL = `${HOST_URL}/farmer/spray-orders`


    const ORDER_SERVICE_URL_ =
        function (pageNo, pageSize) {
            return ORDER_SERVICE_URL + `?sort=orderStatus&order=desc&pageNo=${pageNo}&pageSize=${pageSize}`
        }


    // document.getElementById("next").style.display = "inline";
    // document.getElementById("prev").style.display = "inline";
    let orderResponse = await sendHttpRequest(
        ORDER_SERVICE_URL_(pageNo, pageSize), 'GET'
    );

    currentOrderList = orderResponse.json;

    countOrder = orderResponse.json.totalElements;
    renderOrderTable(currentOrderList.content);


    /////////PAGINATION
    pagination();
    document.getElementById('pagination' + lastPage).classList.remove("active");
    document.getElementById('pagination' + currentPage).classList.add("active");



}


function renderOrderTable(OrderList) {

    let OrderTblBody = document.getElementById('OrderTblBody');

    OrderTblBody.innerHTML = '';

    OrderList.forEach(Order => {
        OrderTblBody.innerHTML += renderOrderRow(Order);

    });
    //////////////////////////////////////////

}

function renderOrderRow(Order) {

    let str = `
        <tr id="OrderRow${Order.sprayId}">

            <td id="idValue${Order.sprayId}">
                ${Order.sprayId}
            </td>
            
           
             <td id="CostValue${Order.sprayId}">
                ${Order.totalCost}
            </td>
             <td id="cropTypeValue${Order.sprayId}">
                ${Order.cropType}
            </td>
            
            <td id="DateValue${Order.sprayId}">
                ${Order.date}
            </td>
             <td id="CalendarValue${Order.sprayId}">
                ${Order.calenderType}
            </td>
   <td id="action">
                ${renderActionButtons(Order)}
            </td>
             </tr>
            `
    return str;

}

function renderActionButtons(order) {
    if (order.orderStatus === "IN_PROGRESS") {
     
        // Show both Confirm and Cancel buttons for pending orders
        return `
            <button type="button" class="btn btn-info"  data-toggle="modal" data-target="#confirmationCashModal" onclick="confirmSetup(${order.sprayId})">
                Confirm completed
            </button>`;
    }
    // Return an empty string for all other statuses (cancelled, assigned, completed)
    return '';
}

function confirmSetup(id){
    confirmID = id;
    document.getElementById("userModalLabel").innerHTML = `Confirm order ${confirmID} has finished`;
}

async function confirmOrder() {
    const response = await fetch(`http://localhost:8081/farmer/confirm/${confirmID}`, {
        method: "PUT",
        credentials: 'include', // Include cookies for authentication if necessary
        headers: {
            'Content-Type': 'application/json'
        }
    });
    console.log(response);
    showOrderByPage(pageNo, page_size);
}


//PAGINATION FUNCTION
function pagination() {

    let string = '';
    console.log(countOrder);
    string += "<ul class=\"pagination justify-content-center\">";
    for (let i = 0; i <= Math.round((countOrder) / page_size); i++) {


        string += `<li class="page-item"><a class="page-link" id ="pagination${i}" onclick="showOrderByPage(${i}, ${page_size});">${i + 1}</a></li>`;

    }


    string += `</ul>`;
    document.getElementById("dynamic_pagination").innerHTML = string;

    document.getElementById("dynamic_pagination").style.display = "inline";


}

async function loadPage() {

    showOrderByPage(0, 5);


}

function nextPage() {
    currentPage++
    showOrderByPage(currentPage, 5)

}


function prevPage() {
    currentPage--
    if (currentPage < 0)
        currentPage = 0
    showOrderByPage(currentPage, 5)

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


