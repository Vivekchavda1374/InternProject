<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Products Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container-fluid mt-3">
        <div class="row">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header bg-success text-white">
                        <h4><i class="fas fa-box"></i> Products Management</h4>
                        <a href="/" class="btn btn-light btn-sm float-end">
                            <i class="fas fa-home"></i> Home
                        </a>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#createProductModal">
                                    <i class="fas fa-plus"></i> Create Product
                                </button>
                            </div>
                            <div class="col-md-6">
                                <div class="input-group">
                                    <select class="form-select" id="companyFilter">
                                        <option value="">All Companies</option>
                                    </select>
                                    <button class="btn btn-outline-secondary" onclick="filterByCompany()">Filter</button>
                                </div>
                            </div>
                        </div>
                        
                        <table id="productsTable" class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Item Code</th>
                                    <th>Company</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <td>${product.productId}</td>
                                        <td>${product.productName}</td>
                                        <td>${product.itemCode}</td>
                                        <td>${product.companyId}</td>
                                        <td>
                                            <button class="btn btn-sm btn-warning" onclick="editProduct(${product.productId})">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-sm btn-danger" onclick="deleteProduct(${product.productId})">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="createProductModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Create Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="createProductForm">
                        <div class="mb-3">
                            <label class="form-label">Product Name</label>
                            <input type="text" class="form-control" name="productName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Item Code</label>
                            <input type="text" class="form-control" name="itemCode">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Company</label>
                            <select class="form-select" name="companyId" required>
                                <option value="">Select Company</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">User ID</label>
                            <input type="number" class="form-control" name="userId" value="1" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-success" onclick="createProduct()">Create</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        let table;
        let isCompany = false;
        
        $(document).ready(function() {
            table = $('#productsTable').DataTable({
                responsive: true,
                pageLength: 10
            });
            
            loadCompanies();
            
            // Get user type from session
            $.get('/api/session', function(response) {
                if (response.success) {
                    isCompany = response.data.isAdmin;
                }
            });
        });
        
        function loadCompanies() {
            $.get('/api/session', function(sessionResponse) {
                if (sessionResponse.success) {
                    const userId = sessionResponse.data.userId;
                    $.get('/api/user-front/companies/' + userId, function(response) {
                        const selects = $('select[name="companyId"], #companyFilter');
                        selects.each(function() {
                            const select = $(this);
                            const placeholder = select.attr('name') === 'companyId' ? 'Select Company' : 'All Companies';
                            select.empty().append('<option value="">' + placeholder + '</option>');
                            response.data.filter(item => !item.parentCompanyId).forEach(company => {
                                select.append('<option value="' + company.userFrontId + '">' + company.name + '</option>');
                            });
                        });
                    });
                }
            });
        }
        
        function createProduct() {
            const formData = new FormData($('#createProductForm')[0]);
            const data = Object.fromEntries(formData);
            data.companyId = parseInt(data.companyId);
            data.userId = parseInt(data.userId);
            
            $.get('/api/session', function(sessionResponse) {
                if (sessionResponse.success) {
                    const userId = sessionResponse.data.userId;
                    $.ajax({
                        url: '/api/products/create',
                        method: 'POST',
                        headers: {
                            'userId': userId,
                            'companyId': data.companyId,
                            'Content-Type': 'application/json'
                        },
                        data: JSON.stringify({
                            productName: data.productName,
                            itemCode: data.itemCode
                        }),
                        success: function() {
                            $('#createProductModal').modal('hide');
                            location.reload();
                            alert('Product created successfully!');
                        },
                        error: function(xhr) {
                            alert('Error creating product: ' + xhr.responseText);
                        }
                    });
                }
            });
        }
        
        function filterByCompany() {
            const companyId = $('#companyFilter').val();
            if (companyId) {
                $.get('/api/session', function(sessionResponse) {
                    if (sessionResponse.success) {
                        const userId = sessionResponse.data.userId;
                        $.ajax({
                            url: '/api/products/company/' + companyId,
                            method: 'GET',
                            headers: {
                                'userId': userId
                            },
                            success: function(response) {
                                table.clear();
                                response.data.forEach(product => {
                                    const deleteBtn = isCompany ? 
                                        '<button class="btn btn-sm btn-danger" onclick="deleteProduct(' + product.productId + ')"><i class="fas fa-trash"></i></button>' : 
                                        '';
                                    table.row.add([
                                        product.productId,
                                        product.productName,
                                        product.itemCode,
                                        product.companyId,
                                        '<button class="btn btn-sm btn-warning" onclick="editProduct(' + product.productId + ')"><i class="fas fa-edit"></i></button> ' + deleteBtn
                                    ]);
                                });
                                table.draw();
                            },
                            error: function() {
                                alert('Error loading products');
                            }
                        });
                    }
                });
            } else {
                location.reload();
            }
        }
        
        function deleteProduct(id) {
            if(confirm('Are you sure you want to delete this product?')) {
                $.get('/api/session', function(sessionResponse) {
                    if (sessionResponse.success) {
                        const userId = sessionResponse.data.userId;
                        $.ajax({
                            url: '/api/products/' + id,
                            method: 'DELETE',
                            headers: {
                                'userId': userId
                            },
                            success: function() {
                                location.reload();
                                alert('Product deleted successfully!');
                            },
                            error: function() {
                                alert('Error deleting product');
                            }
                        });
                    }
                });
            }
        }
        
        function editProduct(id) {
            alert('Edit functionality can be implemented with a modal form similar to create');
        }
    </script>
</body>
</html>