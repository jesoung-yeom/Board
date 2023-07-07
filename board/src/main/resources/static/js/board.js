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

    if (label.classList.contains("hidden")) {
        label.classList.remove("hidden");
        textarea.classList.add("hidden");
        completeButton.classList.add("hidden");
        fixButton.classList.remove("hidden");
        cancelButton.classList.add("hidden");
        deleteButton.classList.remove("hidden");
    } else {
        label.classList.add("hidden");
        textarea.classList.remove("hidden");
        fixButton.classList.add("hidden");
        completeButton.classList.remove("hidden");
        deleteButton.classList.add("hidden");
        cancelButton.classList.remove("hidden");
        textarea.value = label.innerText;
    }
}