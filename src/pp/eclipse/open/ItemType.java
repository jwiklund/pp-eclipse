package pp.eclipse.open;

public enum ItemType {
    Content, InputTemplate, OutputTemplate, Class;

    public String getName(String string) {
        return name() + ":" + string;
    }
}
