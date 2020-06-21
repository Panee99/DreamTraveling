<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" scope="page" value="View Cart" />

<%@include file="/includes/header.jsp" %>

<!-- top navbar -->
<%@include file="/includes/navbar.jsp" %>
<!-- end top navbar -->
<!-- body -->
<!-- container -->
<div class="container">
    <c:if test="${not empty requestScope.error}">
        <h1 class="text-danger">${requestScope.error}</h1>
        <a href="./">Back</a>
    </c:if>
    <c:if test="${empty requestScope.error}">
        <section id="view-cart">
            <div class="card shopping-card">
                <div class="card-header bg-dark text-light">
                    <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                    Shopping cart
                    <a href="./" class="btn btn-outline-info btn-sm float-right">Continue shopping</a>
                    <div class="clearfix"></div>
                </div>
                <c:if test="${not empty requestScope.view_cart}">
                    <div class="card-body">
                        <c:forEach var="tour" items="${requestScope.view_cart}">
                            <div class="row" id="row-tour-${tour.key}">
                                <div class="col-2 text-center">
                                    <figure class="figure">
                                        <label class="tour-image sm figure-img img-fluid rounded" style="background-image: url(${tour.value.image})"></label>
                                    </figure>
                                </div>
                                <div class="col-6">
                                    <h4 class="product-name"><strong>${tour.value.name}</strong></h4>
                                    <h6>
                                        <fmt:formatDate value="${tour.value.fromDate}" pattern="dd/MM/yyyy" /></span> - <span>
                                            <fmt:formatDate value="${tour.value.toDate}" pattern="dd/MM/yyyy" /></span>
                                    </h6>
                                </div>
                                <div class="col-4 row">
                                    <div class="col-6 text-md-right" style="padding-top: 5px">
                                        <h6 class="my-2"><strong class="numberCommas">${tour.value.price} đ<span class="text-muted ml-3">x</span></strong></h6>
                                    </div>
                                    <div class="col-4">
                                        <div class="quantity">
                                            <input type="number" max="100" min="1" value="${tour.value.quantity}" class="swal2-input my-0" onchange="updateFromCart(this, ${tour.key}, '${tour.value.name}', ${tour.value.quantity})">
                                            <c:if test="${not empty requestScope.over_quantity && requestScope.over_quantity.containsKey(tour.key)}">
                                                <small class="text-danger">We just have ${requestScope.over_quantity.get(tour.key)} ${tour.value.name}</small>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="col-2text-right">
                                        <a href="#" type="button" class="btn btn-outline-danger btn-xs my-1" onclick="removeFromCard(${tour.key})">
                                            <i class="fa fa-trash" aria-hidden="true"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="card-footer clearfix">
                        <div class="col-5 float-left">
                            <div class="row">
                                <div class="col-6">
                                    <input type="text" class="form-control" placeholder="Discount" id="discountCode">
                                </div>
                                <div class="col-6">
                                    <button class="btn btn-primary" onclick="useDiscountCode()">Use discount</button>
                                </div>
                            </div>
                        </div>
                        <div class="float-right">
                            <a href="#" class="btn btn-success float-right">Checkout</a>
                            <div class="float-right form-control w-auto mr-3">
                                Total price: <b><span class="numberCommas" id="totalPrice">0</span> đ</b>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${empty requestScope.view_cart}">
                    <div class="card-footer">
                        <h3 class="text-danger">Your cart is empty <a href="./" class="btn btn-outline-info btn-sm">Continue shopping</a></h3>
                    </div>
                </c:if>
            </div>
        </section>
    </c:if>
</div>
<!-- end container -->
<!-- end body -->

<%@include file="/includes/footer.jsp" %>