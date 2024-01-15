document.memberPasswordUpdateFrm.addEventListener('submit', (e) => {
    const frm = e.target;

    // 비밀번호 유효성 검사
    const newPassword = frm.newPassword;
    const newPasswordConfirmation = frm.newPasswordConfirmation;

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
        if(!RegExp.test(newPassword.value)){
            alert(string);
            e.preventDefault();
            return;
        }
    }

    // 비밀번호 확인
    if(newPassword.value !== newPasswordConfirmation.value){
        alert("두 비밀번호가 다릅니다.");
        e.preventDefault();
        return;
    }

});