import sys
from rdflib import Graph, Namespace
from rdflib.namespace import RDF, RDFS, FOAF

# RDF graph defined
graph = Graph()

# List of namespaces
ns = {}


def main():

    # Command line arguments
    if len(sys.argv) > 2:
        in_path = sys.argv[1]
        out_name = sys.argv[2]

        # 1. Read input from file, add to graph
        graph.parse(in_path, format="turtle")

        # read through namespaces, add to graph
        for name, uri in graph.namespaces():

            prefix = Namespace(uri)
            graph.bind(name, prefix)
            ns[name] = prefix

            print(f"@Prefix {name}: <{uri}>")

        # Task 2
        manager = graph.namespace_manager.store
        sim = manager.namespace("sim")
        rdf = manager.namespace("rdf")
        foaf = manager.namespace("foaf")
        xsd = manager.namespace("xsd")

        subject = sim.Maggie

    else:
        print(f"Missing arguments <input_file> <output_file>")


if __name__ == "__main__":
    main()

