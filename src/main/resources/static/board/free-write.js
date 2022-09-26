$(function () {
    $('#summernote').summernote({
        // 에디터 높이
        minHeight: 500,
        maxHeight: null,
        focus: true,
        lang: "ko-KR",
        placeholder: '자유롭게 작성해주세요.',
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
            ['color', ['color']],
            ['fontsize', ['fontsize']],
            ['para', ['paragraph', 'height']],
            ['insert', ['link', 'picture']]
        ],
        callbacks: {
            onImageUpload: function (files) {

                var file = files[0];
                var reader = new FileReader();
                reader.onloadend = function() {
                    var image = $('<img>').attr('src',  reader.result);
                    image.attr('width','80%');
                    $('#summernote').summernote("insertNode", image[0]);
                }

                uploadSummernoteImageFile(files[0], this);
            },
            onPaste: function (e) {
                var clipboardData = e.originalEvent.clipboardData;
                if (clipboardData && clipboardData.items && clipboardData.items.length) {
                    var item = clipboardData.items[0];
                    if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
                        e.preventDefault();
                    }
                }
            }


        }
    });



    /**
     * 이미지 파일 업로드
     */
    function uploadSummernoteImageFile(file, editor) {
        data = new FormData();
        data.append("file", file);
        $.ajax({
            data : data,
            type : "POST",
            url : "/uploadSummernoteImageFile",
            contentType : false,
            processData : false,
            success : function(data) {
                //항상 업로드된 파일의 url이 있어야 한다.
                $(editor).summernote('insertImage', data.url);
            }
        });
    }


    $('#freeWrite').click(function () {
        if ($('#title').val() == '') {
            alert("제목을 입력하세요");
            $('#title').focus();
            return false;
        }

        if ($('#summernote').val() == '') {
            alert("내용을 입력하세요");
            $('#summernote').focus();
            return false;
        }

        let data = {
            title: $('#title').val(),
            content: $('#summernote').val()
        };

        $.ajax({
            type: 'POST',
            url: '/free/write',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("글쓰기가 완료되었습니다.");
            window.location.href = '/board/free';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    });


})
;