package komeiji.back.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import komeiji.back.config.QiNiuConfig;
import org.springframework.stereotype.Service;

@Service
public class QiNiuService {

    private final QiNiuConfig properties;
    private Configuration cfg;
    private UploadManager uploadManager;

    public QiNiuService(QiNiuConfig properties) {
        this.properties = properties;
        cfg = new Configuration();
        uploadManager = new UploadManager(cfg);
    }

    public String upload(byte[] bytes,String fileName) throws QiniuException {
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        String upToken = auth.uploadToken(properties.getBucket(),fileName);

        Response res = uploadManager.put(bytes, fileName, upToken);
        if (!res.isOK()) {
            throw new RuntimeException("上传失败：" + res.bodyString());
        }

        return properties.getDomain() + "/" +  fileName;
    }

}
