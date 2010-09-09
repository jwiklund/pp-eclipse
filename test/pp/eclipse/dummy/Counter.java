package pp.eclipse.dummy;

public class Counter extends BaseProgressMonitor {

    private int count = 0;
    private Counter() {}

    public static Counter counter() {
        return new Counter();
    }

    @Override
    public void worked(int work) {
        count = count + work;
    }

    public int count() {
        return count;
    }

}
