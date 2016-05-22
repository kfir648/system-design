package logic.classes;

import java.sql.SQLException;

import DBManegment.DataBaseService;

public class Worker {
	private String userName;
	private String passward;
	private PermissionType permission;
	
	public enum PermissionType {
		BANK_MANAGER(0) , LOAN_OFFICER(1) , TELLER(2);
		private int value;
		private PermissionType(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}
	
	public Worker(String userName, String passward , PermissionType permission) {
		this.userName = userName;
		this.passward = passward;
		this.permission = permission;
	}
	
	public Worker(String userName, String passward) throws Exception {
		this.userName = userName;
		this.passward = passward;
		
		this.permission = DataBaseService.getDataBaseService().getPermissions(this);
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassward() {
		return passward;
	}
	
	public PermissionType getPermission() throws Exception
	{
		return DataBaseService.getDataBaseService().getPermissions(this);
	}
	
	public boolean conformUserName() throws SQLException
	{
		return DataBaseService.getDataBaseService().conformUserName(userName, passward);
	}
	
	public void addUser(Worker manager) throws Exception {
		DataBaseService.getDataBaseService().insertWorker(manager , this , permission);
	}
	
}
