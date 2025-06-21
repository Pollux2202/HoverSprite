async function submitForm(event) {
        event.preventDefault(); // Prevent default form submission

        const formData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            phoneNumber: document.getElementById('phone').value,
            address: document.getElementById('address').value,
            password: document.getElementById('password').value
        };

        // Confirm Password validation
        const confirmPassword = document.getElementById('confirmPassword').value;
        if (formData.password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        try {
            const response = await fetch('http://localhost:8081/farmer/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                throw new Error('Failed to register. Please try again.');
            }

            const data = await response.json();
            window.location.href = "../html/FarmerLogin.html"
            // Console log the data and success message
            console.log('Signup success:', data);

            alert('Signup successful!');
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred during signup.');
        }
    }

