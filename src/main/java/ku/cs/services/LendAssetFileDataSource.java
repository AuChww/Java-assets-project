package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LendAssetFileDataSource implements DataSource<LendAssetList>{
    private String directoryName;
    private String filename;

    LendAssetList lendAssetList = new LendAssetList();
    public LendAssetFileDataSource(){
        this("data/csv","lendAssets.csv");
    }
    public LendAssetFileDataSource(String directoryName,String filename){
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
    public LendAssetList readData() {
        String filePath = directoryName + File.separator + filename;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";
            while((line = buffer.readLine()) != null){
                String[] data = line.split(",");

                if (data.length >= 6) {
                    LendAsset lendAsset = new LendAsset(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim()
                    );
                    lendAssetList.addAsset(lendAsset);
                }
            }

            /*while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                LendAsset lendAsset = new LendAsset(
                        data[0].trim(),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        data[4].trim()
                );
                lendAssetList.addAsset(lendAsset);
            }*/

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
        return lendAssetList;
    }
    @Override
    public void writeData(LendAssetList lendAssetList) {
        String filePath = directoryName + File.separator + filename;
        File file = new File(filePath);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file);
            buffer = new BufferedWriter(writer);

            for (LendAsset lendAsset : lendAssetList.getLendAssetList()) {
                String line = lendAsset.toCsv();
                buffer.append(line);
                buffer.newLine();
            }

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
    public LendAssetList getAllLendList(){return lendAssetList;}
}
