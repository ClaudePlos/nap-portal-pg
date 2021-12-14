package pl.kskowronski.data.service.admin;


import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PdfService {

    public void removeFileFromDisk( String path ) throws InterruptedException {
        Thread.sleep(20000); // 4 secound
        File file = new File(path);
        file.delete();
    }

}
