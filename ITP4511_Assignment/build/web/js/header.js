!function () {
    var e =  document.getElementById("search").children[0];
    logo.onclick = function () {
        window.location = "index.jsp";
    };
    if (menus.children.length > 0) {
        member.onmouseover = function () {
            logo.classList.add("hover");
        };
    }
    category.lastElementChild.onclick = function () {
        menu.classList.add("active"), e.focus()
    }, document.body.onclick = function (e) {
        if (e.target == this) {
            menu.classList.remove("active");
            logo.classList.remove("hover");
        }
    }, e.onkeypress = function (e) {
        if (13 == e.keyCode && this.value.length){
            window.location = "search?q=" + this.value;
        }
    }
}();