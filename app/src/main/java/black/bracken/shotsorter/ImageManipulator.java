package black.bracken.shotsorter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import black.bracken.shotsorter.util.FileUtil;

/**
 * @author BlackBracken
 */
public final class ImageManipulator {

    private File image;
    private File destination;
    private int secondsDeletingLater;

    public ImageManipulator(File image) {
        this.image = image;
    }

    @SuppressLint("NewApi")
    public void manipulate(Context context) {
        if (image == null) {
            throw new IllegalArgumentException("Image must not be null");
        }

        if (destination != null) {
            try {
                Files.copy(Paths.get(image.toURI()), Paths.get(destination.toURI()));
                FileUtil.deleteImageCertainly(context, image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (secondsDeletingLater > 0) {
            File deleted = destination != null ? destination : image;

            new Handler().postDelayed(() -> {
                FileUtil.deleteImageCertainly(context, deleted);
                if (deleted.exists()) {
                    deleted.delete();
                }
            }, secondsDeletingLater * 1000);
        }
    }

    public void setImage(File image) {
        this.image = image;
    }

    public void setDestination(File destination) {
        this.destination = destination;
    }

    public void setDestinationAsFolder(File folder) {
        setDestination(new File(folder.getPath() + File.separator + image.getName()));
    }

    public void setSecondsDeletingLater(int secondsDeletingLater) {
        this.secondsDeletingLater = 0 < secondsDeletingLater ? secondsDeletingLater : 0;
    }

}
