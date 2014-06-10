#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${package}.persistence.PaginationContext;
import ${package}.service.ServiceBase;
import ${package}.model.EntityBase;
import ${package}.model.ExampleBase;
import ${package}.service.ServiceBase;

public interface ServiceBase<T extends EntityBase, D extends ExampleBase> {
	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceBase.class);

	void add(T t);

	void update(T t);
	
	void update(T t,D d);

	void delete(T t);
	
	void delete(D d);

	List<T> getAll();
	
	List<T> getAll(D d);
	
	List<T> getPaginated(D example,PaginationContext paginationContext);
}