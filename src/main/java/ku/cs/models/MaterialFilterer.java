package ku.cs.models;

public class MaterialFilterer implements Filterer<Material> {
    private String category;

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean filter(Material material) {
        if (category != null) {
            return material.getCategory().equals(category);
        }
        return true;
    }
}
