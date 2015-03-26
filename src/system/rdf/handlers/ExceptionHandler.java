package system.rdf.handlers;

import java.sql.SQLException;

import org.postgresql.util.PSQLException;

public class ExceptionHandler {

	public ExceptionHandler() {
	}

	public String error(Exception e) {

		if (e instanceof PSQLException){
			return e.getMessage();
		} 
		 else if (e instanceof ClassNotFoundException) {
			return "No se encuentra una clase";
		} else if (e instanceof IllegalAccessException){
		    return "Acceso ilegal a la base de datos";
		} else if (e instanceof InstantiationException){
			return "Acceso ilegal a la base de datos";
		} else if (e instanceof SQLException){
		    return "Error al instanciar algo";
		}
	return e.getMessage();
	}

	
	
	
	
}
