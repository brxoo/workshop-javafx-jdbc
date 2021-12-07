package model.dao;

import db.DB;
import model.dao.impl.DepartmentDAOjdbc;
import model.dao.impl.SellerDAOjdbc;

public class DAOFactory {

	public static SellerDAO createSellerDAO() {
		return new SellerDAOjdbc(DB.getConnection());
	}
	
	public static DepartmentDAO createDepartmentDAO() {
		return new DepartmentDAOjdbc(DB.getConnection());
	}
}
