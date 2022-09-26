$(function () {
    $('#summernote').summernote({
        lang: 'ko-KR',
        placeholder: '자유롭게 작성해보세요!',
        minHeight: 500,
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
            ['color', ['color']],
            ['fontsize', ['fontsize']],
            ['para', ['paragraph', 'height']],
            ['insert', ['link', 'picture']]
        ]
    });

    $('#freeUpdate').click(function (){

        let id = $('#freeUpdate').val();

        let data = {
            title : $('#title').val(),
            content : $('#summernote').val()
        };

        if ($('#title').val()==''){
            alert("제목을 입력하세요");
            $('#title').focus();
            return false;
        }

        if ($('#summernote').val()==''){
            alert("내용을 입력하세요");
            $('#summernote').focus();
            return false;
        }

        $.ajax({
            type : 'PUT',
            url:'/board/free/read/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert("수정 완료 되었습니다.");
            window.location.href='/board/free/read/'+id;
        }).fail(function (error){
            alert(JSON.stringify(error));
        })
    });
});