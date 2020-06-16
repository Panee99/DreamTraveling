<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- modal Login -->
<div class="modal fade" id="ModalLogin" tabindex="-1" role="dialog" aria-labelledby="ModalLogin" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/Login" id="FormLogin">
                <div class="modal-header">
                    <h5 class="modal-title">Login</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <small class="text-danger" id="error-login"></small>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="username" placeholder="Username" autofocus>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" name="password" placeholder="Password">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Login</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- end modal -->

<!-- modal Register -->
<div class="modal fade" id="ModalRegister" tabindex="-1" role="dialog" aria-labelledby="ModalRegister" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/Register" id="FormRegister">
                <div class="modal-header">
                    <h5 class="modal-title">Register</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <small class="text-danger" id="error-register"></small>
                        <small class="text-success" id="success-register"></small>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="username" placeholder="Username" autofocus>
                        <small class="text-danger" id="error-username"></small>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" name="password" placeholder="Password">
                        <small class="text-danger" id="error-password"></small>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" name="repassword" placeholder="RePassword">
                        <small class="text-danger" id="error-repassword"></small>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="name" placeholder="Name">
                        <small class="text-danger" id="error-name"></small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Register</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- end modal -->