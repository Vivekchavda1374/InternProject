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
                        <button id="btnNewCompany" onclick="openCreateCompanyModal()"
                            class="btn btn-primary btn-sm me-1" style="display:none;"><i class="fas fa-plus"></i> New
                            Company</button>
                        <button id="btnNewBranch" onclick="openCreateBranchModal()" class="btn btn-success btn-sm me-1"
                            style="display:none;"><i class="fas fa-code-branch"></i> New Branch</button>
                        <button id="btnNewProduct" onclick="openCreateProductModal()" class="btn btn-info btn-sm me-1"
                            style="display:none;"><i class="fas fa-box"></i> New Product</button>
                        <a href="/" class="btn btn-light btn-sm"><i class="fas fa-home"></i> Home</a>
                        <button onclick="logout()" class="btn btn-danger btn-sm"><i class="fas fa-sign-out-alt"></i>
                            Logout</button>
                    </div>
                </div>
                <div class="card-body">
                    <ul class="nav nav-tabs mb-3">
                        <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab"
                                data-bs-target="#data-tab">Data</button></li>
                        <li class="nav-item"><button class="nav-link" data-bs-toggle="tab"
                                data-bs-target="#transactions-tab">Transactions</button></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade show active" id="data-tab">
                            <div id="accessDenied" class="alert alert-danger" style="display:none;">
                                <i class="fas fa-exclamation-triangle"></i> Access Denied. You don't have permission to
                                view
                                this data.
                            </div>
                            <div class="mb-3">
                                <label for="countryFilter" class="form-label"><i class="fas fa-filter"></i> Filter by
                                    Country:</label>
                                <select id="countryFilter" class="form-select" style="max-width: 300px;">
                                    <option value="">All Countries</option>
                                </select>
                            </div>
                            <table id="completeTable" class="table table-striped table-bordered table-hover table-sm">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Company</th>
                                        <th>Branch</th>
                                        <th>Total Purchase</th>
                                        <th>Total Sales</th>
                                        <th>Total Products</th>
                                        <th>GST</th>
                                        <th>Phone</th>
                                        <th>Address</th>
                                        <th>City</th>
                                        <th>State</th>
                                        <th>Country</th>
<%--                                        <th>Actions</th>--%>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                        <div class="tab-pane fade" id="transactions-tab">
                            <div class="mb-3">
                                <button onclick="openSalesModal()" class="btn btn-success btn-sm me-1"><i
                                        class="fas fa-plus"></i> New Sale</button>
                                <button onclick="openPurchaseModal()" class="btn btn-primary btn-sm"><i
                                        class="fas fa-plus"></i> New Purchase</button>
                                <button onclick="openExternalPurchaseModal()" class="btn btn-warning btn-sm ms-1"><i
                                        class="fas fa-exchange-alt"></i> External Purchase</button>
                            </div>
                            <ul class="nav nav-pills mb-3">
                                <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab"
                                        data-bs-target="#sales-tab">Sales</button></li>
                                <li class="nav-item"><button class="nav-link" data-bs-toggle="tab"
                                        data-bs-target="#purchase-tab">Purchases</button></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade show active" id="sales-tab">
                                    <table id="salesTable" class="table table-striped table-bordered table-sm">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>ID</th>
                                                <th>Company</th>
                                                <th>Branch</th>
                                                <th>Sales No</th>
                                                <th>Total</th>
                                                <th>Date</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                                <div class="tab-pane fade" id="purchase-tab">
                                    <table id="purchaseTable" class="table table-striped table-bordered table-sm">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>ID</th>
                                                <th>Company</th>
                                                <th>Branch</th>
                                                <th>Purchase No</th>
                                                <th>Total</th>
                                                <th>Date</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="toast-container position-fixed top-0 end-0 p-3" id="toastContainer" style="z-index: 1080;"></div>

        <div class="modal fade" id="createProductModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Create Product</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="createProductForm">
                            <div class="mb-2">
                                <label>Company/Branch</label>
                                <select class="form-select" name="companyId" id="productCompanyId" required></select>
                            </div>
                            <div class="mb-2"><label>Product Name</label><input type="text" class="form-control"
                                    name="productName" required></div>
                            <div class="mb-2"><label>Item Code</label><input type="text" class="form-control"
                                    name="itemCode"></div>
                            <div class="row">
                                <div class="col-md-6 mb-2"><label>MRP</label><input type="number" step="0.01"
                                        class="form-control" name="mrp"></div>
                                <div class="col-md-6 mb-2"><label>Selling Price</label><input type="number" step="0.01"
                                        class="form-control" name="sellingPrice"></div>
                            </div>
                            <div class="mb-2"><label>Stock Quantity</label><input type="number" step="0.01"
                                    class="form-control" name="stockQuantity"></div>
                            <div class="mb-2"><label>Description</label><textarea class="form-control"
                                    name="description"></textarea></div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" onclick="saveProduct()">Create</button>
                    </div>
                </div>
            </div>
        </div>

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
                                        name="name" autocomplete="organization" required></div>
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
                                        name="name" autocomplete="organization" required></div>
                                <div class="col-md-6 mb-2"><label>Password</label><input type="password"
                                        class="form-control" name="password" autocomplete="new-password"></div>
                                <div class="col-md-6 mb-2"><label>GST No</label><input type="text" class="form-control"
                                        name="gstNo"></div>
                                <div class="col-md-6 mb-2"><label>Phone</label><input type="text" class="form-control"
                                        name="phoneNo"></div>
                            </div>

                            <hr>
                            <h6>Addresses <button type="button" class="btn btn-sm btn-outline-primary float-end"
                                    onclick="addAddressField('#branchAddresses')"><i class="fas fa-plus"></i> Add
                                    Address</button></h6>
                            <div id="branchAddresses">
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
                        <button type="button" class="btn btn-primary" onclick="saveBranch()">Create</button>
                    </div>
                </div>
            </div>
        </div>

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
                                        <input type="text" class="form-control" name="name" id="editUserFrontName" autocomplete="organization"
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

        <div class="modal fade" id="salesModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Create Sale</h5><button type="button" class="btn-close"
                            data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="salesForm">
                            <div class="row">
                                <div class="col-md-6 mb-2"><label>Company</label><select class="form-select"
                                        name="companyId" required></select></div>
                                <div class="col-md-6 mb-2"><label>Branch</label><select class="form-select"
                                        name="branchId" required></select></div>
                                <div class="col-md-4 mb-2"><label>Prefix</label><input type="text" class="form-control"
                                        name="prefix" value="INV"></div>
                                <div class="col-md-8 mb-2"><label>Sales No</label><input type="text"
                                        class="form-control" name="salesNo" required></div>
                            </div>
                            <hr>
                            <h6>Items <button type="button" class="btn btn-sm btn-primary float-end"
                                    onclick="addSalesItem()"><i class="fas fa-plus"></i></button></h6>
                            <div id="salesItems"></div>
                            <div class="text-end mt-3">
                                <h5>Total: ₹<span id="salesTotal">0.00</span></h5><input type="hidden"
                                    name="totalAmount" id="salesTotalInput">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer"><button type="button" class="btn btn-secondary"
                            data-bs-dismiss="modal">Close</button><button type="button" class="btn btn-success"
                            onclick="saveSales()">Create</button></div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="purchaseModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Create Purchase</h5><button type="button" class="btn-close"
                            data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="purchaseForm">
                            <div class="row">
                                <div class="col-md-6 mb-2"><label>Company</label><select class="form-select"
                                        name="companyId" required></select></div>
                                <div class="col-md-6 mb-2"><label>Branch</label><select class="form-select"
                                        name="branchId" required></select></div>
                                <div class="col-md-4 mb-2"><label>Prefix</label><input type="text" class="form-control"
                                        name="prefix" value="PO"></div>
                                <div class="col-md-8 mb-2"><label>Purchase No</label><input type="text"
                                        class="form-control" name="purchaseNo" required></div>
                            </div>
                            <hr>
                            <h6>Items <button type="button" class="btn btn-sm btn-primary float-end"
                                    onclick="addPurchaseItem()"><i class="fas fa-plus"></i></button></h6>
                            <div id="purchaseItems"></div>
                            <div class="text-end mt-3">
                                <h5>Total: ₹<span id="purchaseTotal">0.00</span></h5><input type="hidden"
                                    name="totalAmount" id="purchaseTotalInput">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer"><button type="button" class="btn btn-secondary"
                            data-bs-dismiss="modal">Close</button><button type="button" class="btn btn-primary"
                            onclick="savePurchase()">Create</button></div>
                </div>
            </div>
        </div>

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
            let table, salesTable, purchaseTable;
            let currentUserId = null;
            let currentUserName = null;
            let currentUserIsAdmin = false;
            let companies = [], products = [];
            let externalPurchaseMode = false;
            const namePattern = /^[A-Za-z0-9 ]+_*/;

            $(document).ready(function () {
                $.get('/api/session', function (response) {
                    if (!response.success) {
                        window.location.href = '/login';
                        return;
                    }

                    currentUserId = response.data.userId;
                    currentUserName = response.data.name;
                    const isAdmin = response.data.isAdmin;
                    currentUserIsAdmin = isAdmin;
                    const isCompany = response.data.isCompany;
                    const userNameDisplay = response.data.name || 'User';

                    $('#userInfo').html('<strong>' + userNameDisplay + '</strong> (' + (isAdmin ? 'Admin' : (isCompany ? 'Company' : 'Branch')) + ')');

                    if (isAdmin) {
                        $('#btnNewCompany').show();
                        $('#btnNewBranch').show();
                        $('#btnNewProduct').show();
                    } else if (isCompany) {
                        $('#btnNewBranch').show();
                        $('#btnNewProduct').show();
                    } else {
                        $('#btnNewProduct').show();
                    }

                    loadTable();
                    loadCountries();
                    setTimeout(loadTransactionData, 500);
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
                const closeClass = type === 'warning' ? 'btn-close' : 'btn-close btn-close-white';
                const toast = $(`
                    <div class="toast align-items-center ${toastClass} border-0 mb-2" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body"></div>
                            <button type="button" class="${closeClass} me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
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
                return namePattern.test(name);
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

            function loadCountries() {
                $('#countryFilter').off('change').on('change', function () {
                    if (table) {
                        table.ajax.reload();
                    }
                });
            }

            function refreshCountryFilterOptions(rows) {
                const select = $('#countryFilter');
                const currentValue = select.val();
                const countries = [...new Set((rows || [])
                    .map(r => (r.country || '').trim())
                    .filter(Boolean))]
                    .sort((a, b) => a.localeCompare(b));

                select.find('option:not(:first)').remove();
                countries.forEach(country => select.append(new Option(country, country)));

                if (currentValue && countries.includes(currentValue)) {
                    select.val(currentValue);
                } else {
                    select.val('');
                }
            }

            function loadTransactionData() {
                $.get('/api/user-front/companies/' + currentUserId, function (r) {
                    companies = r.data || [];
                    if (salesTable) salesTable.ajax.reload(null, false);
                    if (purchaseTable) purchaseTable.ajax.reload(null, false);
                });

                $.ajax({
                    url: '/api/products',
                    type: 'GET',
                    success: function (response) {
                        const list = response?.data || [];
                        products = list.map(p => ({
                            id: p.productId ?? p.id,
                            productName: p.productName,
                            companyId: p.companyId ?? p.userFrontId,
                            mrp: p.mrp,
                            sellingPrice: p.sellingPrice
                        }));
                    }
                });


                salesTable = $('#salesTable').DataTable({
                    ajax: {
                        url: '/api/sales',
                        dataSrc: function (json) {
                            if (currentUserIsAdmin) {
                                return json.data || [];
                            } else {
                                return (json.data || []).filter(s => s.companyId == currentUserId || s.branchId == currentUserId);
                            }
                        }
                    },
                    columns: [
                        { data: 'salesId' },
                        { data: 'companyId', render: function (data) { return resolveEntityName(data); } },
                        { data: 'branchId', render: function (data) { return resolveEntityName(data); } },
                        { data: 'salesNo' },
                        { data: 'totalAmount', render: d => '₹' + d },
                        { data: 'salesDate' }
                    ]
                });

                purchaseTable = $('#purchaseTable').DataTable({
                    ajax: {
                        url: '/api/purchases',
                        dataSrc: function (json) {
                            if (currentUserIsAdmin) {
                                return json.data || [];
                            } else {
                                return (json.data || []).filter(p => p.companyId == currentUserId || p.branchId == currentUserId);
                            }
                        }
                    },
                    columns: [
                        { data: 'purchaseId' },
                        { data: 'companyId', render: function (data) { return resolveEntityName(data); } },
                        { data: 'branchId', render: function (data) { return resolveEntityName(data); } },
                        { data: 'purchaseNo' },
                        { data: 'totalAmount', render: d => '₹' + d },
                        { data: 'purchaseDate' }
                    ]
                });

                $(document).on('input', '.quantity, .price', calcTotal);
                $(document).on('change', '#salesForm .product-select', function () {
                    updateItemPrice($(this), 'sales');
                });
                $(document).on('change', '#purchaseForm .product-select', function () {
                    updateItemPrice($(this), 'purchase');
                });
            }

            function resolveEntityName(entityId) {
                if (entityId === null || entityId === undefined || entityId === '') {
                    return '';
                }
                const entity = companies.find(c => (c.userFrontId ?? c.id) == entityId);
                return entity ? entity.name : entityId;
            }

            function loadTable() {

                if (table) {
                    table.ajax.reload()
                    return;
                }
                table = $('#completeTable').DataTable({
                    ajax: {
                        url: '/api/complete',
                        data: function(d) {
                            d.country = $('#countryFilter').val();
                        },
                        headers: {
                            'userId': currentUserId,
                            'isAdmin': currentUserIsAdmin
                        },
                        dataSrc: function (json) {
                            refreshCountryFilterOptions(json || []);
                            return json || [];
                        },
                        error: function () {
                            alert('Error loading data. Please check your permissions.');
                        }
                    },
                    columns: [
                        { data: 'id' },
                        { data: 'companyName', defaultContent: '' },
                        { data: 'branchName', defaultContent: '' },
                        {
                            data: 'totalPurchaseAmount',
                            defaultContent: '0',
                            render: function (data) {
                                const value = parseFloat(data || 0);
                                return '₹' + value.toFixed(2);
                            }
                        },
                        {
                            data: 'totalSalesAmount',
                            defaultContent: '0',
                            render: function (data) {
                                const value = parseFloat(data || 0);
                                return '₹' + value.toFixed(2);
                            }
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
                        { data: 'country', defaultContent: '' }
                        // {
                        //     data: null,
                        //     render: function (data, type, row) {
                        //         let buttons = '';
                        //         if (row.type === 'Product') {
                        //             buttons += '<button class="btn btn-warning btn-sm me-1" onclick="openEditModal(this)" title="Edit Product"><i class="fas fa-edit"></i> Prod</button>';
                        //             buttons += '<button class="btn btn-info btn-sm me-1" onclick="openEditCompanyModal(' + row.userFrontId + ', \'' + (row.companyName || row.branchName || '').replace(/'/g, "\\'") + '\')" title="Edit Company/Branch"><i class="fas fa-building"></i> Co.</button>';
                        //         } else {
                        //             buttons += '<button class="btn btn-warning btn-sm me-1" onclick="openEditModal(this)" title="Edit"><i class="fas fa-edit"></i> Edit</button>';
                        //             buttons += '<button class="btn btn-info btn-sm me-1" onclick="openCreateProductForEntity(' + row.id + ', \'' + (row.type === 'Company' ? row.companyName : row.branchName).replace(/'/g, "\\'") + '\')" title="Add Product"><i class="fas fa-plus"></i> Prod</button>';
                        //         }
                        //         buttons += '<button class="btn btn-danger btn-sm" onclick="openDeleteModal(' + row.id + ', \'' + row.type + '\')" title="Delete"><i class="fas fa-trash"></i></button>';
                        //         return buttons;
                        //     }
                        // }
                    ],
                    scrollX: true,
                    pageLength: 25,
                    order: [[0, 'asc']],
                    searching: true,
                });
            }

            function openCreateProductModal() {
                $('#createProductForm')[0].reset();
                const select = $('#productCompanyId');
                select.empty();

                $.get('/api/session', function (response) {
                    const isAdmin = response.data.isAdmin;
                    const isCompany = response.data.isCompany;
                    const userId = response.data.userId;
                    const userName = response.data.name;

                    if (isAdmin) {
                        $.get('/api/user-front/companies', function (compResponse) {
                            if (compResponse.success) {
                                compResponse.data.forEach(c => {
                                    const entityId = c.userFrontId ?? c.id;
                                    select.append(new Option(c.name, entityId));
                                });
                            }
                            $('#createProductModal').modal('show');
                        });
                    } else {
                        select.append(new Option(userName, userId));
                        $('#createProductModal').modal('show');
                    }
                });
            }
            function openCreateProductForEntity(id, name) {
                $('#createProductForm')[0].reset();
                const select = $('#productCompanyId');
                select.empty();
                select.append(new Option(name, id));
                $('#createProductModal').modal('show');
            }

            function saveProduct() {
                const form = $('#createProductForm');
                const data = getFormData('#createProductForm');
                data.mrp = parseFloat(data.mrp) || 0;
                data.sellingPrice = parseFloat(data.sellingPrice) || 0;
                data.stockQuantity = parseFloat(data.stockQuantity) || 0;
                let companyId = data.companyId;
                if (isNaN(parseInt(companyId))) {
                    const company = companies.find(c => c.name === companyId);
                    companyId = company ? (company.userFrontId ?? company.id) : null;
                }

                companyId = parseInt(companyId);
                if (!companyId || isNaN(companyId)) {
                    alert('Invalid company selection. Please select a valid company.');
                    return;
                }

                data.companyId = companyId;

                $.ajax({
                    url: '/api/products/create',
                    type: 'POST',
                    headers: { 'userId': currentUserId, 'companyId': companyId },
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function (response) {
                        $('#createProductModal').modal('hide');
                        table.ajax.reload();
                        alert('Product created successfully');
                    },
                    error: function (xhr) {
                        alert('Error creating product: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function openCreateCompanyModal() {
                $('#createCompanyForm')[0].reset();
                $('#companyAddresses').find('.address-block:not(:first)').remove();
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
                        name: mainData.name,
                        addressType: block.find('[name="addressType"]').val() || 'Primary',
                        addressLine1: block.find('[name="addressLine1"]').val(),
                        addressLine2: block.find('[name="addressLine2"]').val(),
                        city: block.find('[name="city"]').val(),
                        state: block.find('[name="state"]').val(),
                        country: block.find('[name="country"]').val()
                    });
                });

                const creationData = { ...mainData, ...addresses[0] };

                $.ajax({
                    url: '/api/user-front/company/create',
                    type: 'POST',
                    headers: { 'userId': currentUserId },
                    contentType: 'application/json',
                    data: JSON.stringify(creationData),
                    success: function (response) {
                        const newId = response.data.userFrontId ?? response.data.id;
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
                $('#branchAddresses').find('.address-block:not(:first)').remove();
                const select = $('#branchParentCompany');
                select.empty().append('<option value="">Select Company</option>');
                let isCompanyUser = $('#btnNewCompany').css('display') === 'none' && $('#btnNewBranch').css('display') !== 'none';
                $.get('/api/session', function (response) {
                    const isAdmin = response.data.isAdmin;
                    const isCompany = response.data.isCompany;
                    const userId = response.data.userId;
                    const userName = response.data.name;

                    if (isCompany && !isAdmin) {
                        select.append(new Option(userName, userId));
                        $('#createBranchModal').modal('show');
                    } else {
                        $.get('/api/user-front/companies', function (compResponse) {
                            if (compResponse.success) {
                                compResponse.data.filter(c => !c.parentCompanyId).forEach(c => {
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

                const mainData = {
                    parentCompanyId: parentCompanyId,
                    name: branchName,
                    password: password,
                    gstNo: form.find('[name="gstNo"]').val(),
                    phoneNo: form.find('[name="phoneNo"]').val()
                };

                const addresses = [];
                form.find('.address-block').each(function () {
                    const block = $(this);
                    addresses.push({
                        name: mainData.name,
                        addressType: block.find('[name="addressType"]').val() || 'Primary',
                        addressLine1: block.find('[name="addressLine1"]').val(),
                        addressLine2: block.find('[name="addressLine2"]').val(),
                        city: block.find('[name="city"]').val(),
                        state: block.find('[name="state"]').val(),
                        country: block.find('[name="country"]').val()
                    });
                });

                const creationData = { ...mainData, ...addresses[0] };

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
                        success: function (response) {
                            const newId = response.data.userFrontId ?? response.data.id;
                            if (addresses.length > 1) {
                                saveAdditionalAddresses(newId, addresses.slice(1), () => {
                                    $('#createBranchModal').modal('hide');
                                    table.ajax.reload();
                                    showToast('Branch and addresses created successfully', 'success');
                                });
                            } else {
                                $('#createBranchModal').modal('hide');
                                table.ajax.reload();
                                showToast('Branch created successfully', 'success');
                            }
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
                        table.ajax.reload();
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
                    headers: { 'userId': currentUserId },
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
                    headers = { 'userId': currentUserId };
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

            function openSalesModal() {
                $('#salesForm')[0].reset();
                $('#salesItems').html('<div class="row mb-2 sales-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" readonly></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.sales-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>');
                populateSelects('#salesForm');
                if (!currentUserIsAdmin) {
                    $('#salesForm [name="companyId"]').val(currentUserId).prop('disabled', true);
                    $('#salesForm [name="branchId"]').val(currentUserId).prop('disabled', true);
                    filterProducts('#salesForm', currentUserId);
                }
                setTimeout(function () {
                    $('#salesForm [name="companyId"]').off('change').on('change', function () {
                        const selectedOption = $(this).find('option:selected');
                        const companyId = selectedOption.val();
                        filterProducts('#salesForm', companyId);
                    });
                }, 100);
                $('#salesModal').modal('show');
            }

            function openPurchaseModal() {
                externalPurchaseMode = false;
                $('#purchaseForm')[0].reset();
                $('#purchaseItems').html('<div class="row mb-2 purchase-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" readonly></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.purchase-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>');
                populateSelects('#purchaseForm');
                if (!currentUserIsAdmin) {
                    $('#purchaseForm [name="companyId"]').val(currentUserId).prop('disabled', true);
                    $('#purchaseForm [name="branchId"]').val(currentUserId).prop('disabled', true);
                    filterProducts('#purchaseForm', currentUserId);
                }
                setTimeout(function () {
                    $('#purchaseForm [name="companyId"]').off('change').on('change', function () {
                        const selectedOption = $(this).find('option:selected');
                        const companyId = selectedOption.val();
                        filterProducts('#purchaseForm', companyId);
                    });
                }, 100);
                $('#purchaseModal').modal('show');
            }

            function openExternalPurchaseModal() {
                externalPurchaseMode = true;
                $('#purchaseForm')[0].reset();
                $('#purchaseItems').html('<div class="row mb-2 purchase-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" readonly></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.purchase-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>');
                populateSelects('#purchaseForm');
                calcTotal();
                loadAllProducts(function () {
                    if (!currentUserIsAdmin) {
                        $('#purchaseForm [name="companyId"]').val(currentUserId).prop('disabled', true);
                        $('#purchaseForm [name="branchId"]').val(currentUserId).prop('disabled', true);
                        filterProducts('#purchaseForm', currentUserId);
                    } else {
                        const companyId = $('#purchaseForm [name="companyId"]').val();
                        filterProducts('#purchaseForm', companyId);
                    }
                    $('#purchaseForm [name="companyId"]').off('change').on('change', function () {
                        const companyId = $(this).val();
                        filterProducts('#purchaseForm', companyId);
                    });
                    $('#purchaseModal').modal('show');
                });
            }

            function filterProducts(formSelector, companyId) {
                // Find the actual numeric ID from companies array
                const company = companies.find(c => c.name === companyId || (c.userFrontId ?? c.id) == companyId);
                const actualCompanyId = company ? (company.userFrontId ?? company.id) : companyId;

                $(formSelector + ' .product-select').each(function () {
                    const sel = $(this);
                    sel.empty().append('<option value="">Select Product...</option>');
                    if (externalPurchaseMode && formSelector === '#purchaseForm') {
                        products.forEach(p => {
                            const ownerName = resolveEntityName(p.companyId);
                            sel.append(new Option(p.productName + ' [' + ownerName + ']', p.id));
                        });
                        return;
                    }
                    if (actualCompanyId) {
                        products
                            .filter(p => p.companyId == actualCompanyId)
                            .forEach(p => sel.append(new Option(p.productName, p.id)));
                    }
                });
            }

            function loadAllProducts(callback) {
                $.ajax({
                    url: '/api/products',
                    type: 'GET',
                    success: function (response) {
                        const list = response?.data || [];
                        products = list.map(p => ({
                            id: p.productId ?? p.id,
                            productName: p.productName,
                            companyId: p.companyId ?? p.userFrontId,
                            mrp: p.mrp,
                            sellingPrice: p.sellingPrice
                        }));
                        if (callback) callback();
                    },
                    error: function () {
                        alert('Failed to load external products');
                    }
                });
            }

            function populateSelects(formSelector) {
                $(formSelector + ' [name="companyId"], ' + formSelector + ' [name="branchId"]').each(function () {
                    const sel = $(this);
                    sel.empty().append('<option value="">Select...</option>');
                    companies.forEach(c => {
                        const entityId = c.userFrontId ?? c.id;
                        sel.append(new Option(c.name, entityId));
                    });
                });
            }

            function addSalesItem() {
                const html = '<div class="row mb-2 sales-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" readonly></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.sales-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>';
                $('#salesItems').append(html);
                const companyId = $('#salesForm [name="companyId"]').val();
                if (companyId) filterProducts('#salesForm', companyId);
            }

            function addPurchaseItem() {
                const html = '<div class="row mb-2 purchase-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" readonly></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.purchase-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>';
                $('#purchaseItems').append(html);
                const companyId = $('#purchaseForm [name="companyId"]').val();
                if (externalPurchaseMode) {
                    filterProducts('#purchaseForm', companyId);
                } else if (companyId) {
                    filterProducts('#purchaseForm', companyId);
                }
            }

            function updateItemPrice(productSelect, transactionType) {
                const productId = parseInt(productSelect.val());
                const row = productSelect.closest('.sales-item, .purchase-item');
                const priceInput = row.find('.price');

                if (!productId || isNaN(productId)) {
                    priceInput.val('');
                    calcTotal();
                    return;
                }

                const product = products.find(p => p.id == productId);
                if (!product) {
                    priceInput.val('');
                    calcTotal();
                    return;
                }

                let price = 0;
                if (transactionType === 'sales') {
                    price = parseFloat(product.sellingPrice) || 0;
                } else {
                    price = resolvePurchaseUnitPrice(product);
                }
                priceInput.val(price.toFixed(2));
                calcTotal();
            }

            function resolvePurchaseUnitPrice(product) {
                const mrp = parseFloat(product.mrp);
                if (!isNaN(mrp) && mrp > 0) {
                    return mrp;
                }
                const sellingPrice = parseFloat(product.sellingPrice);
                if (!isNaN(sellingPrice) && sellingPrice > 0) {
                    return sellingPrice;
                }
                return 0;
            }

            function calcTotal() {
                let total = 0;
                $('.sales-item, .purchase-item').each(function () {
                    const qty = parseFloat($(this).find('.quantity').val()) || 0;
                    const price = parseFloat($(this).find('.price').val()) || 0;
                    total += qty * price;
                });
                $('#salesTotal, #purchaseTotal').text(total.toFixed(2));
                $('#salesTotalInput, #purchaseTotalInput').val(total);
            }

            function saveSales() {
                const form = $('#salesForm');
                const items = [];
                $('#salesItems .sales-item').each(function () {
                    items.push({
                        productId: parseInt($(this).find('.product-select').val()),
                        quantity: parseFloat($(this).find('.quantity').val()),
                        sellingPrice: parseFloat($(this).find('.price').val())
                    });
                });

                const companyId = parseInt(form.find('[name="companyId"]').val());
                const branchId = parseInt(form.find('[name="branchId"]').val());

                if (!companyId || !branchId || isNaN(companyId) || isNaN(branchId)) {
                    alert('Please select both Company and Branch');
                    return;
                }

                const data = {
                    companyId: companyId,
                    branchId: branchId,
                    prefix: form.find('[name="prefix"]').val(),
                    salesNo: form.find('[name="salesNo"]').val(),
                    totalAmount: parseFloat($('#salesTotalInput').val()),
                    items: items
                };

                $.ajax({
                    url: '/api/sales',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        $('#salesModal').modal('hide');
                        salesTable.ajax.reload();
                        alert('Sale created successfully');
                    },
                    error: function (xhr) {
                        alert('Error: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function savePurchase() {
                const form = $('#purchaseForm');
                const items = [];
                let invalidItem = false;
                $('#purchaseItems .purchase-item').each(function () {
                    const productId = parseInt($(this).find('.product-select').val());
                    const quantity = parseFloat($(this).find('.quantity').val());
                    const purchasePrice = parseFloat($(this).find('.price').val());
                    if (!productId || isNaN(productId) || !quantity || isNaN(quantity) || quantity <= 0 || isNaN(purchasePrice) || purchasePrice <= 0) {
                        invalidItem = true;
                        return;
                    }
                    items.push({
                        productId: productId,
                        quantity: quantity,
                        purchasePrice: purchasePrice
                    });
                });

                if (invalidItem || items.length === 0) {
                    alert('Please select product and enter valid quantity for all purchase items.');
                    return;
                }

                const companyId = parseInt(form.find('[name="companyId"]').val());
                const branchId = parseInt(form.find('[name="branchId"]').val());

                if (!companyId || !branchId || isNaN(companyId) || isNaN(branchId)) {
                    alert('Please select both Company and Branch');
                    return;
                }

                const data = {
                    companyId: companyId,
                    branchId: branchId,
                    allowExternalProducts: externalPurchaseMode,
                    prefix: form.find('[name="prefix"]').val(),
                    purchaseNo: form.find('[name="purchaseNo"]').val(),
                    totalAmount: parseFloat($('#purchaseTotalInput').val()),
                    items: items
                };

                $.ajax({
                    url: '/api/purchases',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        $('#purchaseModal').modal('hide');
                        purchaseTable.ajax.reload();
                        alert('Purchase created successfully');
                    },
                    error: function (xhr) {
                        alert('Error: ' + (xhr.responseJSON?.message || 'Unknown error'));
                    }
                });
            }

            function logout() {
                $.post('/api/logout', function () {
                    window.location.href = '/login';
                });
            }
        </script>
    </body>

    </html>
