<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-light shadow-sm mb-5">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}">${initParam.web_name}</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarMainMenu" aria-controls="navbarMainMenu" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMainMenu">
            <!-- <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Link</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Dropdown
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="#">Action</a>
                        <a class="dropdown-item" href="#">Another action</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#">Something else here</a>
                    </div>
                </li>
            </ul> -->
            <ul class="navbar-nav ml-auto">
                <c:if test="${empty sessionScope.user}">
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-toggle="modal" data-target="#ModalLogin">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-toggle="modal" data-target="#ModalRegister">Register</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item">
                        <div class="dropdown">
                            <a class="btn btn-outline-secondary" href="#" role="button" data-target="#DropdownUserMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-user mr-1"></i> ${sessionScope.user.name}
                            </a>
                            <div class="dropdown-menu dropdown-menu-right w-100" id="DropdownUserMenu">
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/Logout">Log out</a>
                            </div>
                        </div>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>