<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Roles Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container-fluid mt-3">
        <div class="row">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header bg-warning text-dark">
                        <h4><i class="fas fa-user-tag"></i> Roles Management</h4>
                        <a href="/" class="btn btn-dark btn-sm float-end">
                            <i class="fas fa-home"></i> Home
                        </a>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#createRoleModal">
                                    <i class="fas fa-plus"></i> Create Role
                                </button>
                                <button class="btn btn-info ms-2" data-bs-toggle="modal" data-bs-target="#assignRoleModal">
                                    <i class="fas fa-user-plus"></i> Assign Role
                                </button>
                            </div>
                        </div>
                        
                        <table id="rolesTable" class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>ID</th>
                                    <th>Role Name</th>
                                    <th>Description</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="role" items="${roles}">
                                    <tr>
                                        <td>${role.roleId}</td>
                                        <td>${role.roleName}</td>
                                        <td>Role Description</td>
                                        <td>
                                            <button class="btn btn-sm btn-danger" onclick="deleteRole(${role.roleId})">
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

    <div class="modal fade" id="createRoleModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Create Role</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="createRoleForm">
                        <div class="mb-3">
                            <label class="form-label">Role Name</label>
                            <input type="text" class="form-control" name="roleName" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-warning" onclick="createRole()">Create</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="assignRoleModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Assign Role to User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="assignRoleForm">
                        <div class="mb-3">
                            <label class="form-label">User/Company</label>
                            <select class="form-select" name="userFrontId" required>
                                <option value="">Select User/Company</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Role</label>
                            <select class="form-select" name="roleId" required>
                                <option value="">Select Role</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-info" onclick="assignRole()">Assign</button>
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
            table = $('#rolesTable').DataTable({
                ajax: {
                    url: '/api/roles/all',
                    dataSrc: 'data'
                },
                columns: [
                    { data: 'roleId' },
                    { data: 'roleName' },
                    { 
                        data: null,
                        render: function() {
                            return 'Role Description';
                        }
                    },
                    {
                        data: null,
                        render: function(data) {
                            return '<button class="btn btn-sm btn-danger" onclick="deleteRole(' + data.roleId + ')"><i class="fas fa-trash"></i></button>';
                        }
                    }
                ]
            });
            
            loadUsers();
            loadRoles();
        });
        
        function loadUsers() {
            $.get('/api/user-front/companies', function(response) {
                const select = $('select[name="userFrontId"]');
                select.empty().append('<option value="">Select User/Company</option>');
                response.data.forEach(user => {
                    const type = user.parentCompanyId ? 'Branch' : 'Company';
                    select.append('<option value="' + user.userFrontId + '">' + user.name + ' (' + type + ')</option>');
                });
            });
        }
        
        function loadRoles() {
            $.get('/api/roles/all', function(response) {
                const select = $('select[name="roleId"]');
                select.empty().append('<option value="">Select Role</option>');
                response.data.forEach(role => {
                    select.append('<option value="' + role.roleId + '">' + role.roleName + '</option>');
                });
            });
        }
        
        function createRole() {
            const formData = new FormData($('#createRoleForm')[0]);
            const data = Object.fromEntries(formData);
            
            $.ajax({
                url: '/api/roles/create',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function() {
                    $('#createRoleModal').modal('hide');
                    table.ajax.reload();
                    loadRoles();
                    alert('Role created successfully!');
                },
                error: function() {
                    alert('Error creating role');
                }
            });
        }
        
        function assignRole() {
            const formData = new FormData($('#assignRoleForm')[0]);
            const data = Object.fromEntries(formData);
            
            if (!data.userFrontId || !data.roleId) {
                alert('Please select both user and role');
                return;
            }
            
            data.userFrontId = parseInt(data.userFrontId);
            data.roleId = parseInt(data.roleId);
            
            $.ajax({
                url: '/api/user-front/assign-role',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function() {
                    $('#assignRoleModal').modal('hide');
                    $('#assignRoleForm')[0].reset();
                    alert('Role assigned successfully!');
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert('Error: Role may already be assigned to this user or invalid data provided.');
                    } else {
                        alert('Error assigning role');
                    }
                }
            });
        }
        
        function deleteRole(id) {
            if(confirm('Are you sure you want to delete this role? This will also remove all user assignments for this role.')) {
                $.ajax({
                    url: '/api/roles/' + id,
                    method: 'DELETE',
                    success: function() {
                        table.ajax.reload();
                        loadRoles();
                        alert('Role deleted successfully!');
                    },
                    error: function(xhr) {
                        if (xhr.status === 400) {
                            alert('Cannot delete role: It may be assigned to users or have dependencies.');
                        } else {
                            alert('Error deleting role');
                        }
                    }
                });
            }
        }
    </script>
</body>
</html>