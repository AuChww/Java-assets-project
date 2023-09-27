package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MaterialList {
    private ArrayList<Material> materialList;

    public MaterialList(){
        materialList = new ArrayList<>();
    }

    public void addMaterial(Material material){
        materialList.add(material);
    }

    public ArrayList<Material> getMaterialList() {
        return materialList;
    }
    public boolean MaterialNameIsUsed(String name){
        Material material = SearchMaterialByName(name);
        if (material != null )
            return true;
        return false;
    }
    public Material SearchMaterialByName(String name){
        for (Material material : materialList)
            if (material.checkMaterial(name))
                return material;
        return null;
    }
    public MaterialList filterBy(Filterer filterer){
        MaterialList filtered = new MaterialList();
        for(Material c : materialList){
            Material found  = c;
            if(filterer.filter(c)){
                filtered.addMaterial(c);
            }
        }
        return filtered;
    }

}


