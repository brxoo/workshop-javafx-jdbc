package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentDAOjdbc implements DepartmentDAO {

	private Connection connection;
	
	public DepartmentDAOjdbc(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement st = null;
		
		try {
			st = connection.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, department.getName());
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				if (rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
				}
				
				DB.closeResultSet(rs);
				
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department department) {
		PreparedStatement st = null;
		
		try {
			st = connection.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
			st.setString(1, department.getName());
			st.setInt(2, department.getId());
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = connection.prepareStatement("DELETE FROM department WHERE Id = ?");
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new DbException("The ID entered does not exist");
			}
			
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		 try {
			st = connection.prepareStatement("SELECT department.* FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Department department = instantiateDepartment(rs);
				return department;
			} 
			
			return null;
			
		 } catch(SQLException e) {
			 throw new DbException(e.getMessage());
		 } finally {
			 DB.closeStatement(st);
			 DB.closeResultSet(rs);
		 }
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("Id"));
		department.setName(rs.getString("Name"));
		return department;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = connection.prepareStatement("SELECT * FROM department");
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			
			while (rs.next()) {
				list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}
			
			return list;
			
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
