async function checkToken() {
    try {
        const response = await fetch('http://localhost:8081/auth/verifyToken', {
            method: 'GET',
            credentials: 'include' // Include cookies in the request
        });

        if (response.ok) {
            const data = await response.json();
            if (data.isValid && !data.isExpired) {
                console.log('Token is valid and not expired');
                // Continue with the current page
            } else {
                console.log('Token is invalid or expired');
                window.location.href = '../html/SprayerLogin.html'; // Redirect to login page
            }
        } else {
            console.log('Error checking token');
            window.location.href = '../html/SprayerLogin.html'; // Redirect to login page
        }
    } catch (error) {
        console.error('Failed to check token', error);
        window.location.href = '../html/SprayerLogin.html'; // Redirect to login page
    }
}


checkToken();