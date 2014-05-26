/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vcplayer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dhart
 */
public class ImageIndexer extends SimpleFileVisitor<Path> {
    protected ArrayList<MediaFile> imageFiles = new ArrayList<>();
    protected String imageBasePath = "/Users/dhart/work/premote/images";
//    protected HashMap<String, Boolean> categories = new HashMap<>();
    
/*    public HashMap<String, Boolean> getCategories() {
        return this.categories;
    } */
    
    public ArrayList<MediaFile> getImages() {
        return this.imageFiles;
    }
    
    public String getImageBasePath() {
        return this.imageBasePath;
    }
    
    public void removeImage(String img) {
        for (int i=this.imageFiles.size() - 1; i>=0; i--) {
            if (this.imageFiles.get(i).getFilePath().equals(img)) {
                this.imageFiles.remove(i);
            }
        }
    }
    
    /*@Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) 
            throws IOException {
        System.out.format("preVisitDirectory: %s\n", dir);
        return super.preVisitDirectory(dir, attrs);
    }*/

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
            throws IOException {
        //System.out.format("visitFile: %s\n", file);
        if (file.getParent().toString().contains("TRASH")) {
            System.out.println("DEBUG: Skipping TRASH folder: " + file.getParent().toString());
            return super.visitFile(file, attrs);
        } else {
            MediaFile mFile = new MediaFile(file, this.imageBasePath);
            this.imageFiles.add(mFile);
            return super.visitFile(file, attrs);
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) 
            throws IOException {
        System.out.format("visitFileFailed: %s\n", file);
        return super.visitFileFailed(file, exc);
    }

    /*@Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
            throws IOException {
        System.out.format("postVisitDirectory: %s\n", dir);
        return super.postVisitDirectory(dir, exc);
    }*/
}
