import sys
from rdflib import Graph, Namespace, Literal, BNode
from rdflib.namespace import RDF, RDFS, FOAF


def main():

    # Command line arguments
    if len(sys.argv) != 3:
        print(f"Missing arguments <input_file> <output_file>")

    input_path = sys.argv[1]
    output_path = sys.argv[2]

    graph = Graph()

    # 1. Read input from file, add to graph
    try:
        graph.parse(input_path, format="turtle")
    except:
        print("error parsing ttl file")

    # read through namespaces, add to graph
    for prefix, namespace in graph.namespaces():
        ns = Namespace(namespace)
        graph.bind(prefix, ns)

        print(f"@Prefix {prefix}: <{namespace}>")

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
    mona = SIM.Mona     # reference to Mona
    graph.add((mona, RDF.type, FOAF.Person))
    graph.add((mona, FOAF.name, Literal("Mona Simpson")))
    graph.add((mona, FOAF.age, Literal(70, datatype=XSD.int)))

    # Abraham is a person, name Abraham Simpson, age  78 of datatype xsd:int
    abraham = SIM.Abraham     # reference to Abraham
    graph.add((mona, RDF.type, FOAF.Person))
    graph.add((mona, FOAF.name, Literal("Abraham Simpson")))
    graph.add((mona, FOAF.age, Literal(78, datatype=XSD.int)))

    # Abraham is spouse of Mona
    graph.add((abraham, FAM.hasSpouse, mona))
    # Mona is Spouse of Abraham
    graph.add((mona, FAM.hasSpouse, abraham))

    # Herb is a person, has an unknown father
    herb = SIM.Herb     # reference to Herb
    father = BNode()
    graph.add((herb, RDF.type, FOAF.Person))
    graph.add((herb, FAM.hasFather, father))

    # Task 4
    try:
        graph.serialize(destination=output_path, format="turtle")
    except Exception as e:
        print(f"Error saving to file: {e}")


if __name__ == "__main__":
    main()
