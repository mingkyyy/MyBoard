$(function (){
    $('#reportButton').click(function (){
        let id = $('#postId').val();
        let reportText =  $('#reportText').val();

        if (reportText == ''){
            alert("신고 내용을 입력해주세요");
            return false;
        }

        $.ajax({
            type: 'POST',
            url:'/report/'+id,
            dataType: 'json',
            data : {
                reportText : reportText
            }
        }).done(function (){
            alert("신고 완료되었습니다.");
            window.close();
        }).fail(function (error) {
            alert(JSON.stringify(error));

        });

    });
});

