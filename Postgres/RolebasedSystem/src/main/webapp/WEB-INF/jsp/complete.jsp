<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Complete Project Data</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container-fluid mt-3">
    <div class="card shadow">
        <div class="card-header bg-dark text-white">
            <h4><i class="fas fa-table"></i> Complete Project Data - All in One Table</h4>
            <div class="float-end">
                <span id="userInfo" class="me-3"></span>
                <a href="/" class="btn btn-light btn-sm"><i class="fas fa-home"></i> Home</a>
                <button onclick="logout()" class="btn btn-danger btn-sm"><i class="fas fa-sign-out-alt"></i> Logout</button>
            </div>
        </div>
        <div class="card-body">
            <div id="accessDenied" class="alert alert-danger" style="display:none;">
                <i class="fas fa-exclamation-triangle"></i> Access Denied. You don't have permission to view this data.
            </div>
            <table id="completeTable" class="table table-striped table-bordered table-hover table-sm">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Company</th>
                        <th>Branch</th>
                        <th>Parent</th>
                        <th>GST</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>City</th>
                        <th>State</th>
                        <th>Country</th>
                        <th>Role</th>
                        <th>Product</th>
                        <th>Item Code</th>
                        <th>MRP</th>
                        <th>Selling Price</th>
                        <th>Stock</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

<script>
let table;
let userRole = null;
let userId = null;

$(document).ready(function() {
    $.get('/api/session', function(response) {
        if (!response.success) {
            window.location.href = '/login';
            return;
        }
        
        userId = response.data.userId;
        const isAdmin = response.data.isAdmin;
        const userName = response.data.userName || 'User';
        
        $('#userInfo').html('<strong>' + userName + '</strong> (' + (isAdmin ? 'Admin' : 'User') + ')');
        
        if (!isAdmin) {
            $('#accessDenied').show();
            return;
        }
        
        table = $('#completeTable').DataTable({
            ajax: {
                url: '/api/complete',
                dataSrc: '',
                error: function() {
                    alert('Error loading data. Please check your permissions.');
                }
            },
            columns: [
                { data: 'id' },
                { data: 'type', defaultContent: '' },
                { data: 'companyName', defaultContent: '' },
                { data: 'branchName', defaultContent: '' },
                { data: 'parentCompany', defaultContent: '' },
                { data: 'gstNo', defaultContent: '' },
                { data: 'phoneNo', defaultContent: '' },
                { 
                    data: null,
                    defaultContent: '',
                    render: (data) => {
                        let addr = [];
                        if (data.addressLine1) addr.push(data.addressLine1);
                        if (data.addressLine2) addr.push(data.addressLine2);
                        return addr.join(', ');
                    }
                },
                { data: 'city', defaultContent: '' },
                { data: 'state', defaultContent: '' },
                { data: 'country', defaultContent: '' },
                { data: 'roleName', defaultContent: '' },
                { data: 'productName', defaultContent: '' },
                { data: 'itemCode', defaultContent: '' },
                { data: 'mrp', defaultContent: '', render: (data) => data ? '₹' + data : '' },
                { data: 'sellingPrice', defaultContent: '', render: (data) => data ? '₹' + data : '' },
                { data: 'stockQuantity', defaultContent: '' },
                { data: 'description', defaultContent: '' }
            ],
            scrollX: true,
            pageLength: 25,
            order: [[0, 'asc']]
        });
    }).fail(function() {
        window.location.href = '/login';
    });
});

function logout() {
    $.post('/api/logout', function() {
        window.location.href = '/login';
    });
}
</script>
</body>
</html>
