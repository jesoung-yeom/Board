
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
        ],callbacks: {
            onChange: function(contents, $editable) {
                $('textarea[name="content"]').val(contents);
            }
        },
        buttons: {
            picture: function (context) {
                var ui = $.summernote.ui;
                var button = ui.button({
                    contents: '<i class="note-icon-picture"></i>',
                    tooltip: '그림 삽입',
                    click: function () {
                    }
                });
                return button.render();
            }
        },
    });
    $('.dropdown-toggle').dropdown();

});

function checkWrite() {
    const title = document.getElementById("title-writer").value;
    const content = document.getElementById("content-writer").value;
    if (title.trim() === '') {
        alert("빈 제목으로 작성하실 수 없습니다.")

        return false;
    } else if(content.trim() === '') {
        alert("빈 내용으로 작성하실 수 없습니다.");

        return false;
    } else{

        alert("글 작성이 완료되었습니다.")
        return true;
    }
}

function handleFileSelect(event) {
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
const fileInput = document.getElementById("attach-file");
    fileInput.addEventListener("change", handleFileSelect);

