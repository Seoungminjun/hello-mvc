document.querySelector("#id").addEventListener('keyup', (e) =>{
    const value = e.target.value;
    console.log(value);

    const guideOk = document.querySelector(".guide.ok");
    const guideError = document.querySelector(".guide.error");
    const idValid = document.querySelector("#idValid");

    if (/^\w{4,}$/.test(value)){
        $.ajax( {
            url : `${contextPath}/member/checkIdDuplicate`,
            data : {
                id : value
            },
            success(response){
                const{result} = response;
                if(result) {
                    console.log(result);
                    // 아이디가 사용가능한 경우
                    guideError.classList.add('hidden');
                    guideOk.classList.remove('hidden');
                    idValid.value = 1;
                }
                else {
                   // 아이디가 이미 사용중인 경우
                    guideOk.classList.add('hidden');
                    guideError.classList.remove('hidden');
                    idValid.value = 0;
                }
            }
        });
    }
    else {
        // 아이디를 지우고 다시쓰기 하는 경우
        guideOk.classList.add('hidden');
        guideError.classList.add('hidden');
        idValid.value = 0;

    }
});



const hobbyEtc = document.querySelector("#hobby-etc");
hobbyEtc.addEventListener('keyup', (e) => {
    // enter를 누른 경우, 입력완료로 간주한다.
    if(e.keyCode === 13) {
        // 자동으로 생겨난 <br><br>을 제거하고 blur 처리
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
         // 특정요소 기준으로 새요소 추가
         // - beforebegin 시작태그 앞. 이전 형제요소 추가
         // - afterbegin 시작태그 뒤. 첫 자식요소로 추가
         // - beforeend 종료태그 앞. 마지막 자식요소로 추가
         // - afterend 종료태그 뒤. 다음 형제요소로 추가
         // e.target.parentElement : label#hobby-etc를 감싼 div태그
         e.target.parentElement.insertAdjacentHTML('beforebegin', html);
         e.target.innerHTML = '직접입력';
     }
});
/**
 * 회원가입 유효성 검사
 */
document.memberRegisterFrm.addEventListener('submit', (e) => {
    const frm = e.target;
    const id = frm.id;
    const password = frm.password;
    const confirmPassword = document.querySelector("#confirm-password");
    const name = frm.name;
    const email = frm.email;
    const idValid = frm.idValid;
    console.log(idValid);

    // 아이디 - 영문자/숫자 4글자 이상
    if(!/^\w{4,}$/.test(id.value)) {
        alert("아이디는 영문자, 숫자 4글자 이상만 작성해주세요...");
        e.preventDefault();
        return;
    }

    // 아이디중복검사 통과여부
    if(idValid.value !== "1") {
        alert('사용가능한 아이디를 입력해주세요...');
        e.preventDefault();
        return;
    }

    // 비밀번호 - 영문자/숫자/특수문자!@#$% 포함 4글자이상
    const regExps = [
        {
            re : /[a-zA-Z]/,
            msg : "비밀번호는 영문자를 하나 이상 포함해주세요.."
        },
        {
            re : /\d/,
            msg : "비밀번호는 숫자를 하나 이상 포함해주세요.."
        },
        {
            re : /[!@#$%^&*]/,
            msg : "비밀번호는 특수문자 !@#$%^&* 중에 하나 이상 포함해주세요.."
        }, {
            re : /^.{4,}$/,
            msg: "비밀번호는 4글자 이상 작성해주세요.."
        }
    ];

    for(let i = 0 ; i < regExps.length; i++) {
        const {re : RegExp, msg : string} = regExps[i];
        if(!RegExp.test(password.value)){
            alert(string);
            e.preventDefault();
            return;
        }
    }

    // 비밀번호 확인
    if(password.value !== confirmPassword.value){
        alert("두 비밀번호가 다릅니다.");
        e.preventDefault();
        return;
    }
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
