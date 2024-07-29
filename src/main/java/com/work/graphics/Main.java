package com.work.graphics;

import javax.swing.SwingUtilities;

/**
 *
 * @author linux
 */
public class Main {

    public static void main(String[] arg) {
        SwingUtilities.invokeLater(() -> MandelbrotSet.createWin());
    }
}
