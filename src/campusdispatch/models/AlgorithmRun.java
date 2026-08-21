package campusdispatch.models;

/**
 * Stores execution metrics for algorithm runs.
 */
public class AlgorithmRun {
    private int runId;
    private String algorithmName;
    private int inputSize;
    private long timeNanoseconds;
    private long memoryKb;
    private long dateRun;

    /**
     * Constructs a new AlgorithmRun entry.
     */
    public AlgorithmRun(int runId, String algorithmName, int inputSize, long timeNanoseconds, 
                        long memoryKb, long dateRun) {
        this.runId = runId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeNanoseconds = timeNanoseconds;
        this.memoryKb = memoryKb;
        this.dateRun = dateRun;
    }

    public int getRunId() { return runId; }
    public void setRunId(int runId) { this.runId = runId; }

    public String getAlgorithmName() { return algorithmName; }
    public void setAlgorithmName(String algorithmName) { this.algorithmName = algorithmName; }

    public int getInputSize() { return inputSize; }
    public void setInputSize(int inputSize) { this.inputSize = inputSize; }

    public long getTimeNanoseconds() { return timeNanoseconds; }
    public void setTimeNanoseconds(long timeNanoseconds) { this.timeNanoseconds = timeNanoseconds; }

    public long getMemoryKb() { return memoryKb; }
    public void setMemoryKb(long memoryKb) { this.memoryKb = memoryKb; }

    public long getDateRun() { return dateRun; }
    public void setDateRun(long dateRun) { this.dateRun = dateRun; }

    @Override
    public String toString() {
        return "AlgorithmRun{id=" + runId + ", name='" + algorithmName + "', timeNs=" + timeNanoseconds + "}";
    }
}
