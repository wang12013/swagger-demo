package com.example.swaggerdemo.utils;
 
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 视频转码工具类
 */
@Slf4j
public class VideoEncodeUtil {
    /**
     * @param inputFile  文件原始路径
     * @param outputFile 文件输出路径
     * @throws Exception
     */
 
    public static void encode(String inputFile, String outputFile) throws Exception {
 
        //final Logger logger = LoggerFactory.getLogger(DownLoadCallBack.class);
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(inputFile);
        Frame captured_frame;
        FFmpegFrameRecorder recorder = null;
 
        try {
            grabber.start();
 
            recorder = new FFmpegFrameRecorder(outputFile, grabber.getImageWidth(), grabber.getImageHeight(),
                grabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setSampleRate(grabber.getSampleRate());
            recorder.setVideoBitrate(grabber.getVideoBitrate());
            recorder.setAspectRatio(grabber.getAspectRatio());
            recorder.setAudioBitrate(grabber.getAudioBitrate());
            recorder.setAudioOptions(grabber.getAudioOptions());
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.start();
 
            while (true) {
                captured_frame = grabber.grabFrame();
                if (captured_frame == null) {
                    log.info("转码成功");
                    break;
                }
                recorder.record(captured_frame);
            }
 
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        } finally {
            if (recorder != null) {
                try {
                    recorder.close();
                } catch (Exception e) {
                    log.info("recorder.close异常");
                }
            }
 
            try {
                grabber.close();
            } catch (FrameGrabber.Exception e) {
                log.info("frameGrabber.close异常");
            }
        }
    }
}