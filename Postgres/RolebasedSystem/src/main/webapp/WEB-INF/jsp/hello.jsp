<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Role Based System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="/">
                <i class="fas fa-shield-alt"></i> Role Based System
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3" id="userInfo"></span>
                <button class="btn btn-outline-light btn-sm" onclick="logout()">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </button>
            </div>
        </div>
    </nav>
    
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-body text-center p-5">
                        <h1 class="card-title mb-4"><i class="fas fa-shield-alt text-primary"></i> Dashboard</h1>
                        <p class="card-text text-muted mb-4">Manage your companies, products, and user roles efficiently</p>
                        <div class="row" id="menuItems">
                            <!-- Menu items will be loaded based on user role -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            checkSession();
        });
        
        function checkSession() {
            $.get('/api/session', function(response) {
                if (response.success) {
                    const user = response.data;
                    $('#userInfo').text(user.username + ' (' + (user.isAdmin ? 'Admin' : 'User') + ')');
                    loadMenu(user.isAdmin);
                } else {
                    window.location.href = '/login';
                }
            }).fail(function() {
                window.location.href = '/login';
            });
        }
        
        function loadMenu(isAdmin) {
            let menuHtml = '';
            
            if (isAdmin) {
                menuHtml = `
                    <div class="col-md-3 mb-3">
                        <a href="/users" class="btn btn-primary btn-lg w-100">
                            <i class="fas fa-users"></i><br>Users & Companies
                        </a>
                    </div>
                    <div class="col-md-3 mb-3">
                        <a href="/products" class="btn btn-success btn-lg w-100">
                            <i class="fas fa-box"></i><br>Products
                        </a>
                    </div>
                    <div class="col-md-3 mb-3">
                        <a href="/roles" class="btn btn-warning btn-lg w-100">
                            <i class="fas fa-user-tag"></i><br>Roles
                        </a>
                    </div>
                    <div class="col-md-3 mb-3">
                        <a href="/branches" class="btn btn-info btn-lg w-100">
                            <i class="fas fa-code-branch"></i><br>Branches
                        </a>
                    </div>
                `;
            } else {
                menuHtml = `
                    <div class="col-md-6 mb-3">
                        <a href="/products" class="btn btn-success btn-lg w-100">
                            <i class="fas fa-box"></i><br>Products
                        </a>
                    </div>
                    <div class="col-md-6 mb-3">
                        <a href="/roles" class="btn btn-warning btn-lg w-100">
                            <i class="fas fa-user-tag"></i><br>View Roles
                        </a>
                    </div>
                `;
            }
            
            $('#menuItems').html(menuHtml);
        }
        
        function logout() {
            $.post('/api/logout', function() {
                window.location.href = '/login';
            });
        }
    </script>
</body>
</html>