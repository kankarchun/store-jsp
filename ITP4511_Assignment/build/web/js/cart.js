if (document.getElementsByTagName("table")[0]) {
    var e = document.getElementsByTagName("table")[0].getElementsByTagName("td");
    for (var i = 0; i < e.length; i = i + 6) {
        (function (select, price, id) {
            select.oninput = function () {
                location="cart?action=update&id="+id+"&quantity="+this.value;
            };
        })(e[i + 3].firstElementChild, e[i + 2].innerHTML.substring(4), e[i].firstElementChild.alt);
    }
  /*  function total() {
        var total = 0;
        for (var i = 0; i < e.length; i = i + 6) {
            total += parseInt(e[i + 4].innerHTML.substring(4));
        }
        var e_ = document.getElementsByTagName("table")[1].getElementsByTagName("tr");
        e_[0].lastElementChild.innerHTML = "HKD$" + total;
        e_[2].lastElementChild.innerHTML = "HKD$" + (total + parseInt(e_[1].lastElementChild.innerHTML.substring(4)));
    }
    total();*/
}
