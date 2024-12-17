package com.example.demo;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

/*JdbcTemplate è una classe di Spring per interagire con il database in modo semplificato.
Fornisce metodi come update per query INSERT, DELETE, UPDATE e query per le SELECT.
JdbcTemplate si basa sulle configurazioni di connessione al database specificate nel file di configurazione Spring.*/


//grazie a questo è possibile  utilizzare l'iniezione delle dipendenze con @Autowired

@Component
public class JdbcTemp {

    // Oggetto JdbcTemplate per eseguire query e update sul database.
    private JdbcTemplate jdbcTemplateObject;

    /*
     * Metodo per iniettare l'istanza di JdbcTemplate nella classe.
     * @Autowired indica a Spring di fornire automaticamente un'istanza di JdbcTemplate.
     * Autowired in Spring indica che un’istanza di JdbcTemplate deve essere iniettata automaticamente.
      In questo caso, Spring creerà automaticamente un'istanza di JdbcTemplate (se è configurato correttamente)
     e la fornirà al setter setJdbcTemplateObject.
     */
    ///insranzio un jdbctemplateoobject  grazie alla dipendence iniection lo faccio una sola volta usando autowuered
    //questo oggetto è in grado di fare le query
    @Autowired
    public void setJdbcTemplateObject(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplateObject = jdbcTemplateObject;
        
    }

   
    public int insertMobile(String nome, String marca, double prezzo, String url) {
        String query = "INSERT INTO mobili (nome, marca, prezzo, url, pezzi, pezziV) VALUES (?, ?, ?, ?,?,?)";
        return jdbcTemplateObject.update(query, nome, marca,  prezzo, url, 50, 0);
       
        
    }

    public int delete(String nome) {
        String query = "DELETE FROM mobili WHERE nome = ?";
        return jdbcTemplateObject.update(query, nome);
    }
  
    public int setPrezzo(String nome, double prezzo) {
    	
    	String query = "UPDATE mobili SET prezzo = (?) WHERE nome = (?)";
    	return jdbcTemplateObject.update(query, prezzo, nome);
    }
    
    public int change(String nome, int pezzi) {
    	
    	String query = "UPDATE mobili SET pezzi = pezzi - (?) WHERE nome = (?)";
    	jdbcTemplateObject.update(query, pezzi, nome);
    	String query1 = "UPDATE mobili SET pezziV = pezziV + (?) WHERE nome = (?)";
    	return jdbcTemplateObject.update(query1, pezzi, nome);
    	
    	
    }
    public ArrayList<arredo> getInventory() {
        String query = "SELECT nome, marca, prezzo, url, pezzi FROM mobili";

        return jdbcTemplateObject.query(query, new ResultSetExtractor<ArrayList<arredo>>() {
            @Override
            public ArrayList<arredo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ArrayList<arredo> inventoryList = new ArrayList<>();

                while (rs.next()) {
                    arredo product = new arredo();
                    product.setNome(rs.getString("nome"));
                    product.setMarca(rs.getString("marca"));
                    product.setPrezzo(rs.getDouble("prezzo"));
                    product.setUrl(rs.getString("url"));
                    product.setPezzi(rs.getInt("pezzi"));

                    inventoryList.add(product);
                }

                return inventoryList;
            }
        });
    }
   
    
    public ArrayList<arredo> getLista() {
        String query = "SELECT * FROM mobili";

        return jdbcTemplateObject.query(query, new ResultSetExtractor<ArrayList<arredo>>() {
       
        	@Override
            public ArrayList<arredo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ArrayList<arredo> listaP = new ArrayList<>();

                // Itera sui risultati della query e crea un nuovo oggetto pc per ciascun record.
                while (rs.next()) {
                	arredo p1 = new arredo();
                    p1.setNome(rs.getString("nome"));
                    p1.setMarca(rs.getString("marca"));
                   
                    p1.setPrezzo(rs.getDouble("prezzo"));
                    p1.setUrl(rs.getString("url"));
                    p1.setPezzi(rs.getInt("pezzi"));
                    p1.setPezziV(rs.getInt("pezziV"));
                    

                    listaP.add(p1);
                }

                return listaP;
            }
        });
    }
}


