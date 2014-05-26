/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vcplayer;

import java.nio.file.Path;

/**
 *
 * @author dhart
 */
public class MediaFile {
    protected Path path;
    protected String type;
    protected String category;
    
    public MediaFile(Path file, String origin) {
        this.path = file;
        this.category = file.toAbsolutePath().getParent().toString();
        this.category = this.category.replace(origin + "/", "");
        String[] parts = this.category.split("/");
        if (parts.length > 1) {
            this.category = parts[0] + "/" + parts[1];
        } else {
            this.category = parts[0];
        }
        System.out.println("DEBUG: " + this.category + "   ORIGIN: " + origin + "    FILE: " + this.path.toAbsolutePath().toString());
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getFilePath() {
        return this.path.toAbsolutePath().toString();
    }
}
