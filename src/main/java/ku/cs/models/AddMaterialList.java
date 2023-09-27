package ku.cs.models;

import java.util.ArrayList;

public class AddMaterialList {
    private ArrayList<AddMaterial> addMaterialList;
    public AddMaterialList(){
        addMaterialList = new ArrayList<>();
    }
    public void addMaterial(AddMaterial addMaterial){
        addMaterialList.add(addMaterial);
    }
    public ArrayList<AddMaterial> getAddMaterialList() {
        return addMaterialList;
    }
    public ArrayList<AddMaterial> searchByMaterial(Material m){
        ArrayList<AddMaterial> selectedMaterialList = new ArrayList<>();
        for(AddMaterial o: addMaterialList){
            if(o.getName().equals(m.getName()))
                selectedMaterialList.add(o);
        }
        return selectedMaterialList ;
    }
}
