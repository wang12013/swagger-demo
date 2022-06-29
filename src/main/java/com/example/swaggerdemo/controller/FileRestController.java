package com.example.swaggerdemo.controller;

import cn.hutool.core.util.StrUtil;
import com.example.swaggerdemo.config.NonStaticResourceHttpRequestHandler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/getVideo")
@AllArgsConstructor
@Slf4j
public class FileRestController {
 
 	@Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;


    /**
     *  这个接口的播放视频可以拖动播放
     * 就这样写是可以播放的*******就这么写
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/video")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName")String fileName) throws Exception {
 
        //sourcePath 是获取编译后 resources 文件夹的绝对地址，获得的原始 sourcePath 以/开头，所以要用 substring(1) 去掉第一个字符/
        //realPath 即视频所在的完整地址
//        String sourcePath = this.getClass().getClassLoader().getResource("").getPath().substring(1);
//        String realPath = sourcePath +"static/video/1.mp4";
       // String realPath = "G:\\videoTest\\1.mp4";
        String realPath = "G:\\videoTest\\" + fileName;

       //怎么从前端穿文件名过来-----
        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)) {
        	// 利用 Files.probeContentType 获取文件类型
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
            	// 设置 response
                response.setContentType(mimeType);
            }
            request.setAttribute(nonStaticResourceHttpRequestHandler.filepath, filePath);
            // 利用 ResourceHttpRequestHandler.handlerRequest() 实现返回视频流
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
            log.info("开始播放监控视频-->{}",fileName);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    //这个接口的视频不能拖动播放
    @RequestMapping(value = "/preview2", method = RequestMethod.GET)
    @ResponseBody
    public void getPreview2( HttpServletResponse response,@RequestParam("fileName")String fileName) {
        try {

            String realPath = "G:\\videoTest\\" + fileName;
            File file = new File(realPath);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename="+file.getName().replace(" ", "_"));
            InputStream iStream = new FileInputStream(file);
            IOUtils.copy(iStream, response.getOutputStream());
            response.flushBuffer();
        } catch (java.nio.file.NoSuchFileException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    //查询当前文件夹下所有后缀为.mp4的文件，返回文件名列表
    @GetMapping("getFileNames")
    public String[] getMp4Names(){
        //目录应该是在配置文件里面写死的
        String dirPath = "G:\\videoTest";
        File file = new File(dirPath);
        String[] list = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mp4");
            }
        });


        return list;
    }



    //**********另一套https://blog.csdn.net/I_am_hardy/article/details/122510053?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-122510053-blog-106586365.pc_relevant_antiscanv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-122510053-blog-106586365.pc_relevant_antiscanv3&utm_relevant_index=9
    //path为本地文件路劲, 支持拖动，播放速度最快
    //直接浏览器 访问就是下载http://localhost:8082/getVideo/video2?fileName=1.mp4
    @RequestMapping("video2")
    public void play(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {


        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();

            //去掉下面这行，还是报错，而且还正常运行？？？
            response.reset();

            //获取请求头中Range的值
            String rangeString = request.getHeader(HttpHeaders.RANGE);

            //打开文件
            String dirPath = "G:\\videoTest\\" + fileName;
            File file = new File(dirPath);
            if (file.exists()) {
                //使用RandomAccessFile读取文件
                targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                long requestSize = (int) fileLength;
                //分段下载视频
                if (StringUtils.hasText(rangeString)) {
                    //从Range中提取需要获取数据的开始和结束位置
                    long requestStart = 0, requestEnd = 0;
                    String[] ranges = rangeString.split("-");//本来是 =
                    if (ranges.length > 1) {
                        String[] rangeDatas = ranges[1].split("-");
                        requestStart = Integer.parseInt(rangeDatas[0]);
                        if (rangeDatas.length > 1) {
                            requestEnd = Integer.parseInt(rangeDatas[1]);
                        }
                    }
                    if (requestEnd != 0 && requestEnd > requestStart) {
                        requestSize = requestEnd - requestStart + 1;
                    }
                    //根据协议设置请求头
                    response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "video/mp4");


                    if (!StringUtils.hasText(rangeString)) {
                        response.setHeader(HttpHeaders.CONTENT_LENGTH, fileLength + "");
                    } else {
                        long length;
                        if (requestEnd > 0) {
                            length = requestEnd - requestStart + 1;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + requestStart + "-" + requestEnd + "/" + fileLength);
                        } else {
                            length = fileLength - requestStart;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + requestStart + "-" + (fileLength - 1) + "/"
                                    + fileLength);
                        }
                    }
                    //断点传输下载视频返回206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设置targetFile，从自定义位置开始读取数据
                    targetFile.seek(requestStart);
                } else {
                    //如果Range为空则下载整个视频
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
                    //设置文件长度
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
                }

                //从磁盘读取数据流返回
               // byte[] cache = new byte[4096]; 改变一下这个字节数组大小 还是会报错
                byte[] cache = new byte[1024 * 1024];
                try {
                    while (requestSize > 0) {
                        int len = targetFile.read(cache);
                        if (requestSize < cache.length) {
                            outputStream.write(cache, 0, (int) requestSize);
                        } else {
                            outputStream.write(cache, 0, len);
                            if (len < cache.length) {
                                break;
                            }
                        }
                        requestSize -= cache.length;
                    }
                } catch (IOException e) {
                    // tomcat原话。写操作IO异常几乎总是由于客户端主动关闭连接导致，所以直接吃掉异常打日志
                    //比如使用video播放视频时经常会发送Range为0- 的范围只是为了获取视频大小，之后就中断连接了
                    log.info(e.getMessage());
                }
            } else {
                throw new RuntimeException("文件路劲有误");
            }
            //这里会报错flush方法是
            //这下面这行也去掉看看
            //outputStream.flush();

        } catch (Exception e) {
            log.error("文件传输错误", e);
            throw new RuntimeException("文件传输错误");
        }finally {
            //不关闭流，会不会就不报错了
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("流释放错误", e);
                }
            }
            if(targetFile != null){
                try {
                    targetFile.close();
                } catch (IOException e) {
                    log.error("文件流释放错误", e);
                }
            }
        }
    }


    //
    @RequestMapping("video3")
    public void play2(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        String path = "G:\\videoTest\\" + fileName;
        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.reset();
            //获取请求头中Range的值
            String rangeString = request.getHeader("Range");//"Range"就是HttpHeaders.RANGE


            //打开文件
            File file = new File(path);
            if (file.exists()) {
                //使用RandomAccessFile读取文件
                targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                long requestSize = (int)fileLength;
                //分段下载视频
                if (StringUtils.hasText(rangeString)) {

                    //请求头中的Range: bytes=210108416-

                    //从Range中提取需要获取数据的开始和结束位置
                    long requestStart = 0, requestEnd = 0;
                    String[] ranges = rangeString.split("=");
                    //索引0位置上 bytes 索引1位置上 210108416-
                    if (ranges.length > 1) {
                        String[] rangeDatas = ranges[1].split("-");
                        //拿到索引0位置上的210108416
                        requestStart = Integer.parseInt(rangeDatas[0]);
                        if (rangeDatas.length > 1) {
                            //如果给了请求范围的末尾，就拿到末尾
                            requestEnd = Integer.parseInt(rangeDatas[1]);
                        }
                    }
                    if (requestEnd != 0 && requestEnd > requestStart) {
                        requestSize = requestEnd - requestStart + 1;
                    }

//todo
                    //根据协议设置请求头,
                    response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "video/mp4");
                    if (!StringUtils.hasText(rangeString)) {
                        response.setHeader(HttpHeaders.CONTENT_LENGTH, fileLength + "");
                    } else {
                        long length;
                        if (requestEnd > 0) {
                            length = requestEnd - requestStart + 1;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE,
                                    "bytes " + requestStart + "-" + requestEnd + "/" + fileLength);
                        } else {
                            length = fileLength - requestStart;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE,
                                    "bytes " + requestStart + "-" + (fileLength - 1) + "/" + fileLength);
                        }
                    }


                    //文段下载视频返回206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设置targetFile，从自定义位置开始读取数据
                    targetFile.seek(requestStart);
                } else {
                    //设置文件长度
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
                    //如果Range为空则下载整个视频
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "");

                }

                //从磁盘读取数据流返回
                //把byte[] cache = new byte[4096]; 数组的长度改变一下试试
                byte[] cache = new byte[1024*1024];
               try {
                    while (requestSize > 0) {
                        int len = targetFile.read(cache);
                        if (requestSize < cache.length) {
                            outputStream.write(cache, 0, (int)requestSize);
                        } else {
                            outputStream.write(cache, 0, len);
                            if (len < cache.length) {
                                break;
                            }
                        }
                        requestSize -= cache.length;
                    }
                } catch (IOException e) {
                    //这里加个日志
                    //就是这里报的：java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
                    log.info(e.getMessage());

                }
            } else {
                throw new RuntimeException("文件路劲有误");
            }
            outputStream.flush();
        } catch (Exception e) {

            //上面不抓，这里也会抓到异常，java.io.IOException: 你的主机中的软件中止了一个已建立的连接
            log.info(e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //这里或流释放错误
                    //拖动视频播放就是两次请求，所以会出你的主机中的软件中止了一个已建立的连接。
                    //那为啥流会释放错误
                    log.info("outputStream释放错误");
                }
            }
            if (targetFile != null) {
                try {
                    targetFile.close();
                } catch (IOException e) {
                    log.info("targetFile释放错误");
                }
            }
        }

    }


    //可以播放，不卡没有报错，但是 没有提前缓冲好像

    /**
     * 他自己一段一段的请求的时候不会报错，直接拖动的时候会报错
     * @param request
     * @param response
     * @param fileName
     */
        @GetMapping("video4")
        public void play(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName") String fileName) {
            RandomAccessFile randomAccessFile = null;
            OutputStream outputStream = null;
            try {
                response.reset();
                File file = new File("G:\\videoTest\\"+fileName);
                long fileLength = file.length();
                // 随机读文件
                randomAccessFile = new RandomAccessFile(file, "r");

                //获取从那个字节开始读取文件
                String rangeString = request.getHeader("Range");
                long range=0;
                if (StrUtil.isNotBlank(rangeString)) {
                    range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                }
                //获取响应的输出流
                outputStream = response.getOutputStream();
                //设置内容类型
                response.setHeader("Content-Type", "video/mp4");
                //返回码需要为206，代表只处理了部分请求，响应了部分数据
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                // 移动访问指针到指定位置
                randomAccessFile.seek(range);
                // 每次请求只返回1MB的视频流不会报错，但是返回2MB就会报错了
                byte[] bytes = new byte[1024 * 1024];
                int len = randomAccessFile.read(bytes);
                //设置此次相应返回的数据长度
                response.setContentLength(len);

                //设置此次相应返回的数据范围
                response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                // 将这1MB的视频流响应给客户端，写出流的时候，超过1MB就会报错
                outputStream.write(bytes, 0, len);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                log.info(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }finally {

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }

            }


            //System.out.println("返回数据区间:【"+range+"-"+(range+len)+"】");
        }

        //不报错原版，点太快的话会报错，正在播放突然刷新也可能会报这个错
    //播放暂定就用这个，先不慌，前端有倍速控件是可以倍速播放的
    @GetMapping("video5")
    public void play5(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName") String fileName)  {
        RandomAccessFile randomAccessFile = null;
        OutputStream outputStream = null;
        try {
            response.reset();
            File file = new File("G:\\videoTest\\"+fileName);
            long fileLength = file.length();
            // 随机读文件
            randomAccessFile = new RandomAccessFile(file, "r");

            //获取从那个字节开始读取文件
            //HttpHeaders.RANGE
            String rangeString = request.getHeader("Range");
            long range=0;
            if (StrUtil.isNotBlank(rangeString)) {
                //截取字符串 bytes=474808320-
                range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            }
            //获取响应的输出流
            outputStream = response.getOutputStream();
            //设置内容类型
            //HttpHeaders.CONTENT_TYPE，，， 可以响应多种视频格式不，
            /*
             <source src="movie.mp4" type="video/mp4" />
            <source src="movie.ogg" type="video/ogg" />
            <source src="movie.webm" type="video/webm" />
             */
            response.setHeader("Content-Type", "video/mp4");
            //返回码需要为206，代表只处理了部分请求，响应了部分数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 移动访问指针到指定位置
            randomAccessFile.seek(range);
            // 每次请求只返回1MB的视频流
            byte[] bytes = new byte[1024 * 1024];
            int len = randomAccessFile.read(bytes);
            //设置此次相应返回的数据长度
            response.setContentLength(len);
            //设置此次相应返回的数据范围
            //HttpHeaders.CONTENT_RANGE
            response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
            // 将这1MB的视频流响应给客户端
            //todo 这里会报错,直接把IOException丢掉
            outputStream.write(bytes, 0, len);

            //服务端在读取数据( client.read(buffer) )的时候，如果客户端断开了连接，会抛该异常。

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //路径错误文件找不到
        } catch (IOException e) {
            //response.getOutputStream(); 这里抓到的异常 IOException
            e.printStackTrace();
        } finally {
            if (outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            //交换两个顺序
            if (randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }

        //百度到的报错原因是：https://blog.csdn.net/wujiandao000/article/details/79068100
        //在tomcat中出现这个错误是由于客户端在发送请求后，还没等服务器响应就断开了连接。

        //或者：流是在for循环外面定义的, 在for循环里面fos.close()之后, 下一次循环往fos写入时就会报错(流被关闭之后就无法再操作了)

        /*因为刚写IO流的时候就断开连接了，所以会报这个异常。解决方法是将ouputStream.flush();
        和ouputStream.close();写在finally{}中。希望我的回答可以帮到你*/

        //System.out.println("返回数据区间:【"+range+"-"+(range+len)+"】");
        //以流的形式将数据写出去，但是等这个流到达管理端的时候这个流已经关闭，所以出现了这个报错？？

        //https://blog.csdn.net/Gabriel_wei/article/details/105613236
    }

    //截屏生成缩略图 http://t.zoukankan.com/doudou0809-p-13949579.html


    //监控，可能是发视频可能是发图片过去
//其一：可以将图片以独立文件的形式存储在服务器的指定文件夹中，再将路径存入数据库字段中;
// 对于第一种存储方式，我们前端直接将存储路径赋值给 src 属性即可轻松显示。
    @GetMapping("getImage")
    public void getImage(HttpServletRequest request,HttpServletResponse response,@RequestParam("fileName") String fileName){

        try {
            //filePath:图片完整路径
            /*去拿到输入流的方式
            URL urls = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection)urls.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(50 * 1000);
            conn.setReadTimeout(50 * 1000);
            InputStream inStream = conn.getInputStream();*/
            //直接自己从文件创建输入流
            InputStream inStream = new FileInputStream("G:\\videoTest\\" + fileName);

            //字节输出流
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;

            //把从文件拿到的输入流写到输出流中
            while( (len=inStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }

            //关闭输入流
            inStream.close();

            //把流转换成字节数组
            byte data[] = outStream.toByteArray();

            response.setContentType("image/jpg");
            //  png图片用这个image/jpg可能会有问题,要对应好图片文件类型
            //也就是说我还要拿到图片文件名的后缀，拼接的形式 "image/"+ 后缀
            //但是现在又没有问题
            //图片很多格式是兼容的，所以改变后缀名一般问题不大
            OutputStream os = response.getOutputStream();

            //把字节数组写到response的输出流中
            os.write(data);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /*
    //path 为图片在服务器的绝对路径
    public static void getPhoto(HttpServletResponse response,String path) throws Exception {
        File file = new File(path);
        FileInputStream fis;
        fis = new FileInputStream(file);

        long size = file.length();
        byte[] temp = new byte[(int) size];
        fis.read(temp, 0, (int) size);
        fis.close();
        byte[] data = temp;
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();

    }
     */

    /*
    try {
	//filePath:图片完整路径
	URL urls = new URL(filePath);
    HttpURLConnection conn = (HttpURLConnection)urls.openConnection();
    conn.setRequestMethod("GET");
    conn.setConnectTimeout(50 * 1000);
    conn.setReadTimeout(50 * 1000);
    InputStream inStream = conn.getInputStream();
	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[2048];
    int len = 0;
    while( (len=inStream.read(buffer)) != -1 ){
        outStream.write(buffer, 0, len);
    }
    inStream.close();
    byte data[] = outStream.toByteArray();
	response.setContentType("image/jpg");
    OutputStream os = response.getOutputStream();
    os.write(data);
    os.flush();
    os.close();
}catch (Exception e){
    e.printStackTrace();
}
     */

}