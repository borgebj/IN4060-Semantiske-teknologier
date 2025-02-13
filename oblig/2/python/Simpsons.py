import sys
from rdflib import Graph, Namespace, Literal, BNode


def getFormat(url):
    if url.endswith(".ttl"):
        return "turtle"
    elif url.endswith(".rdf"):
        return "xml"
    elif url.endswith(".n3"):
        return "n3"
    elif url.endswith(".nt"):
        return "nt"


def main():
    # Command line arguments
    if len(sys.argv) != 3:
        print(f"Missing arguments <input_file> <output_file>")
        exit(0)

    input_path = sys.argv[1]
    output_path = sys.argv[2]
    format = getFormat(input_path)

    print(f"{input_path} og {output_path}")

    graph = Graph()

    # Task 1
    try:
        graph.parse(input_path, format=format)
    except Exception as e:
        print(f"error parsing ttl file: {e}")

    # read through namespaces, add to graph
    for prefix, namespace in graph.namespaces():
        ns = Namespace(namespace)
        graph.bind(prefix, ns)

    # Task 2
    manager = graph.namespace_manager.store
    SIM = Namespace(manager.namespace("sim"))
    FAM = Namespace(manager.namespace("fam"))
    RDF = Namespace(manager.namespace("rdf"))
    FOAF = Namespace(manager.namespace("foaf"))
    XSD = Namespace(manager.namespace("xsd"))

    # Maggie is a person, name Maggie Simpsons, age 1 of datatype xsd:int
    maggie = SIM.Maggie  # reference to Maggie
    graph.add((maggie, RDF.type, FOAF.Person))
    graph.add((maggie, FOAF.name, Literal("Maggie Simpson")))
    graph.add((maggie, FOAF.age, Literal(1, datatype=XSD.int)))

    # Mona is a person, name Mona Simpson, age 70 of datatype xsd:int
    mona = SIM.Mona  # reference to Mona
    graph.add((mona, RDF.type, FOAF.Person))
    graph.add((mona, FOAF.name, Literal("Mona Simpson")))
    graph.add((mona, FOAF.age, Literal(70, datatype=XSD.int)))

    # Abraham is a person, name Abraham Simpson, age  78 of datatype xsd:int
    abraham = SIM.Abraham  # reference to Abraham
    graph.add((abraham, RDF.type, FOAF.Person))
    graph.add((abraham, FOAF.name, Literal("Abraham Simpson")))
    graph.add((abraham, FOAF.age, Literal(78, datatype=XSD.int)))

    # Abraham is spouse of Mona, Mona is spouse of Abraham
    graph.add((abraham, FAM.hasSpouse, mona))
    graph.add((mona, FAM.hasSpouse, abraham))

    # Herb is a person, has an unknown father
    herb = SIM.Herb  # reference to Herb
    father = BNode()  # reference to blank father
    graph.add((herb, RDF.type, FOAF.Person))
    graph.add((herb, FAM.hasFather, father))

    # Task 3
    for (subj, pred, obj) in graph.triples((None, RDF.type, FOAF.Person)):
        age = graph.value(subj, FOAF.age)

        if age:
            age = age.toPython()

            # checks age, adds age-tag
            if age < 2:
                graph.add((subj, RDF.type, FAM.Infant))
            if age < 18:
                graph.add((subj, RDF.type, FAM.Minor))
            if age > 70:
                graph.add((subj, RDF.type, FAM.Old))

    # Task 4
    turtle_output = graph.serialize(format=format)

    # rdflib does not serialize rdf prefix due to 'a', so we add it
    if not "@prefix rdf:" in turtle_output:
        turtle_output = f"@prefix rdf: <{RDF}> .\n" + turtle_output

    with open(output_path, "w") as f:
        f.write(turtle_output)


if __name__ == "__main__":
    main()

response = g.querY(sparl_query)
for row in response:
    animal = namespace.split_uri(row.animal)[1]
    print(f"{animal} has vertebra.")