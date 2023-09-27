package ku.cs.services;

import ku.cs.models.Material;
import ku.cs.models.MaterialList;

import java.io.*;

public class MaterialListDataSource implements DataSource<MaterialList> {
    public MaterialListDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private String directoryName;
    private String fileName;


    public MaterialListDataSource(){
        this("data/csv/","Materials.csv");
    }

    private void checkFileIsExisted(){
        File file = new File(directoryName);
        if ( !file.exists()) {
            file.mkdirs();

        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()){
            try{
                file.createNewFile();
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public MaterialList readData(){
        MaterialList materialList = new MaterialList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";
            while((line = buffer.readLine()) != null){
                String[] data = line.split(",");
                Material material = new Material(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());
                materialList.addMaterial(material);

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                reader.close();

            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        return materialList;
    }

    @Override
    public void writeData(MaterialList materialList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file);
            buffer = new BufferedWriter(writer);

            for (Material material : materialList.getMaterialList()){
                String line = material.toCSV();
                buffer.append(line);
                buffer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
