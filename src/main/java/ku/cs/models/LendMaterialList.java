package ku.cs.models;

import java.util.ArrayList;

public class LendMaterialList {
    private static ArrayList<LendMaterial> lendMaterialList;
    public LendMaterialList(){
        lendMaterialList = new ArrayList<>();
    }
    public void lendMaterial(LendMaterial lendMaterial){lendMaterialList.add(lendMaterial);}
    public ArrayList<LendMaterial> getLendMaterialList() {
        return lendMaterialList;
    }
    public static ArrayList<LendMaterial> searchByMaterial(Material m){
        ArrayList<LendMaterial> selectedMaterialList = new ArrayList<>();
        for(LendMaterial o: lendMaterialList){
            if(o.getName().equals(m.getName()))
                selectedMaterialList.add(o);
        }
        return selectedMaterialList ;
    }
    public static ArrayList<LendMaterial> searchByUsername(Account a){
        ArrayList<LendMaterial> selectedMaterialList = new ArrayList<>();
        for(LendMaterial o: lendMaterialList){
            if(o.getUsername().equals(a.getUsername()))
                selectedMaterialList.add(o);
        }
        return selectedMaterialList ;
    }

}
