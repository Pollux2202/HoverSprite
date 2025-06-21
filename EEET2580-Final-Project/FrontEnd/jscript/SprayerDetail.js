document.addEventListener('DOMContentLoaded', () => {
    const sprayerData = JSON.parse(localStorage.getItem('sprayerData'));

    if (sprayerData) {
        // Populate the profile details on the page
        document.getElementById('headerFullName').textContent = `${sprayerData.firstName} ${sprayerData.lastName}`;
        document.getElementById('sprayerId').textContent = sprayerData.sId;
        document.getElementById('fullName').textContent = `${sprayerData.firstName} ${sprayerData.lastName}`;
        document.getElementById('email').textContent = sprayerData.email;
        document.getElementById('phone').textContent = sprayerData.phoneNumber;
        document.getElementById('address').textContent = sprayerData.address;
        document.getElementById('expertise').textContent = sprayerData.sprayerExpertise;

    } else {
        console.error('No sprayer data found');
    }

    // Handle logout on the SprayerDetail page
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
                    localStorage.removeItem('sprayerData');
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
