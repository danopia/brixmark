package mon.pc.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import lejos.pc.comm.NXTCommException;
import mon.pc.print.jobs.SimpleBitmapJob;
import mon.pc.print.jobs.TextJob;

import org.jfree.ui.tabbedui.VerticalLayout;

@SuppressWarnings("serial")
class PrintGUI extends JPanel implements ActionListener, ChangeListener {
    JButton openButton, processButton, printButton, textButton, previewButton, estopButton;
    JFileChooser fc;
    ImagePanel inputDisplay, printDisplay;
    JSplitPane splitPane;
    BufferedImage inputImg, printImg;
	ImageIO print;
	JTextField textBox;
	
	JSlider satSlider, lightSlider;
	
	Connection comm;
	SimpleBitmapJob job;
    
	public PrintGUI(Connection comm) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        
        this.comm = comm;
        
        fc = new JFileChooser();
        fc.setDialogTitle("Load image file");
        fc.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".jpg")
                    || f.getName().toLowerCase().endsWith(".png")
                    || f.getName().toLowerCase().endsWith(".gif")
                    || f.isDirectory();
              }

              public String getDescription() {
                return "Image Files";
              }
            });
        
        openButton = new JButton("Load image...");
        openButton.addActionListener(this);
        
        processButton = new JButton("Process image");
        processButton.addActionListener(this);
        
        printButton = new JButton("Print image");
        printButton.addActionListener(this);
        
        previewButton = new JButton("Preview image");
        previewButton.addActionListener(this);
        
        estopButton = new JButton("E-Stop");
        estopButton.setForeground(Color.WHITE);
        estopButton.setBackground(Color.RED);
        estopButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(processButton);
        buttonPanel.add(previewButton);
        buttonPanel.add(printButton);
        buttonPanel.add(estopButton);


        satSlider = new JSlider();
        satSlider.setMajorTickSpacing(20);
        satSlider.setMinorTickSpacing(5);
        satSlider.setPaintTicks(true);
        satSlider.setPaintLabels(true);
        satSlider.addChangeListener(this);

        lightSlider = new JSlider();
        lightSlider.setMajorTickSpacing(20);
        lightSlider.setMinorTickSpacing(5);
        lightSlider.setPaintTicks(true);
        lightSlider.setPaintLabels(true);
        lightSlider.addChangeListener(this);

        JPanel sliderPanel = new JPanel();
        sliderPanel.add(satSlider);
        sliderPanel.add(lightSlider);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new VerticalLayout());
        topPanel.add(buttonPanel);
        topPanel.add(sliderPanel);
        
        
        inputDisplay = new ImagePanel();
        printDisplay = new ImagePanel();
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputDisplay, printDisplay);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(400);
        


        textBox = new JTextField();
        textBox.setMinimumSize(new Dimension(200,0));
        
        textButton = new JButton("Print text");
        textButton.addActionListener(this);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textBox, BorderLayout.CENTER);
        textPanel.add(textButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.PAGE_START);
        add(splitPane, BorderLayout.CENTER);
        add(textPanel, BorderLayout.PAGE_END);
    }
	
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(PrintGUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
        		try {
        			inputImg = ImageIO.read(file);
                    inputDisplay.load(inputImg);
        		} catch (IOException ex) {}
            }
        } else if (e.getSource() == processButton) {
        	processImage();
        } else if (e.getSource() == previewButton) {
        	job.printTo((Graphics2D)printDisplay.getGraphics());
        } else if (e.getSource() == printButton) {
        	job.printTo(comm);
        } else if (e.getSource() == textButton) {
        	TextJob tp = new TextJob(textBox.getText());
        	tp.printTo(comm);
        } else if (e.getSource() == estopButton) {
        	comm.estop();
        }
    }
    
    public void processImage() {
    	printImg = new BufferedImage(inputImg.getWidth()/4, inputImg.getHeight()/6, BufferedImage.TYPE_INT_RGB);
    	
    	job = new SimpleBitmapJob(3);
    	Stroke stroke;

    	for (int y = 0; y < printImg.getHeight(); y++) {
        	stroke = null;
        	
        	for (int x = 0; x < printImg.getWidth(); x++) {
        		int rgb = inputImg.getRGB(x*4, y*6);
        		int marker;
        		double[] hsl = ImageUtils.rgbToHsl(rgb);
        		
        		if (hsl[1] < ((double)satSlider.getValue() / 100)) {
        			if (hsl[2] < ((double)lightSlider.getValue() / 100)) {
            			rgb = 0x000000;
            			marker = 0;
            		} else {
            			rgb = 0xffffff;
            			marker = -1;
            		}
        		} else {
        			if (hsl[0] < 0.5) {
            			rgb = 0x0000ff;
            			marker = 2;
            		} else {
            			rgb = 0xff0000;
            			marker = 1;
            		}
        		}
        		
        		if (stroke == null || stroke.marker != marker) {
        			stroke = new Stroke(marker, y, x, x);
        			job.strokes.get(marker).add(stroke);
        		} else if (stroke.marker == marker) {
        			stroke.end = x;
        		}
        		
        		printImg.setRGB(x, y, rgb);
        	}
    	}
    	
    	printDisplay.load(printImg);
    }

	@Override
	public void stateChanged(ChangeEvent e) {
		processImage();
	}
}

public class MakeJob {
	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Connection comm = new Connection();
            	comm.connect("Mon Print Arm", "00:16:53:19:F6:0B");
            	
                JFrame frame = new JFrame("BrixMark Printing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
                frame.add(new PrintGUI(comm));
         
                frame.pack();
                frame.setVisible(true);
            }
        });
	}
}
