/* Use Like This
 ImageWaiter waiter = new ImageWaiter();
 g2d.drawImage(inImage, tx, waiter);
 waiter.waitFor();
*/

 import java.awt.Image;
 import java.awt.image.ImageObserver;
 public class ImageWaiter implements ImageObserver {
     private boolean isDone;
     public boolean imageUpdate(
         Image img,
         int infoflags,
         int x,
         int y,
         int width,
         int height) {
         if ((infoflags & ALLBITS) > 0) {
             synchronized (this) {
                 isDone = true;
                 return true;
             }
         }
         return false;
     }
     public synchronized void waitFor() {
         while (!isDone)
             try {
                 wait();
             } catch (InterruptedException ignore) {}
     }
 }