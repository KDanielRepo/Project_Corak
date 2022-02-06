package ui;

import lombok.Data;
import utils.ScreenCaptureUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;

@Data
public class CustomListener implements AWTEventListener{
    private Point point;
    private JFrame jFrame;

    public void eventDispatched(AWTEvent event) {
        point = ScreenCaptureUtils.selectAreaForCapture();
        System.out.println("X: "+point.x);
        System.out.println("y: "+point.y);
        jFrame.dispose();
        System.out.println("jframe close");
    }
}
