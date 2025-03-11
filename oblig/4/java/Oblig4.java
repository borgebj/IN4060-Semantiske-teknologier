import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.query.*;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;

public class Oblig4 {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Missing arguments <schema_file> <query_file> <output_file>");
            return;
        }

        // Arguments
        String schema_file = args[0];  
        String sparql = args[1];   
        String output_name = args[2];  

        // Models for RDF and RDFS
        Model schemaModel = ModelFactory.createDefaultModel();
        Model dataModel = ModelFactory.createDefaultModel();

        // 1: Loads the RDFS schema (family.ttl)
        try (FileReader schemaReader = new FileReader(schema_file)) {
            String format = fileFormat(schema_file);
            schemaModel.read(schemaReader, null, format);
        } catch (IOException e) {
            System.out.println("Error reading the RDFS schema file: " + e.getMessage());
            return;
        }

        // 2: Loads the data model (Simpsons.ttl)
        String schema_url = "https://www.uio.no/studier/emner/matnat/ifi/IN3060/v23/obliger/simpsons.ttl";
        String format = fileFormat(schema_file);
        try {
            dataModel.read(schema_url, format);
        } catch (Exception e) {
            System.out.println("Error reading instance data: " + e.getMessage());
            return;
        }

        // 3: Applies RDFS reasoning
        Model rdfsModel = ModelFactory.createRDFSModel(schemaModel, dataModel);

        // 4: Reads given SPARQL query
        String queryStr = readQueryFile(sparql);
        if (queryStr == null) {
            System.out.println("Error reading SPARQL query file.");
            return;
        }

        // 5: Executes SPARQL query on the rdfs model
        Query query = QueryFactory.create(queryStr);
        try (QueryExecution qe = QueryExecutionFactory.create(query, rdfsModel)) {
            Model constructResult = qe.execConstruct();

            // Write the result to a file
            try (Writer writer = new FileWriter(new File(output_name))) {
                constructResult.write(writer, format);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error executing the SPARQL query: " + e.getMessage());
        }

    }

    /**
     * Takes in a string and determines file-format for jena usage
     * 
     * @param url 
     * @return format for jena
     */
    private static String fileFormat(String url) {
        if (url.endsWith(".ttl")) {
            return "TURTLE";
        }
        else if (url.endsWith(".rdf")) {
            return "RDF/XML";
        }
        else if (url.endsWith(".n3")) {
            return "N3";
        }
        else if (url.endsWith(".nt")) {
            return "N-TRIPLES";
        }
        else return null;
    }

    /**
     * Reads query from .rq file
     * 
     * @param path to file
     * @return query in string form or null
     */
    public static String readQueryFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder qb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                qb.append(line).append("\n");
            }
            return qb.toString();
        }
        catch (IOException ignored) {
            return null;
        }
    }
}
