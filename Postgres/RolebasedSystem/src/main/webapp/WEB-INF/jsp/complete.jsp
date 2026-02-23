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
                    <h4><i class="fas fa-table"></i> Project Data</h4>
                    <div class="float-end">
                        <span id="userInfo" class="me-3"></span>
                        <button onclick="openCreateCompanyModal()" class="btn btn-primary btn-sm me-1"><i
                                class="fas fa-plus"></i> New Company</button>
                        <button onclick="openCreateBranchModal()" class="btn btn-success btn-sm me-1"><i
                                class="fas fa-code-branch"></i> New Branch</button>
                        <a href="/" class="btn btn-light btn-sm"><i class="fas fa-home"></i> Home</a>
                        <button onclick="logout()" class="btn btn-danger btn-sm"><i class="fas fa-sign-out-alt"></i>
                            Logout</button>
                    </div>
                </div>
                <div class="card-body">
                    <div id="accessDenied" class="alert alert-danger" style="display:none;">
                        <i class="fas fa-exclamation-triangle"></i> Access Denied. You don't have permission to view
                        this data.
                    </div>
                    <div class="mb-3">
                        <label for="countryFilter" class="form-label">Filter by Country:</label>
                        <select id="countryFilter" class="form-select" style="width: 200px;">
                            <option value="">All Countries</option>
                        </select>
                    </div>
                    <table id="completeTable" class="table table-striped table-bordered table-hover table-sm">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th class="d-none">Order</th>
                                <th>Company</th>
                                <th>Branch</th>
                                <th>Parent</th>
                                <th>Total Purchase</th>
                                <th>Total Products</th>
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
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="toast-container position-fixed top-0 end-0 p-3" id="toastContainer" style="z-index: 1080;"></div>

        <!-- Create Company Modal -->
        <div class="modal fade" id="createCompanyModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Create Company</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="createCompanyForm">
                            <div class="row">
                                <div class="col-md-6 mb-2"><label>Name</label><input type="text" class="form-control"
                                        name="name" required></div>
                                <div class="col-md-6 mb-2"><label>Password</label><input type="password"
                                        class="form-control" name="password" autocomplete="new-password"></div>
                                <div class="col-md-6 mb-2"><label>GST No</label><input type="text" class="form-control"
                                        name="gstNo"></div>
                                <div class="col-md-6 mb-2"><label>Phone</label><input type="text" class="form-control"
                                        name="phoneNo"></div>
                            </div>

                            <hr>
                            <h6>Addresses <button type="button" class="btn btn-sm btn-outline-primary float-end"
                                    onclick="addAddressField('#companyAddresses')"><i class="fas fa-plus"></i> Add
                                    Address</button></h6>
                            <div id="companyAddresses">
                                <!-- Initial Address Block -->
                                <div class="card p-2 mb-2 bg-light address-block">
                                    <h6 class="card-subtitle mb-2 text-muted">Primary Address</h6>
                                    <input type="hidden" name="addressType" value="Primary">
                                    <div class="mb-2"><label>Address Line 1</label><input type="text"
                                            class="form-control" name="addressLine1"></div>
                                    <div class="mb-2"><label>Address Line 2</label><input type="text"
                                            class="form-control" name="addressLine2"></div>
                                    <div class="row">
                                        <div class="col-md-4 mb-2"><label>City</label><input type="text"
                                                class="form-control" name="city"></div>
                                        <div class="col-md-4 mb-2"><label>State</label><input type="text"
                                                class="form-control" name="state"></div>
                                        <div class="col-md-4 mb-2"><label>Country</label><input type="text"
                                                class="form-control" name="country"></div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" onclick="saveCompany()">Create</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Create Branch Modal -->
        <div class="modal fade" id="createBranchModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Create Branch</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="createBranchForm">
                            <div class="mb-2">
                                <label>Parent Company</label>
                                <select class="form-select" name="parentCompanyId" id="branchParentCompany"
                                    required></select>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-2"><label>Name</label><input type="text" class="form-control"
                                        name="name" required></div>
                                <div class="col-md-6 mb-2"><label>Password</label><input type="password"
                                        class="form-control" name="password" autocomplete="new-password"></div>
                                <div class="col-md-6 mb-2"><label>GST No</label><input type="text" class="form-control"
                                        name="gstNo"></div>
                                <div class="col-md-6 mb-2"><label>Phone</label><input type="text" class="form-control"
                                        name="phoneNo"></div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" onclick="saveBranch()">Create</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Company/Branch Modal -->
        <div class="modal fade" id="editUserFrontModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Company/Branch</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <ul class="nav nav-tabs" id="editTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="details-tab" data-bs-toggle="tab"
                                    data-bs-target="#edit-details" type="button">Details</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="addresses-tab" data-bs-toggle="tab"
                                    data-bs-target="#edit-addresses" type="button">Addresses</button>
                            </li>
                        </ul>
                        <div class="tab-content p-3 border border-top-0 rounded-bottom" id="editTabsContent">
                            <div class="tab-pane fade show active" id="edit-details">
                                <form id="editUserFrontForm">
                                    <input type="hidden" name="id" id="editUserFrontId">
                                    <div class="mb-2">
                                        <label>Name</label>
                                        <input type="text" class="form-control" name="name" id="editUserFrontName"
                                            required>
                                    </div>
                                    <div class="alert alert-warning small">
                                        Note: Only the Name can be updated here. Use the 'Addresses' tab to manage
                                        addresses.
                                    </div>
                                    <div class="text-end">
                                        <button type="button" class="btn btn-primary" onclick="updateUserFront()">Save
                                            Name</button>
                                    </div>
                                </form>
                            </div>
                            <div class="tab-pane fade" id="edit-addresses">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h6>Existing Addresses</h6>
                                    <button class="btn btn-sm btn-success" onclick="openAddAddressModal()"><i
                                            class="fas fa-plus"></i> Add New Address</button>
                                </div>
                                <div id="existingAddressesList"></div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Address Modal (Sub-modal) -->
        <div class="modal fade" id="addAddressModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Address</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addAddressForm">
                            <input type="hidden" name="name" value="New Address"> <!-- Default name -->
                            <div class="mb-2">
                                <label>Address Type</label>
                                <select class="form-select" name="addressType">
                                    <option value="Secondary">Secondary</option>
                                    <option value="Warehouse">Warehouse</option>
                                    <option value="Billing">Billing</option>
                                    <option value="Shipping">Shipping</option>
                                </select>
                            </div>
                            <div class="mb-2"><label>Address Line 1</label><input type="text" class="form-control"
                                    name="addressLine1" required></div>
                            <div class="mb-2"><label>Address Line 2</label><input type="text" class="form-control"
                                    name="addressLine2"></div>
                            <div class="row">
                                <div class="col-md-4 mb-2"><label>City</label><input type="text" class="form-control"
                                        name="city"></div>
                                <div class="col-md-4 mb-2"><label>State</label><input type="text" class="form-control"
                                        name="state"></div>
                                <div class="col-md-4 mb-2"><label>Country</label><input type="text" class="form-control"
                                        name="country"></div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-primary" onclick="saveNewAddress()">Save Address</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Product Modal -->
        <div class="modal fade" id="editProductModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Product</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editProductForm">
                            <input type="hidden" name="id" id="editProductId">
                            <div class="mb-2"><label>Product Name</label><input type="text" class="form-control"
                                    name="productName" id="editProductName" required></div>
                            <div class="mb-2"><label>Item Code</label><input type="text" class="form-control"
                                    name="itemCode" id="editItemCode"></div>
                            <div class="row">
                                <div class="col-md-6 mb-2"><label>MRP</label><input type="number" step="0.01"
                                        class="form-control" name="mrp" id="editMrp"></div>
                                <div class="col-md-6 mb-2"><label>Selling Price</label><input type="number" step="0.01"
                                        class="form-control" name="sellingPrice" id="editSellingPrice"></div>
                            </div>
                            <div class="mb-2"><label>Stock Quantity</label><input type="number" step="0.01"
                                    class="form-control" name="stockQuantity" id="editStockQuantity"></div>
                            <div class="mb-2"><label>Description</label><textarea class="form-control"
                                    name="description" id="editDescription"></textarea></div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" onclick="updateProduct()">Save Changes</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Generic Delete Confirmation Modal -->
        <div class="modal fade" id="deleteConfirmModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Delete</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to delete this record?</p>
                        <input type="hidden" id="deleteId">
                        <input type="hidden" id="deleteType">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" onclick="confirmDelete()">Delete</button>
                    </div>
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
            const BRANCH_NAME_REGEX = /^[A-Za-z0-9 ]+$/;

            $(document).ready(function () {
                $.get('/api/session', function (response) {
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

                    loadTable();
                }).fail(function () {
                    window.location.href = '/login';
                });
            });

            function showToast(message, type = 'danger') {
                const classes = {
                    success: 'bg-success text-white',
                    warning: 'bg-warning text-dark',
                    danger: 'bg-danger text-white'
                };
                const toastClass = classes[type] || classes.danger;
                const closeStyle = 'border: 0; background: transparent; color: inherit; font-size: 1.25rem; line-height: 1; opacity: 0.9;';
                const toast = $(`
                    <div class="toast align-items-center ${toastClass} border-0 mb-2" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body"></div>
                            <button type="button" class="me-2 m-auto p-0" style="${closeStyle}" data-bs-dismiss="toast" aria-label="Close">&times;</button>
                        </div>
                    </div>
                `);
                toast.find('.toast-body').text(message);
                $('#toastContainer').append(toast);
                const bsToast = new bootstrap.Toast(toast[0], { delay: 3000 });
                toast.on('hidden.bs.toast', function () {
                    $(this).remove();
                });
                bsToast.show();
            }

            function hasValidBranchName(name) {
                return BRANCH_NAME_REGEX.test(name);
            }

            function fetchBranchExists(parentCompanyId, branchName) {
                return $.ajax({
                    url: '/api/user-front/branch/exists',
                    method: 'GET',
                    data: {
                        parentCompanyId: parentCompanyId,
                        name: branchName
                    }
                });
            }

            function loadTable() {
                if (table) {
                    table.ajax.reload();
                    return;
                }
                
                loadCountries();
                
                table = $('#completeTable').DataTable({
                    ajax: {
                        url: '/api/complete',
                        data: function(d) {
                            d.country = $('#countryFilter').val();
                        },
                        dataSrc: '',
                        error: function () {
                            alert('Error loading data. Please check your permissions.');
                        }
                    },
                    columns: [
                        { data: 'id' },
                        { data: 'hierarchyOrder', visible: false },
                        { data: 'companyName', defaultContent: '' },
                        { data: 'branchName', defaultContent: '' },
                        { data: 'parentCompany', defaultContent: '' },
                        {
                            data: 'totalPurchaseAmount',
                            defaultContent: '',
                            render: (data) => data ? '₹' + data : '₹0'
                        },
                        { data: 'totalProducts', defaultContent: '0' },
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
                        { data: 'description', defaultContent: '' },
                        {
                            data: null,
                            render: function (data, type, row) {
                                let buttons = '';
                                if (row.type === 'Product') {
                                    buttons += '<button class="btn btn-warning btn-sm me-1" onclick="openEditModal(this)" title="Edit Product"><i class="fas fa-edit"></i> Prod</button>';
                                    buttons += '<button class="btn btn-info btn-sm me-1" onclick="openEditCompanyModal(' + row.userFrontId + ', \'' + (row.companyName || row.branchName || '').replace(/'/g, "\\'") + '\')" title="Edit Company/Branch"><i class="fas fa-building"></i> Co.</button>';
                                } else {
                                    buttons += '<button class="btn btn-warning btn-sm me-1" onclick="openEditModal(this)" title="Edit"><i class="fas fa-edit"></i> Edit</button>';
                                }
                                buttons += '<button class="btn btn-danger btn-sm" onclick="openDeleteModal(' + row.id + ', \'' + row.type + '\')" title="Delete"><i class="fas fa-trash"></i></button>';
                                return buttons;
                            }
                        }
                    ],
                    scrollX: true,
                    pageLength: 25,
                    order: [[1, 'asc']]
                });
            }

            function openCreateCompanyModal() {
                $('#createCompanyForm')[0].reset();
                $('#companyAddresses').find('.address-block:not(:first)').remove(); // Reset addresses
                $('#createCompanyModal').modal('show');
            }

            function saveCompany() {
                const form = $('#createCompanyForm');
                const mainData = {
                    name: form.find('[name="name"]').val(),
                    password: form.find('[name="password"]').val(),
                    gstNo: form.find('[name="gstNo"]').val(),
                    phoneNo: form.find('[name="phoneNo"]').val()
                };

                const addresses = [];
                form.find('.address-block').each(function () {
                    const block = $(this);
                    addresses.push({
                        name: mainData.name, // Use company name for address name
                        addressType: block.find('[name="addressType"]').val() || 'Primary',
                        addressLine1: block.find('[name="addressLine1"]').val(),
                        addressLine2: block.find('[name="addressLine2"]').val(),
                        city: block.find('[name="city"]').val(),
                        state: block.find('[name="state"]').val(),
                        country: block.find('[name="country"]').val()
                    });
                });

                // Use the first address for the creation API
                const creationData = { ...mainData, ...addresses[0] };

                $.ajax({
                    url: '/api/user-front/company/create',
                    type: 'POST',
                    headers: { 'userId': userId },
                    contentType: 'application/json',
                    data: JSON.stringify(creationData),
                    success: function (response) {
                        const newId = response.data.userFrontId ?? response.data.id;
                        // Add remaining addresses if any
                        if (addresses.length > 1) {
                            saveAdditionalAddresses(newId, addresses.slice(1), () => {
                                $('#createCompanyModal').modal('hide');
                                table.ajax.reload();
                                alert('Company and addresses created successfully');
                            });
                        } else {
                            $('#createCompanyModal').modal('hide');
                            table.ajax.reload();
                            alert('Company created successfully');
                        }
                    },
                    error: function (xhr) {
                        alert('Error creating company: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function openCreateBranchModal() {
                $('#createBranchForm')[0].reset();
                $.get('/api/user-front/companies', function (response) {
                    if (response.success) {
                        const select = $('#branchParentCompany');
                        select.empty().append('<option value="">Select Company</option>');
                        response.data.filter(c => !c.parentCompanyId).forEach(c => {
                            const entityId = c.userFrontId ?? c.id;
                            select.append(new Option(c.name, entityId));
                        });
                        if (select.find('option').length === 1) {
                            showToast('No parent companies available to create a branch', 'warning');
                            return;
                        }
                        $('#createBranchModal').modal('show');
                    } else {
                        showToast('Failed to load companies');
                    }
                });
            }

            function saveBranch() {
                const form = $('#createBranchForm');
                const branchName = (form.find('[name="name"]').val() || '').trim();
                const password = (form.find('[name="password"]').val() || '').trim();
                const parentCompanyId = parseInt(form.find('[name="parentCompanyId"]').val(), 10);

                if (!branchName) {
                    showToast('Branch name cannot be empty', 'warning');
                    return;
                }
                if (!password) {
                    showToast('Password cannot be empty', 'warning');
                    return;
                }
                if (!hasValidBranchName(branchName)) {
                    showToast('Branch name must not contain special characters', 'warning');
                    return;
                }
                if (!parentCompanyId) {
                    showToast('Please select parent company', 'warning');
                    return;
                }

                const creationData = {
                    parentCompanyId: parentCompanyId,
                    name: branchName,
                    password: password,
                    gstNo: form.find('[name="gstNo"]').val(),
                    phoneNo: form.find('[name="phoneNo"]').val()
                };

                fetchBranchExists(parentCompanyId, branchName).done(function (existsResponse) {
                    if (existsResponse.success && existsResponse.data === true) {
                        showToast('Branch already exists.', 'warning');
                        return;
                    }

                    $.ajax({
                        url: '/api/user-front/branch/create',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(creationData),
                        success: function () {
                            $('#createBranchModal').modal('hide');
                            table.ajax.reload();
                            showToast('Branch created successfully', 'success');
                        },
                        error: function (xhr) {
                            showToast(xhr.responseJSON?.message || 'Error creating branch');
                        }
                    });
                }).fail(function () {
                    showToast('Unable to validate branch name right now');
                });
            }

            function saveAdditionalAddresses(userFrontId, addresses, callback) {
                let completed = 0;
                addresses.forEach(addr => {
                    $.ajax({
                        url: '/api/user-front/' + userFrontId + '/addresses',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(addr),
                        complete: function () {
                            completed++;
                            if (completed === addresses.length) {
                                callback();
                            }
                        }
                    });
                });
            }

            function addAddressField(containerSelector) {
                const container = $(containerSelector);
                const count = container.find('.address-block').length + 1;
                const html = `
                    <div class="card p-2 mb-2 bg-light address-block position-relative">
                        <button type="button" class="btn-close position-absolute top-0 end-0 m-2" onclick="$(this).parent().remove()"></button>
                        <h6 class="card-subtitle mb-2 text-muted">Address ${count}</h6>
                        <div class="mb-2">
                            <label>Address Type</label>
                            <select class="form-select" name="addressType">
                                <option value="Secondary">Secondary</option>
                                <option value="Warehouse">Warehouse</option>
                                <option value="Billing">Billing</option>
                                <option value="Shipping">Shipping</option>
                            </select>
                        </div>
                        <div class="mb-2"><label>Address Line 1</label><input type="text" class="form-control" name="addressLine1"></div>
                        <div class="mb-2"><label>Address Line 2</label><input type="text" class="form-control" name="addressLine2"></div>
                        <div class="row">
                            <div class="col-md-4 mb-2"><label>City</label><input type="text" class="form-control" name="city"></div>
                            <div class="col-md-4 mb-2"><label>State</label><input type="text" class="form-control" name="state"></div>
                            <div class="col-md-4 mb-2"><label>Country</label><input type="text" class="form-control" name="country"></div>
                        </div>
                    </div>`;
                container.append(html);
            }

            function openEditCompanyModal(id, name) {
                $('#editUserFrontId').val(id);
                $('#editUserFrontName').val(name);
                loadExistingAddresses(id);
                $('#details-tab').tab('show');
                $('#editUserFrontModal').modal('show');
            }

            function openEditModal(btn) {
                const row = table.row($(btn).closest('tr')).data();
                if (row.type === 'Company' || row.type === 'Branch') {
                    $('#editUserFrontId').val(row.id);
                    $('#editUserFrontName').val(row.type === 'Company' ? row.companyName : row.branchName);
                    loadExistingAddresses(row.id);
                    // Reset tab to details
                    $('#details-tab').tab('show');
                    $('#editUserFrontModal').modal('show');
                } else if (row.type === 'Product') {
                    $('#editProductId').val(row.id);
                    $('#editProductName').val(row.productName);
                    $('#editItemCode').val(row.itemCode);
                    $('#editMrp').val(row.mrp);
                    $('#editSellingPrice').val(row.sellingPrice);
                    $('#editStockQuantity').val(row.stockQuantity);
                    $('#editDescription').val(row.description);
                    $('#editProductModal').modal('show');
                }
            }

            function loadExistingAddresses(userFrontId) {
                const list = $('#existingAddressesList');
                list.html('<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Loading...</div>');

                $.get('/api/user-front/' + userFrontId + '/addresses', function (response) {
                    if (response.success) {
                        list.empty();
                        if (response.data.length === 0) {
                            list.html('<div class="alert alert-info">No addresses found.</div>');
                            return;
                        }
                        response.data.forEach(addr => {
                            const html = `
                                <div class="card mb-2">
                                    <div class="card-body p-2">
                                        <div class="d-flex justify-content-between">
                                            <h6 class="card-title">${addr.addressType}</h6>
                                            <button class="btn btn-sm btn-outline-danger" onclick="deleteAddress(${addr.id})"><i class="fas fa-trash"></i></button>
                                        </div>
                                        <p class="card-text small mb-0">
                                            ${addr.addressLine1 || ''} ${addr.addressLine2 || ''}<br>
                                            ${addr.city || ''}, ${addr.state || ''}, ${addr.country || ''}
                                        </p>
                                    </div>
                                </div>
                            `;
                            list.append(html);
                        });
                    } else {
                        list.html('<div class="alert alert-danger">Failed to load addresses</div>');
                    }
                });
            }

            function openAddAddressModal() {
                $('#addAddressForm')[0].reset();
                $('#addAddressModal').modal('show');
            }

            function saveNewAddress() {
                const userFrontId = $('#editUserFrontId').val();
                const data = getFormData('#addAddressForm');

                $.ajax({
                    url: '/api/user-front/' + userFrontId + '/addresses',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        $('#addAddressModal').modal('hide');
                        loadExistingAddresses(userFrontId);
                        table.ajax.reload(); // Refresh table to show changes
                        alert('Address added successfully');
                    },
                    error: function (xhr) {
                        alert('Error adding address: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function deleteAddress(addressId) {
                if (!confirm('Are you sure you want to delete this address?')) return;

                $.ajax({
                    url: '/api/user-front/addresses/' + addressId,
                    type: 'DELETE',
                    success: function () {
                        loadExistingAddresses($('#editUserFrontId').val());
                        table.ajax.reload();
                    },
                    error: function (xhr) {
                        alert('Error deleting address: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function updateUserFront() {
                const id = $('#editUserFrontId').val();
                const data = { name: $('#editUserFrontName').val() };
                $.ajax({
                    url: '/api/user-front/' + id,
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        $('#editUserFrontModal').modal('hide');
                        table.ajax.reload();
                        alert('Updated successfully');
                    },
                    error: function (xhr) {
                        alert('Error updating: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function updateProduct() {
                const id = $('#editProductId').val();
                const data = getFormData('#editProductForm');
                data.mrp = parseFloat(data.mrp);
                data.sellingPrice = parseFloat(data.sellingPrice);
                data.stockQuantity = parseFloat(data.stockQuantity);

                $.ajax({
                    url: '/api/products/' + id,
                    type: 'PUT',
                    headers: { 'userId': userId },
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        $('#editProductModal').modal('hide');
                        table.ajax.reload();
                        alert('Product updated successfully');
                    },
                    error: function (xhr) {
                        alert('Error updating product: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function openDeleteModal(id, type) {
                $('#deleteId').val(id);
                $('#deleteType').val(type);
                $('#deleteConfirmModal').modal('show');
            }

            function confirmDelete() {
                const id = $('#deleteId').val();
                const type = $('#deleteType').val();
                let url = '';
                let headers = {};

                if (type === 'Product') {
                    url = '/api/products/' + id;
                    headers = { 'userId': userId };
                } else {
                    url = '/api/user-front/' + id;
                }

                $.ajax({
                    url: url,
                    type: 'DELETE',
                    headers: headers,
                    success: function () {
                        $('#deleteConfirmModal').modal('hide');
                        table.ajax.reload();
                        alert('Deleted successfully');
                    },
                    error: function (xhr) {
                        alert('Error deleting: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function getFormData(selector) {
                const array = $(selector).serializeArray();
                const json = {};
                $.each(array, function () {
                    json[this.name] = this.value || '';
                });
                return json;
            }

            function loadCountries() {
                $.get('/api/countries', function(response) {
                    if (response.success && response.data) {
                        const select = $('#countryFilter');
                        response.data.forEach(country => {
                            select.append(new Option(country.name, country.name));
                        });
                    }
                });
            }
            
            $('#countryFilter').on('change', function() {
                table.ajax.reload();
            });

            function logout() {
                $.post('/api/logout', function () {
                    window.location.href = '/login';
                });
            }
        </script>
    </body>

    </html>
