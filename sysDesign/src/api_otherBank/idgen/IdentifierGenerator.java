package api_otherBank.idgen;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/*

This subsystem allocates blocks of unique transfer-request identifiers. 

*/

public interface IdentifierGenerator extends Remote {
    int allocateBlock(int numIds) throws RemoteException, IOException;
}
