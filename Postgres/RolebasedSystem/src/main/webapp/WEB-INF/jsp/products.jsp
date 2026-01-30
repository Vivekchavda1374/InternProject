<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                                        <button class="btn btn-success" data-bs-toggle="modal"
                                            data-bs-target="#createProductModal">
                                            <i class="fas fa-plus"></i> Create Product
                                        </button>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="input-group">
                                            <select class="form-select" id="companyFilter">
                                                <option value="">All Companies</option>
                                            </select>
                                            <button class="btn btn-outline-secondary"
                                                onclick="filterByCompany()">Filter</button>
                                        </div>
                                    </div>
                                </div>

                                <table id="productsTable" class="table table-striped table-hover">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Item Code</th>
                                            <th>MRP</th>
                                            <th>Selling Price</th>
                                            <th>Stock</th>
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
                                                <td>₹${product.mrp}</td>
                                                <td>₹${product.sellingPrice}</td>
                                                <td>${product.stockQuantity}</td>
                                                <td>${product.companyId}</td>
                                                <td>
                                                    <button class="btn btn-sm btn-warning"
                                                        onclick="editProduct(${product.productId})">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-danger"
                                                        onclick="deleteProduct(${product.productId})">
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
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">MRP (₹)</label>
                                            <input type="number" step="0.01" class="form-control" name="mrp">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">Selling Price (₹)</label>
                                            <input type="number" step="0.01" class="form-control" name="sellingPrice">
                                        </div>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Stock Quantity</label>
                                    <input type="number" step="0.01" class="form-control" name="stockQuantity">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Description</label>
                                    <textarea class="form-control" name="description" rows="3"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Company</label>
                                    <select class="form-select" name="companyId" required>
                                        <option value="">Select Company</option>
                                    </select>
                                </div>
<%--                                <div class="mb-3">--%>
<%--                                    <label class="form-label">User ID</label>--%>
<%--                                    <input type="number" class="form-control" name="userId" value="1" required>--%>
<%--                                </div>--%>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-success" onclick="createProduct()">Create</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Edit Product Modal -->
            <div class="modal fade" id="editProductModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Edit Product</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form id="editProductForm">
                                <input type="hidden" name="productId">
                                <div class="mb-3">
                                    <label class="form-label">Product Name</label>
                                    <input type="text" class="form-control" name="productName" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Item Code</label>
                                    <input type="text" class="form-control" name="itemCode">
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">MRP (₹)</label>
                                            <input type="number" step="0.01" class="form-control" name="mrp">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">Selling Price (₹)</label>
                                            <input type="number" step="0.01" class="form-control" name="sellingPrice">
                                        </div>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Stock Quantity</label>
                                    <input type="number" step="0.01" class="form-control" name="stockQuantity">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Description</label>
                                    <textarea class="form-control" name="description" rows="3"></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-warning" onclick="updateProduct()">Update</button>
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

                $(document).ready(function () {
                    table = $('#productsTable').DataTable({
                        responsive: true,
                        pageLength: 10
                    });

                    loadCompanies();

                    // Get user type from session
                    $.get('/api/session', function (response) {
                        if (response.success) {
                            isCompany = response.data.isAdmin;
                        }
                    });
                });

                function loadCompanies() {
                    $.get('/api/session', function (sessionResponse) {
                        if (sessionResponse.success) {
                            const userId = sessionResponse.data.userId;
                            $.get('/api/user-front/companies/' + userId, function (response) {
                                const selects = $('select[name="companyId"], #companyFilter');
                                selects.each(function () {
                                    const select = $(this);
                                    const placeholder = select.attr('name') === 'companyId' ? 'Select Company' : 'All Companies';
                                    select.empty().append('<option value="">' + placeholder + '</option>');


                                    const allItems = response.data;
                                    const parents = allItems.filter(item => !item.parentCompanyId);

                                    if (parents.length === 0 && allItems.length > 0) {
                                        // Fallback for branch users seeing themselves
                                        allItems.forEach(item => {
                                            select.append('<option value="' + item.userFrontId + '">' + item.name + '</option>');
                                        });
                                    } else {
                                        parents.forEach(company => {
                                            select.append('<option value="' + company.userFrontId + '">' + company.name + '</option>');
                                            allItems.filter(b => b.parentCompanyId === company.userFrontId).forEach(branch => {
                                                select.append('<option value="' + branch.userFrontId + '">&nbsp;&nbsp;&nbsp;└─ ' + branch.name + '</option>');
                                            });
                                        });
                                    }
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

                    $.get('/api/session', function (sessionResponse) {
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
                                    itemCode: data.itemCode,
                                    mrp: data.mrp ? parseFloat(data.mrp) : null,
                                    sellingPrice: data.sellingPrice ? parseFloat(data.sellingPrice) : null,
                                    stockQuantity: data.stockQuantity ? parseFloat(data.stockQuantity) : null,
                                    description: data.description
                                }),
                                success: function () {
                                    $('#createProductModal').modal('hide');
                                    location.reload();
                                    alert('Product created successfully!');
                                },
                                error: function (xhr) {
                                    alert('Error creating product: ' + xhr.responseText);
                                }
                            });
                        }
                    });
                }

                function filterByCompany() {
                    const companyId = $('#companyFilter').val();
                    if (companyId) {
                        $.get('/api/session', function (sessionResponse) {
                            if (sessionResponse.success) {
                                const userId = sessionResponse.data.userId;
                                $.ajax({
                                    url: '/api/products/company/' + companyId,
                                    method: 'GET',
                                    headers: {
                                        'userId': userId
                                    },
                                    success: function (response) {
                                        table.clear();
                                        response.data.forEach(product => {
                                            const deleteBtn = isCompany ?
                                                '<button class="btn btn-sm btn-danger" onclick="deleteProduct(' + product.productId + ')"><i class="fas fa-trash"></i></button>' :
                                                '';
                                            table.row.add([
                                                product.productId,
                                                product.productName,
                                                product.itemCode,
                                                '₹' + (product.mrp || '0'),
                                                '₹' + (product.sellingPrice || '0'),
                                                product.stockQuantity || '0',
                                                product.companyId,
                                                '<button class="btn btn-sm btn-warning" onclick="editProduct(' + product.productId + ')"><i class="fas fa-edit"></i></button> ' + deleteBtn
                                            ]);
                                        });
                                        table.draw();
                                    },
                                    error: function () {
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
                    if (confirm('Are you sure you want to delete this product?')) {
                        $.get('/api/session', function (sessionResponse) {
                            if (sessionResponse.success) {
                                const userId = sessionResponse.data.userId;
                                $.ajax({
                                    url: '/api/products/' + id,
                                    method: 'DELETE',
                                    headers: {
                                        'userId': userId
                                    },
                                    success: function () {
                                        location.reload();
                                        alert('Product deleted successfully!');
                                    },
                                    error: function () {
                                        alert('Error deleting product');
                                    }
                                });
                            }
                        });
                    }
                }

                function editProduct(id) {
                    $.get('/api/session', function (sessionResponse) {
                        if (sessionResponse.success) {
                            const userId = sessionResponse.data.userId;
                            $.ajax({
                                url: '/api/products/' + id,
                                method: 'GET',
                                headers: {
                                    'userId': userId
                                },
                                success: function (response) {
                                    const product = response.data;
                                    $('#editProductForm input[name="productId"]').val(product.productId);
                                    $('#editProductForm input[name="productName"]').val(product.productName);
                                    $('#editProductForm input[name="itemCode"]').val(product.itemCode);
                                    $('#editProductForm input[name="mrp"]').val(product.mrp);
                                    $('#editProductForm input[name="sellingPrice"]').val(product.sellingPrice);
                                    $('#editProductForm input[name="stockQuantity"]').val(product.stockQuantity);
                                    $('#editProductForm textarea[name="description"]').val(product.description);
                                    $('#editProductModal').modal('show');
                                },
                                error: function () {
                                    alert('Error loading product details');
                                }
                            });
                        }
                    });
                }

                function updateProduct() {
                    const formData = new FormData($('#editProductForm')[0]);
                    const data = Object.fromEntries(formData);
                    const productId = data.productId;

                    $.get('/api/session', function (sessionResponse) {
                        if (sessionResponse.success) {
                            const userId = sessionResponse.data.userId;
                            $.ajax({
                                url: '/api/products/' + productId,
                                method: 'PUT',
                                headers: {
                                    'userId': userId,
                                    'Content-Type': 'application/json'
                                },
                                data: JSON.stringify({
                                    productName: data.productName,
                                    itemCode: data.itemCode,
                                    mrp: data.mrp ? parseFloat(data.mrp) : null,
                                    sellingPrice: data.sellingPrice ? parseFloat(data.sellingPrice) : null,
                                    stockQuantity: data.stockQuantity ? parseFloat(data.stockQuantity) : null,
                                    description: data.description
                                }),
                                success: function () {
                                    $('#editProductModal').modal('hide');
                                    location.reload();
                                    alert('Product updated successfully!');
                                },
                                error: function (xhr) {
                                    alert('Error updating product: ' + xhr.responseText);
                                }
                            });
                        }
                    });
                }
            </script>
        </body>

        </html>