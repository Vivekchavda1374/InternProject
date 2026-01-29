<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Users & Companies</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
            <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        </head>

        <body class="bg-light">
            <div class="container-fluid mt-3">
                <div class="row">
                    <div class="col-12">
                        <div class="card shadow">
                            <div class="card-header bg-primary text-white">
                                <h4><i class="fas fa-users"></i> Users & Companies Management</h4>
                                <a href="/" class="btn btn-light btn-sm float-end">
                                    <i class="fas fa-home"></i> Home
                                </a>
                            </div>
                            <div class="card-body">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <button id="createCompanyBtn" class="btn btn-success" data-bs-toggle="modal"
                                            data-bs-target="#createCompanyModal">
                                            <i class="fas fa-plus"></i> Create Company
                                        </button>
                                        <button class="btn btn-info ms-2" data-bs-toggle="modal"
                                            data-bs-target="#createBranchModal">
                                            <i class="fas fa-code-branch"></i> Create Branch
                                        </button>
                                        <a href="/branches" class="btn btn-secondary ms-2">
                                            <i class="fas fa-eye"></i> View Branches by Company
                                        </a>
                                    </div>
                                </div>

                                <table id="usersTable" class="table table-striped table-hover">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>GST No</th>
                                            <th>Phone</th>
                                            <th>Address</th>
                                            <th>Type</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="createCompanyModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Create Company</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form id="createCompanyForm">
                                <div class="mb-3">
                                    <label class="form-label">Company Name</label>
                                    <input type="text" class="form-control" name="name" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Password</label>
                                    <input type="password" class="form-control" name="password" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">GST Number</label>
                                    <input type="text" class="form-control" name="gstNo">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Phone Number</label>
                                    <input type="text" class="form-control" name="phoneNo">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Address Line 1</label>
                                    <input type="text" class="form-control" name="addressLine1">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Address Line 2</label>
                                    <input type="text" class="form-control" name="addressLine2">
                                </div>
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">City</label>
                                        <input type="text" class="form-control" name="city">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">State</label>
                                        <input type="text" class="form-control" name="state">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Country</label>
                                        <select class="form-select" name="country">
                                            <option value="">Select Country</option>
                                            <c:forEach items="${countries}" var="c">
                                                <option value="${c}">${c}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-success" onclick="createCompany()">Create</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="createBranchModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Create Branch</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form id="createBranchForm">
                                <div class="mb-3">
                                    <label class="form-label">Branch Name</label>
                                    <input type="text" class="form-control" name="name" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Password</label>
                                    <input type="password" class="form-control" name="password" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">GST Number</label>
                                    <input type="text" class="form-control" name="gstNo">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Phone Number</label>
                                    <input type="text" class="form-control" name="phoneNo">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Address Line 1</label>
                                    <input type="text" class="form-control" name="addressLine1">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Address Line 2</label>
                                    <input type="text" class="form-control" name="addressLine2">
                                </div>
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">City</label>
                                        <input type="text" class="form-control" name="city">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">State</label>
                                        <input type="text" class="form-control" name="state">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Country</label>
                                        <select class="form-select" name="country">
                                            <option value="">Select Country</option>
                                            <c:forEach items="${countries}" var="c">
                                                <option value="${c}">${c}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Parent Company</label>
                                    <select class="form-select" name="parentCompanyId" required>
                                        <option value="">Select Company</option>
                                    </select>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-info" onclick="createBranch()">Create</button>
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
                let isAdmin = false;

                $(document).ready(function () {
                    $.get('/api/session', function (sessionResponse) {
                        if (sessionResponse.success) {
                            const userId = sessionResponse.data.userId;
                            isAdmin = sessionResponse.data.isAdmin;

                            // Hide create company button for non-admin users
                            if (!isAdmin) {
                                $('#createCompanyBtn').hide();
                            }
                            table = $('#usersTable').DataTable({
                                ajax: {
                                    url: '/api/user-front/companies/' + userId,
                                    dataSrc: function (json) {
                                        // Filter to show only companies (no branches)
                                        return json.data.filter(item => !item.parentCompanyId);
                                    }
                                },
                                columns: [
                                    { data: 'userFrontId', defaultContent: '' },
                                    {
                                        data: 'name',
                                        defaultContent: '',
                                        render: function (data, type, row) {
                                            return '<a href="#" onclick="showBranches(' + row.userFrontId + ')" class="text-primary text-decoration-none">' + data + '</a>';
                                        }
                                    },
                                    { data: 'gstNo', defaultContent: '' },
                                    { data: 'phoneNo', defaultContent: '' },
                                    {
                                        data: null,
                                        defaultContent: '',
                                        render: function (data) {
                                            let parts = [];
                                            if (data.city) parts.push(data.city);
                                            if (data.state) parts.push(data.state);
                                            if (data.country) parts.push(data.country);
                                            return parts.length > 0 ? parts.join(', ') : '';
                                        }
                                    },
                                    {
                                        data: 'parentCompanyId',
                                        defaultContent: '',
                                        render: function (data) {
                                            return data ? 'Branch' : 'Company';
                                        }
                                    },
                                    {
                                        data: null,
                                        defaultContent: '',
                                        render: function (data) {
                                            return '<button class="btn btn-sm btn-danger" onclick="deleteUser(' + data.userFrontId + ')"><i class="fas fa-trash"></i></button>';
                                        }
                                    }
                                ]
                            });
                            loadCompanies();
                        } else {
                            window.location.href = '/login';
                        }
                    });
                });

                function loadCompanies() {
                    $.get('/api/session', function (sessionResponse) {
                        if (sessionResponse.success) {
                            const userId = sessionResponse.data.userId;
                            $.get('/api/user-front/companies/' + userId, function (response) {
                                const select = $('select[name="parentCompanyId"]');
                                select.empty().append('<option value="">Select Company</option>');
                                response.data.filter(item => !item.parentCompanyId).forEach(company => {
                                    select.append('<option value="' + company.userFrontId + '">' + company.name + '</option>');
                                });
                            });
                        }
                    });
                }

                function createCompany() {
                    const formData = new FormData($('#createCompanyForm')[0]);
                    const data = Object.fromEntries(formData);

                    $.get('/api/session', function (sessionResponse) {
                        if (sessionResponse.success) {
                            const userId = sessionResponse.data.userId;
                            $.ajax({
                                url: '/api/user-front/company/create',
                                method: 'POST',
                                headers: {
                                    'userId': userId,
                                    'Content-Type': 'application/json'
                                },
                                data: JSON.stringify(data),
                                success: function () {
                                    $('#createCompanyModal').modal('hide');
                                    table.ajax.reload();
                                    loadCompanies();
                                    alert('Company created successfully!');
                                },
                                error: function (xhr) {
                                    alert('Error: ' + (xhr.responseJSON?.message || 'Error creating company'));
                                }
                            });
                        }
                    });
                }

                function createBranch() {
                    const formData = new FormData($('#createBranchForm')[0]);
                    const data = Object.fromEntries(formData);
                    if (data.parentCompanyId) {
                        data.parentCompanyId = parseInt(data.parentCompanyId);
                    }

                    $.ajax({
                        url: '/api/user-front/branch/create',
                        method: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(data),
                        success: function () {
                            $('#createBranchModal').modal('hide');
                            table.ajax.reload();
                            alert('Branch created successfully!');
                        },
                        error: function () {
                            alert('Error creating branch');
                        }
                    });
                }

                function deleteUser(id) {
                    if (confirm('Are you sure you want to delete this item?')) {
                        $.ajax({
                            url: '/api/user-front/' + id,
                            method: 'DELETE',
                            success: function () {
                                table.ajax.reload();
                                loadCompanies();
                                alert('Deleted successfully!');
                            },
                            error: function () {
                                alert('Error deleting item');
                            }
                        });
                    }
                }

                function showBranches(companyId) {
                    $.get('/api/user-front/' + companyId, function (companyResponse) {
                        if (companyResponse.success) {
                            const company = companyResponse.data;

                            $.ajax({
                                url: '/api/user-front/branches/' + companyId,
                                method: 'GET',
                                success: function (branchResponse) {
                                    let tableData = [];

                                    // Add company
                                    let companyRow = Object.assign({}, company);
                                    companyRow.name = '<strong>' + company.name + '</strong>';
                                    tableData.push(companyRow);

                                    if (branchResponse.success && branchResponse.data.length > 0) {
                                        branchResponse.data.forEach(branch => {
                                            let branchRow = Object.assign({}, branch);
                                            branchRow.name = '&nbsp;&nbsp;&nbsp;&nbsp;└─ ' + branch.name;
                                            tableData.push(branchRow);
                                        });
                                    }

                                    table.clear();
                                    table.rows.add(tableData);
                                    table.draw();
                                },
                                error: function () {
                                    alert('Error loading branches');
                                }
                            });
                        }
                    });
                }

                function showAllCompanies() {
                    table.ajax.reload();
                }
            </script>
        </body>

        </html>