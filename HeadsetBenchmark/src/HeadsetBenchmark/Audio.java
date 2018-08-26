/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeadsetBenchmark;

import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Cristhyan De Marchena
 * @author Cristhian Lopierre
 * @author Luis Parra
 */
public class Audio extends Thread{
    private TargetDataLine targetDataLine;
    private AudioFormat format;
    
    byte[] temp = new byte[targetDataLine.getBufferSize() / 1000];
    AudioFileFormat.Type tipo = AudioFileFormat.Type.AIFF;
    File archivo = new File("grabacion.wav");

    public Audio(TargetDataLine targetDataLine, AudioFormat format) {
        this.targetDataLine = targetDataLine;
        this.format = format;
        this.start();
    }
    
    public void run() {
        try {
            targetDataLine.open(format);
            targetDataLine.start();
            AudioSystem.write(new AudioInputStream(targetDataLine), tipo, archivo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
