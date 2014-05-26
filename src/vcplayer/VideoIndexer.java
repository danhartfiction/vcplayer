/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vcplayer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author dhart
 */
public class VideoIndexer extends SimpleFileVisitor<Path> {
    
    protected ArrayList<MediaFile> videoFiles = new ArrayList<>();
    protected String videoBasePath = "/Users/dhart/work/premote/videos";
    protected HashMap<String, Boolean> categories = new HashMap<>();
    
    public HashMap<String, Boolean> getCategories() {
        return this.categories;
    }
    
    public ArrayList<MediaFile> getVideos() {
        return this.videoFiles;
    }
    
    public String getVideoBasePath() {
        return this.videoBasePath;
    }
    
    public void removeVideo(String vid) {
        for (int i=this.videoFiles.size() - 1; i>=0; i--) {
            if (this.videoFiles.get(i).getFilePath().equals(vid)) {
                this.videoFiles.remove(i);
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
            MediaFile mFile = new MediaFile(file, this.videoBasePath);
            this.videoFiles.add(mFile);
            this.categories.put(mFile.getCategory(), Boolean.FALSE);
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
