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

        // Retrieve seen notifications from localStorage
        const savedSeenNotificationIds = JSON.parse(localStorage.getItem('seenNotificationIds') || '[]');
        let seenNotificationIds = new Set(savedSeenNotificationIds);

        const pollInterval = 5000; // 5 seconds
        let previousNotificationCount = 0; // Track the previous notification count

        const fetchNotifications = async () => {
            try {
                const notificationResponse = await fetch('http://localhost:8081/api/notifications/sprayer', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (notificationResponse.ok) {
                    const notifications = await notificationResponse.json();
                    const notificationList = document.getElementById('notificationList');

                    // Clear the current notification list
                    notificationList.innerHTML = '';

                    // Populate notifications in the modal
                    notifications.forEach(notification => {
                        const li = document.createElement('li');
                        li.className = 'list-group-item';
                        li.innerHTML = `<strong>${notification.subject}:</strong> ${notification.body}`;
                        notificationList.appendChild(li);
                    });

                    // Update notification count
                    const currentNotificationCount = notifications.length;
                    document.getElementById('countNotification').textContent = currentNotificationCount;

                    // Check if there's a new notification
                    if (currentNotificationCount > previousNotificationCount) {
                        // Filter out seen notifications
                        const newNotifications = notifications.filter(notification => !seenNotificationIds.has(notification.id));

                        if (newNotifications.length > 0) {
                            // Show the latest new notification
                            const latestNotification = newNotifications[newNotifications.length - 1];
                            showNewNotificationModal(latestNotification);

                            // Mark this notification as seen
                            newNotifications.forEach(notification => seenNotificationIds.add(notification.id));

                            // Save seen notifications to localStorage
                            localStorage.setItem('seenNotificationIds', JSON.stringify(Array.from(seenNotificationIds)));
                        }
                    }

                    // Update the previous notification count
                    previousNotificationCount = currentNotificationCount;
                } else {
                    console.error('Failed to fetch notifications');
                }
            } catch (error) {
                console.error('Error fetching notifications:', error);
            }
        };

        // Function to show the modal for a new notification
        const showNewNotificationModal = (notification) => {
            const modalElement = document.getElementById('newNotificationModal');
            if (modalElement) {
                // Use Bootstrap's Modal constructor to create an instance
                const modal = new bootstrap.Modal(modalElement);
                const modalTitle = document.getElementById('newNotificationModalTitle');
                const modalBody = document.getElementById('newNotificationModalBody');

                // Set the content of the modal
                modalTitle.textContent = notification.subject;
                modalBody.textContent = notification.body;

                // Show the modal
                modal.show();
            } else {
                console.error('Modal element not found');
            }
        };

        // Initial fetch of notifications
        fetchNotifications();

        // Set up polling for notifications
        setInterval(fetchNotifications, pollInterval);

        // Handle profile section link click
        const profileSectionLink = document.querySelector('.profile-section-link');
        if (profileSectionLink) {
            profileSectionLink.addEventListener('click', async (event) => {
                event.preventDefault();
                const sprayerResponse = await fetch('http://localhost:8081/sprayer/email/{email}', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (sprayerResponse.ok) {
                    const sprayerData = await sprayerResponse.json();
                    localStorage.setItem('sprayerData', JSON.stringify(sprayerData));
                    window.location.href = '../html/SprayerDetail.html';
                } else {
                    console.error('Failed to fetch sprayer data');
                }
            });
        }

        const profileButton = document.querySelector('#profileDropdownMenu .profile-link');
        if (profileButton) {
            profileButton.addEventListener('click', async (event) => {
                event.preventDefault();
                const sprayerResponse = await fetch('http://localhost:8081/sprayer/email/{email}', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (sprayerResponse.ok) {
                    const sprayerData = await sprayerResponse.json();
                    localStorage.setItem('sprayerData', JSON.stringify(sprayerData));
                    window.location.href = '../html/SprayerDetail.html';
                } else {
                    console.error('Failed to fetch sprayer data');
                }
            });
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
    } catch (error) {
        console.error('Failed to verify session:', error);
    }
});
