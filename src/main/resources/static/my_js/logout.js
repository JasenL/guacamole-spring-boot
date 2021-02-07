
// 登出处理
$("#logout").click(function () {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.confirm('真退出么', function (index) {
            $.ajax({
                url: '/dqgac/logout',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    if (data.code == 200) {
                        window.location.href = "login_page";
                    }
                }
            });
            layer.close(index);
        });
    });

});