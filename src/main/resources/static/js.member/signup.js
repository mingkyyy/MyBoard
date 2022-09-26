


function check() {
    if (signupDto.hidden.value == "aa"){
        alert("핸드폰 인증에 성공하셔야 합니다.");
        return false;
    }
}

$(function () {
    $('#sendPhoneNumber').click(function () {
        let phoneNumber = $('#tel').val();
        if (phoneNumber == null) {
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
                        alert("핸드폰 인증에 성공하였습니다.");
                    } else {
                        alert("인증번호가 다릅니다.");
                    }
                })


            }
        })
    });
});
