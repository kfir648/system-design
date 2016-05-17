package otherBankTrans;

import java.sql.SQLException;

public interface ConformTransactionListener {
	void conformTransaction(ConformEvent event);
}
