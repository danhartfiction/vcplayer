/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vcplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author dhart
 */
public class FileManager {
    
    protected ArrayList<String> videoHistory = new ArrayList<>();
    protected ArrayList<String> imageHistory = new ArrayList<>();
    protected SortedSet<String> sortedCategories;
    
    protected String currentVideo = "";
    protected String currentImage = "";
    protected VideoIndexer videoIndexer;
    protected ImageIndexer imageIndexer;
        
    private final Random randomGenerator = new Random();
    
    public String getCurrentVideo() {
        return this.currentVideo;
    }
    
    public String getCurrentImage() {
        return this.currentImage;
    }
    
    public FileManager() {
        this.videoIndexer = new VideoIndexer();
        this.imageIndexer = new ImageIndexer();
    }
        
    public void crawlVideos() {
        Path path = Paths.get(this.videoIndexer.getVideoBasePath());
                 
        try {
            Files.walkFileTree(path, this.videoIndexer);
        } catch (IOException ex) {
            System.out.println("walkFileTree FAILURE!");
        }
        
        this.sortedCategories = new TreeSet<>(this.videoIndexer.getCategories().keySet());
        
        System.out.println(this.sortedCategories.toString());
    }
    
    public void crawlImages() {
        Path path = Paths.get(this.imageIndexer.getImageBasePath());
                 
        try {
            Files.walkFileTree(path, this.imageIndexer);
        } catch (IOException ex) {
            System.out.println("walkFileTree FAILURE!");
        }
    }
    
    public SortedSet<String> getCategories() {
        return this.sortedCategories;
    }
    
    public void noop() {
        System.out.println("NOOP");
    }
    
    public String getNextVideo(ArrayList<String> activeCategories) {
        Iterator it = this.videoIndexer.getVideos().iterator();
        ArrayList<String> videoBucket = new ArrayList<>();
        while (it.hasNext()) {        
            MediaFile mf = (MediaFile) it.next();
            if (activeCategories.contains(mf.getCategory())) {
                //System.out.println("DEBUG: " + mf.getFilePath());
                videoBucket.add(mf.getFilePath());
            }
        }
        if (videoBucket.size() >= 1) {
            int index = this.randomGenerator.nextInt(videoBucket.size());
            String nextVideo = videoBucket.get(index);
            this.videoHistory.add(nextVideo);
            System.out.println("DEBUG: " + this.videoHistory.toString());
            this.currentVideo = nextVideo;
            return nextVideo;
        } else {
            return "No such files exist.";
        }
    }
    
    public String getNextImage(ArrayList<String> activeCategories) {
        Iterator it = this.imageIndexer.getImages().iterator();
        ArrayList<String> imageBucket = new ArrayList<>();
        while (it.hasNext()) {        
            MediaFile mf = (MediaFile) it.next();
            if (activeCategories.contains(mf.getCategory())) {
                //System.out.println("DEBUG: " + mf.getFilePath());
                imageBucket.add(mf.getFilePath());
            }
        }
        if (imageBucket.size() >= 1) {
            int index = this.randomGenerator.nextInt(imageBucket.size());
            String nextImage = imageBucket.get(index);
            this.imageHistory.add(nextImage);
            System.out.println("DEBUG: " + this.imageHistory.toString());
            this.currentImage = nextImage;
            return nextImage;
        } else {
            return "No such files exist.";
        }
    }
    
    public String getPreviousVideo() {
        if (this.videoHistory.size() > 1) {
            String pVid = this.videoHistory.get(this.videoHistory.size() - 2);
            this.videoHistory.remove(this.videoHistory.size() - 2);
            this.currentVideo = pVid;
            return pVid;
        } else {
            return "No such files exist.";
        }
    }
    
        public String getPreviousImage() {
        if (this.imageHistory.size() > 1) {
            String pImg = this.imageHistory.get(this.imageHistory.size() - 2);
            this.imageHistory.remove(this.imageHistory.size() - 2);
            this.currentImage = pImg;
            return pImg;
        } else {
            return "No such files exist.";
        }
    }
    
    public boolean trashCurrentVideo() {
        if (!this.currentVideo.isEmpty()) {
            System.out.println("DEBUG: Trashing " + this.currentVideo);
            Path rmFile = Paths.get(this.currentVideo);
            ArrayList<String> tf = new ArrayList<>();
            tf.add(this.currentVideo);
            this.videoHistory.removeAll(tf);
            this.videoIndexer.removeVideo(this.currentVideo);
            this.currentVideo = "";
            return this.trashFile(rmFile);
        } else {
            return false;
        }
    }
        
    public boolean trashCurrentImage() {
        if (!this.currentImage.isEmpty()) {
            System.out.println("DEBUG: Trashing " + this.currentImage);
            Path rmFile = Paths.get(this.currentImage);
            ArrayList<String> tf = new ArrayList<>();
            tf.add(this.currentImage);
            this.imageHistory.removeAll(tf);
            this.imageIndexer.removeImage(this.currentImage);
            this.currentImage = "";
            return this.trashFile(rmFile);
        } else {
            System.out.println("DEBUG: this.currentImage is empty.");
            return false;
        }
    }
    
    public boolean trashFile(Path rmFile) {
        String fn = rmFile.getFileName().toString();
        String parent = rmFile.getParent().toString();
        if (!Files.exists(rmFile)) {
            System.out.println("Debug: " + fn + " does not exist.");
            return false;
        }
        System.out.println("DEBUG: Moving " + fn + " to: " + parent + "/TRASH");
        File oldFile = new File(rmFile.toString());
        Path testPath = Paths.get(parent + "/TRASH");
        if (Files.notExists(testPath)) {
            System.out.println("Creating missing /TRASH folder in category.");
            new File(parent + "/TRASH").mkdir();
        }
        oldFile.renameTo(new File(parent + "/TRASH/" + fn));
        return true;
    }
}
 