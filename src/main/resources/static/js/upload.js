
const input = document.getElementById('file-input');
input.onchange = function() {
    const file = input.files[0];
    //上传开始
    uploadFile(fileSystem, file);
};


uploadFile = (object, file) => {
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.msg("开始上传，在上传成功提示出现前请勿上传其他文件！");
    });
    const _this      = this;
    const fileUpload = {};

    //需要读取文件内容，使用filereader
    const reader     = new FileReader();

    var current_path = $("#header_title").text();  //上传到堡垒机的目录，可以自己动态获取
    var STREAM_BLOB_SIZE = 4096;
    reader.onloadend = function fileContentsLoaded() {
        //上面源码分析过，这里先创建一个连接服务端的数据通道
        const stream = object.createOutputStream(file.type, current_path + '/' + file.name);
        const bytes  = new Uint8Array(reader.result);

        let offset   = 0;
        let progress = 0;

        fileUpload.name     = file.name;
        fileUpload.mimetype = file.type;
        fileUpload.length   = bytes.length;

        stream.onack = function ackReceived(status) {
            if (status.isError()) {
                //提示错误信息
                layui.use('layer', function(){
                    var layer = layui.layer;

                    layer.msg(status.message);
                });
                //layer.msg(status.message);
                return false;
            }

            const slice  = bytes.subarray(offset, offset + STREAM_BLOB_SIZE);
            const base64 = bufferToBase64(slice);

            // Write packet
            stream.sendBlob(base64);

            // Advance to next packet
            offset += STREAM_BLOB_SIZE;

            if (offset >= bytes.length) {
                stream.sendEnd();
                layui.use('layer', function(){
                    var layer = layui.layer;
                    input.value = "";
                    layer.msg("上传成功！");
                });
            }
        }
    };

    reader.readAsArrayBuffer(file);

    return fileUpload;
};

function bufferToBase64(buf) {
    var binstr = Array.prototype.map.call(buf, function (ch) {
        return String.fromCharCode(ch);
    }).join('');
    return btoa(binstr);
}