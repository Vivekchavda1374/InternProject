<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Company Branches</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container-fluid mt-3">
        <div class="row">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header bg-info text-white">
                        <h4><i class="fas fa-code-branch"></i> Company Branches</h4>
                        <a href="/users" class="btn btn-light btn-sm float-end">
                            <i class="fas fa-arrow-left"></i> Back to Users
                        </a>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <div class="input-group">
                                    <select class="form-select" id="companySelect">
                                        <option value="">Select Company</option>
                                    </select>
                                    <button class="btn btn-outline-secondary" onclick="loadBranches()">Load Branches</button>
                                </div>
                            </div>
                        </div>
                        
                        <table id="branchesTable" class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>Branch ID</th>
                                    <th>Branch Name</th>
                                    <th>Parent Company ID</th>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        let table;
        
        $(document).ready(function() {
            table = $('#branchesTable').DataTable({
                responsive: true,
                pageLength: 10
            });
            
            loadCompanies();
        });
        
        function loadCompanies() {
            $.get('/api/user-front/companies', function(response) {
                const select = $('#companySelect');
                select.empty().append('<option value="">Select Company</option>');
                response.data.filter(item => !item.parentCompanyId).forEach(company => {
                    select.append('<option value="' + company.userFrontId + '">' + company.name + '</option>');
                });
            });
        }
        
        function loadBranches() {
            const companyId = $('#companySelect').val();
            if (!companyId) {
                alert('Please select a company first');
                return;
            }
            
            $.ajax({
                url: '/api/user-front/branches/' + companyId,
                method: 'GET',
                success: function(response) {
                    table.clear();
                    response.data.forEach(branch => {
                        table.row.add([
                            branch.userFrontId,
                            branch.name,
                            branch.parentCompanyId,
                            '<button class="btn btn-sm btn-danger" onclick="deleteBranch(' + branch.userFrontId + ')"><i class="fas fa-trash"></i></button>'
                        ]);
                    });
                    table.draw();
                },
                error: function() {
                    alert('Error loading branches');
                }
            });
        }
        
        function deleteBranch(id) {
            if(confirm('Are you sure you want to delete this branch?')) {
                $.ajax({
                    url: '/api/user-front/' + id,
                    method: 'DELETE',
                    success: function() {
                        loadBranches();
                        alert('Branch deleted successfully!');
                    },
                    error: function() {
                        alert('Error deleting branch');
                    }
                });
            }
        }
    </script>
</body>
</html>