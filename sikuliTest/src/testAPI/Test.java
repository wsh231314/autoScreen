package testAPI;

import org.sikuli.basics.Debug;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Screen;

public class Test {
        public static void main(String[] args) {
                Screen s = new Screen();
                Debug.info("Screen: %s", s);
                String img = "desktop.png";
                String inJarFolder = "imgs";
                ImagePath.add(inJarFolder);
                
                s.exists(img);
                s.click();
                
                s.exists("qqbr.png"); 
                s.doubleClick();
                
                s.exists("163.png");
                s.click();
                
                s.type("www.163.com");
                
                s.exists("web-enter.png");
                s.click();

                Debug.info("... leaving");
        }
}