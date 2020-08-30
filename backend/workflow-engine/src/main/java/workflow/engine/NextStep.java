package workflow.engine;

public class NextStep {
    public int ndx;
    public long delay;

    public NextStep(int ndx, long delay) {
        this.ndx = ndx;
        this.delay = delay;
    }
}
