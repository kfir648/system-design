package otherBankTrans;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;

import api_otherBank.IBTS;
import api_otherBank.transf.TransferRequestTuple;

public class OtherBankTrans {
	
	private final String name = "IBTS";
    private IBTS server;
    private final int id = 255;
    private final String secret;
	
    private static OtherBankTrans otherBankTrans = null;
	
    public static OtherBankTrans getBankTrans()
	{
		if(otherBankTrans == null)
			otherBankTrans = new OtherBankTrans();
		return otherBankTrans;
	}
	
	private OtherBankTrans() {
		registry = LocateRegistry.getRegistry(args[0]);
		server = (IBTS) registry.lookup(name);
		id = 255;
		secret = "7cufHngk";
	}
	
	public static void main(String[] args) {
		try {
            String name = "IBTS";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            IBTS server = (IBTS) registry.lookup(name);

            int myId = 666;
            String mySecret = "secret";
            
            server.send(mySecret,
                12, // reqid 
                myId, // sender bank id
                8,  // sender account id
                76, // receiver bank id
                400, // receiver account id
                1200 // amount
            );
            server.send(mySecret,
                13, // reqid 
                myId, // sender bank id
                8,  // sender account id
                76, // receiver bank id
                405, // receiver account id
                83 // amount
            );
            server.send(mySecret,
                12, // reqid 
                myId, // sender bank id
                8,  // sender account id
                76, // receiver bank id
                400, // receiver account id
                1200 // amount
            );
            
            Set<TransferRequestTuple> incomingReqs = server.getNewRequestsFor(mySecret, myId, 3);
            for(TransferRequestTuple reqTuple : incomingReqs) {
                server.accept(mySecret, myId, reqTuple.receiverAccountId, reqTuple.id);
            }
            
        } catch (Exception e) {
            System.err.println("DemoClient exception:");
            e.printStackTrace();
        }
	}
}
