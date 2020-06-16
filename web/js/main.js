$(function () {
  let currentDate = {
    date: new Date(),
    toString: function () {
      let dd = String(this.date.getDate()).padStart(2, "0");
      let mm = String(this.date.getMonth() + 1).padStart(2, "0");
      let yyyy = this.date.getFullYear();
      return dd + "/" + mm + "/" + yyyy;
    },
    add: function (days) {
      this.date.setDate(this.date.getDate() + days);
      return this;
    },
    minus: function (days) {
      this.date.setDate(this.date.getDate() - days);
      return this;
    },
  };
  // init datedropper
  $(".date-dropper-check-in-out").dateDropper({
    format: "d/m/Y",
    largeOnly: true,
    lock: "from",
    theme: "m",
    defaultDate: null,
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
  $(".range-slider").jRange("setValue", "0,10000000");
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
      console.log("shown");
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
          } else if (data.action === "refresh") {
            location.reload();
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
            }else{
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
});
