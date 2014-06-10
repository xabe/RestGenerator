#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.persistence;

import java.util.List;

import ${package}.model.EntityBase;
import ${package}.model.ExampleBase;

public interface PersistenceBase <T extends EntityBase, D extends ExampleBase> {

	void deleteAllData();
	
	List<T> selectByExamplePagination(D example);
}