$(document).ready(function () {
    $('#content-editor').summernote({
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
                $('textarea[name="content"]').val(contents);
            }
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

class BoardUpdate {
    title;
    content;
    fileInput;

    boardId;

    updateButton;
    cancelButton;
    logoutButton;

    updateForm;

    constructor() {
        this.boardId = document.getElementById("board-id").value;
        this.title = document.getElementById("title-editor");
        this.content = document.getElementById("content-editor");
        this.fileInput = document.getElementById("attach-file");
        this.updateButton = document.getElementById("update-button");
        this.cancelButton = document.getElementById("cancel-button");
        this.logoutButton = document.getElementById("logout-button");

        this.updateForm = document.getElementById("update-form");

        this.fileInput.addEventListener("change", this.handleFileSelect);

        this.updateButton.addEventListener("click", this.submitUpdateForm);
        this.logoutButton.addEventListener("click", this.logout);
        this.cancelButton.addEventListener("click", this.cancel);

    }

    submitUpdateForm = () => {
        event.preventDefault();
        if (this.checkEdit()) {
            this.updateForm.action = "/home/board/update";
            this.updateForm.method = "POST";
            this.updateForm.enctype = "multipart/form-data";
            this.updateForm.submit();
        }
    }
    cancel = () => {
        if (this.checkConfirm("수정을 취소하시겠습니까?")) {
            window.location.href = "/home/board?id=" + this.boardId;
        }
    }

    logout = () => {
        if (this.checkConfirm("로그아웃하시겠습니까?")) {
            window.location.href = "/signout";
        }
    }

    checkEdit = () => {
        if (this.title.value.trim() === '') {
            alert("빈 제목으로 수정하실 수 없습니다.")

            return false;
        } else if (this.content.value.trim() === '') {
            alert("빈 내용으로 수정하실 수 없습니다.");

            return false;
        } else {

            alert("글 수정이 완료되었습니다.")
            return true;
        }
    }

    handleFileSelect = (event) => {
        const fileList = event.target.files;

        const listContainer = document.getElementById("attach-file-list");

        listContainer.innerHTML = "";

        for (let i = 0; i < fileList.length; i++) {
            const file = fileList[i];
            const listItem = document.createElement("li");
            listItem.textContent = file.name;

            listContainer.appendChild(listItem);
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

}