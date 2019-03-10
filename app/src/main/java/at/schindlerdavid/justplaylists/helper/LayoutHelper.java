package at.schindlerdavid.justplaylists.helper;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class LayoutHelper {
    private static int width = Resources.getSystem().getDisplayMetrics().widthPixels;

    public static int getSmallImagePXForDevice() {
        return (int)(width*0.12);
    }

    public static int getLargeImagePXForDevice() {
        return (int) (width*0.44);
    }

    public static int getLargeborderSize() {
        return (int) (width*0.04);
    }

    public static int getFirstBorderDetail() {
        return (int) (width*0.05);
    }

    public static int getBorderAfterSmallPic() {
        return (int) (width*0.03);
    }

    public static int getTextViewDetailSize() {
        return (int) (width*0.73);
    }

    public static int getRightBorderDetailSize() {
        return (int) (width*0.07);
    }
}