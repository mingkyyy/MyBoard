$(function () {


    $(".like").click(function () { // 좋아요 하는 기능
        let id = $(this).val();

        $.ajax({
            type: 'POST',
            url: '/board/like',
            data: {
                id: id
            },
            dataType: 'json',

        }).done(function (data) {
            if (data.result == "좋아요 성공") {
                alert(data.result);
                $("#" + id).attr("src", "/images/like_red.svg");
                $('#likeNum' + id).html(data.likeCount);
            } else if (data.result == "좋아요 취소") {
                alert(data.result);
                $("#" + id).attr("src", "/images/like.svg");
                $('#likeNum' + id).html(data.likeCount);
            } else {
                alert(data.result);
            }
        }).fail(function () {
            alert("error");
        })
    });

    $('.reComment').click(function () {
        let id = $(this).val();
        if ($('#reCommentDiv' + id).css("display") == "none") {
            $('#reCommentDiv' + id).css("display", "");
        } else {
            $('#reCommentDiv' + id).css("display", "none");
        }
    });


    $('.updateComment').click(function () {
        let id = $(this).val();
        if ($('#updateDiv' + id).css("display") == "none") {
            $('#updateDiv' + id).css("display", "");
        } else {
            $('#updateDiv' + id).css("display", "none");
        }
    });


    $('.updateButton').click(function () {
        let id = $(this).val();
        let data = $('#commentUpdateValue' + id).val();

        if (data == '') {
            alert("수정할 댓글 내용을 입력하세요");
            return false;
        }

        $.ajax({
            type: 'PUT',
            url: '/board/comment/' + id,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            dataType: 'json'
        }).done(function () {
            alert("수정 완료 하였습니다.");
            $('#updateDiv' + id).css("display", "none");
            $('#commentText' + id).text(data);

        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    });


    $('.deleteComment').click(function () {
        let id = $(this).val();
        let post = $('#postId').val();

        if (confirm("댓글을 삭제 하겠습니까?")) {
            $.ajax({
                type: 'DELETE',
                url: '/board/comment/' + id,
                dataType: 'json'
            }).done(function () {
                alert("댓글 삭제 완료되었습니다.");
                window.location.href = '/board/free/read/' + post;
            }).fail(function (error) {
                alert(JSON.stringify(error))
            })
        } else {
        }
    });


    $('.deleteFree').click(function () {
        let id = $(this).val();
        if (confirm("삭제 하겠습니까?")) {
            $.ajax({
                type: 'DELETE',
                URL: '/board/free/read/' + id,
                dataType: 'json',
            }).done(function () {
                alert("삭제 완료 했습니다.");
                window.location.href = '/board/free';
            }).fail(function (error) {
                alert(JSON.stringify(error));
            })
        } else {

        }
    });


    $('.reCommentButton').click(function () {
        let id = $('#postId').val();
        let commentId = $(this).val();

        let data = {
            comment: $('#recommentValue' + commentId).val(),
            parentComment: $(this).val()
        };

        if ($('#recommentValue' + id).val() == '') {
            alert("댓글 내용을 입력해주세요");
            return false;
        }

        $.ajax({
            type: 'POST',
            url: '/board/comment/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("댓글 완료");
            window.location.href = '/board/free/read/' + id;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })

    });

    $('#commentButton').click(function () {

        let id = $('#postId').val(); //post id
        let data = {
            comment: $('#comment').val(),
            parentComment: -1
        };
        if ($('#comment').val() == '') {
            alert("댓글 내용을 입력해주세요");
            return false;
        }
        $.ajax({
            type: 'POST',
            url: '/board/comment/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("댓글 완료");
            window.location.href = '/board/free/read/' + id;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })

    });
});