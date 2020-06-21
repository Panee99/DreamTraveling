$(function () {
  let currentDate = {
    date: new Date(),
    toString: function () {
      let dd = String(this.date.getDate()).padStart(2, "0");
      let mm = String(this.date.getMonth() + 1).padStart(2, "0");
      let yyyy = this.date.getFullYear();
      return dd + "/" + mm + "/" + yyyy;
    },
    // ,
    // add: function (days) {
    //   this.date.setDate(this.date.getDate() + days);
    //   return this;
    // },
    // minus: function (days) {
    //   this.date.setDate(this.date.getDate() - days);
    //   return this;
    // },
  };
  // init date picker
  $(".user-date-picker-couple").daterangepicker({
    showDropdowns: true,
    autoUpdateInput: false,
    locale: {
      format: "DD/MM/YYYY",
      applyLabel: "OK",
      cancelLabel: "Clear",
    },
    minDate: currentDate.toString(),
    opens: "center",
  });
  $(".user-date-picker-couple").on("apply.daterangepicker", function (
    ev,
    picker
  ) {
    $(this).val(
      picker.startDate.format("DD/MM/YYYY") +
        " - " +
        picker.endDate.format("DD/MM/YYYY")
    );
  });

  $(".user-date-picker-couple").on("cancel.daterangepicker", function (
    ev,
    picker
  ) {
    $(this).val("");
  });
  $(".admin-date-picker-couple").daterangepicker({
    showDropdowns: true,
    autoUpdateInput: false,
    locale: {
      format: "DD/MM/YYYY",
      applyLabel: "OK",
      cancelLabel: "Clear",
    },
    opens: "center",
  });
  $(".admin-date-picker-couple").on("apply.daterangepicker", function (
    ev,
    picker
  ) {
    $(this).val(
      picker.startDate.format("DD/MM/YYYY") +
        " - " +
        picker.endDate.format("DD/MM/YYYY")
    );
  });

  $(".admin-date-picker-couple").on("cancel.daterangepicker", function (
    ev,
    picker
  ) {
    $(this).val("");
  });
  // init range slider
  $(".range-slider").jRange({
    from: 0,
    to: 10000000,
    step: 500000,
    snap: true,
    format: function (value, pointer) {
      return (
        value.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ".") + " đ"
      );
    },
    width: 300,
    showLabels: true,
    isRange: true,
  });
  $(".range-slider").each(function () {
    let value = $(this).data("value");
    if (value && value.length > 0) {
      $(this).jRange("setValue", value);
    } else {
      $(this).jRange("setValue", "0,10000000");
    }
  });
  // modal animate
  $(".modal").each(function () {
    $(this).on("show.bs.modal", function () {
      $(this)
        .find(".modal-dialog")
        .attr(
          "class",
          "modal-dialog modal-dialog-centered animate__animated animate__bounceIn"
        );
    });
  });
  $(".modal").each(function () {
    $(this).on("hide.bs.modal", function () {
      $(this)
        .find(".modal-dialog")
        .attr(
          "class",
          "modal-dialog modal-dialog-centered animate__animated animate__zoomOut"
        );
    });
  });
  // modal auto focus
  $(".modal").each(function () {
    $(this).on("shown.bs.modal", function () {
      $(this).find("[autofocus]").focus();
    });
  });

  // ajax login
  function ajaxLogin() {
    let formLogin = $("#FormLogin");
    let errorLogin = $("#error-login");
    formLogin.submit((e) => {
      e.preventDefault();
      errorLogin.html("");
      let data = formLogin.serialize();
      let url = formLogin.attr("action");
      $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        success: function (data) {
          if (data.error) {
            errorLogin.html(data.error);
          } else if (data.redirect) {
            window.location.replace(data.redirect);
          }
        },
        error: function (xhr, ajaxOptions, thrownError) {
          alert(thrownError);
        },
      });
    });
  }
  ajaxLogin();
  // ajax register
  function ajaxRegister() {
    let formRegister = $("#FormRegister");
    let errorRegister = $("#error-egister");
    formRegister.submit((e) => {
      e.preventDefault();
      errorRegister.html("");
      $("[id^=error-]").html("");
      $("#success-register").html("");
      let data = formRegister.serialize();
      let url = formRegister.attr("action");
      $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        success: function (data) {
          if (data.error) {
            if (typeof data.error === "object") {
              console.log("is object");
              for (const err in data.error) {
                console.log(err);
                $("#error-" + err).html(data.error[err]);
              }
            } else {
              $("#error-register").html(data.error);
            }
          } else {
            $("#success-register").html(data.message);
          }
        },
        error: function (xhr, ajaxOptions, thrownError) {
          alert(thrownError);
        },
      });
    });
  }
  ajaxRegister();
  // format number
  function formatNumberWithCommas() {
    $(".numberCommas").each(function () {
      $(this).html(
        $(this)
          .html()
          .replace(/\B(?=(\d{3})+(?!\d))/g, ".")
      );
    });
  }
  formatNumberWithCommas();
  // form
  $("input").attr("autocomplete", "off");
  // upload image
  $("#fileTourImage").on("change", function () {
    console.log("changed");
    let input = this;
    if (input.files.length > 0) {
      let file = input.files[0];
      let fileType = file["type"];
      let validImageTypes = ["image/jpeg", "image/png"];
      if ($.inArray(fileType, validImageTypes) < 0) {
        alert("please upload image jpg or png");
        return;
      }
      let reader = new FileReader();
      reader.onload = (e) => {
        $("#fileTourImageDemo").css({
          "background-image": `url(${e.target.result})`,
        });
      };
      reader.readAsDataURL(input.files[0]);
    }
  });
});
// ajax booking
async function bookTour(id, name, totalAmount) {
  const { value: amount } = await Swal.fire({
    title: `Booking [${name}]`,
    input: "number",
    inputValue: 1,
    inputPlaceholder: "Amount",
    showCancelButton: true,
    inputValidator: (value) => {
      if (value < 1) {
        return "Amount min: 1";
      } else if (value > totalAmount) {
        return `Amount max: ${totalAmount}`;
      }
    },
  });
  if (amount) {
    $.ajax({
      type: "POST",
      url: "BookTour",
      data: {
        id: id,
        amount: amount,
      },
      dataType: "json",
      success: function (data, textStatus, xhr) {
        if (data.action !== undefined) {
          Swal.fire("Success!", `${data.action} tour ${name}`, "success");
          console.log(data.cart);
        }
      },
      error: function (xhr, ajaxOptions, thrownError) {
        if (xhr.status === 401) {
          Swal.fire("Failed!", "Please login before book tour", "error");
        } else {
          alert(thrownError);
        }
      },
    });
  }
}
// alert not login
function requireLogin() {
  Swal.fire("Failed!", "Please login before book tour", "error");
}
// viewCart action
async function removeFromCard(id, name) {
  Swal.fire({
    title: "Are you sure?",
    text: `Remove ${name} form shopping cart`,
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#28a745",
    cancelButtonColor: "#d33",
    confirmButtonText: "Yes, delete it!",
  }).then((result) => {
    if (result.value) {
      $.ajax({
        type: "POST",
        url: "RemoveFromCart",
        data: {
          id: id,
        },
        dataType: "json",
        success: function (data) {
          Swal.fire("Deleted!", `You removed ${name} from shopping cart`, "success");
          $(`#row-tour-${id}`).remove().then(updateViewShoppingCart());
        },
        error: function (xhr, ajaxOptions, thrownError) {
          alert(thrownError);
        },
      });
    }
  });
}
function updateViewShoppingCart(){
  if ($("#view-cart .card-body>.row").length === 0){
    $("#view-cart .card-body").remove();
    $("#view-cart .card-footer").remove();
    $(".shopping-card").append(` <div class="card-footer">
    <h3 class="text-danger">Your cart is empty <a href="./" class="btn btn-outline-info btn-sm">Continue shopping</a></h3>
</div>`);
  }
}