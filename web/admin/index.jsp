<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" scope="page" value="Admin" />
<%@include file="../includes/header.jsp" %>

<!-- top navbar -->
<%@include file="../includes/navbar.jsp" %>
<!-- end top navbar -->
<!-- body -->
<!-- container -->
<div class="container">
    <!-- search -->
    <section class="search-tour">
        <form class="form-inline justify-content-end" action="LoadAdmin" method="POST">
            <div class="form-group mr-2">
                <input type="text" class="form-control date-picker-couple" placeholder="From - To" value="${param.dateRange}" name="dateRange">
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
    <!-- new tour -->
    <a href="NewTour" class="btn btn-success">New</a>
    <!-- end new tour -->
    <!-- table list tour -->
    <table class="table table-bordered" id="table-tour">
        <thead>
            <tr class="text-center">
                <th scope="col">ID</th>
                <th scope="col">Name</th>
                <th scope="col">Image</th>
                <th scope="col">Date</th>
                <th scope="col">Amount</th>
                <th scope="col">Price</th>
                <th scope="col">Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="tour" items="${requestScope.list_tour}" varStatus="count">
                <%-- row tour --%>
                <tr class="text-center">
                    <th scope="row" class="td-id">${count.index + 1}</th>
                    <td class="td-name">${tour.value.name}</td>
                    <td class="td-img">
                        <figure class="figure">
                            <img src="${tour.value.image}" class="figure-img img-fluid rounded" alt="${tour.value.name}">
                        </figure>
                    </td>
                    <td class="td-date"><fmt:formatDate value="${tour.value.fromDate}" pattern="dd/MM/yyyy" /><span>-</span><fmt:formatDate value="${tour.value.toDate}" pattern="dd/MM/yyyy" /></td>
                    <td class="td-amount">${tour.value.quantity}</td>
                    <td class="td-price numberCommas">${tour.value.price}<span>đ</span></td>
                    <td class="td-action">
                        <a href="UpdateTour?id=${tour.value.id}" class="btn btn-primary">Edit</a>
                        <a href="DeleteTour?id=${tour.value.id}" class="btn btn-danger">Delete</a>
                    </td>
                </tr>
                <%-- end row tour --%>
            </c:forEach>
        </tbody>
    </table>
    <!-- end table list tour -->
    <c:if test="${not empty requestScope.total_page}">
        <!-- pagination -->
        <nav aria-label="Tours mb-5">
            <ul class="pagination justify-content-end">
                <c:forEach var="page" begin="1" end="${requestScope.total_page}" step="1">
                    <form action="LoadAdmin" method="POST">
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

</div>
<!-- end container -->
<!-- end body -->

<%@include file="../includes/footer.jsp" %>