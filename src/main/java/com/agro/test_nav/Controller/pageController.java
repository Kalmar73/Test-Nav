package com.agro.test_nav.Controller;

import com.agro.test_nav.Model.NavData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class pageController {

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String greetingForm(Model model) {
        return "main";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(file.getName() + "-uploaded")));
                stream.write(bytes);
                stream.close();
                NavData navData = new NavData(file.getName() + "-uploaded");
                return "Пройденный путь: " + navData.countPath();
            } catch (Exception e) {
                return "Вам не удалось загрузить " + file.getName()  + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + file.getName()  + " потому что файл пустой.";
        }
    }
}
