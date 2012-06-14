package mon.pc.print;

public class ImageUtils {
	static double[] rgbToHsl(int rgb) {
		int r = (rgb >> 0) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = (rgb >> 16) & 0xff;
		
		return rgbToHsl(r, g, b);
	}
	
	static double[] rgbToHsl(int rI, int gI, int bI) {
		double r = (double)rI / 255;
		double g = (double)gI / 255;
		double b = (double)bI / 255;
	    
		double max = Math.max(Math.max(r, g), b),
	           min = Math.min(Math.min(r, g), b);
		double h = 0, s, l = (max + min) / 2;

	    if (max == min) {
	        h = s = 0; // achromatic
	    } else {
	    	double d = max - min;
	        s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
	        
	        if (r == max) h = (g - b) / d + (g < b ? 6 : 0);
            if (g == max) h = (b - r) / d + 2;
            if (b == max) h = (r - g) / d + 4;
            
	        h /= 6;
	    }

	    return new double[] {h, s, l};
	}
}
