package main.java.utils;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SqlQueries extends Utility{
	
	public CallableStatement cstmt;
	public String sql;
	
	
	public boolean checkRecordAvailable(String query) {
		
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			result.next();
			if (result.getInt(1) == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		
	}
	
	public boolean ifAssetAvailable(String assetCode){
		
		String query = "SELECT COUNT(*) FROM CATS_ASSET WHERE ASSETCODE='"+assetCode+"'";
		
		return checkRecordAvailable(query);
	}
	
	public boolean ifPartAvailable(String partCode){
		
		String query = "SELECT COUNT(*) FROM CATS_PART WHERE PARTCODE='"+partCode+"'";
		
		return checkRecordAvailable(query);
	}

	
	public void stock_serializedItem(String i_assetcode, String i_tolocation,
			                         String i_tolocationstatus, String i_tolocatorcode,
			                         String i_partcode, String i_addcontactcode,
			                         int i_tobusinessunitid, String lotNumber) {
		
		String query = 
				 "declare "
                + "aValues t_NameValue_tab := t_NameValue_tab(); " 
                + "begin " 
				+ "cats_p_nvp.set_value(aValues,'LOTNUMBER', '"+lotNumber+"'); "
				+ "cats_p_asset_xapi.sp_stock "
				+ "( "
						+ "i_assetcode         => '"+i_assetcode+"', "
						+ "i_tolocation        => '"+i_tolocation+"', "
						+ "i_tolocationstatus  => '"+i_tolocationstatus+"', "
						+ "i_tolocatorcode     => NULL, "
						+ "i_partcode          => '"+i_partcode+"', "
						+ "i_addcontactcode    => 'CATSADM', "
						+ "i_tobusinessunitid  => "+i_tobusinessunitid+", "
						+ "io_Values           => avalues "
				+ "); "
				+ "end;";
		
		executeQuery(query, "Stock - Serialized item - Assetcode: "+i_assetcode);

	}
	

	
	public void stock_nonSerializedItem(String i_partcode, int i_qty,
										String i_tolocation, String i_tolocationstatus,
										String i_tolocatorcode,int i_tobusinessunitid,
										String i_lotnumber, String i_addcontactcode
										){
		
		String query = 
				"DECLARE "
              + "aValues t_NameValue_tab := t_NameValue_tab(); "
              + "BEGIN "
              + "cats_p_item_xapi.sp_stock "
              + "( "
              		+ "i_partcode => '"+i_partcode+"', "
              		+ "i_qty => "+i_qty+", "
              		+ "i_tolocation => '"+i_tolocation+"', "
              		+ "i_tolocationstatus => '"+i_tolocationstatus+"', "
              		+ "i_tolocatorcode => NULL, "
              		+ "i_tobusinessunitid => "+i_tobusinessunitid+", "
              		+ "i_lotnumber => '"+i_lotnumber+"', "
              		+ "i_addcontactcode => 'CATSADM', "
              		+ "io_Values => avalues "
              + "); "
              + "END; ";
		
		executeQuery(query, "Stock - Non Serialized item - Partcode: "+i_partcode);
	}
	
	


}
