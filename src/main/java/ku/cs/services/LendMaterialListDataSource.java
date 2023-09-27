package ku.cs.services;

import ku.cs.models.LendMaterial;
import ku.cs.models.LendMaterialList;

import java.io.*;

public class LendMaterialListDataSource implements DataSource<LendMaterialList> {
    public LendMaterialListDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private String directoryName;
    private String fileName;


    public LendMaterialListDataSource() {
        this("data/csv/", "requestMaterials.csv");
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
    public LendMaterialList readData() {
        LendMaterialList lendMaterialList = new LendMaterialList();
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
                LendMaterial lendMaterial = new LendMaterial(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim());
                lendMaterialList.lendMaterial(lendMaterial);

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
        return lendMaterialList;
    }

    @Override
    public void writeData(LendMaterialList lendMaterialList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file);
            buffer = new BufferedWriter(writer);

            for (LendMaterial lendMaterial : lendMaterialList.getLendMaterialList()) {
                String line = lendMaterial.toCSV();
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
