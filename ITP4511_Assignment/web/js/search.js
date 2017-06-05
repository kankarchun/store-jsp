for (var i = 0; i < fliter.children.length; i++) {
    (function (e) {
        e.children[0].children[0].onclick = function () {
            if (e.children[1].classList.toggle("hide")) {
                this.className = "fa fa-plus";
            } else {
                this.className = "fa fa-minus";
            }
        };
    })(fliter.children[i]);
}

var orgin = result.cloneNode(true);

function restore() {
    container.removeChild(result);
    container.appendChild(orgin);
    orgin = result.cloneNode(true);
}

function sortByPrice(desc) {
    var price = [];
    if (result.children.length >= 3) {
        for (var i = 1; i < result.children.length; i++) {
            price.push(parseInt(result.children[i].children[6].children[5].innerHTML.substring(9)));
        }
        price.sort();
        if (desc) {
            price.reverse();
        }
        var sorting_result = document.createElement("div");
        sorting_result.id = "result";
        var title = document.createElement("b");
        if (desc) {
            title.innerHTML = "Sort By Price (high to low):";
        } else {
            title.innerHTML = "Sort By Price (low to high):";
        }
        sorting_result.appendChild(title);
        result.removeChild(result.children[0]);
        while (price.length) {
            var index = price.shift();
            for (var i = 0; i < result.children.length; i++) {
                if (index === parseInt(result.children[i].children[6].children[5].innerHTML.substring(9))) {
                    sorting_result.appendChild(result.children[i]);
                    break;
                }
            }
        }
        container.removeChild(result);
        container.appendChild(sorting_result);
    }
}

function sortByName(desc) {
    var price = [];
    if (result.children.length >= 3) {
        for (var i = 1; i < result.children.length; i++) {
            price.push(result.children[i].children[6].children[2].innerHTML.substring(10));
        }
        price.sort();
        if (desc) {
            price.reverse();
        }
        var sorting_result = document.createElement("div");
        sorting_result.id = "result";
        var title = document.createElement("b");
        if (desc) {
            title.innerHTML = "Sort By Name (Z to A):";
        } else {
            title.innerHTML = "Sort By Name (A to Z):";
        }
        sorting_result.appendChild(title);
        result.removeChild(result.children[0]);
        while (price.length) {
            var index = price.shift();
            for (var i = 0; i < result.children.length; i++) {
                if (index === result.children[i].children[6].children[2].innerHTML.substring(10)) {
                    sorting_result.appendChild(result.children[i]);
                    break;
                }
            }
        }
        container.removeChild(result);
        container.appendChild(sorting_result);
    }
}
var fliters = {
    fliterByID: [0, 3],
    fliterByCategory: [1, 14],
    fliterByName: [2, 10],
    fliterByDesigner: [3, 9],
    fliterByDescription: [4, 12]
};

function fliterBy(type, value) {
    restore();
    if (result.children.length >= 2) {
        var sorting_result = document.createElement("div");
        sorting_result.id = "result";
        var sorting_result = document.createElement("div");
        sorting_result.id = "result";
        var title = document.createElement("b");
        title.innerHTML = "Fliter Result:";
        sorting_result.appendChild(title);
        result.removeChild(result.children[0]);
        for (var i = 0; i < result.children.length; i++) {
            if (result.children[i].children[6].children[type[0]].innerHTML.substring(type[1]).indexOf(value) >= 0) {
                sorting_result.appendChild(result.children[i].cloneNode(true));
            }
        }
        container.removeChild(result);
        container.appendChild(sorting_result);
    }
}

function priceLess(value) {
    restore();
    if (result.children.length >= 2) {
        var sorting_result = document.createElement("div");
        sorting_result.id = "result";
        var title = document.createElement("b");
        title.innerHTML = "Fliter Result:";
        sorting_result.appendChild(title);
        result.removeChild(result.children[0]);
        for (var i = 0; i < result.children.length; i++) {
            if (parseInt(result.children[i].children[6].children[5].innerHTML.substring(9)) <= value) {
                sorting_result.appendChild(result.children[i].cloneNode(true));
            }
        }
        container.removeChild(result);
        container.appendChild(sorting_result);
    }
}

function quatityLarge(value) {
    restore();
    if (result.children.length >= 2) {
        var sorting_result = document.createElement("div");
        sorting_result.id = "result";
        var title = document.createElement("b");
        title.innerHTML = "Fliter Result:";
        sorting_result.appendChild(title);
        result.removeChild(result.children[0]);
        for (var i = 0; i < result.children.length; i++) {
            if (parseInt(result.children[i].children[6].children[6].innerHTML.substring(9)) >= value) {
                sorting_result.appendChild(result.children[i].cloneNode(true));
            }
        }
        container.removeChild(result);
        container.appendChild(sorting_result);
    }
}

