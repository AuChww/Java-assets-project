package ku.cs.services;

import ku.cs.models.AddMaterial;
import ku.cs.models.AddMaterialList;

import java.io.*;

public class AddMaterialListDataSource implements DataSource<AddMaterialList> {
    public AddMaterialListDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private String directoryName;
    private String fileName;


    public AddMaterialListDataSource() {
        this("data/csv/", "Materials.csv");
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();

        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AddMaterialList readData() {
        AddMaterialList addMaterialList = new AddMaterialList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                AddMaterial addMaterial = new AddMaterial(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());
                addMaterialList.addMaterial(addMaterial);

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
        return addMaterialList;
    }

    @Override
    public void writeData(AddMaterialList addMaterialList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file);
            buffer = new BufferedWriter(writer);

            for (AddMaterial addMaterial : addMaterialList.getAddMaterialList()) {
                String line = addMaterial.toCSV();
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
}
