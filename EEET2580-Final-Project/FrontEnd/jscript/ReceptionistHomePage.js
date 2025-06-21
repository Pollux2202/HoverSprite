document.addEventListener('DOMContentLoaded', async () => {
    try {
        // Fetch and verify the JWT token
        const response = await fetch('http://localhost:8081/auth/verifyToken', {
            method: 'GET',
            credentials: 'include' // Send JWT token stored in the HttpOnly cookie
        });

        if (!response.ok) {
            throw new Error('Session invalid or expired');
        }

        // If the token is valid, proceed
        console.log('User session is valid.');

        // When "Profile" button is clicked
        const profileSectionLink = document.querySelector('.profile-section-link');
        if (profileSectionLink) {
            profileSectionLink.addEventListener('click', async (event) => {
                event.preventDefault();
                // Fetch receptionist details from the server using the token
                const receptionistResponse = await fetch('http://localhost:8081/receptionist/email/{email}', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (receptionistResponse.ok) {
                    const receptionistData = await receptionistResponse.json();
                    // Save receptionist data to localStorage for use on ReceptionistDetail page
                    localStorage.setItem('receptionistData', JSON.stringify(receptionistData));
                    window.location.href = '../html/ReceptionistDetail.html'; // Redirect to the ReceptionistDetail page
                } else {
                    console.error('Failed to fetch receptionist data');
                }
            });
        }

        const profileButton = document.querySelector('#profileDropdownMenu .profile-link');
        if (profileButton) {
            profileButton.addEventListener('click', async (event) => {
                event.preventDefault();
                // Fetch receptionist details from the server using the token
                const receptionistResponse = await fetch('http://localhost:8081/receptionist/email/{email}', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (receptionistResponse.ok) {
                    const receptionistData = await receptionistResponse.json();
                    // Save receptionist data to localStorage for use on FarmerDetail page
                    localStorage.setItem('receptionistData', JSON.stringify(receptionistData));
                    window.location.href = '../html/ReceptionistDetail.html'; // Redirect to the FarmerDetail page
                } else {
                    console.error('Failed to fetch receptionist data');
                }
            });
        }

        // Add event listeners for View Sprayer and View Farmer buttons
        document.getElementById('viewSprayer').addEventListener('click', () => {
            fetchAndDisplayData('sprayers');
        });

        document.getElementById('viewFarmer').addEventListener('click', () => {
            fetchAndDisplayData('farmers');
        });

        // Function to fetch and display data in the modal
        async function fetchAndDisplayData(type) {
            const endpoint = type === 'sprayers' ? '/receptionist/view/sprayers' : '/receptionist/view/farmers';
            try {
                const response = await fetch(`http://localhost:8081${endpoint}`, {
                    method: 'GET',
                    credentials: 'include'
                });

                if (response.ok) {
                    const data = await response.json();
                    displayDataInModal(data, type);
                } else {
                    console.error(`Failed to fetch ${type} data:`, response.statusText);
                }
            } catch (error) {
                console.error(`Error fetching ${type} data:`, error);
            }
        }

        const viewFeedbackLink = document.getElementById('viewFeedbackLink');

        if (viewFeedbackLink) {
            viewFeedbackLink.addEventListener('click', () => {
                // Redirect to the Feedback list page
                openFeedbackModal();
            });
        }
        // Function to display data in the modal
        function displayDataInModal(data, type) {
            const listContent = document.getElementById('listContent');
            listContent.innerHTML = ''; // Clear existing content

            // Display fetched data
            data.content.forEach(user => {
                const userElement = document.createElement('div');
                userElement.className = 'mb-2 p-2 border-bottom';
            
                userElement.innerHTML = type === 'sprayers' ?
                    `<strong>ID:</strong> ${user.sId}, <strong>Name:</strong> ${user.firstName} ${user.lastName}, <strong>Phone:</strong> ${user.phoneNumber}, <strong>Email:</strong> ${user.email}, <strong>Expertise:</strong> ${user.sprayerExpertise}.` :
                    `<strong>ID:</strong> ${user.fId}, <strong>Name:</strong> ${user.firstName} ${user.lastName}, <strong>Phone:</strong> ${user.phoneNumber}, <strong>Email:</strong> ${user.email}, <strong>Address:</strong> ${user.address}`;
            
                listContent.appendChild(userElement);
            });
            

            // Update the modal title
            const listModalLabel = document.getElementById('listModalLabel');
            listModalLabel.textContent = `View ${type.charAt(0).toUpperCase() + type.slice(1)}`;
        }

        // Handle logout button click
        const logoutButton = document.querySelector('#profileDropdownMenu .logout-link');
        if (logoutButton) {
            logoutButton.addEventListener('click', async (event) => {
                event.preventDefault();
                try {
                    const response = await fetch('http://localhost:8081/auth/logout', {
                        method: 'POST',
                        credentials: 'include'
                    });

                    if (response.ok) {
                        // Remove receptionist data from localStorage
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
    } catch (error) {
        console.error('Failed to verify session:', error);
        window.location.href = '../html/ReceptionistDetail.html.html';
    }
});

function openFeedbackModal() {
    // Show the Feedback modal
    const feedbackModal = new bootstrap.Modal(document.getElementById('feedbackModal'));
    feedbackModal.show();

    // Load and display feedback content
    loadFeedbackContent();
}

/**
 * Function to load and display feedback content in the modal.
 */
async function loadFeedbackContent() {
    const feedbackContent = document.getElementById('feedbackContent');
    feedbackContent.innerHTML = 'Loading feedback...'; // Placeholder text while loading

    try {
        const response = await fetch('http://localhost:8081/feedback/all', {
            method: 'GET',
            credentials: 'include' // Include credentials (e.g., cookies) in the request
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();

        // Clear placeholder text
        feedbackContent.innerHTML = '';

        // Check if data is empty
        if (data.length === 0) {
            feedbackContent.innerHTML = '<p>No feedback available.</p>';
            return;
        }

        // Render feedback items
        data.forEach(feedback => {
            const feedbackElement = document.createElement('div');
            feedbackElement.className = 'mb-2 p-2 border-bottom';
            feedbackElement.innerHTML = `
                <h5 class ="v"><strong>${feedback.orderId}</strong></h5>
                <p class ="FeedbackText"><strong>Email:</strong> ${feedback.farmerEmail}</p>
                <p class ="FeedbackText"><strong>Description:</strong> ${feedback.description}</p>
                <p class ="FeedbackText"><strong>Attentive:</strong> ${feedback.attentiveRating}</p>
                <p class ="FeedbackText"><strong>Friendly:</strong> ${feedback.friendlyRating}</p>
                <p class ="FeedbackText"><strong>Professional:</strong> ${feedback.professionalRating}</p>

            `;
            feedbackContent.appendChild(feedbackElement);
        });
    } catch (error) {
        feedbackContent.innerHTML = '<p>Error loading feedback. Please try again later.</p>';
    }
}
