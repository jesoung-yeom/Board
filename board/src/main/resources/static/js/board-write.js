$(document).ready(function () {
    $('#content-writer').summernote({
        lang: 'ko-KR',
        toolbar: [
            ['style', ['bold', 'italic', 'underline']],
            ['font', ['strikethrough']],
            ['fontname'],
            ['fontsize'],
            ['color'],
            ['para', ['ul', 'ol']],
            ['insert', ['picture']],
        ], callbacks: {
            onChange: function (contents, $editable) {

            },
        },
        buttons: {
            picture: function (context) {
                const ui = $.summernote.ui;
                const button = ui.button({
                    contents: '<i class="note-icon-picture"></i>',
                    tooltip: '그림 삽입',
                    click: function () {
                    }
                });
                return button.render();
            }
        },
        height: 300,
    });
    $('.dropdown-toggle').dropdown();
});

class BoardWrite {

    boardId;

    title;
    content;
    fileInput;
    listContainer;

    logoutButton;
    cancelButton;
    submitButton;

    createForm;

    constructor() {
        this.boardId = document.getElementById("board-id");

        this.title = document.getElementById("title-writer");
        this.content = document.getElementById("content-writer");
        this.fileInput = document.getElementById("attach-file");
        this.listContainer = document.getElementById("attach-file-list")

        this.logoutButton = document.getElementById("logout-button");
        this.cancelButton = document.getElementById("cancel-button");
        this.submitButton = document.getElementById("submit-button");

        this.createForm = document.getElementById("create-form");

        this.submitButton.addEventListener("click", this.submitCreateForm);
        this.logoutButton.addEventListener("click", this.logout);
        this.cancelButton.addEventListener("click", this.cancel);

        this.fileInput.addEventListener("change", this.handleFileSelect);
        this.title.addEventListener("input", this.limitTitleLength);
    }

    logout = (event) => {
        event.preventDefault();

        if (this.checkConfirm("로그아웃하시겠습니까?")) {
            window.location.href = "/signout";
        }
    }

    checkConfirm = (msg) => {
        event.preventDefault();
        const confirmed = confirm(msg);

        if (confirmed) {

            return true;
        }

        return false;
    }

    submitCreateForm = (event) => {
        event.preventDefault();
        const check = this.checkWrite();

        if (check === "") {
            if (this.checkConfirm("작성하시겠습니까?")) {
                this.createForm.action = "/home/board/create";
                this.createForm.method = "POST";
                this.createForm.enctype = "multipart/form-data"
                this.createForm.submit();
            }
        } else {
            alert(check);
        }
    }
    cancel = (event) => {
        event.preventDefault();

        if (this.checkConfirm("작성을 취소하시겠습니까?")) {
            window.location.href = "/home";
        }
    }
    checkWrite = () => {
        let msg = "";

        if (this.title.value.trim() === '') {
            msg = "빈 제목으로 작성하실 수 없습니다.";
        } else if (this.content.value.trim() === '') {
            msg = "빈 내용으로 작성하실 수 없습니다.";
        }

        return msg;
    }

    handleFileSelect = () => {
        const fileList = this.fileInput.files;
        const maxSize = 500 * 1024 * 1024;
        this.listContainer.innerHTML = "";

        for (let i = 0; i < fileList.length; i++) {
            const file = fileList[i];

            if (file.size > maxSize) {
                alert("파일 크기가 제한을 초과하였습니다. 500MB 이하의 파일만 업로드할 수 있습니다.");
                this.fileInput.value = null; // 파일 선택 해제

                return false;
            }

            const listItem = document.createElement("li");
            listItem.textContent = file.name;

            this.listContainer.appendChild(listItem);
        }
    }

    limitTitleLength = () => {
        const titleLength = this.title.value.length;
        const maxLength = parseInt(this.title.getAttribute("maxlength"));

        if (titleLength >= maxLength) {
            this.title.value = this.title.value.slice(0, maxLength - 1);
            alert("제목의 글자 수는 " + maxLength + "자를 넘길 수 없습니다.")
        }
    }
}





