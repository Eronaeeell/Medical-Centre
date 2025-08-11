<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>APU Medical Centre - Login</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body class="bg-light d-flex justify-content-center align-items-center" style="height:100vh;">
    <div class="card p-4 shadow" style="width: 22rem;">
        <h3 class="text-center mb-3">APU Medical Centre</h3>
        <p class="text-center text-muted">Sign in to access your account</p>
        
        <form action="staffLoginServlet" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" class="form-control" placeholder="Enter your email" required>
            </div>
            
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" placeholder="Enter your password" required>
            </div>
            
        <div class="form-group">
                <label>Role</label>
                <select name="role" class="form-control" required>
                    <option value="">Select your role</option>
                    <option value="manager">Manager</option>
                    <option value="counter">Counter Staff</option>
                    <option value="doctor">Doctor</option>
                </select>
            </div>
            
            <% String error = request.getParameter("error"); %>
            <% if (error != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= error %>
                </div>
            <% } %>
            
            <button type="submit" class="btn btn-primary btn-block">Sign In</button>
        </form>
    </div>
</body>
</html>
