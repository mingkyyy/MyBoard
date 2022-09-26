$(function () {

    $('#memberDelete').click(function (){
        let id = $(this).val();

        if (confirm("회원 탈퇴 하시겠습니까?")){
            $.ajax({
                type: 'DELETE',
                url: '/member/delete/' + id,
                dataType: 'json',
            }).done(function (){
                alert("회원탈퇴 완료 되었습니다.");
                window.location.href = '/';
            }).fail(function (error){
                alert(JSON.stringify(error))
            })

        }else {

        }
    });


    $('#telupdate').click(function () {
        let id = $(this).val();
        if ($('#phoneDiv' + id).css("display") == "none") {
            $('#phoneDiv' + id).css("display", "");
        } else {
            $('#phoneDiv' + id).css("display", "none");
        }
    });

    $('#sendPhoneNumber').click(function () {

        let id = $('#memberId').val();
        let phoneNumber = $('#tel').val();

        let regexp = /[^0-9]/gi;

        if (regexp.test(phoneNumber)){
            alert("숫자만 입력해주세요");
             return;
        }

        if (phoneNumber == '') {
            alert("핸드폰 번호를 입력해주세요")
            return;
        }
        alert("인증번호가 전송되었습니다");
        $.ajax({
            type: "GET",
            url: "/check/sendSMS",
            data: {
                "phoneNumber": phoneNumber
            },
            success: function (res) {
                $('#checkBtn').click(function () {
                    if ($.trim(res) == $('#inputCertifiedNumber').val()) {
                        $('#hidden').val("zzz");

                        $.ajax({
                            type: 'PUT',
                            url: '/phoneChange/' + id,
                            dataType: 'json',
                            data: {
                                tel: phoneNumber
                            },
                            success: function (data) {
                                if (data.result == "번호 수정이 완료되었습니다.") {
                                    alert(data.result);
                                    $('#tel').html(data);
                                } else if (data.result == "중복된 번호 입니다.") {
                                    alert(data.result);
                                } else {
                                    alert(data.result);
                                }
                            }
                        });
                    } else {
                        alert("인증번호가 다릅니다.");
                    }
                })
            }
        });
    });

    $('#nicknameUpdate').click(function () {

        let nickname = $('#nickname').val()

        let id = $('#memberId').val();


        if (nickname == '') {
            alert("변경햘 닉네임을 입력해주세요");
            return false;
        }

        $.ajax({
            type: 'PUT',
            url: '/nicknameChange/' + id,
            dataType: 'json',
            data: {
                nickname: nickname
            },
            success: function (data) {
                if (data.result == "닉네임 수정 완료되었습니다.") {
                    alert(data.result);
                    $('#nickname').html(data);
                } else if (data.result == "중복된 닉네임 입니다.") {
                    alert(data.result);
                } else {
                    alert(data.result);
                }
            }
        });
    });
});