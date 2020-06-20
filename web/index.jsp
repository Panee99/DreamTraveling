<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" scope="page" value="${initParam.web_name}" />

<%@include file="/includes/header.jsp" %>

<!-- top navbar -->
<%@include file="/includes/navbar.jsp" %>
<!-- end top navbar -->
<!-- body -->
<!-- container -->
<div class="container">
    <!-- search -->
    <section class="search-tour">
        <form class="form-inline justify-content-end" action="LoadHome" method="POST">
            <div class="form-group mr-2">
                <input type="text" class="form-control user-date-picker-couple" placeholder="From - To" value="${param.dateRange}" name="dateRange">
            </div>
            <div class="form-group mr-2">
                <input type="text" class="form-control" name="name" placeholder="Name" value="${param.name}">
            </div>
            <button type="submit" class="btn btn-primary">Search</button>
            <div class="form-group justify-content-end w-100 mt-5 pr-5">
                <label class="mr-5">Price:</label>
                <input type="hidden" class="range-slider" name="priceRange" data-value="${param.priceRange}" />
            </div>
        </form>
    </section>
    <!-- end search -->
    <!-- list card -->
    <section class="list-card mb-5">
        <div class="row row-cols-3">
            <c:forEach var="tour" items="${requestScope.list_tour}">
                <!-- card -->
                <div class="col mb-4">
                    <div class="card">
                        <figure class="figure">
                            <label class="tour-image figure-img img-fluid rounded" style="background-image: url(${tour.value.image});">
                            </label>
                        </figure>
                        <div class="card-body">
                            <h5 class="card-title">${tour.value.name}</h5>
                            <div class="small mb-4"><span>
                                    <fmt:formatDate value="${tour.value.fromDate}" pattern="dd/MM/yyyy" /></span> - <span>
                                    <fmt:formatDate value="${tour.value.toDate}" pattern="dd/MM/yyyy" /></span></div>
                            <p>${tour.value.review}</p>
                            <c:if test="${not empty sessionScope.user}">
                                <button class="btn btn-success w-100 mt-3" onclick="bookTour(${tour.value.id}, '${tour.value.name}',${tour.value.quantity})">Book This Tour</button>
                            </c:if>
                            <c:if test="${empty sessionScope.user}">
                                <button class="btn btn-success w-100 mt-3" onclick="requireLogin()">Book This Tour</button>
                            </c:if>
                        </div>
                        <div class="card-footer">
                            <div class="row row-cols-2">
                                <div class="col">
                                    <small>Amount: <span>${tour.value.quantity}</span></small>
                                </div>
                                <div class="col text-right">
                                    <small class="numberCommas">${tour.value.price}<span>đ</span></small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end card -->
            </c:forEach>
        </div>
    </section>
    <c:if test="${not empty requestScope.total_page}">
        <!-- pagination -->
        <nav aria-label="Tours mb-5">
            <ul class="pagination justify-content-end">
                <c:forEach var="page" begin="1" end="${requestScope.total_page}" step="1">
                    <form action="LoadHome" method="POST">
                        <input type="hidden" name="page" value="${page}">
                        <c:if test="${page eq requestScope.page}">
                            <div class="page-item active">
                                <button class="page-link" type="button">${page}</button>
                            </div>
                        </c:if>
                        <c:if test="${page ne requestScope.page}">
                            <div class="page-item">
                                <button class="page-link" type="submit">${page}</button>
                            </div>
                        </c:if>
                    </form>
                </c:forEach>
            </ul>
        </nav>
        <!-- end pagination -->
    </c:if>
    <!-- end list cart -->
</div>
<!-- end container -->
<!-- end body -->

<%@include file="/includes/footer.jsp" %>