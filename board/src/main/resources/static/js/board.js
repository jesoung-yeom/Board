class Board {

    label;
    textarea;
    contentOfComment;

    logoutButton;
    listButton;
    updateButton;

    cancelButton;
    deleteButton;
    createCommentButton;
    deleteCommentButton;
    updateCommentButton;
    cancelCommentButton;
    completeCommentButton;

    deleteForm;
    createCommentForm;
    updateCommentForm;

    constructor() {
        this.contentOfComment = document.getElementById("content-of-comment");

        this.logoutButton = document.getElementById("logout-button");
        this.listButton = document.getElementById("list-button");
        this.updateButton = document.getElementById("update-button");
        this.deleteButton = document.getElementById("delete-button");
        this.cancelButton = document.querySelectorAll("#cancel-button")
        this.createCommentButton = document.getElementById("create-comment-button");
        this.deleteCommentButton = document.querySelectorAll("#delete-comment-button");
        this.updateCommentButton = document.querySelectorAll("#update-comment-button");
        this.cancelCommentButton = document.querySelectorAll("#cancel-comment-button");

        this.deleteForm = document.getElementById("delete-form");
        this.createCommentForm = document.getElementById("create-comment-form");
        this.updateCommentForm = document.getElementById("update-comment-form")

        this.deleteButton.addEventListener("click", this.submitDeleteForm);
        this.createCommentButton.addEventListener("click", this.submitCreateCommentForm);
        this.completeCommentButton = document.querySelectorAll("#complete-comment-button");

        const submitDeleteCommentForm = this.submitDeleteCommentForm;

        this.deleteCommentButton.forEach(function (button) {
            button.addEventListener("click", (event) => {
                event.preventDefault();
                const commentIdInput = button.parentNode.querySelector("input[name='id']");
                const commentId = commentIdInput.value;
                submitDeleteCommentForm(commentId);
            });
        });

        const updateComment = this.updateComment;

        this.updateCommentButton.forEach(function (button) {
            button.addEventListener("click", (event) => {
                event.preventDefault();
                const commentIdInput = button.parentNode.querySelector("input[name='id']");
                const commentId = commentIdInput.value;
                const deleteCommentButton = button.parentNode.querySelector("button[name='deleteCommentButton']");
                const cancelCommentButton = button.parentNode.querySelector("button[name='cancelCommentButton']");
                const completeCommentButton = button.parentNode.querySelector("button[name='completeCommentButton']");
                const commentLabel = document.getElementById("comment-label" + commentId);
                const commentTextarea = document.getElementById("comment-textarea" + commentId);
                updateComment(deleteCommentButton, cancelCommentButton, completeCommentButton, button, commentLabel, commentTextarea);
            });
        });

        const cancelComment = this.cancelComment;

        this.cancelCommentButton.forEach(function (button) {
            button.addEventListener("click", (event) => {
                event.preventDefault();
                const commentIdInput = button.parentNode.querySelector("input[name='id']");
                const commentId = commentIdInput.value;
                const deleteCommentButton = button.parentNode.querySelector("button[name='deleteCommentButton']");
                const updateCommentButton = button.parentNode.querySelector("button[name='updateCommentButton']");
                const completeCommentButton = button.parentNode.querySelector("button[name='completeCommentButton']");
                const commentLabel = document.getElementById("comment-label" + commentId);
                const commentTextarea = document.getElementById("comment-textarea" + commentId);
                cancelComment(deleteCommentButton, updateCommentButton, completeCommentButton, button, commentLabel, commentTextarea);
            })
        });

        const submitUpdateCommentForm = this.submitUpdateCommentForm;

        this.completeCommentButton.forEach(function (button) {
            button.addEventListener("click", (event) => {
                event.preventDefault();
                const commentIdInput = button.parentNode.querySelector("input[name='id']");
                const commentId = commentIdInput.value;
                submitUpdateCommentForm(commentId);
            });
        });

        this.updateButton.addEventListener("click", this.update);

        this.logoutButton.addEventListener("click", this.logout);

        this.listButton.addEventListener("click", this.backList);

    }

    submitDeleteForm = (event) => {
        event.preventDefault();
        const confirmed = confirm("정말 삭제하시겠습니까?");
        if (confirmed) {
            this.deleteForm.action = "/home/board/delete";
            this.deleteForm.method = "POST";
            this.deleteForm.submit();
        }
    }

    submitCreateCommentForm = (event) => {
        event.preventDefault();
        const msg = this.checkEmpty(this.contentOfComment);
        if (msg === "") {
            this.createCommentForm.action = "/home/board/comment/create";
            this.createCommentForm.method = "POST";
            this.createCommentForm.submit();
        } else {
            alert(msg);
        }
    }

    submitDeleteCommentForm = (commentId) => {
        const form = document.getElementById("delete-comment-form" + commentId);
        form.action = "/home/board/comment/delete";
        form.method = "POST";
        form.submit();
    }

    submitUpdateCommentForm = (commentId) => {
        const form = document.getElementById("update-comment-form" + commentId);
        const textarea = document.getElementById("comment-textarea" + commentId);
        const msg = this.checkEmpty(textarea);
        if (msg === "") {
            form.action = "/home/board/comment/update";
            form.method = "POST";
            form.submit();
        } else {
            alert(msg);
        }
    }

    logout = (event) => {
        event.preventDefault();
        const confirmed = confirm("로그아웃하시겠습니까?");
        if (confirmed) {
            window.location.href = "/signout";
        }
    }

    backList = (event) => {
        event.preventDefault();
        window.location.href = "/home";
    }

    update = (event) => {
        event.preventDefault();
        const boardIdInput = this.updateButton.parentNode.querySelector("input[name='id']");
        const boardId = boardIdInput.value;
        window.location.href = "/home/board/update?id=" + boardId;
    }

    checkEmpty = (target) => {
        if (target.value === "") {
            const msg = "댓글은 빈 칸으로 작성하실 수 없습니다."

            return msg;
        }

        if (target.value.length > 100) {
            const msg = "댓글은 100자 이상 작성하실 수 없습니다."

            return msg;
        }

        return "";
    }

    updateComment = (deleteCommentButton, cancelCommentButton, completeCommentButton, button, commentLabel, commentTextarea) => {
        deleteCommentButton.classList.add("hide-component");
        cancelCommentButton.classList.remove("hide-component");
        completeCommentButton.classList.remove("hide-component");
        button.classList.add("hide-component");
        commentLabel.classList.add("hide-component");
        commentTextarea.classList.remove("hide-component");
    }

    cancelComment = (deleteCommentButton, updateCommentButton, completeCommentButton, button, commentLabel, commentTextarea) => {
        deleteCommentButton.classList.remove("hide-component");
        button.classList.add("hide-component");
        completeCommentButton.classList.add("hide-component");
        updateCommentButton.classList.remove("hide-component");
        commentLabel.classList.remove("hide-component");
        commentTextarea.classList.add("hide-component");
        commentTextarea.value = commentLabel.innerText;
    }
}
