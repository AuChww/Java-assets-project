package ku.cs.services;

import ku.cs.models.Asset;
import ku.cs.models.AssetList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AssetFileDataSource implements DataSource<AssetList>{
    private String directoryName;
    private String filename;

    private AssetList assetList = new AssetList();
    public AssetFileDataSource(){
        this("data/csv","assets.csv");
    }
    public AssetFileDataSource(String directoryName,String filename){
        this.directoryName = directoryName;
        this.filename = filename;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);

        if(!file.exists()){ //ถ้า directory ไม่มีอยู่ให้สร้าง
            file.mkdir();
        }

        String path = directoryName+File.separator+filename;

        file = new File(path);

        //ถ้าไม่มี file ให้สร้าง file
        if(!file.exists()){
            try {
                file.createNewFile();

            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    @Override
    public AssetList readData() {
        String path = directoryName + File.separator + filename;
        File file = new File(path);

        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 10) {
                    assetList.addAsset(new Asset(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            data[5],
                            data[6],
                            data[7],
                            data[8],
                            data[9]
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return assetList;
    }

    @Override
    public void writeData(AssetList assetList) {
        String path = directoryName + File.separator + filename;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write(assetList.toCsv());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public AssetList getAllAssetList(){
        return assetList;
    }
}
