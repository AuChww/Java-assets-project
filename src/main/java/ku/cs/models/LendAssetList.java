package ku.cs.models;

import java.util.ArrayList;

public class LendAssetList {

    private ArrayList<LendAsset> lendAssets;

    public LendAssetList(){
        lendAssets = new ArrayList<>();
    }
    public void lendAsset(LendAsset lendAsset){lendAssets.add(lendAsset);}
    public ArrayList<LendAsset> getLendAssetList() {
        return lendAssets;
    }
    public void addAsset(LendAsset lendasset){lendAssets.add(lendasset);}
    public void del(LendAsset lendasset){lendAssets.remove(lendasset);}
}
