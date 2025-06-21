document.addEventListener('DOMContentLoaded', () => {
    const receptionistData = JSON.parse(localStorage.getItem('receptionistData'));

    if (receptionistData) {
        // Populate the profile details on the page
        document.getElementById('headerFullName').textContent = `${receptionistData.firstName} ${receptionistData.lastName}`;
        document.getElementById('receptionistId').textContent = receptionistData.rId;
        document.getElementById('fullName').textContent = `${receptionistData.firstName} ${receptionistData.lastName}`;
        document.getElementById('email').textContent = receptionistData.email;
        document.getElementById('phone').textContent = receptionistData.phoneNumber;
        document.getElementById('address').textContent = receptionistData.address;
    } else {
        console.error('No receptionist data found');
    }

    // Handle logout on the ReceptionistDetail page
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
                    localStorage.removeItem('receptionistData');
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
