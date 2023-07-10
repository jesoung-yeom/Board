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
        ],callbacks: {
            onChange: function(contents, $editable) {
                // Summernote 내용 변경 시 호출되는 콜백 함수
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
        height:300,
    });
    $('.dropdown-toggle').dropdown();

});
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

function checkEdit() {
    const title = document.getElementById("title-editor").value;
    const content = document.getElementById("content-editor").value;

    if (title.trim() === '') {
        alert("빈 제목으로 수정하실 수 없습니다.")

        return false;
    } else if(content.trim() === '') {
        alert("빈 내용으로 수정하실 수 없습니다.");

        return false;
    } else{

        alert("글 수정이 완료되었습니다.")
        return true;
    }
}

