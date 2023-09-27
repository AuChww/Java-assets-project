package ku.cs.models;

import java.util.ArrayList;

public class AssetList {
    private Asset asset;

    private ArrayList<Asset> assets;

    public AssetList(){
        assets = new ArrayList<>();
    }

    public ArrayList<Asset> getAssetType(){return assets;}

    public String getAssetType1(){return asset.getCategory();}

    public ArrayList<Asset> getAllAsset(){
        return assets;
    }

    public String toCsv() {
        String result = "";
        for (Asset asset : this.assets){
            result += asset.toCsv() + "\n";
        }
        return result;
    }

    public  ArrayList<Asset> searchAssetByUsername(String Username){
        ArrayList<Asset> searchAsset = new ArrayList<>();
        for(Asset asset : this.assets){
            if(asset.isUsername(Username)){
                searchAsset.add(asset);
            }
        }return searchAsset;
    }
    public  Asset searchAssetSerialNumber(String SerialNumber){
        for(Asset asset : this.assets){
            if(asset.isSerialNumber(SerialNumber)){
                return asset;
            }
        }return null;
    }
    public void addAsset(Asset asset){assets.add(asset);}
}
