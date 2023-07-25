let eMailPattern = /^[\w\.-]+@[\w\.-]+\.\w+$/;

class SignUp {

    userPw;
    reconfirmPw;
    userId;
    userName;
    userEmail;
    signUpButton;
    signInButton;
    signUpForm;

    constructor() {
        this.userPw = document.getElementById("user-pw");
        this.reconfirmPw = document.getElementById("reconfirm-pw");
        this.userId = document.getElementById("user-id");
        this.userName = document.getElementById("user-name");
        this.userEmail = document.getElementById("user-email");
        this.signUpButton = document.getElementById("signup-button");
        this.signInButton = document.getElementById("signin-button");
        this.signUpForm = document.getElementById("signup-form");

        this.signInButton.addEventListener("click", this.signIn);
        this.signUpButton.addEventListener("click", this.submitDeleteForm);
    }

    submitDeleteForm = (event) => {
        event.preventDefault();
        const errorMessage = this.checkSignUp();

        if (this.resultSignUp(errorMessage)) {
            this.signUpForm.action = "/signup";
            this.signUpForm.method = "POST";
            this.signUpForm.submit();
        }
    }
    signIn = (event) => {
        event.preventDefault();
        window.location.href = "/";
    }

    checkSignUp = () => {

        let msg = "";

        if (this.userName.value === "") {
            msg = "이름이 작성되지 않았습니다.";
        } else if (this.userId.value === "") {
            msg = "아이디가 작성되지 않았습니다.";
        } else if (this.userEmail.value === "") {
            msg = "이메일이 작성되지 않았습니다.";
        } else if (!eMailPattern.test(this.userEmail.value)) {
            msg = "이메일 형식이 올바르지 않습니다.";
        } else if (this.userPw.value === "") {
            msg = "비밀번호가 작성되지 않았습니다.";
        } else if (this.reconfirmPw.value !== this.userPw.value) {
            msg = "비밀번호가 일치하지 않습니다.";
        }

        return msg;
    }

    resultSignUp = (msg) => {
        if (msg === "") {

            return true;
        }

        alert(msg);

        return false;
    }
}
