var fileSystem;
//初始化文件系统
guac.onfilesystem = (object) => {
    fileSystem=object;

    //监听onbody事件，对返回值进行处理，返回内容可能有两种，一种是文件夹，一种是文件。
    object.onbody = function(stream, mimetype, filename){
        stream.sendAck('OK', Guacamole.Status.Code.SUCCESS);
        downloadFile(stream, mimetype, filename);
    }
}

//连接有滞后，初始化文件系统给个延迟
setTimeout(function(){
    //从根目录开始,想服务端发送get请求
    let path = '/';
    fileSystem.requestInputStream(path);
}, 5000);

downloadFile = (stream, mimetype, filename) => {

    //使用blob reader处理数据
    var blob_builder;
    if      (window.BlobBuilder)       blob_builder = new BlobBuilder();
    else if (window.WebKitBlobBuilder) blob_builder = new WebKitBlobBuilder();
    else if (window.MozBlobBuilder)    blob_builder = new MozBlobBuilder();
    else
        blob_builder = new (function() {

            var blobs = [];

            /** @ignore */
            this.append = function(data) {
                blobs.push(new Blob([data], {"type": mimetype}));
            };

            /** @ignore */
            this.getBlob = function() {
                return new Blob(blobs, {"type": mimetype});
            };

        })();

    // 收到blob的处理，因为收到的可能是一块一块的数据，需要把他们整合，这里用到了blob_builder
    stream.onblob = function(data) {

        // Convert to ArrayBuffer
        var binary = window.atob(data);
        var arrayBuffer = new ArrayBuffer(binary.length);
        var bufferView = new Uint8Array(arrayBuffer);

        for (var i=0; i<binary.length; i++)
            bufferView[i] = binary.charCodeAt(i);

        blob_builder.append(arrayBuffer);
        length += arrayBuffer.byteLength;

        // Send success response
        stream.sendAck("OK", 0x0000);

    };

    // 结束后的操作
    stream.onend = function(){
        //获取整合后的数据
        var blob_data = blob_builder.getBlob();

        //数据传输完成后进行下载等处理
        if(mimetype.indexOf('stream-index+json') != -1){
            //如果是文件夹,需要解决如何将数据读出来，这里使用filereader读取blob数据，最后得到一个json格式数据
            var blob_reader = new FileReader();
            blob_reader.addEventListener("loadend", function() {
                let folder_content = JSON.parse(blob_reader.result)
                //这里加入自己代码，实现文件目录的ui，重新组织当前文件目录
            });
            blob_reader.readAsBinaryString(blob_data);
        } else {
            //如果是文件，直接下载，但是需要解决个问题，就是如何下载blob数据
            //借鉴了https://github.com/eligrey/FileSaver.js这个库
            var file_arr = filename.split("/");
            var download_file_name = file_arr[file_arr.length - 1];
            saveAs(blob_data, download_file_name);
        }
    }
}
