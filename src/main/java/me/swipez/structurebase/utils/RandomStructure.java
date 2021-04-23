package me.swipez.structurebase.utils;

import me.swipez.structurebase.StructureBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStructure {

    Random random = new Random();

    public String generateRandomStructure(StructureBase plugin){
        File path = new File(plugin.getDataFolder().getPath()+"/structures");
        List<String> fileNames = new ArrayList<>();
        for (String string : path.list()){
            fileNames.add(string.replace(".yml", ""));
        }
        fileNames.remove("structures");
        return fileNames.get(random.nextInt(fileNames.size()));
    }
}
