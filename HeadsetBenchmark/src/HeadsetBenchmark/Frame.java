/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeadsetBenchmark;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Cristhyan De Marchena
 * @author Cristhian Lopierre
 * @author Luis Parra
 */
public class Frame extends JFrame{
    
    private boolean record, selected;
    private final double width;
    private final double height;
    private java.io.File f;
    private JPanel buttons, idk;
    private JLabel title, nowSelected, nowPlaying;
    private JButton sel, rec, play, conv; 
    private JFileChooser fc;
    
    public Frame() throws HeadlessException {
        record = false;
        selected = false;
        width = Math.ceil(Toolkit.getDefaultToolkit().getScreenSize().getWidth() *.25f);
        height = Math.ceil(Toolkit.getDefaultToolkit().getScreenSize().getHeight() *.6f);
        
        setLayout(new GridBagLayout());
        setSize((int) width, (int) height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Benchmark(Diademas)");
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        setIconImage(icon);
        
        setGUI();
        
        setVisible(true);
    }
    
    public void setGUI(){
        GridBagConstraints gbc = new GridBagConstraints();
        
        // PANELS
        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
        buttons.setBackground(Color.decode("0xFAFAFA"));
        gbc.gridx = 0;                                   
	gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = (int)(width) -(int)buttons.getMinimumSize().getWidth();
	gbc.ipady = (int)(height) -(int)buttons.getMinimumSize().getHeight();
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
	getContentPane().add(buttons, gbc);
        
        buttons.add(Box.createRigidArea(new Dimension(0, 40)));
        
        //TITLE
        title = new JLabel();
        title.setSize(200, 30);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setBackground(Color.decode("0xFAFAFA"));
        title.setForeground(Color.black);
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setText("Audio Benchmark");
        title.setVisible(true);
        buttons.add(title);
        buttons.add(Box.createRigidArea(new Dimension(0, 100)));
        
        // Buttons
        play = new JButton();
        play.setText("Reproducir");
        play.setFont(new Font("Arial", Font.PLAIN, 14));
        play.setMaximumSize(new Dimension((int) (width *.5f), (int) (height *.08f)));
        play.setFocusable(false);
        play.setAlignmentX(CENTER_ALIGNMENT);
        play.setBackground(Color.decode("0x555555"));
        play.setForeground(Color.white);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                playActionPerformed(ae);
            }
        });
        buttons.add(play);
        buttons.add(Box.createRigidArea(new Dimension(0, 20)));
        
        rec = new JButton();
        rec.setText("Grabar");
        rec.setFont(new Font("Arial", Font.PLAIN, 14));
        rec.setMaximumSize(new Dimension((int) (width *.5f), (int) (height *.08f)));
        rec.setFocusable(false);
        rec.setAlignmentX(CENTER_ALIGNMENT);
        rec.setBackground(Color.decode("0x555555"));
        rec.setForeground(Color.white);
        rec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    recordActionPerformed(ae);
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttons.add(rec);
        buttons.add(Box.createRigidArea(new Dimension(0, 20)));
        
        sel = new JButton();
        sel.setText("Seleccionar");
        sel.setFont(new Font("Arial", Font.PLAIN, 14));
        sel.setMaximumSize(new Dimension((int) (width *.5f), (int) (height *.08f)));
        sel.setFocusable(false);
        sel.setAlignmentX(CENTER_ALIGNMENT);
        sel.setBackground(Color.decode("0x555555"));
        sel.setForeground(Color.white);
        sel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selectActionPerformed(ae);
            }
        });
        buttons.add(sel);
        buttons.add(Box.createRigidArea(new Dimension(0, 20)));
        
        conv = new JButton();
        conv.setText("Evaluar");
        conv.setFont(new Font("Arial", Font.PLAIN, 14));
        conv.setMaximumSize(new Dimension((int) (width *.5f), (int) (height *.08f)));
        conv.setFocusable(false);
        conv.setAlignmentX(CENTER_ALIGNMENT);
        conv.setBackground(Color.decode("0x555555"));
        conv.setForeground(Color.white);
        conv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                testActionPerformed(ae);
            }
        });
        buttons.add(conv);
        buttons.add(Box.createRigidArea(new Dimension(0, 100)));
        
        // FOOTER
        nowSelected = new JLabel();
        nowSelected.setAlignmentX(CENTER_ALIGNMENT);
        nowSelected.setBackground(Color.decode("0xFAFAFA"));
        nowSelected.setForeground(Color.black);
        nowSelected.setFont(new Font("Arial", Font.PLAIN, 18));
        nowSelected.setText("Seleccionado: ");
        nowSelected.setVisible(true);
        buttons.add(nowSelected);
        buttons.add(Box.createRigidArea(new Dimension(0, 10)));
        
        nowPlaying = new JLabel();
        nowPlaying.setAlignmentX(CENTER_ALIGNMENT);
        nowPlaying.setBackground(Color.decode("0xFAFAFA"));
        nowPlaying.setForeground(Color.black);
        nowPlaying.setFont(new Font("Arial", Font.PLAIN, 18));
        nowPlaying.setText("Reproduciendo: ");
        nowPlaying.setVisible(true);
        buttons.add(nowPlaying);
        
        // Fc settings
        fc = new javax.swing.JFileChooser(System.getProperty("user.dir") 
                +System.getProperty("file.separator")+ "Sonidos");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".wav", "wav", "sound");
        fc.setFileFilter(filter);
    }
    
    public void selectActionPerformed(java.awt.event.ActionEvent ae){
        int fcReturnVal = fc.showOpenDialog(this);
        
        if (fcReturnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            selected = true;
            nowSelected.setText("Seleccionado: " +f.getName());
        }else{
            JOptionPane.showMessageDialog(this, "Ningun archivo fue seleccionado", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            selected = false;
        }
    }
    
    public void recordActionPerformed(java.awt.event.ActionEvent ae) throws LineUnavailableException, InterruptedException{
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
        if(!AudioSystem.isLineSupported(info)){
            System.out.println("Formato de audio no respaldado");
        }    
        final TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        
        Thread stopper = null;
        
        if (record == true) {
            rec.setText("Grabar");
            stopper.sleep(0);
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Rec stopped");
            
            record = false;
        } else {
            rec.setText("Detener");
            String name = (String) JOptionPane.showInputDialog(this, "", "Nombre la grabación", JOptionPane.PLAIN_MESSAGE);
            stopper = new Thread(new Runnable() {
                @Override
                public void run() {
                    AudioInputStream audioStream = new AudioInputStream(targetDataLine);
                    File recording = new File("Sonidos/" +name +".wav");

                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, recording);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            
            System.out.println("Start rec.");
            targetDataLine.open();
            targetDataLine.start();
            stopper.start();
            record = true;
        }
    }
    
    public void playActionPerformed(java.awt.event.ActionEvent ae){
        String[] options= {"Default", "Seleccion"};
        URL url = null;
        String filename = "";
	try {
            int option = JOptionPane.showOptionDialog(this,
                                                      "¿Que archivo se debe de reproducir?",
                                                      "Seleccione",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.QUESTION_MESSAGE,
                                                      null,
                                                      options,
                                                      options[0]);
            File file;
            if(option == 0){
                file = new File("Sonidos/Default.wav");
            }else{
                if(selected){
                    file = f;
                }else{
                   JOptionPane.showMessageDialog(this, "Ningun archivo ha sido seleccionado, se reproducira el archivo base o por defecto.", "Advertencia", JOptionPane.INFORMATION_MESSAGE); 
                   file = new File("Sonidos/Default.wav");
                }
            }
            
            if (file.canRead()){
                url = file.toURI().toURL();
                filename = file.getName();
            }
	} catch (MalformedURLException e) {
		throw new IllegalArgumentException("could not play the file", e);
	}

	if (url == null) {
		throw new IllegalArgumentException("could not play the file");
	}

	AudioClip clip = Applet.newAudioClip(url);
        nowPlaying.setText("Reproduciendo: " +filename);
	clip.play();
    }
    
    public void testActionPerformed(java.awt.event.ActionEvent ae){
        File file;
        if(selected){
            file = f;
            byte[] byteInput = new byte[(int)file.length() - 44];
            short[] input = new short[(int)(byteInput.length / 2f)];

            try{
                FileInputStream fis = new FileInputStream(file);
                fis.read(byteInput, 44, byteInput.length - 45);
                ByteBuffer.wrap(byteInput).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(input);
            }catch(Exception e  ){
                e.printStackTrace();
            }

            System.out.println(input.length);
            for (int i = 0; i < input.length; i++) {
                System.out.println(input[i]);
            }
        }else{
           JOptionPane.showMessageDialog(this, "No hay ninguna pista de audio seleccionada.");
        }
    }
}
