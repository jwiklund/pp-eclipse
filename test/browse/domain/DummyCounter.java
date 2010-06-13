package browse.domain;

public class DummyCounter extends BaseDummyProgressMonitor {

    private int count = 0;
    private DummyCounter() {}

    public static DummyCounter counter() {
        return new DummyCounter();
    }
    
    @Override
    public void worked(int work) {
        count = count + work;
    }

    public int count() {
        return count;
    }
    
}
