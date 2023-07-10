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
                $('textarea[name="content"]').val(contents);
            },
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
        height: 300,
    });
    $('.dropdown-toggle').dropdown();
});

function checkWrite() {
    const title = document.getElementById("title-writer").value;
    const content = document.getElementById("content-writer").value;
    if (title.trim() === '') {
        alert("빈 제목으로 작성하실 수 없습니다.")

        return false;
    } else if (content.trim() === '') {
        alert("빈 내용으로 작성하실 수 없습니다.");

        return false;
    } else {

        alert("글 작성이 완료되었습니다.")
        return true;
    }
}

function handleFileSelect(event) {
    const fileList = event.target.files;
    const maxSize = 500 * 1024 * 1024;
    const listContainer = document.getElementById("attach-file-list");

    listContainer.innerHTML = "";

    for (let i = 0; i < fileList.length; i++) {
        const file = fileList[i];

        if (file.size > maxSize) {
            alert("파일 크기가 제한을 초과하였습니다. 500MB 이하의 파일만 업로드할 수 있습니다.");
            fileInput.value = null; // 파일 선택 해제
            return false; // 업로드 막기
        }
        const listItem = document.createElement("li");
        listItem.textContent = file.name;


        listContainer.appendChild(listItem);
    }
}

const fileInput = document.getElementById("attach-file");
fileInput.addEventListener("change", handleFileSelect);
