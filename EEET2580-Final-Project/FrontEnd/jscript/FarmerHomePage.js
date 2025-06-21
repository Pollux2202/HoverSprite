document.addEventListener('DOMContentLoaded', async () => {
    const pageSize = 8;
    let currentPage = 0;

    try {
        // Validate session by verifying JWT token
        const validateSession = async () => {
            const response = await fetch('http://localhost:8081/auth/verifyToken', {
                method: 'GET',
                credentials: 'include' // Include HttpOnly cookie for authentication
            });

            if (!response.ok) {
                throw new Error('Session invalid or expired');
            }
            console.log('User session is valid.');
        };

        await validateSession();

        let seenNotificationIds = new Set(JSON.parse(localStorage.getItem('seenNotificationIds') || '[]'));

        // Fetch and display notifications
        const fetchNotifications = async () => {
            try {
                const notificationResponse = await fetch('http://localhost:8081/api/notifications/farmer', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (notificationResponse.ok) {
                    const notifications = await notificationResponse.json();
                    const notificationList = document.getElementById('notificationList');

                    if (notificationList) {
                        notificationList.innerHTML = '';
                        notifications.forEach(notification => {
                            const li = document.createElement('li');
                            li.className = 'list-group-item';
                            li.innerHTML = `<strong>${notification.subject}:</strong> ${notification.body}`;
                            notificationList.appendChild(li);
                        });

                        const newNotifications = notifications.filter(notification => !seenNotificationIds.has(notification.id));
                        if (newNotifications.length > 0) {
                            const latestNotification = newNotifications[newNotifications.length - 1];
                            showNewNotificationModal(latestNotification);
                            seenNotificationIds.add(latestNotification.id);
                            localStorage.setItem('seenNotificationIds', JSON.stringify([...seenNotificationIds]));
                        }
                    } else {
                        console.error('Notification list element not found');
                    }
                } else {
                    console.error('Failed to fetch notifications');
                }
            } catch (error) {
                console.error('Error fetching notifications:', error);
            }
        };

        // Show modal for new notifications
        const showNewNotificationModal = (notification) => {
            const modal = new bootstrap.Modal(document.getElementById('newNotificationModal'));
            document.getElementById('newNotificationModalTitle').textContent = notification.subject;
            document.getElementById('newNotificationModalBody').textContent = notification.body;
            modal.show();
        };

        // Set up interval to periodically fetch notifications
        fetchNotifications();
        setInterval(fetchNotifications, 5000);

        // Handle profile section click event
        const setupProfileLinkEvent = (selector) => {
            const element = document.querySelector(selector);
            if (element) {
                element.addEventListener('click', async (event) => {
                    event.preventDefault();
                    try {
                        const farmerResponse = await fetch('http://localhost:8081/farmer/email/{email}', {
                            method: 'GET',
                            credentials: 'include'
                        });

                        if (farmerResponse.ok) {
                            const farmerData = await farmerResponse.json();
                            localStorage.setItem('farmerData', JSON.stringify(farmerData));
                            window.location.href = '../html/FarmerDetail.html';
                        } else {
                            console.error('Failed to fetch farmer data');
                        }
                    } catch (error) {
                        console.error('Error fetching farmer data:', error);
                    }
                });
            } else {
                console.error(`Element not found: ${selector}`);
            }
        };

        setupProfileLinkEvent('.profile-section-link');
        setupProfileLinkEvent('#profileDropdownMenu .profile-link');

        // Logout functionality
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
                        localStorage.removeItem('farmerData');
                        localStorage.removeItem('seenNotificationIds');
                        window.location.href = '../html/RoleSelectionLogin.html';
                    } else {
                        console.error('Failed to log out:', response.statusText);
                    }
                } catch (error) {
                    console.error('Logout error:', error);
                }
            });
        } else {
            console.error('Logout button element not found');
        }

        // Fetch and display order history
        // Updated code within the booking history fetching function
const fetchOrderHistory = async (page = 0) => {
    try {
        const response = await fetch(`http://localhost:8081/farmer/spray-orders?page=${page}&size=${pageSize}`, {
            method: 'GET',
            credentials: 'include'
        });

        if (response.ok) {
            const orders = await response.json();
            const bookingHistoryList = document.getElementById('bookingHistoryList');

            if (bookingHistoryList) {
                bookingHistoryList.innerHTML = '';

                orders.content.forEach(order => {
                    const listItem = document.createElement('li');
                    listItem.className = 'list-group-item order-list-item';

                    const statusColors = {
                        'cancelled': 'red',
                        'confirmed': '#006400',
                        'completed': '#32CD32',
                        'pending': '#FFA500',
                        'assigned': '#000080',
                        'in_progress': '#1E90FF'
                    };

                    const orderDetails = `
                        <strong>Order ID:</strong> ${order.sprayId}
                        <br> <strong>Cost:</strong> ${order.totalCost} VND
                        <br> <strong>Order Status:</strong> <span style="color: ${statusColors[order.orderStatus.toLowerCase()] || 'black'};">${order.orderStatus}</span>
                        <br> <strong>Type:</strong> ${order.cropType}
                        <br> <strong>Date:</strong> ${order.date}
                        <br> <strong>Calendar:</strong> ${order.calenderType}`;

                    listItem.innerHTML = `<div class="order-details">${orderDetails}</div>`;
                    const orderActions = document.createElement('div');
                    orderActions.className = 'order-actions';

                    if (order.orderStatus.toLowerCase() === 'completed') {
                        const feedbackButton = document.createElement('button');
                        feedbackButton.textContent = 'Send Feedback';
                        feedbackButton.className = 'btn btn-success';
                        feedbackButton.onclick = () => {
                            console.log(`Sending feedback for order: ${order.sprayId}`);  // Debug log
                            
                            // Store the order ID in localStorage
                            localStorage.setItem('orderID', order.sprayId);

                            // Verify if orderID is correctly stored
                            const storedOrderId = localStorage.getItem('orderID');
                            console.log('Stored orderID:', storedOrderId);  // Check if orderID is being saved
                            
                            window.location.href = "../html/Feedback.html";
                        };
                        orderActions.appendChild(feedbackButton);
                    } else if (order.orderStatus.toLowerCase() === 'in_progress') {
                        const confirmButton = document.createElement('button');
                        confirmButton.textContent = 'Confirm';
                        confirmButton.className = 'btn btn-primary';
                        confirmButton.onclick = async () => {
                            try {
                                const confirmResponse = await fetch(`http://localhost:8081/farmer/confirm/${order.sprayId}`, {
                                    method: 'PUT',
                                    headers: { 'Content-Type': 'application/json' },
                                    credentials: 'include'
                                });

                                const resultText = await confirmResponse.text();
                                if (confirmResponse.ok) {
                                    alert(resultText || 'Order confirmed successfully');
                                    fetchOrderHistory(currentPage); // Refresh order history
                                } else {
                                    alert(`Error: ${resultText || 'An error occurred'}`);
                                }
                            } catch (error) {
                                console.error('Error confirming order:', error);
                                alert('An error occurred while confirming the order.');
                            }
                        };
                        orderActions.appendChild(confirmButton);
                    }

                    listItem.appendChild(orderActions);
                    bookingHistoryList.appendChild(listItem);
                });

                // Update pagination controls
                const paginationControls = document.getElementById('paginationControls');
                if (paginationControls) {
                    paginationControls.style.display = orders.totalPages > 1 ? 'block' : 'none';
                    document.getElementById('prevPage').style.display = currentPage > 0 ? 'inline-block' : 'none';
                    document.getElementById('nextPage').style.display = currentPage < orders.totalPages - 1 ? 'inline-block' : 'none';
                }
            } else {
                console.error('Booking history list element not found');
            }
        } else {
            console.error('Failed to fetch order history');
        }
    } catch (error) {
        console.error('Error fetching order history:', error);
    }
};


        

        // Pagination controls
        document.getElementById('prevPage').addEventListener('click', () => {
            if (currentPage > 0) {
                currentPage--;
                fetchOrderHistory(currentPage);
            }
        });

        document.getElementById('nextPage').addEventListener('click', () => {
            currentPage++;
            fetchOrderHistory(currentPage);
        });

        // Initial order history fetch
        fetchOrderHistory();
    } catch (error) {
        console.error('Error validating session:', error);
        alert('Session invalid or expired. Please log in again.');
        window.location.href = '../html/RoleSelectionLogin.html'; // Redirect to login
    }
});
