import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Model;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.io.File;


public class Simpsons {


    /**
     * Takes in a string and determines file-format for jena usage
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


    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Missing arguments <input_file> <output_file>");
            return;
        }

        // I/O info
        String input_path = args[0];
        String output_path = args[1];

        
        /** Task 1 */

        // Creates model hold .ttl graph
        Model model = ModelFactory.createDefaultModel();

        // Read from input file to model
        try (Reader reader = new FileReader(new File(input_path))) {
            
            String format = fileFormat(input_path);
            model.read(reader, null, format);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        // extract namespaces from the model
        // assuming these exists
        Map<String, String> namespaces = model.getNsPrefixMap();
        String SIM = namespaces.get("sim");
        String FAM = namespaces.get("fam");
        String RDF = namespaces.get("rdf");
        String FOAF = namespaces.get("foaf");
        String XSD = namespaces.get("xsd");


        /** Task 2 */
        Property hasSpouse = model.createProperty(FAM + "hasSpouse");
        Property hasFather = model.createProperty(FAM + "hasFather");
        Property foafName = model.createProperty(FOAF + "name");
        Property rdfType = model.createProperty(RDF + "type");
        Property foafAge = model.createProperty(FOAF + "age");
        Resource foafPerson = model.createResource(FOAF + "Person");

        // Maggie is a person named "Maggie Simpson" of age 1 datatype xsd:int
        Resource maggie = model.createResource(SIM + "Maggie")
            .addProperty(rdfType, foafPerson)
            .addProperty(foafAge, model.createTypedLiteral("1", XSD + "int"))
            .addProperty(foafName, "Maggie Simpson");

        // Mona is a person named "Mona Simpson" of age 70 datatype xsd:int
        Resource mona = model.createResource(SIM + "Mona")
            .addProperty(rdfType, foafPerson)
            .addProperty(foafAge, model.createTypedLiteral("70", XSD + "int"))
            .addProperty(foafName, "Mona Simpson");
       
        // Abraham is a person named "Abraham Simpson" of age 78 datatype xsd:int
        Resource abraham = model.createResource(SIM + "Abraham")
            .addProperty(rdfType, foafPerson)
            .addProperty(foafAge, model.createTypedLiteral("78", XSD + "int"))
            .addProperty(foafName, "Abraham Simpson");

        // Abraham is spouse to Mona and vice versa
        model.add(abraham, hasSpouse, mona);
        model.add(mona, hasSpouse, abraham);

        // Herb is a person, and has _a_ father
        Resource blank = model.createResource();
        Resource herb = model.createResource(SIM + "Herb")
            .addProperty(rdfType, foafPerson)
            .addProperty(hasFather, blank);


        /** Task 3 */
        Iterator<Resource> it = model.listSubjectsWithProperty(rdfType, foafPerson);

        while (it.hasNext()) {
            Resource person = it.next();
            
            Statement ageStmt = person.getProperty(foafAge);
            if (ageStmt != null) {
                int age = ageStmt.getInt();

                // check age, add age-type
                if (age < 2) {
                    model.add(person, rdfType, model.createResource(FAM + "Infant"));
                }
                if (age < 18) {
                    model.add(person, rdfType, model.createResource(FAM + "Minor"));
                }
                if (age > 70) {
                    model.add(person, rdfType, model.createResource(FAM + "Old"));
                }
            }
        }


        /** Task 4 */
        try (Writer writer = new FileWriter(new File(output_path))) {
            String format = fileFormat(output_path);
            model.write(writer, format);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
