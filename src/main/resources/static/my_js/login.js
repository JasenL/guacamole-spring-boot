
// 登录处理
layui.use(['form', 'layer', 'jquery'], function () {


    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;

    form.on('submit(login)', function (data) {
        var username = $('#username').val();
        var password = $('#password').val();
        var telephone = $('#telephone').val();
        // layer.msg(username);
        $.ajax({

            url: "/dqgac/login",
            data: {
                "username": username,
                "password": password,
                "telephone": telephone,
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

    })

});


