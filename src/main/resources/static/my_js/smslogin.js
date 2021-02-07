
// 登录处理
layui.use(['form', 'layer', 'jquery'], function () {


    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;


    form.on('submit(login)', function (data) {

        var mobile = $('#mobile').val();
        var smsCode = $('#smsCode').val();
        // layer.msg(username);
        $.ajax({

            url: "/dqgac/login/mobile",
            data: {
                "mobile": mobile,
                "smsCode": smsCode,
            },
            dataType: "json",
            type: 'post',
            async: false,
            success: function (data) {
                if (data.code == 401) {
                    layer.alert(data.msg);

                }
                if (data.code == 200) {
                    window.location.href = "/dqgac/";
                }

            }
        });

    });

    $("#sendSms").click(function () {
        let mobile = $('#mobile').val();
        //验证手机号码是否正确
        let reg = /^1\d{10}$/g;
        if (isNaN(mobile) || mobile.length != 11 || !reg.test(mobile)) {
            layer.alert("请输入正确的11位移动号码！");
            return false;
        }

        $.ajax({
            url: 'code/sms',
            data: {
                "mobile": mobile,
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.code != "0") {
                    layer.alert(data.msg);
                } else {
                    $("#sendSms").addClass('layui-btn-disabled');
                    $("#sendSms").attr('disabled', true);
                    startCountDown();
                }
            }
        });

    });

});


