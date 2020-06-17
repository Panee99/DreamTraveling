<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" scope="page" value="Admin"/>
<%@include file="../includes/header.jsp" %>

<!-- top navbar -->
<%@include file="../includes/navbar.jsp" %>
<!-- end top navbar -->
<!-- body -->
<!-- container -->
<div class="container">
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
                <tr class="text-center">
                    <th scope="row" class="td-id">1</th>
                    <td class="td-name">Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark Mark</td>
                    <td class="td-img">
                        <figure class="figure">
                            <img src="https://loremflickr.com/cache/resized/65535_49893876713_6f84caeef0_320_240_nofilter.jpg" class="figure-img img-fluid rounded" alt="cat">
                        </figure>
                    </td>
                    <td class="td-date">13/06/2020<span>-</span>13/06/2020</td>
                    <td class="td-amount">10</td>
                    <td class="td-price">1000000<span>đ</span></td>
                    <td class="td-action">
                        <a href="#" class="btn btn-primary">Edit</a>
                        <a href="#" class="btn btn-danger">Delete</a>
                    </td>
                </tr>
            </tbody>
        </table>
        <!-- end table list tour -->
    </div>
<!-- end container -->
<!-- end body -->

<%@include file="../includes/footer.jsp" %>