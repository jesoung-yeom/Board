class SignIn {

    userId;
    userPw;
    signUpButton;
    signInButton;
    signInForm;

    constructor() {
        this.userId = document.getElementById("user-id");
        this.userPw = document.getElementById("user-pw");
        this.signUpButton = document.getElementById("signup-button");
        this.signInForm = document.getElementById("login-form");
        this.signInButton = document.getElementById("signin-button")

        this.signUpButton.addEventListener("click", this.signUp);
        this.signInButton.addEventListener("click", this.submitSignInForm);
    }

    submitSignInForm = (event) => {
        event.preventDefault();

        if (this.checkSignIn()) {
            this.signInForm.action = "/login";
            this.signInForm.method = "POST";
            this.signInForm.submit();
        }
    }

    signUp = (event) => {
        event.preventDefault();
        window.location.href = "/signup";
    }

    checkSignIn = () => {
        if (this.userId.value === "") {
            alert("아이디가 작성되지 않았습니다.");

            return false;
        }

        if (this.userPw.value === "") {
            alert("비밀번호가 작성되지 않았습니다.");

            return false;
        }

        return true;
    }
}
