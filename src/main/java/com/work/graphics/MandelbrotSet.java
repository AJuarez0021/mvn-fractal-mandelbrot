package com.work.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.commons.numbers.complex.Complex;

/**
 *
 * @author linux
 */
public class MandelbrotSet extends JPanel {

    private static final double REALMIN = -2.0;
    private static final double IMAGMIN = -1.5;
    private static final double REALMAX = 1.0;
    private static final double IMAGMAX = 1.5;
    private static final double DIVERGE = 4.0;
    private static final int MAX = 256;

    private int maxx;
    private int maxy;
    private final Palette[] paleta;

    public MandelbrotSet() {
        this.paleta = load("paleta.pal");
        this.maxx = getWidth();
        this.maxy = getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        clearDevice(g2d);
        draw(g2d);
    }

    private Palette[] load(String fileName) {
        Palette[] color = new Palette[MAX];
        try {
            URL url = getClass().getClassLoader().getResource(fileName);
            List<String> lines = Files.readAllLines(Paths.get(url.toURI()));
            int i = 0;
            Scanner sc;
            for (String line : lines) {
                sc = new Scanner(line);
                int r = sc.nextInt();
                int g = sc.nextInt();
                int b = sc.nextInt();
                color[i] = new Palette(r, g, b);
                i++;
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(MandelbrotSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return color;
    }

    private Palette getColor(int index) {
        return paleta[index];
    }

    private void setPixel(Graphics2D g2d, int x, int y, Color color) {
        g2d.setColor(color);
        g2d.fill3DRect(x, y, 1, 1, true);
    }

    private void clearDevice(Graphics2D g2d) {
        g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, this.maxx, this.maxy);
    }

    private void draw(Graphics2D g2d) {
        this.maxx = getWidth();
        this.maxy = getHeight();
        Complex z;
        Complex c;
        int cont;

        for (int i = 0; i < maxx; ++i) {
            for (int j = 0; j < maxy; ++j) {
                //asignamos a C las coordenadas del plano complejo
                c = Complex.ofCartesian(((REALMAX - REALMIN) / (maxx)) * i + REALMIN, ((IMAGMAX - IMAGMIN) / (maxy)) * j + IMAGMIN);
                //iniciamos Z=0+0i
                z = Complex.ZERO;
                //inicializamos el contador
                cont = 0;

                do {
                    //Z = (Z * Z) + C --> ecuacion Z=Z²+C                    
                    z = z.multiply(z).add(c);
                    cont++;

                    if (z.norm() > DIVERGE) {
                        break;
                    }
                } while (cont < MAX);

                Palette palette = getColor(clamp(cont + 5));
                int r = palette.r();
                int g = palette.g();
                int b = palette.b();

                if (cont == MAX) {
                    setPixel(g2d, i, maxy - j, new Color(0, 0, 0));
                } else {
                    setPixel(g2d, i, maxy - j, new Color(r, g, b));
                }

            }
        }

        g2d.dispose();

    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public static void createWin() {
        JFrame frame = new JFrame("Mandelbrot Set");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(640, 480);
        frame.setResizable(true);
        frame.add(new MandelbrotSet());
        frame.setVisible(true);
        setLocationCenter(frame);
    }

    private static void setLocationCenter(JFrame frame) {
        setLocationMove(frame, 0, 0);
    }

    private static void setLocationMove(JFrame frame, int moveWidth, int moveHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frameSize.width = frameSize.width > screenSize.width ? screenSize.width : frameSize.width;
        frameSize.height = frameSize.height > screenSize.height ? screenSize.height : frameSize.height;
        frame.setLocation((screenSize.width - frameSize.width) / 2 + moveWidth, (screenSize.height - frameSize.height) / 2 + moveHeight);
    }

}
