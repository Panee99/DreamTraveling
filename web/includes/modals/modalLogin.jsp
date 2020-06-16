<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- modal -->
<div class="modal fade" id="ModalLogin" tabindex="-1" role="dialog" aria-labelledby="ModalLogin" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/Login" id="FormLogin">
                <div class="modal-header">
                    <h5 class="modal-title" id="ModalLogin">Login</h5>
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