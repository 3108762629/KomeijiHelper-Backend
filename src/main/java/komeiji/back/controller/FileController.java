package komeiji.back.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import komeiji.back.service.FileService;
import komeiji.back.service.QiNiuService;
import komeiji.back.utils.Result;
import net.sf.image4j.codec.ico.ICOEncoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Tag(name="文件相关功能")
@RestController
@RequestMapping(path="/file")
public class FileController {

    private static final Tika tika = new Tika();
    private static final MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();

    @Resource
    private FileService fileService;


    @Resource
    private QiNiuService qiNiuService;

    private static String getFileHashName(String fileName,byte[] data) throws MimeTypeException {
        String prefix = DigestUtils.md5Hex(data);
        String fileType = tika.detect(data,fileName);
        String postfix = mimeTypes.forName(fileType).getExtension();
        return String.format("%s%s",prefix,postfix);
    }


    @PostMapping("/qiniu/upload")
    public Result<String> uploadQiNiu(@RequestParam("file") MultipartFile file) {
        try {
            byte[] data = file.getBytes();
            String fileName = getFileHashName(file.getName(),data);
            return Result.success(qiNiuService.upload(file.getBytes(),fileName));
        } catch (IOException | MimeTypeException e) {
            return Result.error("500",e.getMessage());
        }
    }

    // used for upload ico to test, don't used in practice
    @PostMapping("/qiniu/upload/ico")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        int iconSize = 256;
        String fileType = file.getContentType();
        if(fileType == null ||  !fileType.startsWith("image/*")) {
            return Result.error("401","文件不是图片类型");
        }

        try {
            BufferedImage origin = ImageIO.read(file.getInputStream());
            Image scaled = origin.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resized.createGraphics();
            g.drawImage(scaled, 0, 0, null);
            g.dispose();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ICOEncoder.write(resized, bos);
            byte[] data = bos.toByteArray();
            String uploadFileName = getFileHashName(file.getName(),data);
            return Result.success(qiNiuService.upload(data,uploadFileName));
        } catch (IOException | MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("400","没有上传文件");
        }

        String url = fileService.fileUploadService(file);

        if(url == null)  {
            return Result.error("500","文件上传时错误");
        }
        return Result.success(url);
    }
}
