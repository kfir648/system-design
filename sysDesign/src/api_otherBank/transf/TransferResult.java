package api_otherBank.transf;

public class TransferResult {

    public TransferResult(int reqId, int outcome) {
        this.reqId = reqId;
        this.outcome = outcome;
    }
    public int getRequestId() { return reqId; }
    
    public int getOutcome() { return outcome; }
    
    private int reqId;
    private int outcome;
}
