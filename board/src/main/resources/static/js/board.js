function checkCreateEmpty() {
    var content = document.getElementById("content-of-comment").value;

    if (content !== "") {

        return true;
    } else {

        return false;
    }
}

function checkFixConent(commentId) {
    var content = document.getElementById("comment-textarea" + commentId).value;

    if (content !== "") {

        return true;
    } else {
        return false;
    }
}

function convertToTextarea(commentId) {
    var label = document.getElementById("comment-label" + commentId);
    var textarea = document.getElementById("comment-textarea" + commentId);
    var fixButton = document.getElementById("fix-button" + commentId);
    var completeButton = document.getElementById("complete-button" + commentId);
    var deleteButton = document.getElementById("delete-button" + commentId);
    var cancelButton = document.getElementById("cancel-button" + commentId);

    if (label.classList.contains("hide-component")) {
        label.classList.remove("hide-component");
        textarea.classList.add("hide-component");
        completeButton.classList.add("hide-component");
        fixButton.classList.remove("hide-component");
        cancelButton.classList.add("hide-component");
        deleteButton.classList.remove("hide-component");
    } else {
        label.classList.add("hide-component");
        textarea.classList.remove("hide-component");
        fixButton.classList.add("hide-component");
        completeButton.classList.remove("hide-component");
        deleteButton.classList.add("hide-component");
        cancelButton.classList.remove("hide-component");
        textarea.value = label.innerText;
    }
}