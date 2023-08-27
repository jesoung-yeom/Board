class Home {

    createButton;
    logoutButton;
    boardButtons;


    constructor() {
        this.boardButtons = document.querySelectorAll("#board-button");
        this.createButton = document.getElementById("create-button");
        this.logoutButton = document.getElementById("logout-button");

        this.boardButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                const boardIdInput = this.parentNode.querySelector("input[name='id']");
                const boardId = boardIdInput.value;
                window.location.href = "/home/board?id=" + boardId;
            });
        });

        this.createButton.addEventListener("click", function () {
            window.location.href = "/home/board/create";
        });

        this.logoutButton.addEventListener("click", function () {
            const confirmed = confirm("로그아웃하시겠습니까?");

            if (confirmed) {
                window.location.href = "/logout";
            }
        });
    }
}