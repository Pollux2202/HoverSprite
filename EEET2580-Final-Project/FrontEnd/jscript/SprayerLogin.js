async function loginForSprayer(event) {
    event.preventDefault(); // Prevent form submission

    // Retrieve values from input fields
    const name = document.getElementById('username').value;
    const pass = document.getElementById('password').value;

    // Construct the data object for authentication
    const authData = {
        emailOrPhoneNumber: name,
        password: pass
    };

    try {
        // Make a POST request to the server
        const response = await fetch('http://localhost:8081/sprayer/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(authData),
            credentials: 'include' // Include cookies in the request
        });

        // Check if the request was successful
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Parse the response to check for authentication status
        const data = await response.json();

        if (data.auth) {
            // If authentication is successful, redirect to the sprayer Home Page
            window.location.href = '../html/SprayerHomePage.html'; // Redirect to the home page
        } else {
            // Handle login failure, e.g., show an error message
            throw new Error('Authentication failed');
        }

    } catch (error) {
        console.error("Failed to login:", error);
        // Optionally display error message on the UI
    }
}

function directToGmailLogin() {
    window.location.href = "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2Fu%2F0%2F&emr=1&followup=https%3A%2F%2Fmail.google.com%2Fmail%2Fu%2F0%2F&osid=1&passive=1209600&service=mail&ifkv=Ab5oB3ohW-_2CFTGtum0InKciQRmqBaCnnUv_PMqgdKUp-JDIBlj4I1RFdcyIFipw_hHyTfp9ONvKw&ddm=0&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
}

function directToFacebookLogin() {
    window.location.href = "https://www.facebook.com/login.php/";
}
