const hobbyEtc = document.querySelector("#hobby-etc");
hobbyEtc.addEventListener('keyup', (e) => {
    if(e.keyCode === 13) {
        e.target.innerHTML = e.target.innerHTML.replace(/<br>/g, '');
        e.target.blur();
    }
});
hobbyEtc.addEventListener('blur', (e) => {
    const value = e.target.innerHTML;
    if(value && value != '직접입력') {
        const html = `
                 <div class="inline-flex items-center mr-4">
                    <input id="hobby-${value}" type="checkbox" name="hobby" checked value="${value}" class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2" >
                    <label for="hobby-${value}" class="ms-2 text-sm font-medium text-gray-900">${value}</label>
                </div>`;
        e.target.parentElement.insertAdjacentHTML('beforebegin', html);
        e.target.innerHTML = '직접입력';
    }
});

/**
 * 회원정보 수정 유효성검사
 */

document.memberUpdateFrm.addEventListener('submit', (e) => {
    const frm = e.target;
    const name = frm.name;
    const email = frm.email;

    // 이름 한글 2글자 이상
    if(!/^[가-힣]{2,}$/g.test(name.value)){
        alert("이름은 한글 2글자 이상만 작성하세요..");
        e.preventDefault();
        return;
    }
    // 이메일 형식
    if(!/^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/.test(email.value)){
        alert("이메일 형식으로만 작성 가능합니다. (@포함)");
        e.preventDefault();
        return;
    }
});