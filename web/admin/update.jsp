<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="../includes/header.jsp" %>

<!-- top navbar -->
<%@include file="includes/navbar.jsp" %>
<!-- end top navbar -->
<c:if test="${requestScope.form_action eq 'UpdateTour'}">
    <c:set var="tName" value="${requestScope.tour.name}"/>
    <fmt:formatDate value="${requestScope.tour.fromDate}" pattern="dd/MM/yyyy" var="fromDate"/>
    <fmt:formatDate value="${requestScope.tour.toDate}" pattern="dd/MM/yyyy" var="toDate"/>
    <c:set var="tDate" value="${pageScope.fromDate} - ${pageScope.toDate}"/>
    <c:set var="tPrice" value="${requestScope.tour.price}"/>
    <c:set var="tQuantity" value="${requestScope.tour.quantity}"/>
    <c:set var="tReview" value="${requestScope.tour.review}"/>
    <c:set var="tImage" value="${requestScope.tour.image}"/>
</c:if>
<!-- body -->
<!-- container -->
<div class="container">
    <c:if test="${requestScope.message.has('success')}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${requestScope.message.get('success')}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>
    <c:if test="${requestScope.message.has('error')}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${requestScope.message.get('error')}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>
    <form action="${requestScope.form_action}" method="POST" enctype="multipart/form-data" id="formTour">
        <input type="hidden" name="id" value="${param.id}">
        <div class="row">
            <div class="col-5 text-center">
                <figure class="figure">
                    <c:if test="${empty tImage}">
                        <c:set var="bgImg" value="${pageContext.request.contextPath}/images/non-image.jpg" />
                    </c:if>
                    <c:if test="${not empty tImage}">
                        <c:set var="bgImg" value="${tImage}" />
                    </c:if>
                    <label id="fileTourImageDemo" class="tour-image figure-img img-fluid rounded" style="background-image: url(${bgImg});">
                        <input type="file" name="fileTourImage" id="fileTourImage">
                    </label>
                </figure>
                <c:if test="${requestScope.error_validate.has('image')}">
                    <p class="text-danger">${requestScope.error_validate.get('image')}</p>
                </c:if>
            </div>
            <div class="col-7">
                <div style="width: 500px;" class="ml-auto">
                    <div class="form-group row">
                        <label for="tourName" class="col-2 col-form-label col-form-label-lg font-weight-bold">Name:</label>
                        <div class="col-10">
                            <input type="text" class="form-control-lg w-100" id="tourName" name="tourName" value="${tName}">
                            <c:if test="${requestScope.error_validate.has('name')}">
                                <p class="text-danger">${requestScope.error_validate.get('name')}</p>     
                            </c:if>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="tourName" class="col-2 col-form-label col-form-label-lg font-weight-bold">Date:</label>
                        <div class="col-10 text-center">
                            <input type="text" class="form-control-lg w-100 admin-date-picker-couple" placeholder="From - To" name="tourDate" value="${tDate}">
                            <c:if test="${requestScope.error_validate.has('date')}">
                                <p class="text-danger text-left">${requestScope.error_validate.get('date')}</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="tourPrice" class="col-2 col-form-label col-form-label-lg font-weight-bold">Price:</label>
                        <div class="col-10">
                            <input type="number" class="form-control-lg w-100" id="tourPrice" name="tourPrice" min="0" max="100000000" value="${tPrice}">
                            <c:if test="${requestScope.error_validate.has('price')}">
                                <p class="text-danger">${requestScope.error_validate.get('price')}</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="tourQuantity" class="col-2 col-form-label col-form-label-lg font-weight-bold">Quantity:</label>
                        <div class="col-10">
                            <input type="number" class="form-control-lg w-100" id="tourQuantity" name="tourQuantity" min="0" max="100" value="${tQuantity}">
                            <c:if test="${requestScope.error_validate.has('quantity')}">
                                <p class="text-danger">${requestScope.error_validate.get('quantity')}</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="tourReview" class="col-2 col-form-label col-form-label-lg font-weight-bold">Review:</label>
                        <div class="col-10">
                            <input type="text" class="form-control-lg w-100" id="tourReview" name="tourReview" value="${tReview}">
                        </div>
                    </div>
                    <button class="btn btn-lg btn-success float-right">Save</button>
                </div>
            </div>
        </div>
    </form>

</div>
<!-- end container -->
<!-- end body -->
<c:if test="${empty requestScope.error_validate}">
    <script>
        document.querySelector("#formTour").reset();
    </script>
</c:if>
<%@include file="../includes/footer.jsp" %>