<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
    </head>
    <script>

function validateForm() {
    const name = document.forms["myForm"]["name"].value;
    const age = document.forms["myForm"]["age"].value;
    const phone = document.forms["myForm"]["phone"].value;

    const nameRegex = /^[A-Za-z]+$/;
    const numberRegex = /^[0-9]+$/;

    if (!nameRegex.test(name)) {
        alert("Name must contain letters only");
        return false;
    }
    if (!numberRegex.test(age)) {
        alert("Age must be a number");
        return false;
    }
    if (!numberRegex.test(phone)) {
        alert("Phone number must be digits only");
        return false;
    }
    return true;
}

    </script>
    <body class="bg-light d-flex justify-content-center align-items-center" style="height:100vh;">
        <div class="card p-4 shadow" style="width: 22rem;">
        <h3 class="text-center mb-3">APU Medical Centre</h3>
        <p class="text-center text-muted">Enter details to access register your account</p>
        
            <form action="register" method="POST">
                <div class="form-group">
                    <label>Name</label>
                    <input type="name" name="name" class="form-control" placeholder="Please enter your name" required>
                </div>
                
                <div class="form-group">
                    <label>Identification Number</label>
                    <input type="ic" name="ic" class="form-control" placeholder="Enter your Identification Number" required>
                </div>
                
                <div class="form-group">
                    <label>Age</label>
                    <input type="age" name="age" class="form-control" placeholder="Enter your Age" required>
                </div>
                
                <div class="form-group">
                    <label>Gender:</label>
                    <select class="form-control" id="type" name="gender" required>
                        <option value="Male"> Male </option>
                        <option value="Female"> Female </option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" class="form-control" placeholder="Enter your Email Address" required>
                </div>
                
                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="phoneNo" name="phoneNo" class="form-control" placeholder="Enter your Phone Number" required>
                </div>
                
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="Enter your Password" required>
                </div>
                
                
                

    <!--            <div class="form-group">
                    <label>Role</label>
                    <select name="role" class="form-control" required>
                        <option value="">Select your role</option>
                        <option value="manager">Manager</option>
                        <option value="counter">Counter Staff</option>
                        <option value="doctor">Doctor</option>
                        <option value="customer">Customer</option>
                    </select>
                </div>-->
    
               <% String error = request.getParameter("error"); %>
                <% if (error != null) { %>
                    <div class="alert alert-danger" role="alert">
                        <%= error %>
                    </div>
                <% } %>

                <button type="register" class="btn btn-primary btn-block">Register</button>
                <a href="login.jsp"> Have Account? Click here to Login</a>
            </form>

<!--        <div class="mt-4">
            <p class="text-sm text-muted mb-1">Demo Credentials:</p>
            <small>Manager: manager@apu.edu / manager123</small><br>
            <small>Counter: counter@apu.edu / counter123</small><br>
            <small>Doctor: doctor@apu.edu / doctor123</small><br>
            <small>Customer: customer@apu.edu / customer123</small>
        </div>-->
        </div>
    </body>
</html>
