<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sales & Purchase Transactions</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container-fluid mt-3">
        <div class="card shadow">
            <div class="card-header bg-dark text-white">
                <h4><i class="fas fa-exchange-alt"></i> Sales & Purchase Transactions</h4>
                <div class="float-end">
                    <button onclick="openSalesModal()" class="btn btn-success btn-sm me-1"><i class="fas fa-plus"></i> New Sale</button>
                    <button onclick="openPurchaseModal()" class="btn btn-primary btn-sm me-1"><i class="fas fa-plus"></i> New Purchase</button>
                    <a href="/" class="btn btn-light btn-sm"><i class="fas fa-home"></i> Home</a>
                </div>
            </div>
            <div class="card-body">
                <ul class="nav nav-tabs mb-3" id="transactionTabs">
                    <li class="nav-item">
                        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#sales-tab">Sales</button>
                    </li>
                    <li class="nav-item">
                        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#purchase-tab">Purchases</button>
                    </li>
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
                                    <th>Total Amount</th>
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
                                    <th>Total Amount</th>
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

    <!-- Sales Modal -->
    <div class="modal fade" id="salesModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Create Sale</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="salesForm">
                        <div class="row">
                            <div class="col-md-6 mb-2">
                                <label>Company</label>
                                <select class="form-select" name="companyId" required></select>
                            </div>
                            <div class="col-md-6 mb-2">
                                <label>Branch</label>
                                <select class="form-select" name="branchId" required></select>
                            </div>
                            <div class="col-md-4 mb-2">
                                <label>Prefix</label>
                                <input type="text" class="form-control" name="prefix" value="INV">
                            </div>
                            <div class="col-md-8 mb-2">
                                <label>Sales No</label>
                                <input type="text" class="form-control" name="salesNo" required>
                            </div>
                        </div>
                        <hr>
                        <h6>Items <button type="button" class="btn btn-sm btn-primary float-end" onclick="addSalesItem()"><i class="fas fa-plus"></i></button></h6>
                        <div id="salesItems">
                            <div class="row mb-2 sales-item">
                                <div class="col-md-5">
                                    <select class="form-select product-select" required></select>
                                </div>
                                <div class="col-md-3">
                                    <input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required>
                                </div>
                                <div class="col-md-3">
                                    <input type="number" class="form-control price" placeholder="Price" step="0.01" required>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest('.sales-item').remove(); calcTotal()"><i class="fas fa-trash"></i></button>
                                </div>
                            </div>
                        </div>
                        <div class="text-end mt-3">
                            <h5>Total: ₹<span id="salesTotal">0.00</span></h5>
                            <input type="hidden" name="totalAmount" id="salesTotalInput">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-success" onclick="saveSales()">Create Sale</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Purchase Modal -->
    <div class="modal fade" id="purchaseModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Create Purchase</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="purchaseForm">
                        <div class="row">
                            <div class="col-md-6 mb-2">
                                <label>Company</label>
                                <select class="form-select" name="companyId" required></select>
                            </div>
                            <div class="col-md-6 mb-2">
                                <label>Branch</label>
                                <select class="form-select" name="branchId" required></select>
                            </div>
                            <div class="col-md-4 mb-2">
                                <label>Prefix</label>
                                <input type="text" class="form-control" name="prefix" value="PO">
                            </div>
                            <div class="col-md-8 mb-2">
                                <label>Purchase No</label>
                                <input type="text" class="form-control" name="purchaseNo" required>
                            </div>
                        </div>
                        <hr>
                        <h6>Items <button type="button" class="btn btn-sm btn-primary float-end" onclick="addPurchaseItem()"><i class="fas fa-plus"></i></button></h6>
                        <div id="purchaseItems">
                            <div class="row mb-2 purchase-item">
                                <div class="col-md-5">
                                    <select class="form-select product-select" required></select>
                                </div>
                                <div class="col-md-3">
                                    <input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required>
                                </div>
                                <div class="col-md-3">
                                    <input type="number" class="form-control price" placeholder="Price" step="0.01" required>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest('.purchase-item').remove(); calcTotal()"><i class="fas fa-trash"></i></button>
                                </div>
                            </div>
                        </div>
                        <div class="text-end mt-3">
                            <h5>Total: ₹<span id="purchaseTotal">0.00</span></h5>
                            <input type="hidden" name="totalAmount" id="purchaseTotalInput">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="savePurchase()">Create Purchase</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

    <script>
        let salesTable, purchaseTable;
        let companies = [], products = [];

        $(document).ready(function() {
            loadData();
            
            salesTable = $('#salesTable').DataTable({
                ajax: { url: '/api/sales', dataSrc: 'data' },
                columns: [
                    { data: 'salesId' },
                    { data: 'companyId' },
                    { data: 'branchId' },
                    { data: 'salesNo' },
                    { data: 'totalAmount', render: d => '₹' + d },
                    { data: 'salesDate' }
                ]
            });

            purchaseTable = $('#purchaseTable').DataTable({
                ajax: { url: '/api/purchases', dataSrc: 'data' },
                columns: [
                    { data: 'purchaseId' },
                    { data: 'companyId' },
                    { data: 'branchId' },
                    { data: 'purchaseNo' },
                    { data: 'totalAmount', render: d => '₹' + d },
                    { data: 'purchaseDate' }
                ]
            });

            $(document).on('input', '.quantity, .price', calcTotal);
        });

        function loadData() {
            $.get('/api/user-front', function(r) {
                companies = r.data || [];
            });
            $.get('/api/products', function(r) {
                products = r.data || [];
            });
        }

        function openSalesModal() {
            $('#salesForm')[0].reset();
            $('#salesItems').html('<div class="row mb-2 sales-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" required></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.sales-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>');
            populateSelects('#salesForm');
            $('#salesModal').modal('show');
        }

        function openPurchaseModal() {
            $('#purchaseForm')[0].reset();
            $('#purchaseItems').html('<div class="row mb-2 purchase-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" required></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.purchase-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>');
            populateSelects('#purchaseForm');
            $('#purchaseModal').modal('show');
        }

        function populateSelects(formSelector) {
            $(formSelector + ' [name="companyId"], ' + formSelector + ' [name="branchId"]').each(function() {
                const sel = $(this);
                sel.empty().append('<option value="">Select...</option>');
                companies.forEach(c => sel.append(new Option(c.name, c.userFrontId)));
            });
            $(formSelector + ' .product-select').each(function() {
                const sel = $(this);
                sel.empty().append('<option value="">Select Product...</option>');
                products.forEach(p => sel.append(new Option(p.productName, p.productId)));
            });
        }

        function addSalesItem() {
            const html = '<div class="row mb-2 sales-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" required></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.sales-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>';
            $('#salesItems').append(html);
            $('#salesItems .product-select:last').each(function() {
                const sel = $(this);
                sel.empty().append('<option value="">Select Product...</option>');
                products.forEach(p => sel.append(new Option(p.productName, p.productId)));
            });
        }

        function addPurchaseItem() {
            const html = '<div class="row mb-2 purchase-item"><div class="col-md-5"><select class="form-select product-select" required></select></div><div class="col-md-3"><input type="number" class="form-control quantity" placeholder="Quantity" step="0.01" required></div><div class="col-md-3"><input type="number" class="form-control price" placeholder="Price" step="0.01" required></div><div class="col-md-1"><button type="button" class="btn btn-danger btn-sm" onclick="$(this).closest(\'.purchase-item\').remove(); calcTotal()"><i class="fas fa-trash"></i></button></div></div>';
            $('#purchaseItems').append(html);
            $('#purchaseItems .product-select:last').each(function() {
                const sel = $(this);
                sel.empty().append('<option value="">Select Product...</option>');
                products.forEach(p => sel.append(new Option(p.productName, p.productId)));
            });
        }

        function calcTotal() {
            let total = 0;
            $('.sales-item, .purchase-item').each(function() {
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
            $('#salesItems .sales-item').each(function() {
                items.push({
                    productId: parseInt($(this).find('.product-select').val()),
                    quantity: parseFloat($(this).find('.quantity').val()),
                    sellingPrice: parseFloat($(this).find('.price').val())
                });
            });

            const data = {
                companyId: parseInt(form.find('[name="companyId"]').val()),
                branchId: parseInt(form.find('[name="branchId"]').val()),
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
                success: function() {
                    $('#salesModal').modal('hide');
                    salesTable.ajax.reload();
                    alert('Sale created successfully');
                },
                error: function(xhr) {
                    alert('Error: ' + (xhr.responseJSON?.message || 'Unknown error'));
                }
            });
        }

        function savePurchase() {
            const form = $('#purchaseForm');
            const items = [];
            $('#purchaseItems .purchase-item').each(function() {
                items.push({
                    productId: parseInt($(this).find('.product-select').val()),
                    quantity: parseFloat($(this).find('.quantity').val()),
                    purchasePrice: parseFloat($(this).find('.price').val())
                });
            });

            const data = {
                companyId: parseInt(form.find('[name="companyId"]').val()),
                branchId: parseInt(form.find('[name="branchId"]').val()),
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
                success: function() {
                    $('#purchaseModal').modal('hide');
                    purchaseTable.ajax.reload();
                    alert('Purchase created successfully');
                },
                error: function(xhr) {
                    alert('Error: ' + (xhr.responseJSON?.message || 'Unknown error'));
                }
            });
        }
    </script>
</body>
</html>
