async function loadReceptionistTable() {
    const HOST_PORT = 8081;
    const HOST_URL = "http://localhost:" + HOST_PORT;
    const RECEPTIONIST_SERVICE_URL = `${HOST_URL}/farmer/receptionist_contacts`;

    // Fetch receptionists
    let ReceptionistResponse = await sendHttpRequest(RECEPTIONIST_SERVICE_URL);

    // Extract JSON data
    let currentReceptionistList = ReceptionistResponse.json;

    // Render receptionist table
    renderReceptionistTable(currentReceptionistList);
}

function renderReceptionistTable(ReceptionistList) {
    let count = 0;
    let ReceptionistTblBody = document.getElementById('ReceptionistTblBody');
    ReceptionistTblBody.innerHTML = '';

    ReceptionistList.forEach(Receptionist => {
        if (count % 2 == 0 && count != 0) {
            ReceptionistTblBody.innerHTML += `</div>`;
            count = 0;
        }

        if (count % 2 == 0) {
            ReceptionistTblBody.innerHTML += `<div class="row">`;
        }

        ReceptionistTblBody.innerHTML += renderReceptionistRow(Receptionist);
        count++;
    });

    // Close any unclosed row
    if (count % 2 !== 0) {
        ReceptionistTblBody.innerHTML += `</div>`;
    }
}

function renderReceptionistRow(Receptionist) {
    return `
        <div class="column">
            <div class="card" id="ReceptionistRow${Receptionist.id}">
                <div class="container">
                    <h2>Name: ${Receptionist.firstName} ${Receptionist.lastName}</h2>
                    <p>Phone Number: ${Receptionist.phoneNumber}</p>
                    <p>Email: ${Receptionist.email}</p>
                </div>
            </div>
        </div>
    `;
}

async function sendHttpRequest(url, method = 'GET', body = null) {
    let response = await fetch(url, {
        headers: {
            "Content-Type": "application/json"
        },
        method: method,
        body: body ? JSON.stringify(body) : null,
        credentials: 'include'
    });

    let jsonData = "{}";

    try {
        jsonData = await response.json();
    } catch (e) {
        console.error("Error parsing JSON response", e);
    }

    return {
        json: jsonData,
        status: response.status,
        responseHeader: response.headers
    };
}

// Call this function when the page loads
loadReceptionistTable();
