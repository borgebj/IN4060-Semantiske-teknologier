
// Jena Model-reading
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

// SPARQL-queries
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;


public class Sparql {

    static String input = "simpsons.ttl";
    static String queryFile = "exercise_5.rq";

    public static void main(String[] args) {

        String format = fileFormat(input);

        Model model = ModelFactory.createDefaultModel();

        // Read file to graph
        try (Reader reader = new FileReader(new File(input))) {
            model.read(reader, null, format);

        }
        catch (IOException e) { e.printStackTrace(); }

        String query = readQueryFile(queryFile);

        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();

                System.out.println(sol);
            }
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
        try (BufferedReader br = new BufferedReader(new FileReader(queryFile))) {
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
