document.loginFrm.addEventListener('submit', (e) => {
    const loginFrm = e.target;
    const checkbox = loginFrm.saveId;
    const id = loginFrm.id;

    if(checkbox.checked) {
        localStorage.setItem("saveId", id.value);
    }
    else {
        localStorage.removeItem("saveId");
    }
});

(() => {
    const checkbox = document.querySelector("#saveId");
    const input = document.querySelector("#id");
    checkbox.checked = false;

    if(!(localStorage.getItem("saveId") == null)) {
        input.value = localStorage.getItem("saveId");
        checkbox.checked = true;
    }
})();