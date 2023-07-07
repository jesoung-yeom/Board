
function checkSignIn() {
    const userId = document.getElementById("user-id").value;
    const userPw = document.getElementById("user-pw").value;

    if (userId.trim() === '' && userPw.trim() === '') {
        alert('아이디와 비밀번호를 입력해주세요.');

        return false;
    } else if (userId.trim() === '') {
        alert('아이디를 입력해주세요.');

        return false;
    } else if (userPw.trim() === '') {
        alert('비밀번호를 입력해주세요.');

        return false;
    } else {

        return true;
    }
}