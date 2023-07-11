function checkSignUp() {
    var userPw = document.getElementById("user-pw").value;
    var reconfirmPw = document.getElementById("reconfirm-pw").value;
    var userId = document.getElementById("user-id").value;
    var userName = document.getElementById("user-name").value;
    var userEmail = document.getElementById("user-email").value;
    var pattern = /^[\w\.-]+@[\w\.-]+\.\w+$/;

    if ((userId !== "" && userPw !== "" && userName !== ""
        && reconfirmPw !== "")
        && (userPw === reconfirmPw)
        && pattern.test(userEmail)) {
        return true;
    } else {
        alert("형식에 맞게 작성하여 주세요.");
        return false;
    }
}
