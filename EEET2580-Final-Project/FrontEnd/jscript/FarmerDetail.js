document.addEventListener('DOMContentLoaded', () => {
    const farmerData = JSON.parse(localStorage.getItem('farmerData'));

    if (farmerData) {
        // Populate the profile details on the page
        document.getElementById('headerFullName').textContent = `${farmerData.firstName} ${farmerData.lastName}`;
        document.getElementById('customerId').textContent = farmerData.fId;
        document.getElementById('fullName').textContent = `${farmerData.firstName} ${farmerData.lastName}`;
        document.getElementById('email').textContent = farmerData.email;
        document.getElementById('phone').textContent = farmerData.phoneNumber;
        document.getElementById('address').textContent = farmerData.address;
    } else {
        console.error('No farmer data found');
    }

    // Handle logout on the FarmerDetail page
    const logoutButton = document.querySelector('#logout-link .logout-link');
    if (logoutButton) {
        logoutButton.addEventListener('click', async (event) => {
            event.preventDefault();
            try {
                const response = await fetch('http://localhost:8081/auth/logout', {
                    method: 'POST',
                    credentials: 'include'
                });

                if (response.ok) {
                    localStorage.removeItem('farmerData');
                    window.location.href = '../html/RoleSelectionLogin.html';
                } else {
                    console.error('Failed to log out:', response.statusText);
                }
            } catch (error) {
                console.error('Logout error:', error);
            }
        });
    }
});
