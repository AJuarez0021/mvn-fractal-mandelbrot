
package com.work.graphics;

/**
 *
 * @author linux
 */
public record Palette(int r, int g, int b) {

    @Override
    public String toString() {
        return "Palette{" + "r=" + r + ", g=" + g + ", b=" + b + '}';
    }

}
